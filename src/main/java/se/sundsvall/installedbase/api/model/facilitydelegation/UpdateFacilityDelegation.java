package se.sundsvall.installedbase.api.model.facilitydelegation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

@Schema(description = "UpdateFacilityDelegation model")
public class UpdateFacilityDelegation {

	@ArraySchema(schema = @Schema(implementation = String.class, description = "List of facility IDs to be delegated", example = "[\"facility1\", \"facility2\"]", requiredMode = NOT_REQUIRED))
	private List<@NotBlank(message = "Facility cannot be blank") String> facilities;

	@Schema(description = "Organization number of the company owning the facility", example = "5591628136", requiredMode = NOT_REQUIRED)
	private String businessEngagementOrgId;

	@ValidUuid(nullable = true)
	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = NOT_REQUIRED)
	private String delegatedTo;

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

	public UpdateFacilityDelegation withFacilities(List<String> facilities) {
		this.facilities = facilities;
		return this;
	}

	public UpdateFacilityDelegation withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public UpdateFacilityDelegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UpdateFacilityDelegation that))
			return false;
		return Objects.equals(facilities, that.facilities) && Objects.equals(businessEngagementOrgId, that.businessEngagementOrgId) && Objects.equals(delegatedTo, that.delegatedTo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(facilities, businessEngagementOrgId, delegatedTo);
	}

	@Override
	public String toString() {
		return "UpdateFacilityDelegation{" +
			"facilities=" + facilities +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			", delegatedTo='" + delegatedTo + '\'' +
			'}';
	}
}
