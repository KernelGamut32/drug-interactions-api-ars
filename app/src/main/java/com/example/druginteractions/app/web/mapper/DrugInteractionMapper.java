package com.example.druginteractions.app.web.mapper;

import com.example.druginteractions.app.web.dto.DrugInteractionResponse;
import com.example.druginteractions.domain.model.DrugPair;
import com.example.druginteractions.domain.model.InteractionNote;
import org.springframework.stereotype.Component;

@Component
public class DrugInteractionMapper {
  public DrugPair toDrugPair(String drugA, String drugB) {
    return new DrugPair(drugA, drugB);
  }

  public DrugInteractionResponse toResponse(InteractionNote note) {
    return new DrugInteractionResponse(
        note.id(), note.pair().drugA(), note.pair().drugB(), note.note(), note.updatedAt());
  }
}
