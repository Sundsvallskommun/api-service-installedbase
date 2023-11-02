package se.sundsvall.installedbase.apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;

/**
 * Read installed base tests
 */
@WireMockAppTestSuite(files = "classpath:/installedbase/", classes = Application.class)
class InstalledBaseIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getInstalledBaseForPerson() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5564786647?partyId=3b7a5955-f481-42bd-a2b3-6ef8bd76b105")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getInstalledBaseForOrganization() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5564786647?partyId=c62239a0-c17f-4927-b62a-f82f781b4336")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_noCustomerEngagementFound() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5565112233?partyId=3b7a5955-f481-42bd-a2b3-6ef8bd76b105")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getInstalledBaseByMultiplePersonIds() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5565027223?partyId=de8237fe-560f-4fad-931c-35f87ad23dba&partyId=413aebb2-bccf-4447-b25e-d2b5f1d47c38")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getInstalledBaseByMultipleCalls() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5564786647?partyId=3b7a5955-f481-42bd-a2b3-6ef8bd76b105")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_getModifiedInstalledBasesAfterSpecificDate() throws Exception {
		setupCall()
			.withServicePath("/installedbase/5564786647?partyId=825754d6-c92b-49c0-a62d-97a706b08038&modifiedFrom=2017-12-07")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
