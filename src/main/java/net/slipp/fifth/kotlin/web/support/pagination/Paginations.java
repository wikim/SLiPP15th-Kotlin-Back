package net.slipp.fifth.kotlin.web.support.pagination;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.slipp.fifth.kotlin.common.mapper.ExtensibleModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Paginations {
    public static Pagination<Object> empty(Pageable pageable) {
        return new Pagination<Object>(Collections.emptyList(), pageable.getPageNumber(), pageable.getPageSize(),
                pageable.getSort(), 0);
    }

    /**
     * 페이징 처리되지 않은 Collection을 페이징처리한다.
     *
     * @param contents
     *            Collections data
     * @param pageable
     * @return
     */
    public static <D> Pagination<D> pagination(List<D> contents, Pageable pageable) {
        return new Pagination<D>(contents, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort(),
                contents.size());
    }

    /**
     * 프레임워크에 의해 페이징 처리된 {@link Page} 를 {@link Pagination}으로 변환한다.
     *
     * @param page
     * @return
     */
    public static <D> Pagination<D> pagination(Page<D> page) {
        return new Pagination<D>(page.getContent(), page.getNumber(), page.getSize(), page.getSort(),
                page.getTotalElements());
    }

    /**
     * Page의 클래스를 destinationType으로 변환하여 페이징처리를 한다.
     *
     * @param page
     * @param destinationType
     * @return
     */
    public static <D> Pagination<D> transform(Page<?> page, Class<D> destinationType) {
        ExtensibleModelMapper modelMapper = new ExtensibleModelMapper();
        List<D> content = Lists.newArrayList();
        if (!page.getContent().isEmpty()) {
            content = modelMapper.map(page.getContent(), destinationType);
        }

        return new Pagination<D>(content, page.getNumber(), page.getSize(), page.getSort(), page.getTotalElements());
    }
}
