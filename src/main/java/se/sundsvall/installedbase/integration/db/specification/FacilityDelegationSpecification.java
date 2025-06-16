package se.sundsvall.installedbase.integration.db.specification;

import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.DELEGATED_TO;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.ID;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.MUNICIPALITY_ID;
import static se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity_.OWNER;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public interface FacilityDelegationSpecification {

	SpecificationBuilder<FacilityDelegationEntity> BUILDER = new SpecificationBuilder<>();

	static Specification<FacilityDelegationEntity> withId(final String id) {
		return BUILDER.buildEqualFilter(ID, id);
	}

	static Specification<FacilityDelegationEntity> withMunicipalityId(final String municipalityId) {
		return BUILDER.buildEqualFilter(MUNICIPALITY_ID, municipalityId);
	}

	static Specification<FacilityDelegationEntity> withOwner(final String owner) {
		return BUILDER.buildEqualFilter(OWNER, owner);
	}

	static Specification<FacilityDelegationEntity> withDelegatedTo(final String delegatedTo) {
		return BUILDER.buildEqualFilter(DELEGATED_TO, delegatedTo);
	}
}
