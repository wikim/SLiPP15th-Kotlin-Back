package net.slipp.fifth.kotlin.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.NoArgsConstructor;

/**
 * Autowired를 할 수 없는 영역에서 Autowired를 해주는 헬퍼클래스
 *
 * @author jiheon<br/>
 *         {@link http://guylabs.ch/2014/02/22/autowiring-pring-beans-in-hibernate-jpa-entity-listeners/}
 *
 */
@NoArgsConstructor
public class AutowireHelper implements ApplicationContextAware {

    private static final AutowireHelper INSTANCE = new AutowireHelper();
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * Tries to autowire the specified instance of the class if one of the
     * specified beans which need to be autowired are null.
     *
     * @param classToAutowire
     *            the instance of the class which holds @Autowire annotations
     * @param beansToAutowireInClass
     *            the beans which have the @Autowire annotation in the specified
     *            {#classToAutowire}
     */
    public static void autowire(Object classToAutowire, Object... beansToAutowiredInClass) {
        for (Object bean : beansToAutowiredInClass) {
            if (null == bean) {
                APPLICATION_CONTEXT.getAutowireCapableBeanFactory().autowireBean(classToAutowire);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AutowireHelper.APPLICATION_CONTEXT = applicationContext;
    }

    public static AutowireHelper getInstance() {
        return INSTANCE;
    }
}
