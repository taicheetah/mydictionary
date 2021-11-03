package com.taicheetah.mydictionary.aop;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jboss.logging.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static Logger logger = Logger.getLogger(LoggingAspect.class.getName());
	
	@Pointcut("execution(* com.taicheetah.mydictionary.service.*.*(..))")
	public void pointcutService() {};
	
	@Pointcut("execution(* com.taicheetah.mydictionary.service.UserDetailsServiceImpl.*(..))")
	public void pointcutUserDetailsService() {};
	
	@Around("pointcutService() && ! pointcutUserDetailsService()")
	public Object aroundService(ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
		
		// method name
		MethodSignature methodSig = (MethodSignature) theProceedingJoinPoint.getSignature();
		logger.info("=====>>> START : " + methodSig);
		
		// args
		Object[] args = theProceedingJoinPoint.getArgs();
		
		String argsStr = "=====> Args :";
		if(args != null) {
			for(Object tempArg : args) {
				argsStr+= " " + tempArg.toString() + ",";
			}
			argsStr = StringUtils.removeEnd(argsStr, ",");
		}
		logger.info(argsStr);
		
		// time (start)
		long begin = System.currentTimeMillis();
		Object result = null;
		try{
			// execute
			result = theProceedingJoinPoint.proceed();
		} catch (Exception exc) {
			logger.error("ERROR", exc);
			
			// rethrow
			throw exc;
		}
		// time (end)
		long end = System.currentTimeMillis();
		
		// log separating by type of return
		if(result instanceof Page) {
			Page page = (Page) result;
			logger.info("=====> Return : ");
			logger.info("=> PageNumber : " + page.getNumber() + ", TotalPages : " + page.getTotalPages() + ", TotalElements : " + page.getTotalElements());
			List list = page.getContent();
			for(Object temp : list) {
				logger.info("=> " + temp);
			}
		} else if(result instanceof List) {
			logger.info("=====> Return : ");
			List list = (List) result;
			for(Object temp : list) {
				logger.info("=> " + temp);
			}
		} else {
			logger.info("=====> Return : " + result);
		}
		
		long duration = end - begin;
		logger.info("=====> Duration: " + duration + "milliseconds");
		
		logger.info("=====>>> END :" + methodSig + "\n");
		
		return result;
	}
}
