package com.hospitalmanagement.security.dto;

import com.hospitalmanagement.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest
{
    private String username;
    private String email;
    private String password;
    private Set<Role> roles;
}
