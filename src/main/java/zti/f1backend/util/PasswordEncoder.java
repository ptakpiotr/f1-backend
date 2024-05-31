package zti.f1backend.util;

import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoder extends Pbkdf2PasswordEncoder {

    public PasswordEncoder(Environment env) {
        super(env.getProperty("app.password_salt"), 16, 1, 256);
    }
}
