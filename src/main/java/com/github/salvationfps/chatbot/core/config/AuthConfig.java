package com.github.salvationfps.chatbot.core.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {

    private String oathToken;
    private String nick;
}
