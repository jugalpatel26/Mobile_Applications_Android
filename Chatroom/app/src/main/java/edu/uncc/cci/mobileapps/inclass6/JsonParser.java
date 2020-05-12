package edu.uncc.cci.mobileapps.inclass6;

import com.google.gson.Gson;

import java.util.ArrayList;

class JsonParser {

    public static ArrayList<MessageThread> parseThreadList(String json) {
        Gson gson = new Gson();
        ThreadListDto threadListDto = gson.fromJson(json, ThreadListDto.class);
        return threadListDto.getThreads();
    }

    public static MessageThread parseThread(String json) {
        Gson gson = new Gson();
        MessageThreadDto threadDto = gson.fromJson(json, MessageThreadDto.class);
        return threadDto.getThread();
    }

    public static ArrayList<Message> parseMessageList(String json) {
        Gson gson = new Gson();
        MessageListDto messageListDto = gson.fromJson(json, MessageListDto.class);
        return messageListDto.getMessages();
    }

    public static Message parseMessage(String json) {
        Gson gson = new Gson();
        MessageDto mesgDto = gson.fromJson(json, MessageDto.class);
        return mesgDto.getMessage();
    }

    public static User parseUser(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

}
