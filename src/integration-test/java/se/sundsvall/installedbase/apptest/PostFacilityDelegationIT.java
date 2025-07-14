package se.sundsvall.installedbase.apptest;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.integration.db.FacilityDelegationRepository;

@WireMockAppTestSuite(files = "classpath:/postfacilitydelegation/", classes = Application.class)
@Sql(scripts = {
	"/sql/truncate.sql",
	"/sql/init-db.sql"
})
class PostFacilityDelegationIT extends AbstractAppTest {

	private static final String REQUEST_FILE = "request.json";
	private static final String RESPONSE_FILE = "response.json";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegates";
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";
	
	@Autowired
	private FacilityDelegationRepository facilityDelegationRepository;
	
	@Test
	void test01_postFacilityDelegation() {
		var responseHeaders = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of("^" + BASE_URL + "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$"))
			.sendRequest()
			.getResponseHeaders();
		// Verify the Location header
		var location = responseHeaders.getFirst(LOCATION);

		// Fetch the delegation to verify it was created
		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	@Test
	void test02_postFacilityDelegation_delegationAlreadyExists() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST_FILE)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(CONFLICT)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
