package com.example.druginteractions.adapters.openfda;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenFdaResponse {
  @JsonProperty("results")
  private List<TermCount> results;

  @Getter
  @Setter
  public static class TermCount {
    @JsonProperty("term")
    private String term;

    @JsonProperty("count")
    private int count;
  }

  @Getter
  @Setter
  public static class Error {
    private String code;
    private String message;
  }
}
