package com.opspot.specification;

import com.opspot.dto.marathon.MarathonFilterParams;
import com.opspot.entity.Marathon;
import com.opspot.enums.ApplicationStatus;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MarathonSpecification {

    public static Specification<Marathon> withFilters(MarathonFilterParams params) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (params.getDistanceCategory() != null && !params.getDistanceCategory().isBlank()) {
                query.distinct(true);
                ListJoin<Marathon, String> dcJoin = root.joinList("distanceCategories", JoinType.LEFT);
                predicates.add(cb.like(cb.lower(dcJoin), "%" + params.getDistanceCategory().toLowerCase() + "%"));
            }
            if (params.getIsFree() != null) {
                predicates.add(cb.equal(root.get("isFree"), params.getIsFree()));
            }
            if (params.getMaxFee() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("entryFee"), params.getMaxFee()));
            }
            if (params.getDateFrom() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), params.getDateFrom()));
            }
            if (params.getDateTo() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), params.getDateTo()));
            }
            if (!params.isShowRejected()) {
                predicates.add(cb.notEqual(root.get("status"), ApplicationStatus.REJECTED));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private MarathonSpecification() {}
}
