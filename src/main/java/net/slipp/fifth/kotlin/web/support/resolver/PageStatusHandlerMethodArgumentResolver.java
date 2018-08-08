package net.slipp.fifth.kotlin.web.support.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import net.slipp.fifth.kotlin.common.pagination.PageStatus;
import net.slipp.fifth.kotlin.common.pagination.PageStatusFactory;

public class PageStatusHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private PageStatusFactory pageStatusFactory;
    @Autowired
    private SortHandlerMethodArgumentResolver sortResolver;

    public void setPageStatusFactory(PageStatusFactory pageStatusFactory) {
        this.pageStatusFactory = pageStatusFactory;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PageStatus.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        PageStatus pageStatus =  pageStatusFactory.create(webRequest);
        Sort sort = sortResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        pageStatus.setSort(sort);
        return pageStatus;
    }

}
