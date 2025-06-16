package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.api.model.delegate.FacilityDelegation;
import se.sundsvall.installedbase.service.InstalledBaseService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class FacilityDelegationResourceTest {

	private static final String MUNICIPALITY_ID = "2281";

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void postDelegate() {
		var id = UUID.randomUUID().toString();

		var facilityDelegation = createFacilityDelegation();
		facilityDelegation.setId(id);

		ResponseEntity<Void> response = created(fromPath("/{municipalityId}/delegates/{id}")
			.buildAndExpand(MUNICIPALITY_ID, id)
			.toUri()).header(CONTENT_TYPE, ALL_VALUE).build();

		when(mockService.createFacilityDelegation(anyString(), any(FacilityDelegation.class))).thenReturn(response);

		webTestClient.post()
			.uri("/{municipalityId}/delegates", MUNICIPALITY_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(facilityDelegation)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL_VALUE)
			.expectHeader().location("/" + MUNICIPALITY_ID + "/delegates/" + id)
			.expectBody(ResponseEntity.class);

		verify(mockService).createFacilityDelegation(MUNICIPALITY_ID, facilityDelegation);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegateById() {
		var delegate = createFacilityDelegation();

		when(mockService.getFacilityDelegation(MUNICIPALITY_ID, delegate.getId())).thenReturn(delegate);

		var response = webTestClient.get()
			.uri("/{municipalityId}/delegates/{id}", MUNICIPALITY_ID, delegate.getId())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(delegate.getId());
		verify(mockService).getFacilityDelegation(MUNICIPALITY_ID, delegate.getId());
		verifyNoMoreInteractions(mockService);
	}
}
