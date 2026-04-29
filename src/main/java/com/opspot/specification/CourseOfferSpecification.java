package com.opspot.specification;

import com.opspot.dto.course.CourseOfferFilterParams;
import com.opspot.entity.CourseOffer;
import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CourseOfferSpecification {

    public static Specification<CourseOffer> withFilters(CourseOfferFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getPlatform() != null && !params.getPlatform().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("platform")),
                        "%" + params.getPlatform().toLowerCase() + "%"));
            }
            if (params.getTopic() != null && !params.getTopic().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("topic")),
                        "%" + params.getTopic().toLowerCase() + "%"));
            }
            if (params.getIsFree() != null) {
                predicates.add(cb.equal(root.get("isFree"), params.getIsFree()));
            }
            if (params.getMaxPrice() != null) {
                // free courses pass through; for paid, check discountedPrice <= maxPrice
                predicates.add(cb.or(
                        cb.isTrue(root.get("isFree")),
                        cb.lessThanOrEqualTo(root.get("discountedPrice"), params.getMaxPrice())
                ));
            }
            if (!params.isShowRejected()) {
                predicates.add(cb.notEqual(root.get("status"), ApplicationStatus.REJECTED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private CourseOfferSpecification() {}
}
