package se.sundsvall.installedbase.apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;

@WireMockAppTestSuite(files = "classpath:/deletefacilitydelegation/", classes = Application.class)
@Sql(scripts = {
	"/sql/truncate.sql",
	"/sql/init-db.sql"
})
class DeleteFacilityDelegationIT extends AbstractAppTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegates";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";
	
	@Autowired
	private FacilityDelegationRepository facilityDelegationRepository;

	@Test
	void test01_deleteFacilityDelegation() {
		var facilityDelegationId = "24504e65-08cf-4bc3-8f4f-a07204748c13";

		//Verify we actually have something to delete
		assertThat(facilityDelegationRepository.existsById(facilityDelegationId)).isTrue();
		
		// Delete it
		setupCall()
			.withServicePath(BASE_URL + "/" + facilityDelegationId)
			.withHttpMethod(DELETE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.sendRequest();

		// Verify that it was deleted
		assertThat(facilityDelegationRepository.existsById(facilityDelegationId)).isFalse();
	}
	
	@Test
	void test02_deleteFacilityDelegationThatDoesNotExist_shouldReturnAccepted() {
		var facilityDelegationId = UUID.randomUUID().toString();

		setupCall()
			.withServicePath(BASE_URL + "/" + facilityDelegationId)
			.withHttpMethod(DELETE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(ACCEPTED)
			.sendRequest();
	}
}
