package se.sundsvall.installedbase.apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.ACCEPTED;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.integration.db.DelegationRepository;
import se.sundsvall.installedbase.integration.db.model.DelegationEntity;
import se.sundsvall.installedbase.integration.db.model.FacilityEntity;

@WireMockAppTestSuite(files = "classpath:/delegation/delete/", classes = Application.class)
@Sql(scripts = {
	"/db/script/truncate.sql",
	"/db/script/init-db.sql"
})
class DelegationDeleteIT extends AbstractAppTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegations";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private DelegationRepository repository;

	private TransactionTemplate transactionTemplate;

	@BeforeEach
	void setUp() {
		transactionTemplate = new TransactionTemplate(transactionManager);
	}

	@Test
	void test01_deleteDelegationWithReferredFacilities() {
		final var delegationId = "24504e65-08cf-4bc3-8f4f-a07204748c13";

		// Verify we actually have something to delete
		assertThat(repository.existsById(delegationId)).isTrue();

		// Delete it
		setupCall()
			.withServicePath(BASE_URL + "/" + delegationId)
			.withHttpMethod(DELETE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.sendRequestAndVerifyResponse();

		// Verify that it was deleted
		assertThat(repository.existsById(delegationId)).isFalse();

		// Verify that facility instances are still there as they also are referred by another delegation
		transactionTemplate.executeWithoutResult(status -> {
			assertThat(repository.findAll().stream()
				.map(DelegationEntity::getFacilities)
				.flatMap(Set::stream)
				.map(FacilityEntity::getFacilityId)
				.toList()).containsAll(List.of("Facility-1", "Facility-2", "Facility-3"));
		});
	}

	@Test
	void test02_deleteDelegationWithNonReferredFacilities() {
		final var delegationId = "cbba5149-82db-4e41-a2bb-f1ca396dc961";

		// Verify we actually have something to delete
		assertThat(repository.existsById(delegationId)).isTrue();

		// Delete it
		setupCall()
			.withServicePath(BASE_URL + "/" + delegationId)
			.withHttpMethod(DELETE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.sendRequestAndVerifyResponse();

		// Verify that it was deleted
		assertThat(repository.existsById(delegationId)).isFalse();

		// Verify that facility instances are no longer there
		transactionTemplate.executeWithoutResult(status -> {
			assertThat(repository.findAll().stream()
				.map(DelegationEntity::getFacilities)
				.flatMap(Set::stream)
				.map(FacilityEntity::getFacilityId)
				.toList()).doesNotContain("facility-420", "facility-421", "facility-422");
		});
	}

	@Test
	void test03_deleteDelegationThatDoesNotExist_shouldReturnAccepted() {
		final var delegationId = UUID.randomUUID().toString();

		setupCall()
			.withServicePath(BASE_URL + "/" + delegationId)
			.withHttpMethod(DELETE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.sendRequest();
	}
}
