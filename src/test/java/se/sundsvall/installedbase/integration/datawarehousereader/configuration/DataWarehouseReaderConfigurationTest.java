package se.sundsvall.installedbase.integration.datawarehousereader.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static se.sundsvall.installedbase.integration.datawarehousereader.configuration.DataWarehouseReaderConfiguration.CLIENT_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;

@ExtendWith(MockitoExtension.class)
class DataWarehouseReaderConfigurationTest {

	@Mock
	private ClientRegistrationRepository clientRegistrationRepositoryMock;

	@Mock
	private ClientRegistration clientRegistrationMock;

	@Spy
	private FeignMultiCustomizer feignMultiCustomizerSpy;

	@Mock
	private FeignBuilderCustomizer feignBuilderCustomizerMock;

	@Mock
	private DataWarehouseReaderProperties propertiesMock;

	@Test
	void testFeignBuilderCustomizer() {
		final var configuration = new DataWarehouseReaderConfiguration();

		when(clientRegistrationRepositoryMock.findByRegistrationId(any())).thenReturn(clientRegistrationMock);
		when(propertiesMock.connectTimeout()).thenReturn(1);
		when(propertiesMock.readTimeout()).thenReturn(2);
		when(feignMultiCustomizerSpy.composeCustomizersToOne()).thenReturn(feignBuilderCustomizerMock);

		try (final MockedStatic<FeignMultiCustomizer> feignMultiCustomizerMock = Mockito.mockStatic(FeignMultiCustomizer.class)) {
			feignMultiCustomizerMock.when(FeignMultiCustomizer::create).thenReturn(feignMultiCustomizerSpy);

			final var customizer = configuration.feignBuilderCustomizer(propertiesMock, clientRegistrationRepositoryMock);

			final ArgumentCaptor<ProblemErrorDecoder> errorDecoderCaptor = ArgumentCaptor.forClass(ProblemErrorDecoder.class);

			verify(feignMultiCustomizerSpy).withErrorDecoder(errorDecoderCaptor.capture());
			verify(clientRegistrationRepositoryMock).findByRegistrationId(CLIENT_ID);
			verify(feignMultiCustomizerSpy).withRetryableOAuth2InterceptorForClientRegistration(same(clientRegistrationMock));
			verify(propertiesMock).connectTimeout();
			verify(propertiesMock).readTimeout();
			verify(feignMultiCustomizerSpy).withRequestTimeoutsInSeconds(1, 2);
			verify(feignMultiCustomizerSpy).composeCustomizersToOne();

			assertThat(customizer).isSameAs(feignBuilderCustomizerMock);

			// Assert ErrorDecoder
			assertThat(errorDecoderCaptor.getValue())
				.isInstanceOf(ProblemErrorDecoder.class)
				.hasFieldOrPropertyWithValue("integrationName", CLIENT_ID)
				.hasFieldOrPropertyWithValue("bypassResponseCodes", List.of(NOT_FOUND.value()));
		}
	}
}
