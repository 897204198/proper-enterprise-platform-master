package com.proper.enterprise.platform.core.utils.sort;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanComparator implements Comparator, Serializable {

    public enum Order {
        ASC,
        DESC
    }

    private Map<String, Order> pair = new LinkedHashMap<>();

    public BeanComparator(String... attrs) {
        for (String attr : attrs) {
            pair.put(attr, Order.ASC);
        }
    }

    public BeanComparator(Map<String, Order> pair) {
        Assert.notNull(pair, "The pair of sorted attributes and orders SHOULD NOT NULL!");
        this.pair = pair;
    }

    @Override
    public int compare(Object o1, Object o2) {
        Assert.isTrue(o1.getClass().equals(o2.getClass()), "Should use SAME type objects to compare!");

        BeanWrapper b1 = new BeanWrapperImpl(o1);
        BeanWrapper b2 = new BeanWrapperImpl(o2);

        String attr;
        Order order;
        Object v1, v2;
        BigDecimal d1, d2;
        int result = 0;
        Comparator<String> cnComparator = new CNStrComparator();
        for (Map.Entry<String, Order> entry : pair.entrySet()) {
            attr = entry.getKey();
            order = entry.getValue();
            v1 = b1.getPropertyValue(attr);
            v2 = b2.getPropertyValue(attr);
            if (v1 == null && v2 == null) {
                result = 0;
            } else if (v1 == null) {
                result = -1;
            } else if (v2 == null) {
                result = 1;
            } else if (v1 instanceof Number) {
                d1 = new BigDecimal(v1.toString());
                d2 = new BigDecimal(v2.toString());
                result = d1.compareTo(d2);
            } else if (v1 instanceof String) {
                result = cnComparator.compare((String) v1, (String) v2);
            } else if (v1 instanceof Comparable) {
                result = ((Comparable) v1).compareTo(v2);
            }
            if (result != 0) {
                result = order.equals(Order.ASC) ? result : result * -1;
                break;
            }
        }
        return result;
    }

}
