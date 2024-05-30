package zti.f1backend.models.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDTO {
    @NotBlank
    private String content;

    @NotBlank
    @URL
    private String photo;

    @Length(max = 255)
    private String title;

    @NotBlank
    private int userId;
}
