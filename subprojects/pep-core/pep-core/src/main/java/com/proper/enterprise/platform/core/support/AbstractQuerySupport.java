package com.proper.enterprise.platform.core.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.proper.enterprise.platform.core.PEPConstants;
import com.proper.enterprise.platform.core.domain.PEPOrder;
import com.proper.enterprise.platform.core.exception.ErrMsgException;
import com.proper.enterprise.platform.core.utils.CollectionUtil;
import com.proper.enterprise.platform.core.utils.JSONUtil;
import com.proper.enterprise.platform.core.utils.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractQuerySupport {

    private static Logger logger = LoggerFactory.getLogger(AbstractQuerySupport.class);

    private static final String ZERO = "0";

    public boolean isPageSearch() {
        String pageNo = RequestUtil.getCurrentRequest().getParameter("pageNo");
        String pageSize = RequestUtil.getCurrentRequest().getParameter("pageSize");
        if (StringUtils.isEmpty(pageNo)) {
            return false;
        }
        return !StringUtils.isEmpty(pageSize);
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

    private Sort buildSort(HttpServletRequest request) {
        String orders = request.getParameter("orders");
        if (StringUtils.isEmpty(orders)) {
            return null;
        }
        try {
            orders = URLDecoder.decode(orders, PEPConstants.DEFAULT_CHARSET.name());
        } catch (UnsupportedEncodingException e) {
            logger.error("buildSort decoder error sort:{}", orders, e);
            throw new ErrMsgException("buildSort decoder error sort:" + orders);
        }
        try {
            List<PEPOrder> sortOrders = JSONUtil.parse(orders, new TypeReference<List<PEPOrder>>() {
            });
            return new Sort(convert(sortOrders));
        } catch (IOException e) {
            logger.error("buildSort error sort:{}", orders, e);
            throw new ErrMsgException("buildSort error sort:" + orders);
        }
    }


    private PageRequest buildPage(Sort sort) {
        HttpServletRequest request = RequestUtil.getCurrentRequest();
        Sort buildSort = buildSort(request);
        return buildPage(request, null == buildSort ? sort : buildSort.and(sort));
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

    private List<Sort.Order> convert(List<PEPOrder> pepOrders) {
        List<Sort.Order> orders = new ArrayList<>();
        if (CollectionUtil.isEmpty(pepOrders)) {
            return orders;
        }
        for (PEPOrder pepOrder : pepOrders) {
            orders.add(new Sort.Order(pepOrder.getDirection(), pepOrder.getProperty()));
        }
        return orders;
    }
}
