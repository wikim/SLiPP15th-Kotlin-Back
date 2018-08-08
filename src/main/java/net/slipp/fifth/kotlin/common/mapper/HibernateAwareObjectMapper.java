package net.slipp.fifth.kotlin.common.mapper;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * <pre>
 * HibernateAwareObjectMapper.java
 * </pre>
 * 
 * @Version  :
 */
public class HibernateAwareObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -8821453185971012130L;

    public HibernateAwareObjectMapper() {
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        registerModule(getHibernate5Module());
    }

    private Module getHibernate5Module() {
    	Hibernate5Module hibernateModule = new Hibernate5Module();
        hibernateModule.disable(Hibernate5Module.Feature.FORCE_LAZY_LOADING);
        return hibernateModule;
    }
}
