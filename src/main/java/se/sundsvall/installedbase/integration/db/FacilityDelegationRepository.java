package se.sundsvall.installedbase.integration.db;

import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withDelegatedTo;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withMunicipalityId;
import static se.sundsvall.installedbase.integration.db.specification.FacilityDelegationSpecification.withOwner;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public interface FacilityDelegationRepository extends JpaRepository<FacilityDelegationEntity, String>, JpaSpecificationExecutor<FacilityDelegationEntity> {

	default Optional<FacilityDelegationEntity> findOne(String municipalityId, String id, String owner, String delegatedTo) {
		return findOne(Specification.where(withId(id))
			.and(withMunicipalityId(municipalityId))
			.and(withOwner(owner))
			.and(withDelegatedTo(delegatedTo)));
	}

	default Optional<FacilityDelegationEntity> findOne(String municipalityId, String id, String owner) {
		return findOne(municipalityId, id, owner, null);
	}

	default Optional<FacilityDelegationEntity> findOne(String municipalityId, String id) {
		return findOne(municipalityId, id, null, null);
	}
}
