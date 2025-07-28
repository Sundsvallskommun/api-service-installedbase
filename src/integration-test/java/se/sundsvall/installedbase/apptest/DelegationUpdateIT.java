package se.sundsvall.installedbase.apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.integration.db.FacilityRepository;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;

@WireMockAppTestSuite(files = "classpath:/delegation/update/", classes = Application.class)
@Sql(scripts = {
	"/db/script/truncate.sql",
	"/db/script/init-db.sql"
})
class DelegationUpdateIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegations";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private FacilityRepository repository;

	private TransactionTemplate transactionTemplate;

	@BeforeEach
	void setUp() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Test
	void test01_patchDelegationWithNewDelegatedTo() {
		final var id = "24504e65-08cf-4bc3-8f4f-a07204748c13";

		setupCall()
			.withServicePath(BASE_URL + "/" + id)
			.withHttpMethod(PATCH)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.withExpectedResponseBodyIsNull()
			.sendRequest();

		// Fetch the delegation to verify it was updated
		setupCall()
			.withServicePath(BASE_URL + "/" + id)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_patchDelegationWithNewFacilities() {
		final var id = "cbba5149-82db-4e41-a2bb-f1ca396dc961";

		setupCall()
			.withServicePath(BASE_URL + "/" + id)
			.withHttpMethod(PATCH)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.withExpectedResponseBodyIsNull()
			.sendRequest();

		// Fetch the delegation to verify it was updated
		setupCall()
			.withServicePath(BASE_URL + "/" + id)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();

		// Verify that facility instances are no longer there
		transactionTemplate.executeWithoutResult(status -> {
			assertThat(repository.findAll().stream()
				.map(FacilityEntity::getFacilityId)
				.toList()).isNotEmpty().doesNotContain("FACILITY-420", "FACILITY-421", "FACILITY-422"); // These should not be present as they have been replaced by 501, 502 and 503
		});
	}

	@Test
	void test03_patchDelegationWithCompleteUpdate() {
		final var id = "78ee675d-4ab5-4c9c-a80a-5c508f1c55af";

		setupCall()
			.withServicePath("/1984/delegations/" + id)
			.withHttpMethod(PATCH)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.withExpectedResponseBodyIsNull()
			.sendRequest();

		// Fetch the delegation to verify it was updated
		setupCall()
			.withServicePath("/1984/delegations/" + id)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();

		// Verify that facility instances are no longer there
		transactionTemplate.executeWithoutResult(status -> {
			System.err.println(
				repository.findAll().stream()
					.map(FacilityEntity::getFacilityId)
					.toList());

			assertThat(repository.findAll().stream()
				.map(FacilityEntity::getFacilityId)
				.toList()).isNotEmpty().doesNotContain("FACILITY-70", "FACILITY-71"); // These should not be present as they have been removed (only 69 should still be there with a new companion - 666)
		});
	}

	@Test
	void test04_patchDelegation_delegationDoesNotExist() {
		final var id = "cce9c949-f392-4dab-8607-29be2ac04fbe";

		setupCall()
			.withServicePath(BASE_URL + "/" + id)
			.withHttpMethod(PATCH)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequest();
	}
}
