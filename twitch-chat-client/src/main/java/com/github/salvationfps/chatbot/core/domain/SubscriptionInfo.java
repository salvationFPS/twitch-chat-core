package com.github.salvationfps.chatbot.core.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionInfo {

    private boolean isSubscriber;
    private int monthsCount;
}
