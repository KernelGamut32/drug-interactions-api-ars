package com.example.druginteractions.adapters.openfda;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.druginteractions.domain.model.OpenFdaSignal;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.reactive.function.client.WebClient;

class OpenFdaAdapterTest {
  @RegisterExtension static WireMockExtension wireMock = WireMockExtension.newInstance().build();

  private final OpenFdaAdapter adapter;

  OpenFdaAdapterTest() {
    WebClient webClient = WebClient.builder().baseUrl(wireMock.baseUrl()).build();
    this.adapter = new OpenFdaAdapter(webClient);
  }

  @Test
  void shouldReturnDrugSignals() {
    // given
    wireMock.stubFor(
        get(urlPathMatching("/drug/event.json.*"))
            .withQueryParam(
                "search",
                equalTo(
                    "patient.drug.medicinalproduct:\"ASPIRIN\" AND patient.drug.medicinalproduct:\"WARFARIN\""))
            .withQueryParam("count", equalTo("patient.reaction.reactionmeddrapt.exact"))
            .withQueryParam("limit", equalTo("10"))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                            {
                                "results": [
                                    {
                                        "term": "HAEMORRHAGE",
                                        "count": 25
                                    },
                                    {
                                        "term": "GASTROINTESTINAL BLEEDING",
                                        "count": 15
                                    }
                                ]
                            }
                            """)));

    // when
    OpenFdaSignal signals = adapter.fetchSignals("aspirin", "warfarin", 10).block();

    // then
    assertThat(signals.count()).isEqualTo(40);
    assertThat(signals.topReactions())
        .hasSize(2)
        .satisfies(
            reactions -> {
              Map.Entry<String, Long> first = reactions.get(0);
              Map.Entry<String, Long> second = reactions.get(1);

              assertThat(first.getKey()).isEqualTo("HAEMORRHAGE");
              assertThat(first.getValue()).isEqualTo(25L);
              assertThat(second.getKey()).isEqualTo("GASTROINTESTINAL BLEEDING");
              assertThat(second.getValue()).isEqualTo(15L);
            });
  }
}
