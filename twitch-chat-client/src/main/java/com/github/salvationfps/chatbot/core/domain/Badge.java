package com.github.salvationfps.chatbot.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Badge {
    private String name;
    private int version;
}
