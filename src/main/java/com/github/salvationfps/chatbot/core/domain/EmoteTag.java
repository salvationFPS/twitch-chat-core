package com.github.salvationfps.chatbot.core.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmoteTag {

    private String emoteId;
    private List<EmotePosition> emotePositions;

    @Data
    @Builder
    public static class EmotePosition{
        private int startIndex;
        private int endIndex;
    }
}
