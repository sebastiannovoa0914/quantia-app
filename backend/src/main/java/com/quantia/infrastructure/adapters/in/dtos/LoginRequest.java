package com.quantia.infrastructure.adapters.in.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contrasena;
}
