package com.github.salvationfps.chatbot.core.parsers;

import com.github.salvationfps.chatbot.core.domain.Badge;
import com.github.salvationfps.chatbot.core.domain.Chatter;
import com.github.salvationfps.chatbot.core.domain.Message;
import com.github.salvationfps.chatbot.core.domain.SubscriptionInfo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.github.salvationfps.chatbot.core.utils.Badges.BROADCASTER;
import static com.github.salvationfps.chatbot.core.utils.StringConstants.COMMA_SEPARATOR;
import static com.github.salvationfps.chatbot.core.utils.StringConstants.SLASH_SEPARATOR;
import static com.github.salvationfps.chatbot.core.utils.Tags.*;

public class MessageParser {

    private static final Pattern PRIV_MSG_PATTERN = Pattern.compile(":\\w+!\\w+@\\w+.tmi.twitch.tv PRIVMSG");

    public Message parseMessage(String message){
        final var matcher = PRIV_MSG_PATTERN.matcher(message);
        if (matcher.find()){
            final var tagsPart = message.substring(1, matcher.start() -1);
            final var messagePart = message.substring(matcher.end());
            final var tags = parseMessageTags(tagsPart);
            final var badges = parseBadges(tags.get(BADGES));
            final var subInfo = parseBadgeInfo(tags.get(SUBSCRIPTION_INFO));
            final var messageSplit = messagePart.split(" :");
            final var displayName = tags.get(DISPLAY_NAME);
            final var color = tags.get(COLOR);
            final var isModerator = Boolean.parseBoolean(tags.get(MODERATOR));
            final var isSubscriber = Boolean.parseBoolean(tags.get(SUBSCRIBER));
            final var isBroadcaster = badges.stream().anyMatch(b -> BROADCASTER.equals(b.getName()));
            final var isTurbo = Boolean.parseBoolean(tags.get(TURBO));
            return Message.builder()
                    .from(Chatter.builder()
                            .name(messageSplit[0].substring(2))
                            .badges(badges)
                            .displayName(displayName)
                            .color(color)
                            .isModerator(isModerator)
                            .isSubscriber(isSubscriber)
                            .isBroadcaster(isBroadcaster)
                            .isTurbo(isTurbo)
                            .subscriptionInfo(subInfo)
                            .build())
                    .text(messageSplit[1])
                    .build();
        }
        return null;
    }

    private Map<String, String> parseMessageTags(String tags){
        return Arrays.stream(tags.split(";"))
                .map(b -> b.split("=", -1))
                .collect(Collectors.toMap(e -> e[0], e-> e[1]));
    }


    private SubscriptionInfo parseBadgeInfo(String badgeInfo){
        if (badgeInfo == null || badgeInfo.length() == 0){
            return SubscriptionInfo.builder()
                    .isSubscriber(false)
                    .build();
        }
        return SubscriptionInfo.builder()
                .isSubscriber(true)
                .monthsCount(Integer.parseInt(badgeInfo.split(SLASH_SEPARATOR)[1]))
                .build();
    }

    private List<Badge> parseBadges(String badges){
        if (badges == null || badges.length() == 0){
            return null;
        }
        if (badges.contains(COMMA_SEPARATOR)){
            return Arrays.stream(badges.split(COMMA_SEPARATOR))
                    .map(b -> {
                        var badgeInfo = b.split(SLASH_SEPARATOR);
                        return Badge.builder()
                                .name(badgeInfo[0])
                                .version(Integer.parseInt(badgeInfo[1]))
                                .build();
                    })
                    .collect(Collectors.toList());
        }
        var badgeInfo = badges.split(SLASH_SEPARATOR);
        return Collections.singletonList(Badge.builder()
                .name(badgeInfo[0])
                .version(Integer.parseInt(badgeInfo[1]))
                .build());
    }
}
