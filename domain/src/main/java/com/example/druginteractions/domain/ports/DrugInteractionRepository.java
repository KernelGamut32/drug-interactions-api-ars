package com.example.druginteractions.domain.ports;

import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrugInteractionRepository {
  Optional<InteractionNote> find(DrugPair pair);

  InteractionNote upsert(InteractionNote note);
}
