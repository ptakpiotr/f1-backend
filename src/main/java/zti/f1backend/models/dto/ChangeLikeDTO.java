package zti.f1backend.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeLikeDTO {
    private int id;

    private int userId;

    private int commentId;

    private boolean likes;
}
