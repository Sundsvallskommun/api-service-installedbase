package se.sundsvall.installedbase.apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.installedbase.Application;

@WireMockAppTestSuite(files = "classpath:/installedbasebypartyid/", classes = Application.class)
class InstalledBaseByPartyIdIT extends AbstractAppTest {

	private static final String RESPONSE_FILE = "response.json";

	@Test
	void test01_getInstalledBaseByPartyId() {
		setupCall()
			.withServicePath("/2281/installedbase?partyId=898b3634-a2f9-483c-8808-37f3f25cf24e&organizationIds=123456789&date=2025-06-01&sortBy=Company&page=1&limit=15")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_getInstalledBaseByPartyIdWithOnlyRequiredParams() {
		setupCall()
			.withServicePath("/2281/installedbase?partyId=898b3634-a2f9-483c-8808-37f3f25cf24e")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_getInstalledBaseByPartyIdWithMultipleOrganizationIds() {
		setupCall()
			.withServicePath("/2281/installedbase?partyId=898b3634-a2f9-483c-8808-37f3f25cf24e&organizationIds=123456789&organizationIds=987654321")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_getInstalledBaseByPartyIdWithInvalidPartyId() {
		setupCall()
			.withServicePath("/2281/installedbase?partyId=not-a-uuid")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(BAD_REQUEST)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_getInstalledBaseByPartyIdWithInvalidMunicipalityId() {
		setupCall()
			.withServicePath("/invalid/installedbase?partyId=898b3634-a2f9-483c-8808-37f3f25cf24e")
			.withHttpMethod(GET)
			.withExpectedResponseStatus(BAD_REQUEST)
			.withExpectedResponse(RESPONSE_FILE)
			.sendRequestAndVerifyResponse();
	}
}
