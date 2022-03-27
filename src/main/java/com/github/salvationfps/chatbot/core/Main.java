package com.github.salvationfps.chatbot.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.salvationfps.chatbot.core.config.AuthConfig;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        var mapper = new ObjectMapper(new YAMLFactory());

        var authData = mapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream("auth_config.yml"), AuthConfig.class);
        var client = new IRCClient("irc.chat.twitch.tv", 6667);
        client.connect(authData.getNick(), authData.getOathToken(), authData.getNick());
    }
}
