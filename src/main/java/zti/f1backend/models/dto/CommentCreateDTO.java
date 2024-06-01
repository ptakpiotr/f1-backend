package zti.f1backend.models.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateDTO {
    @NotBlank
    @Length(max = 250)
    private String comment;
    
    @NotBlank
    private String raceId;

    private int userId;
}
