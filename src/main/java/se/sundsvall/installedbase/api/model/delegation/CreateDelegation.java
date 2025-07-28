package se.sundsvall.installedbase.api.model.delegation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.validation.UniqueElements;

@Schema(description = "CreateDelegation model")
public class CreateDelegation {

	@UniqueElements
	@NotEmpty(message = "facilities must contain at least one member")
	@ArraySchema(uniqueItems = true, schema = @Schema(implementation = Facility.class, description = "List of facilities to be delegated", requiredMode = REQUIRED))
	private List<@Valid Facility> facilities;

	@ValidUuid
	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = REQUIRED)
	private String delegatedTo;

	@ValidUuid
	@Schema(description = "Party ID of the delegation owner", example = "81471222-5798-11e9-ae24-57fa13b361e1", requiredMode = REQUIRED)
	private String owner;

	public static CreateDelegation create() {
		return new CreateDelegation();
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

	public CreateDelegation withFacilities(List<Facility> facilities) {
		this.facilities = facilities;
		return this;
	}

	public CreateDelegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	public CreateDelegation withOwner(String owner) {
		this.owner = owner;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final CreateDelegation that)) {
			return false;
		}
		return Objects.equals(facilities, that.facilities) && Objects.equals(delegatedTo, that.delegatedTo) && Objects.equals(owner, that.owner);
	}

	@Override
	public int hashCode() {
		return Objects.hash(facilities, delegatedTo, owner);
	}

	@Override
	public String toString() {
		return "CreateDelegation{" +
			"facilities=" + facilities +
			", delegatedTo='" + delegatedTo + '\'' +
			", owner='" + owner + '\'' +
			'}';
	}
}
