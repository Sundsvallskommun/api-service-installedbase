package se.sundsvall.installedbase.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;

public interface DelegationRepository extends JpaRepository<DelegationEntity, String>, JpaSpecificationExecutor<DelegationEntity> {
}
