package zti.f1backend.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDeleteDTO {
    @NotBlank
    private int id;
}
