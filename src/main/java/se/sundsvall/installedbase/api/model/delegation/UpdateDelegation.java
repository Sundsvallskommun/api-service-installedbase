package se.sundsvall.installedbase.api.model.delegation;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.validation.UniqueElements;

@Schema(description = "UpdateDelegation model")
public class UpdateDelegation {

	@UniqueElements
	@ArraySchema(uniqueItems = true, schema = @Schema(implementation = Facility.class, description = "List of facilities to be delegated", requiredMode = NOT_REQUIRED))
	private List<@Valid Facility> facilities;

	@ValidUuid(nullable = true)
	@Schema(description = "Party ID of the delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2", requiredMode = NOT_REQUIRED)
	private String delegatedTo;

	public static UpdateDelegation create() {
		return new UpdateDelegation();
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

	public UpdateDelegation withFacilities(List<Facility> facilities) {
		this.facilities = facilities;
		return this;
	}

	public UpdateDelegation withDelegatedTo(String delegatedTo) {
		this.delegatedTo = delegatedTo;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof final UpdateDelegation that)) {
			return false;
		}
		return Objects.equals(facilities, that.facilities) && Objects.equals(delegatedTo, that.delegatedTo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(facilities, delegatedTo);
	}

	@Override
	public String toString() {
		return "UpdateDelegation{" +
			"facilities=" + facilities +
			", delegatedTo='" + delegatedTo + '\'' +
			'}';
	}
}
