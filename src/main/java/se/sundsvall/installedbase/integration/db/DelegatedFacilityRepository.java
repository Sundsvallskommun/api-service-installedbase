package se.sundsvall.installedbase.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.installedbase.integration.db.model.DelegatedFacilityEntity;

public interface DelegatedFacilityRepository extends JpaRepository<DelegatedFacilityEntity, Long> {
}
