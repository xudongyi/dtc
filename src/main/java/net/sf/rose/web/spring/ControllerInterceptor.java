package net.sf.rose.web.spring;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sf.rose.jdbc.UserBean;
import net.sf.rose.jdbc.service.Service;
import net.sf.rose.util.ConstantCode;

public class ControllerInterceptor {

	private static Logger LOG = Logger.getLogger(ControllerInterceptor.class);
	private static String SESSION_SERVICE = "SESSION_SERVICE";

	/**
	 * 功能：借助Spring的AOP方法，截获所有Action类
	 * 如果需要，注入数据库连接服务类，并全面负责数据连接管理
	 * @param pjp - 切入点
	 * @return 执行结果
	 * @throws Throwable - 过程发生异常时抛出
	 */
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		// String message = "excute class: " + pjp.getTarget().getClass().getName() + "'s method:" + pjp.getSignature().getName();
		// System.out.println(message);
		// long time = System.currentTimeMillis();
		Object[] arg = pjp.getArgs();
		Service service = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			for (Object obj : arg) {
				if (obj != null && obj instanceof Service) {
					service = (Service) obj;
					obj = request.getSession().getAttribute(ConstantCode.USER_BEAN_NAME);
					if (obj != null) {
						UserBean user = (UserBean) obj;
						service.setUser(user);
					}
					request.setAttribute(SESSION_SERVICE, service);
				}
			}
		}
		Object retVal = pjp.proceed(arg);
		if (service != null) {
			service.close();
		}
		// time = System.currentTimeMillis() - time;
		// System.out.println(message + " ending...   process time: " + time + " ms");
		return retVal;
	}

	/**
	 * 功能：1.抛出异常时关闭数据库连接
	 * 2.捕获所有的异常，记录日志，并处理后返回信息给前台用户
	 * @param jp - 切入点
	 * @param ex - 异常
	 * @throws Throwable - 过程中抛出的异常
	 */
	public void doThrowing(JoinPoint jp, Throwable ex) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Object obj = request.getAttribute(SESSION_SERVICE);
		if (obj != null) {
			Service service = (Service) obj;
			service.rollback();
			service.close();
			LOG.error(ex.getMessage(), ex);
		}
    }

}
