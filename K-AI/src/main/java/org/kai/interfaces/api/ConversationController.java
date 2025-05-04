package org.kai.interfaces.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.kai.application.conversation.ConversationService;
import org.kai.interfaces.api.common.Result;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 对话控制器
 */
@RestController
@RequestMapping("/api/conversation")
public class ConversationController {
    
    private final Logger logger = LoggerFactory.getLogger(ConversationController.class);
    
    @Resource
    private ConversationService conversationService;
    
    /**
     * 对话接口
     */
    @PostMapping("/chat")
    public Result<Map<String, Object>> chat(@RequestBody Map<String, String> params) {
        try {
            String message = params.get("message");
            String sessionId = params.get("sessionId");
            String provider = params.get("provider");
            String model = params.get("model");
            
            if (message == null || message.isEmpty()) {
                return Result.badRequest("消息内容不能为空");
            }
            
            Map<String, Object> result = conversationService.chat(message, sessionId, provider, model);
            return Result.success(result);
        } catch (Exception e) {
            logger.error("对话请求处理失败", e);
            return Result.error("对话请求处理失败: " + e.getMessage());
        }
    }
    
    /**
     * 流式对话接口
     */
    @PostMapping(value = "/stream-chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody Map<String, String> params) {
        String message = params.get("message");
        String sessionId = params.get("sessionId");
        String provider = params.get("provider");
        String model = params.get("model");
        
        if (message == null || message.isEmpty()) {
            return Flux.error(new IllegalArgumentException("消息内容不能为空"));
        }
        
        return conversationService.streamChat(message, sessionId, provider, model);
    }
}
