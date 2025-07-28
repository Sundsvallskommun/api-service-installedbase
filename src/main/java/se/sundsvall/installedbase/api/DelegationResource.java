package se.sundsvall.installedbase.api;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.accepted;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static se.sundsvall.installedbase.Constants.DELEGATES_BY_ID_PATH;
import static se.sundsvall.installedbase.api.model.validation.ValidatorUtil.validateDelegationParameters;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.common.validators.annotation.ValidMunicipalityId;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.delegation.CreateDelegation;
import se.sundsvall.installedbase.api.model.delegation.Delegation;
import se.sundsvall.installedbase.api.model.delegation.UpdateDelegation;
import se.sundsvall.installedbase.service.DelegationService;

@RestController
@Validated
@RequestMapping("/{municipalityId}/delegations")
@Tag(name = "Facility Delegation", description = "Facility delegation operations")
@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = {
	Problem.class, ConstraintViolationProblem.class
})))
@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
class DelegationResource {

	private final DelegationService service;

	public DelegationResource(final DelegationService service) {
		this.service = service;
	}

	@GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get a specific facility delegation",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	ResponseEntity<Delegation> getDelegationById(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "id", description = "Id of the delegation", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @PathVariable(value = "id") final @ValidUuid String id) {

		return ok(service.getDelegation(municipalityId, id));
	}

	@GetMapping(path = "", produces = APPLICATION_JSON_VALUE)
	@Operation(summary = "Get delegation by owner and/or delegate",
		responses = {
			@ApiResponse(responseCode = "200", description = "Successful Operation", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	public ResponseEntity<List<Delegation>> getDelegations(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "owner", description = "Owner of the delegation", example = "81471222-5798-11e9-ae24-57fa13b361e1") @ValidUuid(nullable = true) @RequestParam(required = false) String owner,
		@Parameter(name = "delegatedTo", description = "The delegate", example = "81471222-5798-11e9-ae24-57fa13b361e2") @ValidUuid(nullable = true) @RequestParam(required = false) String delegatedTo) {

		validateDelegationParameters(owner, delegatedTo);

		return ok(service.getDelegations(municipalityId, owner, delegatedTo));
	}

	@PostMapping(produces = ALL_VALUE)
	@Operation(summary = "Create a new facility delegation",
		responses = {
			@ApiResponse(responseCode = "201", description = "Created", headers = @Header(name = LOCATION, schema = @Schema(type = "string")), useReturnTypeSchema = true),
			@ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	ResponseEntity<Void> createDelegation(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@RequestBody @Valid CreateDelegation delegation) {

		final var id = service.createDelegation(municipalityId, delegation);

		final var uri = fromPath(DELEGATES_BY_ID_PATH)
			.buildAndExpand(municipalityId, id)
			.toUri();

		return created(uri)
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@PatchMapping(path = "/{id}", produces = ALL_VALUE)
	@Operation(summary = "Update an existing facility delegation",
		responses = {
			@ApiResponse(responseCode = "202", description = "Accepted", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
		})
	ResponseEntity<Void> updateDelegation(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "id", description = "Id of the delegation", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @PathVariable(value = "id") final @ValidUuid String id,
		@RequestBody @Valid UpdateDelegation delegation) {

		service.updateDelegation(municipalityId, id, delegation);

		return accepted()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}

	@DeleteMapping(path = "/{id}", produces = ALL_VALUE)
	@Operation(summary = "Delete a facility delegation",
		responses = {
			@ApiResponse(responseCode = "202", description = "Accepted", useReturnTypeSchema = true)
		})
	ResponseEntity<Void> deleteDelegation(
		@Parameter(name = "municipalityId", description = "Municipality id", example = "2281") @ValidMunicipalityId @PathVariable final String municipalityId,
		@Parameter(name = "id", description = "Id of the delegation", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @PathVariable(value = "id") final @ValidUuid String id) {

		service.deleteDelegation(municipalityId, id);

		return accepted()
			.header(CONTENT_TYPE, ALL_VALUE)
			.build();
	}
}
