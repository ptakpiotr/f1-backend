package zti.f1backend.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangedScoreDTO {
    private int id;

    private int rating;

    public ChangedScoreDTO(int id, int rating) {
        this.id = id;
        this.rating = rating;
    }
}
