package org.kai.domain.llm.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LLM请求模型
 */
public class LlmRequest {
    private String model;
    private List<Map<String, Object>> messages;
    private Double temperature;
    private Integer maxTokens;
    private Boolean stream;
    private Map<String, Object> functions;
    
    public LlmRequest() {
        this.messages = new ArrayList<>();
        this.temperature = 0.7;
        this.stream = false;
    }
    
    /**
     * 添加系统消息
     * 
     * @param content 消息内容
     */
    public void addSystemMessage(String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "system");
        message.put("content", content);
        this.messages.add(message);
    }
    
    /**
     * 添加用户消息
     * 
     * @param content 消息内容
     */
    public void addUserMessage(String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", content);
        this.messages.add(message);
    }
    
    /**
     * 添加助手消息
     * 
     * @param content 消息内容
     */
    public void addAssistantMessage(String content) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "assistant");
        message.put("content", content);
        this.messages.add(message);
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public List<Map<String, Object>> getMessages() {
        return messages;
    }
    
    public void setMessages(List<Map<String, Object>> messages) {
        this.messages = messages;
    }
    
    public Double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    
    public Integer getMaxTokens() {
        return maxTokens;
    }
    
    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }
    
    public Boolean getStream() {
        return stream;
    }
    
    public void setStream(Boolean stream) {
        this.stream = stream;
    }
    
    public Map<String, Object> getFunctions() {
        return functions;
    }
    
    public void setFunctions(Map<String, Object> functions) {
        this.functions = functions;
    }
}
