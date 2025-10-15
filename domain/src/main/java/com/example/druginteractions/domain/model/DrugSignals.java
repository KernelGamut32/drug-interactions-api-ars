package com.example.druginteractions.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DrugSignals {
  int count;
  List<AdverseReaction> topReactions;

  @Value
  @Builder
  public static class AdverseReaction {
    String reaction;
    int n;
  }
}
