package se.sundsvall.installedbase.api.model.delegate;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@Schema(description = "FacilityDelegation model")
public class FacilityDelegation {

	@Schema(description = "Unique identifier for the delegation", example = "12345678-1234-1234-1234-123456789012", accessMode = READ_ONLY)
	private String id;

	@NotEmpty(message = "facilities must contain at least one facility")
	@ArraySchema(schema = @Schema(implementation = String.class, description = "List of facility IDs to be delegated", example = "[\"facility1\", \"facility2\"]", requiredMode = REQUIRED))
	private List<@NotBlank(message = "Facility cannot be blank") String> facilities;

	@Schema(description = "Organization number of the company owning the facility", example = "5591628136", requiredMode = NOT_REQUIRED)
	private String businessEngagementOrgId;

	@ValidUuid
	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = REQUIRED)
	private String delegatedTo;

	@ValidUuid
	@Schema(description = "Party ID of the delegation owner", example = "81471222-5798-11e9-ae24-57fa13b361e1", requiredMode = REQUIRED)
	private String owner;

	// Status will be determined by the operation performed via the API.
	// ACTIVE == /post, DELETED == /delete
	@Schema(description = "Status of the delegation", examples = {
		"ACTIVE | DELETED"
	}, example = "ACTIVE", accessMode = READ_ONLY)
	private String status;

	@Schema(description = "Municipality ID for the delegation", example = "1234", accessMode = READ_ONLY)
	private String municipalityId;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "When the delegation was created", example = "2025-01-01T12:00:00", accessMode = READ_ONLY)
	private LocalDateTime created;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "When the delegation was last updated", example = "2025-04-01T12:00:00", accessMode = READ_ONLY)
	private LocalDateTime updated;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "When the delegation was deleted", example = "2025-06-01T12:00:00", accessMode = READ_ONLY)
	private LocalDateTime deleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<String> facilities) {
		this.facilities = facilities;
	}

	public String getBusinessEngagementOrgId() {
		return businessEngagementOrgId;
	}

	public void setBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
	}

	public String getDelegatedTo() {
		return delegatedTo;
	}

	public void setDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}

	public LocalDateTime getDeleted() {
		return deleted;
	}

	public void setDeleted(LocalDateTime deleted) {
		this.deleted = deleted;
	}

	public FacilityDelegation withId(String id) {
		this.id = id;
		return this;
	}

	public FacilityDelegation withFacilities(List<String> facilities) {
		this.facilities = facilities;
		return this;
	}

	public FacilityDelegation withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public FacilityDelegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public FacilityDelegation withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public FacilityDelegation withStatus(String status) {
		this.status = status;
		return this;
	}

	public FacilityDelegation withCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public FacilityDelegation withUpdated(LocalDateTime updated) {
		this.updated = updated;
		return this;
	}

	public FacilityDelegation withDeleted(LocalDateTime deleted) {
		this.deleted = deleted;
		return this;
	}

	public FacilityDelegation withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FacilityDelegation facilityDelegation))
			return false;
		return Objects.equals(id, facilityDelegation.id) && Objects.equals(facilities, facilityDelegation.facilities) && Objects.equals(businessEngagementOrgId, facilityDelegation.businessEngagementOrgId) && Objects.equals(delegatedTo,
			facilityDelegation.delegatedTo) && Objects.equals(owner, facilityDelegation.owner) && Objects.equals(status, facilityDelegation.status) && Objects.equals(municipalityId, facilityDelegation.municipalityId) && Objects.equals(created,
				facilityDelegation.created) && Objects.equals(updated, facilityDelegation.updated) && Objects.equals(deleted, facilityDelegation.deleted);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, facilities, businessEngagementOrgId, delegatedTo, owner, status, municipalityId, created, updated, deleted);
	}

	@Override
	public String toString() {
		return "FacilityDelegation{" +
			"id='" + id + '\'' +
			", facilities=" + facilities +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			", delegatedTo='" + delegatedTo + '\'' +
			", owner='" + owner + '\'' +
			", status='" + status + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", created=" + created +
			", updated=" + updated +
			", deleted=" + deleted +
			'}';
	}
}
