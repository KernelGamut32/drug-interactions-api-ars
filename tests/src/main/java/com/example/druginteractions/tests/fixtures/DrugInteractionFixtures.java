package com.example.druginteractions.tests.fixtures;

import com.example.druginteractions.domain.model.DrugInteraction;
import com.example.druginteractions.domain.model.DrugSignals;
import java.util.List;

public class DrugInteractionFixtures {
  public static DrugInteraction createInteraction() {
    return DrugInteraction.builder()
        .drugA("Aspirin")
        .drugB("Warfarin")
        .note("Increased risk of bleeding when used together")
        .build();
  }

  public static DrugSignals createSignals() {
    return DrugSignals.builder()
        .count(150)
        .topReactions(
            List.of(
                DrugSignals.AdverseReaction.builder().reaction("HAEMORRHAGE").n(25).build(),
                DrugSignals.AdverseReaction.builder()
                    .reaction("GASTROINTESTINAL BLEEDING")
                    .n(15)
                    .build()))
        .build();
  }
}
