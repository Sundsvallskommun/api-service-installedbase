package se.sundsvall.installedbase.apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;

@WireMockAppTestSuite(files = "classpath:/getfacilitydelegation/", classes = Application.class)
@Sql(scripts = {
	"/sql/truncate.sql",
	"/sql/init-db.sql"
})
class GetFacilityDelegationIT extends AbstractAppTest {
	
	private static final String RESPONSE_FILE = "response.json";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/" + MUNICIPALITY_ID + "/delegates";
	// Not really used, but included for good measure
	private static final String X_SENT_BY = "X-Sent-By";
	private static final String X_SENT_BY_VALUE = "joe001doe; type=adAccount";
	
	// Get one delegation by id
	@Test
	void test01_getFacilityDelegationById() {
		var facilityDelegationId = "24504e65-08cf-4bc3-8f4f-a07204748c13";
		setupCall()
			.withServicePath(BASE_URL + "/" + facilityDelegationId)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	// Get delegation for a specific owner and delegatedTo.
	@Test
	void test02_getFacilityDelegationsByOwnerAndDelegatedTo() {
		var owner = "81471222-5798-11e9-ae24-57fa13b361e1";
		var delegatedTo = "f2949c12-bb83-406a-be96-bb8628f14612";
		setupCall()
			.withServicePath(BASE_URL + "?owner=" + owner + "&delegatedTo=" + delegatedTo)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	// Get multiple delegations by the same owner, should return a list of facility delegations.
	@Test
	void test03_getFacilityDelegationsByOwner() {
		var owner = "81471222-5798-11e9-ae24-57fa13b361e1";
		setupCall()
			.withServicePath(BASE_URL + "?owner=" + owner)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	// The delegate has delegations in two different municipalities
	// Should return delegations for the specific delegate in the specified municipality (2281).
	@Test
	void test04_getFacilityDelegationsByDelegatedTo() {
		var delegatedTo = "81471222-5798-11e9-ae24-57fa13b361e2";
		setupCall()
			.withServicePath(BASE_URL + "?delegatedTo=" + delegatedTo)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	// The delegate has delegations in two different municipalities
	// Should return delegations for the specific delegate in the specified municipality (1984).
	@Test
	void test05_getFacilityDelegationsByDelegatedToAndAnotherMunicipalityId() {
		var delegatedTo = "81471222-5798-11e9-ae24-57fa13b361e2";
		setupCall()
			.withServicePath("/1984/delegates?delegatedTo=" + delegatedTo)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
	
	// "Same" as above but with a specific delegation id.
	@Test
	void test06_getFacilityDelegationByIdAndMunicipalityId() {
		var facilityDelegationId = "78ee675d-4ab5-4c9c-a80a-5c508f1c55af";
		setupCall()
			.withServicePath("/1984/delegates" + "/" + facilityDelegationId)
			.withHttpMethod(GET)
			.withHeader(X_SENT_BY, X_SENT_BY_VALUE)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
