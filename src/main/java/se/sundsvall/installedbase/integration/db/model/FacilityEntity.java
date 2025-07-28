package se.sundsvall.installedbase.integration.db.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "facility",
	indexes = {
		@Index(name = "idx_facility_id", columnList = "facility_id")
	},
	uniqueConstraints = @UniqueConstraint(name = "uk_facility_id", columnNames = {
		"facility_id"
	}))
public class FacilityEntity {

	@Id
	@UuidGenerator
	@Column(name = "id", length = 36)
	private String id;

	@Column(name = "facility_id", length = 256, nullable = false)
	private String facilityId;

	@Column(name = "business_engagement_org_id", length = 20)
	private String businessEngagementOrgId;

	@ManyToMany(mappedBy = "facilities")
	private List<DelegationEntity> delegations;

	public static FacilityEntity create() {
		return new FacilityEntity();
	}

	public String getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(String facilityId) {
		this.facilityId = facilityId;
	}

	public FacilityEntity withFacilityId(String facilityId) {
		this.facilityId = facilityId;
		return this;
	}

	public String getBusinessEngagementOrgId() {
		return businessEngagementOrgId;
	}

	public void setBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
	}

	public FacilityEntity withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public List<DelegationEntity> getDelegations() {
		return delegations;
	}

	public void setDelegations(List<DelegationEntity> delegations) {
		this.delegations = delegations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(businessEngagementOrgId, facilityId, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof final FacilityEntity other)) { return false; }
		return Objects.equals(businessEngagementOrgId, other.businessEngagementOrgId) && Objects.equals(facilityId, other.facilityId) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		final var builder = new StringBuilder();
		builder.append("FacilityEntity [id=").append(id).append(", facilityId=").append(facilityId).append(", businessEngagementOrgId=").append(businessEngagementOrgId).append("]");
		return builder.toString();
	}

}
