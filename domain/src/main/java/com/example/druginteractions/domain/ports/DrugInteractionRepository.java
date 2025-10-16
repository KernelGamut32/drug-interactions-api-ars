package com.example.druginteractions.domain.ports;

import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface DrugInteractionRepository {
  Optional<InteractionNote> find(DrugPair pair);

  InteractionNote upsert(InteractionNote note);
}
