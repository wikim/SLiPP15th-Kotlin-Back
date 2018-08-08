package net.slipp.fifth.kotlin.common.pagination;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.WebUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import net.slipp.fifth.kotlin.web.support.SortPropertyEditorSupport;

@Slf4j
@Component
public class PageStatusFactory {

    private static final String[] ignoreParameters = new String[] { "_method" };
    private static final String[] ignorePageableField = new String[] { "pageNumber", "pageSize" };
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String REQUEST_ATTRIBUTE_NAME = PageStatusFactory.class.getName();
    private static final String DEFAULT_PREFIX = "ps";
    private static final String DEFAULT_SEPARATOR = ".";
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_SIZE_NUMBER = 300;

    private String prefix = DEFAULT_PREFIX;
    private String separator = DEFAULT_SEPARATOR;
    private int defaultPageNumber = DEFAULT_PAGE_NUMBER;
    private int defaultSizeNumber = DEFAULT_SIZE_NUMBER;

    public void setPrefix(String prefix) {
        this.prefix = null == prefix ? DEFAULT_PREFIX : prefix;
    }

    public void setSeparator(String separator) {
        this.separator = null == separator ? DEFAULT_SEPARATOR : separator;
    }

    public void setDefaultPageNumber(int defaultPageNumber) {
        this.defaultPageNumber = defaultPageNumber <= 0 ? DEFAULT_PAGE_NUMBER : defaultPageNumber;
    }

    public void setDefaultSizeNumber(int defaultSizeNumber) {
        this.defaultSizeNumber = defaultSizeNumber <= 0 ? DEFAULT_SIZE_NUMBER : defaultSizeNumber;
    }

    public PageStatus create(WebRequest request) {
        NativeWebRequest webRequest = (NativeWebRequest) request;
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // request에 저장된 pageStatus가 있으면 꺼내기
        PageStatus pageStatus = (PageStatus) request.getAttribute(REQUEST_ATTRIBUTE_NAME, WebRequest.SCOPE_REQUEST);

        // FlashScope에서 pageStatus 생성
        if (pageStatus == null) {
            pageStatus = createFromFlashMap(servletRequest);
            request.setAttribute(REQUEST_ATTRIBUTE_NAME, pageStatus, WebRequest.SCOPE_REQUEST);

            log.debug("found pageStatus in flashMap: {}", pageStatus);
        }

        if (pageStatus == null) {
            // pageable 값 초기화 및 prefix가 붙은 파라메터들을 추출
            pageStatus = initPageableValueFromServletRequest(servletRequest, prefix, separator);
            Map<String, Object> parameters = WebUtils.getParametersStartingWith(servletRequest, prefix + separator);
            if (CollectionUtils.isEmpty(parameters)) {
                // prefix가 붙은 파라메터들이 없으면 request에 담겨있는 모든 파라메터를 대상으로 추출
                pageStatus = initPageableValueFromServletRequest(servletRequest, null, null);
                parameters = WebUtils.getParametersStartingWith(servletRequest, null);
                if (pageStatus.getSort() != null) {
                    parameters.put("sort", pageStatus.getSort());
                }
            }

            // 불필요한 값은 제거
            Map<String, Object> attributes = Maps.filterKeys(parameters, new Predicate<String>() {
                @Override
                public boolean apply(String input) {
                    return !CollectionUtils.containsInstance(Lists.newArrayList(ignoreParameters), input);
                }
            });
            String queryString = createQueryString(attributes, ignorePageableField);
            String pageableQueryString = createQueryString(attributes, new String[0]);
            String jsonString = createJsonString(attributes, ignorePageableField);
            String pageableJsonString = createJsonString(attributes, new String[0]);

            pageStatus = new PageStatus(pageStatus.getPageNumber(), pageStatus.getPageSize(), pageStatus.getSort(),
                    attributes, queryString, pageableQueryString, jsonString, pageableJsonString);

            request.setAttribute(REQUEST_ATTRIBUTE_NAME, pageStatus, WebRequest.SCOPE_REQUEST);

            log.debug("created pageStatus from request: {}", pageStatus);
        }

        return pageStatus;
    }

