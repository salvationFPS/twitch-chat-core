package com.github.salvationfps.chatbot.core.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
public class Chatter {

    private String name;
    private String displayName;
    private String color;
    private SubscriptionInfo subscriptionInfo;
    private List<Badge> badges;
    private boolean isBroadcaster;
    private boolean isModerator;
    private boolean isSubscriber;
    private boolean isTurbo;

}
