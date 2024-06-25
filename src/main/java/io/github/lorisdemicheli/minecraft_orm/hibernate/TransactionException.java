package io.github.lorisdemicheli.minecraft_orm.hibernate;

public class TransactionException extends RuntimeException {

	private static final long serialVersionUID = 3806659587846581762L;

	public TransactionException() {	
		super();
	}

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
