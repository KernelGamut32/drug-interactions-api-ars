package com.example.druginteractions.domain.ports;

import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import java.util.Optional;

public interface DrugInteractionRepository {
  Optional<InteractionNote> find(DrugPair pair);

  InteractionNote upsert(InteractionNote note);
}
