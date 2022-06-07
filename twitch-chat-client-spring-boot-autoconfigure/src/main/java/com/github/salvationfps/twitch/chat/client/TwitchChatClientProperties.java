package com.github.salvationfps.twitch.chat.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "twitch.chat")
@Getter
@Setter
public class TwitchChatClientProperties {

    private String host;
    private int port;
    private String name;
    private String oauthToken;
}
