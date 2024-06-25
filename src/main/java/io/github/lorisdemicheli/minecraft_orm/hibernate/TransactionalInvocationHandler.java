package io.github.lorisdemicheli.minecraft_orm.hibernate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.transaction.InvalidTransactionException;
import javax.transaction.TransactionRequiredException;
import javax.transaction.Transactional;
import javax.transaction.TransactionalException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class TransactionalInvocationHandler implements InvocationHandler {
	private final Object target;
	private Transactional classTransactional;

	public TransactionalInvocationHandler(Object target) {
		this.target = target;
		this.classTransactional = target.getClass().getAnnotation(Transactional.class);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		EntityManager em = HibernateUtil.getEntityManager();
		Transactional transactional = method.getAnnotation(Transactional.class);
		EntityTransaction currentTransaction = null;
		if (transactional == null) {
			transactional = this.classTransactional;
			if(transactional == null) {
				transactional = defaultTransactional();
			}
		}
		switch (transactional.value()) {
		case REQUIRED:
			if (em.getTransaction() == null) {
				em.joinTransaction();
				if (!em.isJoinedToTransaction()) {
					throw new TransactionException("Unable to join transaction");
				}
				currentTransaction = em.getTransaction();
			} else {
				currentTransaction = em.getTransaction();
				currentTransaction.begin();
			}
			break;
		case REQUIRES_NEW:
			currentTransaction = em.getTransaction();
			if (currentTransaction != null) {
				currentTransaction.commit();
				em.clear();
			}
			currentTransaction = em.getTransaction();
			currentTransaction.begin();
			break;
		case MANDATORY:
			currentTransaction = em.getTransaction();
			if(currentTransaction == null) {
				throw new TransactionalException("Mandatory require active Transactional",new TransactionRequiredException());
			}
			break;
		case NEVER:
			currentTransaction = em.getTransaction();
			if(currentTransaction != null) {
				throw new TransactionalException("Never require active Transactional",new InvalidTransactionException());
			}
			break;
		case SUPPORTS:
			currentTransaction = em.getTransaction();
			
			break;
		case NOT_SUPPORTED:

			break;
		}

		try {
			Object result = method.invoke(target, args);
			if(currentTransaction != null) {
				currentTransaction.commit();
				em.clear();
			}
			return result;
		} catch (Exception e) {
			if ((transactional.rollbackOn().length == 0
					|| Stream.of(transactional.rollbackOn()).anyMatch(c -> c.isInstance(e)))) {
				if (transactional.dontRollbackOn().length == 0
						|| Stream.of(transactional.rollbackOn()).noneMatch(c -> c.isInstance(e))) {
					if (currentTransaction.isActive()) {
						currentTransaction.rollback();
					}
				}
			}
			throw e;
		}
	}
	
	private Transactional defaultTransactional() {
		return new Transactional() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return Transactional.class;
			}

			@Override
			public TxType value() {
				return TxType.REQUIRES_NEW;
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Class[] rollbackOn() {
				return new Class[0];
			}

			@SuppressWarnings("rawtypes")
			@Override
			public Class[] dontRollbackOn() {
				return new Class[0];
			}
		};
	}
}