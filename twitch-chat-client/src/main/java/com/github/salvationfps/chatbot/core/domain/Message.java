package com.github.salvationfps.chatbot.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    private Chatter from;
    private String text;
}
