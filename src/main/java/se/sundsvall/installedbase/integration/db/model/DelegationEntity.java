package se.sundsvall.installedbase.integration.db.model;

import static org.hibernate.annotations.TimeZoneStorageType.NORMALIZE;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "delegation",
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
public class DelegationEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", length = 36)
	private String id;

	@ManyToMany(cascade = {
		CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH
	})
	@JoinTable(
		name = "delegation_facility",
		joinColumns = @JoinColumn(
			name = "delegation_ref_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_delegation_facility_delegation")),
		inverseJoinColumns = @JoinColumn(
			name = "facility_ref_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "fk_delegation_facility_facility")),
		uniqueConstraints = @UniqueConstraint(
			name = "uk_delegation_facility",
			columnNames = {
				"delegation_ref_id", "facility_ref_id"
			}))
	private Set<FacilityEntity> facilities = new HashSet<>();

	@Column(length = 36, name = "delegated_to", nullable = false)
	private String delegatedTo; // PartyId of the person to whom the facilities are delegated

	@Column(length = 4, name = "municipality_id")
	private String municipalityId;

	@Column(length = 36, name = "owner", nullable = false)
	private String owner; // PartyId of the person who owns the facilities

	@Column(name = "created", nullable = false)
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime created;

	@Column(name = "updated")
	@TimeZoneStorage(NORMALIZE)
	private OffsetDateTime updated;

	public static DelegationEntity create() {
		return new DelegationEntity();
	}

	@PreUpdate
	void onUpdate() {
		updated = OffsetDateTime.now();
	}

	@PrePersist
	void onCreate() {
		if (Objects.isNull(created)) {
			created = OffsetDateTime.now();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<FacilityEntity> getFacilities() {
		return facilities;
	}

	public void setFacilities(Set<FacilityEntity> facilities) {
		this.facilities = facilities;
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

	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}

	public OffsetDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(OffsetDateTime changed) {
		this.updated = changed;
	}

	public DelegationEntity withId(String id) {
		this.id = id;
		return this;
	}

	public DelegationEntity withFacilities(Set<FacilityEntity> facilities) {
		this.facilities = facilities;
		return this;
	}

	public DelegationEntity withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public DelegationEntity withMunicipalityId(String municipalityId) {
		this.municipalityId = municipalityId;
		return this;
	}

	public DelegationEntity withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	public DelegationEntity withCreated(OffsetDateTime created) {
		this.created = created;
		return this;
	}

	public DelegationEntity withUpdated(OffsetDateTime updated) {
		this.updated = updated;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final DelegationEntity that)) {
			return false;
		}
		return Objects.equals(id, that.id) && Objects.equals(delegatedTo, that.delegatedTo) && Objects.equals(municipalityId, that.municipalityId)
			&& Objects.equals(owner, that.owner) && Objects.equals(created, that.created) && Objects.equals(updated, that.updated);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, delegatedTo, municipalityId, owner, created, updated);
	}

	@Override
	public String toString() {
		return "DelegationEntity{" +
			"id=" + id +
			", facilities=" + facilities +
			", delegatedTo='" + delegatedTo + '\'' +
			", municipalityId='" + municipalityId + '\'' +
			", owner='" + owner + '\'' +
			", created=" + created +
			", updated=" + updated +
			'}';
	}
}
