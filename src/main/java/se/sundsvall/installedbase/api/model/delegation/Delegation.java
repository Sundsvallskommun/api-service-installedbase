package se.sundsvall.installedbase.api.model.delegation;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "Delegation response model")
public class Delegation {

	@Schema(description = "Unique identifier for the delegation", example = "12345678-1234-1234-1234-123456789012")
	private String id;

	@ArraySchema(schema = @Schema(implementation = Facility.class, description = "List of facilities to be delegated"))
	private List<Facility> facilities;

	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2")
	private String delegatedTo;

	@Schema(description = "Party ID of the delegation owner", example = "81471222-5798-11e9-ae24-57fa13b361e1")
	private String owner;

	@Schema(description = "Municipality ID of the delegation", example = "1234")
	private String municipalityId;

	@DateTimeFormat(iso = DATE_TIME)
	@Schema(description = "When the delegation was created", example = "2025-01-01T12:00:00")
	private OffsetDateTime created;

	@DateTimeFormat(iso = DATE_TIME)
	@Schema(description = "When the delegation was last updated", example = "2025-04-01T12:00:00")
	private OffsetDateTime updated;

	public static Delegation create() {
		return new Delegation();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
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

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public OffsetDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(OffsetDateTime updated) {
		this.updated = updated;
	}

	public Delegation withId(String id) {
		this.id = id;
		return this;
	}

	public Delegation withFacilities(List<Facility> facilities) {
		this.facilities = facilities;
		return this;
	}

	public Delegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public Delegation withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public Delegation withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public Delegation withUpdated(OffsetDateTime updated) {
		this.updated = updated;
		return this;
	}

	public Delegation withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final Delegation that)) {
			return false;
		}
		return Objects.equals(id, that.id) && Objects.equals(facilities, that.facilities) && Objects.equals(delegatedTo, that.delegatedTo)
			&& Objects.equals(owner, that.owner) && Objects.equals(municipalityId, that.municipalityId) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, facilities, delegatedTo, owner, municipalityId, created, updated);
	}

	@Override
	public String toString() {
		return "FacilityDelegation{" +
			"id='" + id + '\'' +
			", facilities=" + facilities +
			", delegatedTo='" + delegatedTo + '\'' +
			", owner='" + owner + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", created=" + created +
			", updated=" + updated +
			'}';
	}
}
