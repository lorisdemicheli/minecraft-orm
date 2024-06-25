//package org.github.lorisdemicheli.bean;
//
//import java.lang.reflect.Field;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//
//@Aspect
//public class BeanInjector {
//	
//    @Before("execution(* *(..)) && @annotation(Component)")
//    public void injectDependencies(JoinPoint joinPoint) throws IllegalArgumentException, IllegalAccessException  {
//        Object target = joinPoint.getTarget();
//        Class<?> targetClass = target.getClass();
//
//		for(Field field : targetClass.getDeclaredFields()) {
//			Wire wire = field.getAnnotation(Wire.class);
//			if(wire != null) {
//				String beanName = field.getType().getSimpleName();
//				if(!wire.value().isEmpty()) {
//					beanName = wire.value();
//				}
//				if(!field.canAccess(target)) {
//					field.setAccessible(true);
//				}
//				field.set(target, BeanStore.getOrCreateBean(field.getType(), beanName));
//			}
//		}
//    }
//}


