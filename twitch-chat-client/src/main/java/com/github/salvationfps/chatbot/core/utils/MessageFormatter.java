package com.github.salvationfps.chatbot.core.utils;

import com.github.salvationfps.chatbot.core.domain.Message;

public class MessageFormatter {

    public static String format(Message message) {

        var sb = new StringBuilder();
        sb.append("New msg recieved: ")
                .append(message.toString());
        return sb.toString();
    }
}
