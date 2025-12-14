package com.insight_pulse.tech.auth.dtos.register;

public record RegisterRequest(
    String fullname,
    String email,
    String password    
) {    
}
