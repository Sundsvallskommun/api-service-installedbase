package se.sundsvall.installedbase.api.model.delegation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidOrganizationNumber;

@Schema(description = "Facility model")
public class Facility {

	@Schema(description = "Facility id", example = "facility1", requiredMode = REQUIRED)
	@NotBlank(message = "Facility id cannot be blank")
	private String id;

	@Schema(description = "Organization number of the company owning the facility", example = "5591628136", requiredMode = REQUIRED)
	@ValidOrganizationNumber
	private String businessEngagementOrgId;

	public static Facility create() {
		return new Facility();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBusinessEngagementOrgId() {
		return businessEngagementOrgId;
	}

	public void setBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
	}

	public Facility withId(String id) {
		this.id = id;
		return this;
	}

	public Facility withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(businessEngagementOrgId, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof final Facility other)) {
			return false;
		}
		return Objects.equals(businessEngagementOrgId, other.businessEngagementOrgId) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Facility{" +
			"id='" + id + '\'' +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			'}';
	}
}
