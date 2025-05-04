package org.kai.infrastructure.integration.llm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.kai.domain.llm.model.LlmRequest;
import org.kai.domain.llm.model.LlmResponse;
import org.kai.domain.llm.service.LlmService;
import reactor.core.publisher.Flux;

/**
 * 抽象LLM服务实现
 */
public abstract class AbstractLlmService implements LlmService {
    
    private final Logger logger = LoggerFactory.getLogger(AbstractLlmService.class);
    
    protected String providerName;
    protected String apiUrl;
    protected String apiKey;
    protected String defaultModel;
    protected int timeout;
    
    public AbstractLlmService(String providerName, String apiUrl, String apiKey, String defaultModel, int timeout) {
        this.providerName = providerName;
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
        this.defaultModel = defaultModel;
        this.timeout = timeout;
    }

    @Override
    public String simpleChat(String text) {
        LlmRequest request = new LlmRequest();
        request.setModel(getDefaultModel());
        request.addUserMessage(text);

        LlmResponse response = chat(request);
        return response.getContent();
    }
    
    @Override
    public String getProviderName() {
        return providerName;
    }
    
    @Override
    public String getDefaultModel() {
        return defaultModel;
    }
}
