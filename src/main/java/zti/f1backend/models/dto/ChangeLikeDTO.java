package zti.f1backend.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLikeDTO {
    private int id;

    @NotBlank
    private int userId;

    @NotBlank
    private int commentId;

    private boolean likes;
}
