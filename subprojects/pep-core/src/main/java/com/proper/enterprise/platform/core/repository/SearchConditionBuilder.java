package com.proper.enterprise.platform.core.repository;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

public class SearchConditionBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchConditionBuilder.class);

    public static <T> Specification<T> build(final SearchCondition... conditions) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = Lists.newArrayListWithCapacity(conditions.length);
                for (SearchCondition condition : conditions) {
                    Path expression = root.get(condition.getField());
                    switch (condition.getOperator()) {
                        case EQ:
                            predicates.add(cb.equal(expression, condition.getValue()));
                            break;
                        case LIKE:
                            predicates.add(cb.like(expression, "%" + condition.getValue() + "%"));
                            break;
                        case LT:
                            predicates.add(cb.lessThan(expression, (Comparable)condition.getValue()));
                            break;
                        case GT:
                            predicates.add(cb.greaterThan(expression, (Comparable)condition.getValue()));
                            break;
                        case LE:
                            predicates.add(cb.lessThanOrEqualTo(expression, (Comparable)condition.getValue()));
                            break;
                        case GE:
                            predicates.add(cb.greaterThanOrEqualTo(expression, (Comparable)condition.getValue()));
                            break;
                        default:
                            LOGGER.debug("Not supported operator: {}", condition.getOperator());
                            break;
                    }
                }

                return predicates.isEmpty() ?
                        cb.conjunction() : cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
