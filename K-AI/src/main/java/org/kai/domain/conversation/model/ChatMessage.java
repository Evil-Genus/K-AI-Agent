package org.kai.domain.conversation.model;

import java.util.Date;

/**
 * 聊天消息模型
 */
public class ChatMessage {
    
    private String id;
    private String sessionId;
    private String role;
    private String content;
    private Date createTime;
    
    public ChatMessage() {
        this.createTime = new Date();
    }
    
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
        this.createTime = new Date();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
