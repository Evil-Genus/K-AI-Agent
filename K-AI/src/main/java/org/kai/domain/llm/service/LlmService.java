package org.kai.domain.llm.service;

import org.kai.domain.llm.model.LlmRequest;
import org.kai.domain.llm.model.LlmResponse;
import reactor.core.publisher.Flux;

/**
 * LLM服务接口
 */
public interface LlmService {
    
    /**
     * 简单对话
     *
     * @param text 用户输入文本
     * @return 模型回复内容
     */
    String simpleChat(String text);
    
    /**
     * 对话
     *
     * @param request 对话请求
     * @return 对话响应
     */
    LlmResponse chat(LlmRequest request);
    
    /**
     * 流式对话
     *
     * @param request 对话请求
     * @return 流式对话响应
     */
    Flux<String> streamChat(LlmRequest request);
    
    /**
     * 获取服务商名称
     *
     * @return 服务商名称
     */
    String getProviderName();
    
    /**
     * 获取默认模型
     *
     * @return 默认模型名称
     */
    String getDefaultModel();
}
