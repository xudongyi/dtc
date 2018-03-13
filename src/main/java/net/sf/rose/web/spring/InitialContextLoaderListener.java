
package net.sf.rose.web.spring;

import javax.servlet.ServletContextEvent;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import net.sf.rose.initial.BootStart;
import net.sf.rose.jdbc.bean.TableBean;
import net.sf.rose.jdbc.bean.annotation.Table;
import net.sf.rose.jdbc.service.Service;

/**
 * 自定义Spring的ContextLoaderListener
 *
 */
public class InitialContextLoaderListener extends ContextLoaderListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
    	String projectName = event.getServletContext().getServletContextName();
        // 接管系统的启动
    	BootStart.initial(projectName);
        super.contextInitialized(event);
        // 遍历所有Spring扫描到的类，如果是数据库表关系映射类就与数据库同步
        Service service = new Service();
		WebApplicationContext wc = super.getCurrentWebApplicationContext();
		boolean autoCreateTable = BootStart.getInstance().isAutoCreateTable();
		for (String name : wc.getBeanDefinitionNames()) {
			Object obj = wc.getBean(name);
			Class<?> clazz = obj.getClass();
			if (clazz.getAnnotation(Table.class) != null) {
				TableBean tableBean = Service.getTableBean(clazz);
				// 如果需要同步数据库表结构，而且是表
				if (autoCreateTable && !tableBean.isView()) {
					service.createDataBaseTable(tableBean);
				}
			}
		}
		service.close();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //接管系统的关闭
    	BootStart.destroyed();
        super.contextDestroyed(event);
    }
}