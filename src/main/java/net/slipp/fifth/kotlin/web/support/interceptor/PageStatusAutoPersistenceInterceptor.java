package net.slipp.fifth.kotlin.web.support.interceptor;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.ui.ModelMap;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.pagination.PageStatusFactory;

@Slf4j
public class PageStatusAutoPersistenceInterceptor implements WebRequestInterceptor {
    public static final String DEFAULT_ATTRIBUTE_NAME = "pageStatus";

    @Inject
    PageStatusFactory pageStatusFactory;

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        if (model != null) {
            PageStatus pageStatus = findPageStatus(model.values());
            if (pageStatus == null) {
                pageStatus = pageStatusFactory.create(request);
                model.addAttribute(DEFAULT_ATTRIBUTE_NAME, pageStatus);

                log.debug("model add {}", pageStatus);
            }
        }
    }

    private PageStatus findPageStatus(Collection<?> values) {
        for (Object value : values) {
            if (ClassUtils.isAssignableValue(PageStatus.class, value)) {
                return (PageStatus) value;
            }
        }
        return null;
    }

    @Override
    public void preHandle(WebRequest request) throws Exception {
        // nothing
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // nothing
    }
}
