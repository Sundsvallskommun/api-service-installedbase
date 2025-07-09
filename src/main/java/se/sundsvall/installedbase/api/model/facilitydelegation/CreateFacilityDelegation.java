package se.sundsvall.installedbase.api.model.facilitydelegation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidOrganizationNumber;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.validation.UniqueElements;

@Schema(description = "CreateFacilityDelegation model")
public class CreateFacilityDelegation {

	@UniqueElements
	@NotEmpty(message = "facilities must contain at least one facility")
	@ArraySchema(schema = @Schema(implementation = String.class, description = "List of facility IDs to be delegated", example = "[\"facility1\", \"facility2\"]", requiredMode = REQUIRED))
	private List<@NotBlank(message = "Facility cannot be blank") String> facilities;

	@ValidOrganizationNumber(nullable = true)
	@Schema(description = "Organization number of the company owning the facility", example = "5591628136", requiredMode = NOT_REQUIRED)
	private String businessEngagementOrgId;

	@ValidUuid
	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = REQUIRED)
	private String delegatedTo;

	@ValidUuid
	@Schema(description = "Party ID of the delegation owner", example = "81471222-5798-11e9-ae24-57fa13b361e1", requiredMode = REQUIRED)
	private String owner;

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

	public CreateFacilityDelegation withFacilities(List<String> facilities) {
		this.facilities = facilities;
		return this;
	}

	public CreateFacilityDelegation withBusinessEngagementOrgId(String businessEngagementOrgId) {
		this.businessEngagementOrgId = businessEngagementOrgId;
		return this;
	}

	public CreateFacilityDelegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public CreateFacilityDelegation withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof CreateFacilityDelegation that))
			return false;
		return Objects.equals(facilities, that.facilities) && Objects.equals(businessEngagementOrgId, that.businessEngagementOrgId) && Objects.equals(delegatedTo, that.delegatedTo) && Objects.equals(owner, that.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(facilities, businessEngagementOrgId, delegatedTo, owner);
	}

	@Override
	public String toString() {
		return "CreateFacilityDelegation{" +
			"facilities=" + facilities +
			", businessEngagementOrgId='" + businessEngagementOrgId + '\'' +
			", delegatedTo='" + delegatedTo + '\'' +
			", owner='" + owner + '\'' +
			'}';
	}
}
