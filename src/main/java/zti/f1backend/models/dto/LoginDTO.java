package zti.f1backend.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
