package com.example.druginteractions.adapters.memory;

import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import com.example.druginteractions.domain.ports.DrugInteractionRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDrugInteractionRepository implements DrugInteractionRepository {
  private final Map<String, InteractionNote> storage = new ConcurrentHashMap<>();

  @Override
  public Optional<InteractionNote> find(DrugPair pair) {
    return Optional.ofNullable(storage.get(createKey(pair)));
  }

  @Override
  public InteractionNote upsert(InteractionNote note) {
    storage.put(createKey(note.pair()), note);
    return note;
  }

  private String createKey(DrugPair pair) {
    // DrugPair already ensures consistent ordering of drugs
    return pair.drugA().toLowerCase() + ":" + pair.drugB().toLowerCase();
  }
}
