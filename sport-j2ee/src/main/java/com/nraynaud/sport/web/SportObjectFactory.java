package com.nraynaud.sport.web;

import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.spring.SpringObjectFactory;
import com.opensymphony.xwork2.util.OgnlUtil;
import com.opensymphony.xwork2.util.XWorkConverter;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsConstants;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.Map;

public class SportObjectFactory extends SpringObjectFactory {
    private static final Log log = LogFactory.getLog(SportObjectFactory.class);

    @Inject
    public SportObjectFactory(
            @Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_AUTOWIRE,
                    required = false) final String autoWire,
            @Inject(value = StrutsConstants.STRUTS_OBJECTFACTORY_SPRING_USE_CLASS_CACHE,
                    required = false) final String useClassCacheStr,
            @Inject final ServletContext servletContext) {
        final boolean useClassCache = "true".equals(useClassCacheStr);
        log.info("Initializing Struts-Spring integration...");
        final ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if (appContext == null) {
            // uh oh! looks like the lifecycle listener wasn't installed. Let's inform the user
            final String message = "********** FATAL ERROR STARTING UP STRUTS-SPRING INTEGRATION **********\n" +
                    "Looks like the Spring listener was not configured for your web app! \n" +
                    "Nothing will work until WebApplicationContextUtils returns a valid ApplicationContext.\n" +
                    "You might need to add the following to web.xml: \n" +
                    "    <listener>\n" +
                    "        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>\n" +
                    "    </listener>";
            log.fatal(message);
            return;
        }
        setApplicationContext(appContext);
        int type = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;   // default
        if ("name".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
        } else if ("type".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;
        } else if ("auto".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;
        } else if ("constructor".equals(autoWire)) {
            type = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;
        }
        setAutowireStrategy(type);
        setUseClassCache(useClassCache);
        log.info("... initialized Struts-Spring integration successfully");
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Result buildResult(final ResultConfig resultConfig, final Map extraContext) throws Exception {
        final String resultClassName = resultConfig.getClassName();
        Result result = null;
        if (resultClassName != null) {
            result = (Result) buildBean(resultClassName, extraContext);
            setProperties(resultConfig, extraContext, result);
        }
        return result;
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    private static void setProperties(final ResultConfig resultConfig, final Map extraContext, final Result result) {
        //noinspection unchecked
        final Map<String, Object> props = resultConfig.getParams();
        if (props == null) {
            return;
        }
        Ognl.setTypeConverter(extraContext, XWorkConverter.getInstance());
        final Object oldRoot = Ognl.getRoot(extraContext);
        Ognl.setRoot(extraContext, result);
        for (final Map.Entry entry : props.entrySet()) {
            final String expression = (String) entry.getKey();
            try {
                OgnlUtil.setValue(expression, extraContext, result, entry.getValue());
            } catch (OgnlException e) {
                //swallow
            }
            Ognl.setRoot(extraContext, oldRoot);
        }
    }
}
