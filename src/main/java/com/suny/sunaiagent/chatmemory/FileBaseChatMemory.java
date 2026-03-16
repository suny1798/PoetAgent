package com.suny.sunaiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileBaseChatMemory implements ChatMemory {

    private final String FILE_PATH;

    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        //设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        //注册 Spring AI 的 Message 相关类
        kryo.register(Message.class);
        kryo.register(UserMessage.class);
        kryo.register(SystemMessage.class);
        kryo.register(AssistantMessage.class);
    }

    // 构建对象时，指定文件保存路径
    public FileBaseChatMemory(String filePath) {
        this.FILE_PATH = filePath;
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> messageList = getOrCreateConversation(conversationId);
        messageList.addAll(messages);
        saveConversation(conversationId, messageList);
    }

    @Override
    public List<Message> get(String conversationId) {
        return getOrCreateConversation(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()){
            file.delete();
        }
    }

    /**
     * 获取会话
     * @param conversationId
     * @return
     * @throws FileNotFoundException
     */
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readObject(input, ArrayList.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 保存会话
     * @param conversationId
     * @param messages
     * @throws FileNotFoundException
     */
    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据会话ID获取文件
     * @param conversationId
     * @return
     */
    private  File getConversationFile(String conversationId) {
        return new File(FILE_PATH, conversationId + ".kryo");
    }

}
