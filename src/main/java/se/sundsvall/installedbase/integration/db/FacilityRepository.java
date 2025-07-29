package se.sundsvall.installedbase.integration.db;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;

public interface FacilityRepository extends JpaRepository<FacilityEntity, String> {
	Optional<FacilityEntity> findByFacilityIdIgnoreCase(String facilityId);

	List<FacilityEntity> findAllByDelegationsIsEmpty();
}
