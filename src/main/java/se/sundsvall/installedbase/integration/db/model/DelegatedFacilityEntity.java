package se.sundsvall.installedbase.integration.db.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "delegated_facility",
	indexes = {
		@Index(name = "idx_delegated_to", columnList = "delegated_to"),
		@Index(name = "idx_owner", columnList = "owner"),
		@Index(name = "idx_municipality_id", columnList = "municipality_id")
	},
	// Unique constraint to ensure that an owner can only have unique delegations to other persons, i.e. no duplicate
	// delegations
	uniqueConstraints = @UniqueConstraint(
		name = "uk_delegated_to_owner",
		columnNames = {
			"delegated_to", "owner"
		}))
public class DelegatedFacilityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "delegated_facility_facilities",
		joinColumns = @JoinColumn(name = "delegated_facility_id",
			foreignKey = @ForeignKey(name = "fk_delegated_facility_facilities_delegated_facility_id")))
	@Column(name = "facility", length = 256)
	private List<String> facilities;

	@Column(name = "business_engagement_org_id", length = 20)
	private String businessEngagementOrgId;

	@Column(length = 36, name = "delegated_to", nullable = false)
	private String delegatedTo;

	@Column(length = 4, name = "municipality_id")
	private String municipalityId;

	@Column(length = 36, name = "owner", nullable = false)
	private String owner;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private DelegationStatus status;

	@Column(name = "start", nullable = false)
	private LocalDateTime start;

	@Column(name = "end")
	private LocalDateTime end;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public void setDelegatedTo(String delegatedToPartyId) {
		this.delegatedTo = delegatedToPartyId;
	}

	public String getMunicipalityId() {
		return municipalityId;
	}

	public void setMunicipalityId(String muniicipalityId) {
		this.municipalityId = muniicipalityId;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String ownerPartyId) {
		this.owner = ownerPartyId;
	}

	public DelegationStatus getStatus() {
		return status;
	}

	public void setStatus(DelegationStatus delegationStatus) {
		this.status = delegationStatus;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public DelegatedFacilityEntity withId(Integer id) {
		this.id = id;
		return this;
	}

	public DelegatedFacilityEntity withFacilities(List<String> facilities) {
		this.facilities = facilities;
		return this;
	}

	public DelegatedFacilityEntity withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public DelegatedFacilityEntity withDelegatedTo(String delegatedToPartyId) {
		this.delegatedTo = delegatedToPartyId;
		return this;
	}

	public DelegatedFacilityEntity withMunicipalityId(String muniicipalityId) {
		this.municipalityId = muniicipalityId;
		return this;
	}

	public DelegatedFacilityEntity withOwner(String ownerPartyId) {
		this.owner = ownerPartyId;
		return this;
	}

	public DelegatedFacilityEntity withStatus(DelegationStatus delegationStatus) {
		this.status = delegationStatus;
		return this;
	}

	public DelegatedFacilityEntity withStart(LocalDateTime start) {
		this.start = start;
		return this;
	}

	public DelegatedFacilityEntity withEnd(LocalDateTime end) {
		this.end = end;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DelegatedFacilityEntity that))
			return false;
		return Objects.equals(id, that.id) && Objects.equals(businessEngagementOrgId, that.businessEngagementOrgId) && Objects.equals(delegatedTo, that.delegatedTo) && Objects.equals(municipalityId, that.municipalityId)
			&& Objects.equals(owner, that.owner) && status == that.status && Objects.equals(start, that.start) && Objects.equals(end, that.end);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, businessEngagementOrgId, delegatedTo, municipalityId, owner, status, start, end);
	}

	@Override
	public String toString() {
		return "DelegatedFacilityEntity{" +
			"id=" + id +
			", facilities=" + facilities +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			", delegatedTo='" + delegatedTo + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", owner='" + owner + '\'' +
			", status=" + status +
			", start=" + start +
			", end=" + end +
			'}';
	}
}
