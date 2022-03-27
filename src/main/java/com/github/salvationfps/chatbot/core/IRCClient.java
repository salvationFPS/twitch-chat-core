package com.github.salvationfps.chatbot.core;

import com.github.salvationfps.chatbot.core.parsers.MessageParser;
import com.github.salvationfps.chatbot.core.utils.MessageFormatter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import static com.github.salvationfps.chatbot.core.utils.StringConstants.MESSAGE_END;

public class IRCClient {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private MessageParser messageParser = new MessageParser();

    public IRCClient(String host, int port) throws Exception{
        this.socket = new Socket(host, port);
        socket.setKeepAlive(true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void connect(String nick, String token, String channel) throws Exception{
        sendMessage("PASS " + token);
        sendMessage("NICK " + nick);
        sendMessage("JOIN #" + channel);
        sendMessage("CAP REQ :twitch.tv/commands twitch.tv/tags" + MESSAGE_END);
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
                if (msg != null){
                    Thread.sleep(1000);
                    sendPrivMessage(MessageFormatter.format(msg));
                }
                if (receivedLine.contains("PING")){
                    sendMessage("PONG");
                }
                receivedLine = null;
            }
    }

    private void sendMessage(String text) throws Exception{
        printRequestMessage(text);
        out.write(text + MESSAGE_END);
        out.flush();
    }

    private void sendPrivMessage(String text) throws Exception{
        printRequestMessage(text);
        out.write("PRIVMSG #salvationfps :" + text + MESSAGE_END);
        out.flush();
    }

    private void printRequestMessage(String text){
        if (text.startsWith("PASS")){
            return;
        }
        System.out.println(text);
    }
}
