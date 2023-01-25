package se.sundsvall.installedbase.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import se.sundsvall.dept44.common.validators.annotation.ValidOrganizationNumber;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.installedbase.api.model.InstalledBaseResponse;
import se.sundsvall.installedbase.service.InstalledBaseService;

@RestController
@Validated
@RequestMapping("/installedbase")
@Tag(name = "Installed base", description = "Installed base operations")
public class InstalledBaseResource {

	private static final Logger LOG = LoggerFactory.getLogger(InstalledBaseResource.class);

	@Autowired
	private InstalledBaseService service;

	@GetMapping(path = "/{organizationNumber}", produces = { APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE })
	@Operation(summary = "Get installed base at company matching organization number for customer matching provided party-ID")
	@ApiResponse(responseCode = "200", description = "Successful Operation", content = @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = InstalledBaseResponse.class)))
	@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(oneOf = { Problem.class, ConstraintViolationProblem.class })))
	@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	@ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(mediaType = APPLICATION_PROBLEM_JSON_VALUE, schema = @Schema(implementation = Problem.class)))
	public ResponseEntity<InstalledBaseResponse> getInstalledBase(
		@Parameter(name = "organizationNumber", description = "Organization number", required = true, example = "5565112233") @ValidOrganizationNumber @PathVariable(name = "organizationNumber") String organizationNumber,
		@Parameter(name = "partyId", description = "Party-ID", required = true, example = "81471222-5798-11e9-ae24-57fa13b361e1") @RequestParam(value = "partyId", required = true) List<@ValidUuid String> partyIds) {
		LOG.debug("Received getInstalledBase request: organizationNumber='{}', partyId='{}'", organizationNumber, partyIds);

		return ok(service.getInstalledBase(organizationNumber, partyIds));
	}
}
