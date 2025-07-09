package se.sundsvall.installedbase.integration.db.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;
import se.sundsvall.installedbase.service.model.DelegationStatus;

@Entity
@Table(name = "facility_delegation",
	indexes = {
		@Index(name = "idx_municipality_id_delegated_to", columnList = "municipality_id, delegated_to"),
		@Index(name = "idx_municipality_id_owner", columnList = "municipality_id, owner")
	},
	// Unique constraint to ensure that an owner can only have unique delegations to other persons, i.e. no duplicate
	// delegations
	uniqueConstraints = @UniqueConstraint(
		name = "uk_delegated_to_owner",
		columnNames = {
			"delegated_to", "owner", "municipality_id"
		}))
public class FacilityDelegationEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", length = 36)
	private String id;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "facility_delegation_facilities",
		joinColumns = @JoinColumn(name = "facility_delegation_id",
			foreignKey = @ForeignKey(name = "fk_facility_delegation_facilities_facility_delegation_id")))
	@Column(name = "facility", length = 256)
	private List<String> facilities;

	@Column(name = "business_engagement_org_id", length = 20)
	private String businessEngagementOrgId;

	@Column(length = 36, name = "delegated_to", nullable = false)
	private String delegatedTo; // PartyId of the person to whom the facilities are delegated

	@Column(length = 4, name = "municipality_id")
	private String municipalityId;

	@Column(length = 36, name = "owner", nullable = false)
	private String owner; // PartyId of the person who owns the facilities

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private DelegationStatus status;

	@Column(name = "created", nullable = false)
	private LocalDateTime created;

	@Column(name = "deleted")
	private LocalDateTime deleted;

	@Column(name = "updated")
	private LocalDateTime updated;

	@PreUpdate
	void onUpdate() {
		updated = LocalDateTime.now();
		// Since we do not actually delete delegations, we also set the deleted timestamp here.
		if (DelegationStatus.DELETED.equals(this.status)) {
			deleted = LocalDateTime.now();
		}
	}

	@PrePersist
	void onCreate() {
		if (Objects.isNull(created)) {
			created = LocalDateTime.now();
		}
	}

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

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public DelegationStatus getStatus() {
		return status;
	}

	public void setStatus(DelegationStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getDeleted() {
		return deleted;
	}

	public void setDeleted(LocalDateTime deleted) {
		this.deleted = deleted;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime changed) {
		this.updated = changed;
	}

	public FacilityDelegationEntity withId(String id) {
		this.id = id;
		return this;
	}

	public FacilityDelegationEntity withFacilities(List<String> facilities) {
		this.facilities = facilities;
		return this;
	}

	public FacilityDelegationEntity withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public FacilityDelegationEntity withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public FacilityDelegationEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public FacilityDelegationEntity withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public FacilityDelegationEntity withStatus(DelegationStatus status) {
		this.status = status;
		return this;
	}

	public FacilityDelegationEntity withCreated(LocalDateTime created) {
		this.created = created;
		return this;
	}

	public FacilityDelegationEntity withDeleted(LocalDateTime deleted) {
		this.deleted = deleted;
		return this;
	}

	public FacilityDelegationEntity withUpdated(LocalDateTime changed) {
		this.updated = changed;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FacilityDelegationEntity that))
			return false;
		return Objects.equals(id, that.id) && Objects.equals(businessEngagementOrgId, that.businessEngagementOrgId) && Objects.equals(delegatedTo, that.delegatedTo) && Objects.equals(municipalityId, that.municipalityId)
			&& Objects.equals(owner, that.owner) && status == that.status && Objects.equals(created, that.created) && Objects.equals(deleted, that.deleted) && Objects.equals(updated, that.updated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, businessEngagementOrgId, delegatedTo, municipalityId, owner, status, created, deleted, updated);
	}

	@Override
	public String toString() {
		return "FacilityDelegationEntity{" +
			"id=" + id +
			", facilities=" + facilities +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			", delegatedTo='" + delegatedTo + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", owner='" + owner + '\'' +
			", status=" + status +
			", created=" + created +
			", deleted=" + deleted +
			", updated=" + updated +
			'}';
	}
}
