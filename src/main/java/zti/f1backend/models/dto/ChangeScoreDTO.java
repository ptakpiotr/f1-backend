package zti.f1backend.models.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeScoreDTO {
    private int id;

    @NotBlank
    private String raceId;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    private int userId;
}
