package org.kai.application.conversation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.kai.domain.conversation.model.ChatMessage;
import org.kai.domain.llm.model.LlmRequest;
import org.kai.domain.llm.model.LlmResponse;
import org.kai.domain.llm.service.LlmService;
import org.kai.infrastructure.config.LlmConfig;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 对话服务
 */
@Service
public class ConversationService {
    
    private final Logger logger = LoggerFactory.getLogger(ConversationService.class);
    
    @Resource
    private Map<String, LlmService> llmServiceMap;
    
    @Resource
    private LlmConfig.ProviderProperties providerProperties;
    
    /**
     * 处理单轮对话
     */
    public Map<String, Object> chat(String message, String sessionId, String provider, String model) {
        // 确定使用的服务商
        String providerKey = provider != null ? provider : providerProperties.getDefaultProvider();
        LlmService llmService = llmServiceMap.get(providerKey);
        
        if (llmService == null) {
            throw new RuntimeException("未找到对应的LLM服务商: " + providerKey);
        }
        
        // 创建请求
        LlmRequest request = new LlmRequest();
        request.setModel(model != null ? model : llmService.getDefaultModel());
        request.addUserMessage(message);
        
        // 发送请求
        LlmResponse response = llmService.chat(request);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("content", response.getContent());
        result.put("sessionId", sessionId != null ? sessionId : UUID.randomUUID().toString());
        result.put("provider", llmService.getProviderName());
        result.put("model", response.getModel());
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * 处理流式对话
     */
    public Flux<String> streamChat(String message, String sessionId, String provider, String model) {
        // 确定使用的服务商
        String providerKey = provider != null ? provider : providerProperties.getDefaultProvider();
        LlmService llmService = llmServiceMap.get(providerKey);
        
        if (llmService == null) {
            throw new RuntimeException("未找到对应的LLM服务商: " + providerKey);
        }
        
        // 创建请求
        LlmRequest request = new LlmRequest();
        request.setModel(model != null ? model : llmService.getDefaultModel());
        request.setStream(true);
        request.addUserMessage(message);
        
        // 发送流式请求
        return llmService.streamChat(request);
    }
}
