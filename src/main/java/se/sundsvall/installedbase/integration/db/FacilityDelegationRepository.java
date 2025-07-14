package se.sundsvall.installedbase.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.installedbase.integration.db.model.FacilityDelegationEntity;

public interface FacilityDelegationRepository extends JpaRepository<FacilityDelegationEntity, String>, JpaSpecificationExecutor<FacilityDelegationEntity> {
}
