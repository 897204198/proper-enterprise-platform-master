package com.proper.enterprise.platform.core.support;

import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractQuerySupport {

    private static final String ZERO = "0";

    public boolean isPageSearch() {
        String pageNo = RequestUtil.getCurrentRequest().getParameter("pageNo");
        String pageSize = RequestUtil.getCurrentRequest().getParameter("pageSize");
        if (StringUtils.isEmpty(pageNo)) {
            return false;
        }
        if (StringUtils.isEmpty(pageSize)) {
            return false;
        }
        return true;
    }


    public PageRequest getPageRequest() {
        return getPageRequest(Sort.unsorted());
    }

    public PageRequest getPageRequest(Sort.Direction direction, String... properties) {
        return getPageRequest(new Sort(direction, properties));
    }

    public PageRequest getPageRequest(Sort sort) {
        return buildPage(sort);
    }

    private Sort buildSort(Sort sort) {
        return sort;
    }

    private PageRequest buildPage(Sort sort) {
        return buildPage(RequestUtil.getCurrentRequest(), buildSort(sort));
    }

    private PageRequest buildPage(HttpServletRequest request, Sort sort) {
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        if (StringUtils.isEmpty(pageNo)) {
            throw new ErrMsgException("missing pageNo to buildPage");
        }
        if (StringUtils.isEmpty(pageSize)) {
            throw new ErrMsgException("missing pageSize to buildPage");
        }
        if (ZERO.equals(pageNo)) {
            throw new ErrMsgException("pageNo cant be 0");
        }
        return new PageRequest(Integer.parseInt(pageNo) - 1, Integer.parseInt(pageSize), sort);
    }
}
