package com.proper.enterprise.platform.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class SearchConditionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchConditionBuilder.class);

    public static <T> Specification<T> build(Class<T> clz, final SearchCondition... conditions) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                int len = conditions.length;
                List<Predicate> predicates = new ArrayList<Predicate>(len);
                List<Order> orders = new ArrayList<Order>(len);
                String[] names;
                Path expression;
                for (SearchCondition condition : conditions) {
                    names = condition.getField().split("\\.");
                    expression = root;
                    for (int i = 0; i < names.length; i++) {
                        expression = expression.get(names[i]);
                    }
                    switch (condition.getOperator()) {
                        case EQ:
                            predicates.add(cb.equal(expression, condition.getValue()));
                            break;
                        case LIKE:
                            predicates.add(cb.like(expression, "%" + condition.getValue() + "%"));
                            break;
                        case LT:
                            predicates.add(cb.lessThan(expression, (Comparable) condition.getValue()));
                            break;
                        case GT:
                            predicates.add(cb.greaterThan(expression, (Comparable) condition.getValue()));
                            break;
                        case LE:
                            predicates.add(cb.lessThanOrEqualTo(expression, (Comparable) condition.getValue()));
                            break;
                        case GE:
                            predicates.add(cb.greaterThanOrEqualTo(expression, (Comparable) condition.getValue()));
                            break;
                        case ASC:
                            orders.add(cb.asc(expression));
                            break;
                        case DESC:
                            orders.add(cb.desc(expression));
                            break;
                        case NOTNULL:
                            predicates.add(cb.isNotNull(expression));
                            break;
                        case IN:
                            predicates.add(expression.in(condition.getValue()));
                            break;
                        default:
                            LOGGER.debug("Not supported operator: {}", condition.getOperator());
                            break;
                    }
                }

                if (!orders.isEmpty()) {
                    query.orderBy(orders);
                }
                return predicates.isEmpty()
                        ? cb.conjunction()
                        : cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
