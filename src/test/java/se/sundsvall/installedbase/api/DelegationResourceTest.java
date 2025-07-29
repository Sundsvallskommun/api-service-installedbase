package se.sundsvall.installedbase.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static se.sundsvall.installedbase.TestDataFactory.createDelegation;
import static se.sundsvall.installedbase.TestDataFactory.createDelegationResponse;
import static se.sundsvall.installedbase.TestDataFactory.updateDelegation;

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
import se.sundsvall.installedbase.api.model.delegation.CreateDelegation;
import se.sundsvall.installedbase.api.model.delegation.Delegation;
import se.sundsvall.installedbase.service.DelegationService;

@ActiveProfiles("junit")
@SpringBootTest(classes = Application.class, webEnvironment = RANDOM_PORT)
class DelegationResourceTest {

	private static final String MUNICIPALITY_ID = "2281";
	private static final String BASE_URL = "/{municipalityId}/delegations";

	@MockitoBean
	private DelegationService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void postDelegation() {
		final var id = UUID.randomUUID().toString();

		final var delegation = createDelegation();

		when(mockService.createDelegation(anyString(), any(CreateDelegation.class))).thenReturn(id);

		webTestClient.post()
			.uri(BASE_URL, MUNICIPALITY_ID)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().contentType(ALL_VALUE)
			.expectHeader().location("/" + MUNICIPALITY_ID + "/delegations/" + id)
			.expectBody(ResponseEntity.class);

		verify(mockService).createDelegation(MUNICIPALITY_ID, delegation);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void patchDelegation() {
		final var id = UUID.randomUUID().toString();
		final var delegation = updateDelegation();

		webTestClient.patch()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, id)
			.contentType(APPLICATION_JSON)
			.bodyValue(delegation)
			.exchange()
			.expectStatus().isAccepted()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody().isEmpty();

		verify(mockService).updateDelegation(MUNICIPALITY_ID, id, delegation);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationById() {
		final var delegation = createDelegationResponse();

		when(mockService.getDelegation(MUNICIPALITY_ID, delegation.getId())).thenReturn(delegation);

		final var response = webTestClient.get()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, delegation.getId())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBody(Delegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isNotNull();
		assertThat(response.getId()).isEqualTo(delegation.getId());

		verify(mockService).getDelegation(MUNICIPALITY_ID, delegation.getId());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByOwner() {
		final var delegation1 = createDelegationResponse();
		// Set the same owner and delegatedTo for both delegations to ensure they are returned
		final var delegation2 = createDelegationResponse();
		delegation2.setOwner(delegation1.getOwner());
		delegation2.setDelegatedTo(delegation1.getDelegatedTo());

		when(mockService.getDelegations(MUNICIPALITY_ID, delegation1.getOwner(), null)).thenReturn(List.of(delegation1, delegation2));

		final var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}", MUNICIPALITY_ID, delegation1.getOwner())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(Delegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(2)
			.extracting(Delegation::getOwner, Delegation::getDelegatedTo)
			.containsExactly(
				tuple(delegation1.getOwner(), delegation2.getDelegatedTo()),
				tuple(delegation2.getOwner(), delegation2.getDelegatedTo()));

		verify(mockService).getDelegations(MUNICIPALITY_ID, delegation1.getOwner(), null);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByOwnerAndDelegatedTo() {
		final var delegation = createDelegationResponse();

		when(mockService.getDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo())).thenReturn(List.of(delegation));

		final var response = webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&delegatedTo={delegatedTo}", MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(Delegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(Delegation::getOwner, Delegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getDelegations(MUNICIPALITY_ID, delegation.getOwner(), delegation.getDelegatedTo());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsByDelegatedTo() {
		final var delegation = createDelegationResponse();

		when(mockService.getDelegations(MUNICIPALITY_ID, null, delegation.getDelegatedTo())).thenReturn(List.of(delegation));

		final var response = webTestClient.get()
			.uri(BASE_URL + "?delegatedTo={delegatedTo}", MUNICIPALITY_ID, delegation.getDelegatedTo())
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(APPLICATION_JSON)
			.expectBodyList(Delegation.class)
			.returnResult()
			.getResponseBody();

		assertThat(response)
			.hasSize(1)
			.extracting(Delegation::getOwner, Delegation::getDelegatedTo)
			.containsExactly(tuple(delegation.getOwner(), delegation.getDelegatedTo()));

		verify(mockService).getDelegations(MUNICIPALITY_ID, null, delegation.getDelegatedTo());
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void getDelegationsWithNoResults() {
		final var owner = UUID.randomUUID().toString();
		final var delegatedTo = UUID.randomUUID().toString();

		when(mockService.getDelegations(MUNICIPALITY_ID, owner, delegatedTo)).thenReturn(List.of());

		webTestClient.get()
			.uri(BASE_URL + "?owner={owner}&delegatedTo={delegatedTo}", MUNICIPALITY_ID, owner, delegatedTo)
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(Delegation.class)
			.hasSize(0);

		verify(mockService).getDelegations(MUNICIPALITY_ID, owner, delegatedTo);
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void testDeleteDelegation() {
		final var id = UUID.randomUUID().toString();

		doNothing().when(mockService).deleteDelegation(MUNICIPALITY_ID, id);

		webTestClient.delete()
			.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, id)
			.exchange()
			.expectStatus().isAccepted()
			.expectHeader().contentType(ALL_VALUE)
			.expectBody(ResponseEntity.class);

		verify(mockService).deleteDelegation(MUNICIPALITY_ID, id);
		verifyNoMoreInteractions(mockService);
	}
}