    // FlashScope로 넘어온 PageStatus 값을 찾아서 생성
    private PageStatus createFromFlashMap(HttpServletRequest request) {
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
        if (!CollectionUtils.isEmpty(flashMap)) {
            for (Object value : flashMap.values()) {
                if (ClassUtils.isAssignableValue(PageStatus.class, value)) {
                    return (PageStatus) value;
                }
            }
        }
        return null;
    }

    private PageStatus createFromDefaultConfig() {
        return new PageStatus(defaultPageNumber, defaultSizeNumber, null, null, "{}", "{}", "{}", "{}");
    }

    private PageStatus initPageableValueFromServletRequest(HttpServletRequest request, String prefix,
            String separator) {
        PageStatus pageStatus = createFromDefaultConfig();

        ServletRequestParameterPropertyValues propertyValues = new ServletRequestParameterPropertyValues(request,
                prefix, separator);

        DataBinder binder = new ServletRequestDataBinder(pageStatus);

        binder.initDirectFieldAccess();
        binder.registerCustomEditor(Sort.class, new SortPropertyEditorSupport("sort.dir", propertyValues));
        binder.bind(propertyValues);

        return pageStatus;
    }

    /**
     * 
     * @param attributes
     * @param ignoreFields
     * @return
     */
    private String createQueryString(Map<String, Object> attributes, String[] ignoreFields) {
        StringBuilder queryString = new StringBuilder();

        for (final Entry<String, Object> entry : attributes.entrySet()) {
            if (!hasIgnoreProperties(ignoreFields, entry)) {
                if (queryString.length() != 0) {
                    queryString.append("&");
                }

                if (!"sort".contains(entry.getKey())) {
                	
                	if (entry.getValue().getClass().isArray()){
                		queryString.append(entry.getKey()).append("=").append( StringUtils.join((Object[]) entry.getValue(), ",") );
                	} else {
                		queryString.append(entry.getKey()).append("=").append(entry.getValue());
                	}
                	
                } else {
                    queryString.append(extractSortQuery(entry));
                }
            }
        }

        return queryString.toString();
    }

    private StringBuilder extractSortQuery(final Entry<String, Object> entry) {
        StringBuilder sortQuery = new StringBuilder();
        String[] sortElements = ((Sort) entry.getValue()).iterator().next().getProperty().split(",");
        int sortElementsLength = sortElements.length;
        int nextIterator = 0;

        for (int i = 0; i < sortElementsLength; i++) {
            if (sortQuery.length() != 0) {
                sortQuery.append("&");
            }

            sortQuery.append("sort=").append(sortElements[i]);
            nextIterator = i + 1;
            if (isLengthInner(sortElementsLength, nextIterator)
                    && ("asc".equals(sortElements[nextIterator].toLowerCase().trim())
                            || "desc".equals(sortElements[nextIterator].toLowerCase().trim()))) {
                sortQuery.append(",").append(sortElements[nextIterator]);
                i++;
            }
        }
        return sortQuery;
    }

    private boolean isLengthInner(int sortElementsLength, int nextIterator) {
        return nextIterator + 1 <= sortElementsLength;
    }

    private boolean hasIgnoreProperties(String[] ignoreFields, final Entry<String, Object> entry) {
        return Iterables.any(Lists.newArrayList(ignoreFields), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains(entry.getKey());
            }
        });
    }

    @SuppressWarnings("serial")
    private String createJsonString(Map<String, Object> attributes, String[] ignoreFields) {
        try {
            Map<String, Object> clone = new HashMap<String, Object>(attributes);
            for (String ifnoreField : ignoreFields) {
                clone.remove(ifnoreField);
            }
            return objectMapper.writeValueAsString(clone);
        } catch (Exception e) {
            throw new ConversionException("convert error - pageStatus attributes to json string", e) {
            };
        }
    }
}
