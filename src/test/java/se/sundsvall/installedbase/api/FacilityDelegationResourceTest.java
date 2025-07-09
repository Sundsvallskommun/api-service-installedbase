package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createFacilityDelegationResponse;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.installedbase.Application;
import se.sundsvall.installedbase.api.model.facilitydelegation.CreateFacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.FacilityDelegation;
import se.sundsvall.installedbase.api.model.facilitydelegation.UpdateFacilityDelegation;
import se.sundsvall.installedbase.service.InstalledBaseService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class FacilityDelegationResourceTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/{municipalityId}/delegates";

	@MockitoBean
	private InstalledBaseService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void postDelegate() {
		var id = UUID.randomUUID().toString();

		var facilityDelegation = createFacilityDelegation();

		when(mockService.createFacilityDelegation(anyString(), any(CreateFacilityDelegation.class))).thenReturn(id);

		webTestClient.post()
			.uri(BASE_URL, MUNICIPALITY_ID)
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
		var delegate = createFacilityDelegationResponse();

		when(mockService.getFacilityDelegation(MUNICIPALITY_ID, delegate.getId())).thenReturn(delegate);

		var response = webTestClient.get()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, delegate.getId())
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

	@Test
	void getDelegationsByOwner() {
		var delegation1 = createFacilityDelegationResponse();
		// Set the same owner and delegatedTo for both delegations to ensure they are returned
		var delegation2 = createFacilityDelegationResponse();
		delegation2.setOwner(delegation1.getOwner());
		delegation2.setDelegatedTo(delegation1.getDelegatedTo());

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, delegation1.getOwner(), null, null)).thenReturn(List.of(delegation1, delegation2));

		var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}", MUNICIPALITY_ID, delegation1.getOwner())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(2)
			.extracting(FacilityDelegation::getOwner, FacilityDelegation::getDelegatedTo)
			.containsExactly(
				tuple(delegation1.getOwner(), delegation2.getDelegatedTo()),
				tuple(delegation2.getOwner(), delegation2.getDelegatedTo()));

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, delegation1.getOwner(), null, null);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByOwnerAndDelegatedTo() {
		var delegation = createFacilityDelegationResponse();

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo(), null)).thenReturn(List.of(delegation));

		var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&delegatedTo={delegatedTo}", MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(FacilityDelegation::getOwner, FacilityDelegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo(), null);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByOwnerAndDelegatedToAndStatus() {
		var delegation = createFacilityDelegationResponse();

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo(), "ACTIVE")).thenReturn(List.of(delegation));

		var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&delegatedTo={delegatedTo}&status={status}", MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo(), "ACTIVE")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(FacilityDelegation::getOwner, FacilityDelegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo(), "ACTIVE");
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByOwnerAndStatus() {
		var delegation = createFacilityDelegationResponse();

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), null, "ACTIVE")).thenReturn(List.of(delegation));

		var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&status={status}", MUNICIPALITY_ID, delegation.getOwner(), "ACTIVE")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(FacilityDelegation::getOwner, FacilityDelegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, delegation.getOwner(), null, "ACTIVE");
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByDelegatedToAndStatus() {
		var delegation = createFacilityDelegationResponse();

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, null, delegation.getDelegatedTo(), "ACTIVE")).thenReturn(List.of(delegation));

		var response = webTestClient.get()
			.uri(BASE_URL + "?delegatedTo={delegatedTo}&status={status}", MUNICIPALITY_ID, delegation.getDelegatedTo(), "ACTIVE")
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(FacilityDelegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(FacilityDelegation::getOwner, FacilityDelegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, null, delegation.getDelegatedTo(), "ACTIVE");
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsWithNoResults() {
		var owner = UUID.randomUUID().toString();
		var delegatedTo = UUID.randomUUID().toString();

		when(mockService.getFacilityDelegations(MUNICIPALITY_ID, owner, delegatedTo, "ACTIVE")).thenReturn(List.of());

		webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&delegatedTo={delegatedTo}&status={status}", MUNICIPALITY_ID, owner, delegatedTo, "ACTIVE")
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(FacilityDelegation.class)
			.hasSize(0);

		verify(mockService).getFacilityDelegations(MUNICIPALITY_ID, owner, delegatedTo, "ACTIVE");
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void putDelegations() {
		var id = UUID.randomUUID().toString();
		var facilityDelegation = createFacilityDelegation();

		doNothing().when(mockService).putFacilityDelegation(eq(MUNICIPALITY_ID), eq(id), any(UpdateFacilityDelegation.class));

		webTestClient.put()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, id)
			.contentType(APPLICATION_JSON)
			.bodyValue(facilityDelegation)
			.exchange()
			.expectStatus().isNoContent()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody(ResponseEntity.class);

		verify(mockService).putFacilityDelegation(eq(MUNICIPALITY_ID), eq(id), any(UpdateFacilityDelegation.class));
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void testDeleteFacilityDelegation() {
		var id = UUID.randomUUID().toString();

		doNothing().when(mockService).deleteFacilityDelegation(MUNICIPALITY_ID, id);

		webTestClient.delete()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, id)
			.exchange()
			.expectStatus().isAccepted()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody(ResponseEntity.class);

		verify(mockService).deleteFacilityDelegation(MUNICIPALITY_ID, id);
		verifyNoMoreInteractions(mockService);
	}
}
