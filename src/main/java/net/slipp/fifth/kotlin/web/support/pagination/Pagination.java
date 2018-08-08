package net.slipp.fifth.kotlin.web.support.pagination;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Sort;

import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class Pagination<T> implements Serializable {
    private static final long serialVersionUID = -8172393682787084700L;
    private static final long FIRST_PAGE_NUMBER = 0L;
    private static final long DEFAULT_BLOCK_SIZE = 10L;
    
    private final List<T> contents = Lists.newArrayList();
    private final int pageNumber;
    private final int pageSize;
    private final Sort sort;
    private final long total;
    private long currentPageNumber;
    private long beginPageNumber;
    private long endPageNumber;
    

    public Pagination(List<T> content, int pageNumber, int pageSize, Sort sort, long total) {
        if (null == content) {
            throw new IllegalArgumentException("system.exception.page-content.must.not-null");
        }

        this.contents.addAll(content);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sort = sort;
        this.total = total;

        this.currentPageNumber = getPageNumber() + 1;
        this.endPageNumber = (long) (Math.ceil(currentPageNumber / new Double(DEFAULT_BLOCK_SIZE)) * DEFAULT_BLOCK_SIZE);
        this.beginPageNumber = endPageNumber - DEFAULT_BLOCK_SIZE + 1;
        this.endPageNumber = endPageNumber > getTotalPages() ? getTotalPages() : endPageNumber;
    }

    public int getTotalPages() {
        return getPageSize() == 0 ? 0 : (int) Math.ceil((double) total / (double) getPageSize());
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public int getContentsSize() {
        return null != contents ? contents.size() : 0;
    }

    public boolean hasContents() {
        return null == contents ? false : !contents.isEmpty();
    }

    public List<T> getContents() {
        return contents;
    }

    public Sort getSort() {
        return sort;
    }

    public long getTotal() {
        return total;
    }

    public long getBlockSize() {
        return DEFAULT_BLOCK_SIZE;
    }

    public long getCurrentPageNumber() {
        return currentPageNumber;
    }

    public long getBeginPageNumber() {
        return beginPageNumber;
    }

    public long getEndPageNumber() {
        return endPageNumber;
    }

    public boolean isFirstBlock() {
        return beginPageNumber == 1;
    }

    public boolean isPrevBlock() {
        return beginPageNumber > DEFAULT_BLOCK_SIZE;
    }

    public boolean isNextBlock() {
        return getTotalPages() > endPageNumber;
    }
    
    public boolean isLastBlock() {
        return getEndPageNumber() == getTotalPages();
    }

    public Iterator<T> iterator() {
        return contents.iterator();
    }
    
    public long getFirstPageNumber() {
        return FIRST_PAGE_NUMBER;
    }
    
    public long getLastPageNumber() {
        return getTotalPages() == 0L ? 0L : getTotalPages() - 1L;
    }
}
