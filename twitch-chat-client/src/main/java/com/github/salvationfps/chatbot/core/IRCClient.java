package com.github.salvationfps.chatbot.core;

import com.github.salvationfps.chatbot.core.parsers.MessageParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.github.salvationfps.chatbot.core.utils.StringConstants.MESSAGE_END;

@Slf4j
public class IRCClient {

    private static final String TOKEN_PREFIX = "PASS ";
    private static final String NAME_PREFIX = "NICK ";
    private static final String JOINING_CHANNEL_NAME_PREFIX = "JOIN # ";
    private static final String CAPABILITIES_PREFIX = "CAP REQ :twitch.tv/commands twitch.tv/tags";
    private static final String PRIVATE_MESSAGE_PREFIX = "PRIVMSG #";

    private final String name;
    private final String oauthToken;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final MessageParser messageParser = new MessageParser();
    private String channel;

    @SneakyThrows
    public IRCClient(String host, int port, String name, String oauthToken) {
        this.name = name;
        this.oauthToken = oauthToken;
        final var socket = new Socket(host, port);
        socket.setKeepAlive(true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void connect(String channel) throws Exception{
        this.channel = channel;
        sendMessage(TOKEN_PREFIX + oauthToken);
        sendMessage(NAME_PREFIX + name);
        sendMessage(JOINING_CHANNEL_NAME_PREFIX + channel);
        sendMessage(CAPABILITIES_PREFIX + MESSAGE_END);
        boolean previousCR = false;
        var sb = new StringBuilder();
        String receivedLine = null;
            while (true){
                var data = in.read();
                if (data == -1){
                    break;
                }
                if (data == '\r'){
                    previousCR = true;
                } else if (data == '\n'){
                    if (previousCR){
                        receivedLine = sb.toString();
                        sb.setLength(0);
                        previousCR = false;
                    }
                } else {
                    sb.append((char)data);
                    previousCR = false;
                }
                if (receivedLine == null) {
                    // Read more characters to get to end of line
                    continue;
                }
                System.out.println(receivedLine);
                var msg = messageParser.parseMessage(receivedLine);
                if (receivedLine.contains("PING")){
                    sendMessage("PONG");
                }
                receivedLine = null;
            }
    }

    private void sendMessage(String text) throws Exception{
        log.debug("Outbound message: " + text);
        out.write(text + MESSAGE_END);
        out.flush();
    }

    private void sendPrivMessage(String text) throws Exception{
        log.debug("Outbound message: " + text);
        out.write(PRIVATE_MESSAGE_PREFIX + channel + " :" + text + MESSAGE_END);
        out.flush();
    }
}
