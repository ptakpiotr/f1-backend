package zti.f1backend.models.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostEditDTO {
    private String content;

    @URL
    private String photo;

    @Length(max = 255)
    private String title;
}
