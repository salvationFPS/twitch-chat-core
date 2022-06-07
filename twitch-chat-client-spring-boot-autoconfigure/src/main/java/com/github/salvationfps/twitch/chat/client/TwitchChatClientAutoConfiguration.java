package com.github.salvationfps.twitch.chat.client;

import com.github.salvationfps.chatbot.core.IRCClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TwitchChatClientProperties.class)
@RequiredArgsConstructor
public class TwitchChatClientAutoConfiguration {

    private final TwitchChatClientProperties properties;

    @Bean
    public IRCClient ircClient(){
        return new IRCClient(properties.getHost(), properties.getPort(), properties.getName(), properties.getOauthToken());
    }

}
