package org.kai.domain.llm.model;

/**
 * LLM响应模型
 */
public class LlmResponse {
    private String id;
    private String content;
    private String model;
    private String finishReason;
    private Integer tokenUsage;
    
    public LlmResponse() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getFinishReason() {
        return finishReason;
    }
    
    public void setFinishReason(String finishReason) {
        this.finishReason = finishReason;
    }
    
    public Integer getTokenUsage() {
        return tokenUsage;
    }
    
    public void setTokenUsage(Integer tokenUsage) {
        this.tokenUsage = tokenUsage;
    }
}
