package application;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("SpringUtils")
public class SpringUtils implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    SpringUtils.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * GetBean
   * 
   * @param bean id
   * @return Object Bean object
   */
  public static Object getBean(String beanId) {
    if (applicationContext != null) {
      try {
        return applicationContext.getBean(beanId);
      } catch (BeansException e) {
        e.printStackTrace();
        return null;
      }
    } else {
      return null;
    }
  }
}