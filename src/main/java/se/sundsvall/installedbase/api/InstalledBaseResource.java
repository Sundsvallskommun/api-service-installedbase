package se.sundsvall.installedbase.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidOrganizationNumber;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.service.InstalledBaseService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/installedbase")
@Tag(name = "Installed base", description = "Installed base operations")
public class InstalledBaseResource {

	private final InstalledBaseService service;

	public InstalledBaseResource(InstalledBaseService service) {
		this.service = service;
	}

	@GetMapping(path = "/{organizationNumber}", produces = {
		APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	@Operation(summary = "Get installed base at company matching organization number for customer matching provided party-ID")
	@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true)
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
		Problem.class, ConstraintViolationProblem.class
	})))
	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<InstalledBaseResponse> getInstalledBase(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "organizationNumber", description = "Organization number", required = true, example = "5565112233") @ValidOrganizationNumber @PathVariable(name = "organizationNumber") final String organizationNumber,
		@Parameter(name = "partyId", description = "Party-ID", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @RequestParam(value = "partyId") final List<@ValidUuid String> partyIds,
		@Parameter(name = "modifiedFrom", description = "Optional date for filtering on installed bases modified at provided date or later", example = "2023-06-01") @RequestParam(name = "modifiedFrom", required = false) final LocalDate modifiedFrom) {

		return ok(service.getInstalledBase(municipalityId, organizationNumber, partyIds, modifiedFrom));
	}
}
