package org.wrabz.note.security.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;

}
