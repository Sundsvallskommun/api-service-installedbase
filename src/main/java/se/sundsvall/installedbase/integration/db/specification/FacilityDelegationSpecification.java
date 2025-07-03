package se.sundsvall.installedbase.integration.db.specification;

import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.DELEGATED_TO;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.ID;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.MUNICIPALITY_ID;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.OWNER;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.STATUS;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public interface FacilityDelegationSpecification {

	static Specification<FacilityDelegationEntity> withId(final String id) {
		return buildEqualFilter(ID, id);
	}

	static Specification<FacilityDelegationEntity> withMunicipalityId(final String municipalityId) {
		return buildEqualFilter(MUNICIPALITY_ID, municipalityId);
	}

	static Specification<FacilityDelegationEntity> withOwner(final String owner) {
		return buildEqualFilter(OWNER, owner);
	}

	static Specification<FacilityDelegationEntity> withDelegatedTo(final String delegatedTo) {
		return buildEqualFilter(DELEGATED_TO, delegatedTo);
	}

	static Specification<FacilityDelegationEntity> withStatus(final String status) {
		return buildEqualFilter(STATUS, status);
	}

	private static Specification<FacilityDelegationEntity> buildEqualFilter(final String attribute, final String value) {
		return (entity, cq, cb) -> {
			if (value != null) {
				return cb.equal(entity.get(attribute), value);
			}
			return cb.and();
		};
	}
}
