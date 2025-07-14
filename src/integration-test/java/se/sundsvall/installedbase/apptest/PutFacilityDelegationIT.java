package se.sundsvall.installedbase.apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;

@WireMockAppTestSuite(files = "classpath:/putfacilitydelegation/", classes = Application.class)
@Sql(scripts = {
	"/sql/truncate.sql",
	"/sql/init-db.sql"
})
class PutFacilityDelegationIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegates";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";
		
	@Test
	void test01_putFacilityDelegation() {
		var facilityDelegationId = "24504e65-08cf-4bc3-8f4f-a07204748c13";
		setupCall()
			.withServicePath(BASE_URL + "/" + facilityDelegationId)
			.withHttpMethod(PUT)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();

		// Fetch the delegation to verify it was updated
		setupCall()
			.withServicePath(BASE_URL + "/" + facilityDelegationId)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
