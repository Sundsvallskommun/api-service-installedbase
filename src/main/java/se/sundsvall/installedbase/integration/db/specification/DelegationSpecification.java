package se.sundsvall.installedbase.integration.db.specification;

import static se.sundsvall.installedbase.integration.db.model.DelegationEntity_.DELEGATED_TO;
import static se.sundsvall.installedbase.integration.db.model.DelegationEntity_.ID;
import static se.sundsvall.installedbase.integration.db.model.DelegationEntity_.MUNICIPALITY_ID;
import static se.sundsvall.installedbase.integration.db.model.DelegationEntity_.OWNER;

import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;

public interface DelegationSpecification {

	static Specification<DelegationEntity> withId(final String id) {
		return buildEqualFilter(ID, id);
	}

	static Specification<DelegationEntity> withMunicipalityId(final String municipalityId) {
		return buildEqualFilter(MUNICIPALITY_ID, municipalityId);
	}

	static Specification<DelegationEntity> withOwner(final String owner) {
		return buildEqualFilter(OWNER, owner);
	}

	static Specification<DelegationEntity> withDelegatedTo(final String delegatedTo) {
		return buildEqualFilter(DELEGATED_TO, delegatedTo);
	}

	private static Specification<DelegationEntity> buildEqualFilter(final String attribute, final String value) {
		return (entity, cq, cb) -> {
			if (value != null) {
				return cb.equal(entity.get(attribute), value);
			}
			return cb.and();
		};
	}
}
