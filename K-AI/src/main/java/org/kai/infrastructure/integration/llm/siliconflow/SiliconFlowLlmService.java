package org.kai.infrastructure.integration.llm.siliconflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.kai.domain.llm.model.LlmRequest;
import org.kai.domain.llm.model.LlmResponse;
import org.kai.infrastructure.integration.llm.AbstractLlmService;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * SiliconFlow LLM服务实现
 */
@Service
public class SiliconFlowLlmService extends AbstractLlmService {
    
    private final Logger logger = LoggerFactory.getLogger(SiliconFlowLlmService.class);

    public SiliconFlowLlmService(
            @Value("${llm.provider.providers.siliconflow.name:SiliconFlow}") String providerName,
            @Value("${llm.provider.providers.siliconflow.api-url:https://api.siliconflow.cn/v1/chat/completions}") String apiUrl,
            @Value("${llm.provider.providers.siliconflow.api-key:}") String apiKey,
            @Value("${llm.provider.providers.siliconflow.model:Qwen/Qwen2.5-VL-72B-Instruct}") String defaultModel,
            @Value("${llm.provider.providers.siliconflow.timeout:30000}") int timeout) {
        super(providerName, apiUrl, apiKey, defaultModel, timeout);

        if (apiKey == null || apiKey.isEmpty()) {
            logger.warn("SiliconFlow API密钥未配置，请通过环境变量SILICONFLOW_API_KEY设置");
        } else {
            logger.info("初始化SiliconFlow服务，默认模型: {}", defaultModel);
        }
    }
    
    @Override
    public LlmResponse chat(LlmRequest request) {
        try {
            // 准备请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", request.getModel() != null ? request.getModel() : defaultModel);
            requestBody.put("messages", request.getMessages());
            requestBody.put("temperature", request.getTemperature());
            
            if (request.getMaxTokens() != null) {
                requestBody.put("max_tokens", request.getMaxTokens());
            }
            
            // 创建HTTP客户端并设置超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(timeout)
                    .setSocketTimeout(timeout)
                    .build();
                    
            try (CloseableHttpClient httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .build()) {
                
                // 创建POST请求
                HttpPost httpPost = new HttpPost(apiUrl);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Authorization", "Bearer " + apiKey);
                httpPost.setEntity(new StringEntity(requestBody.toString(), StandardCharsets.UTF_8));
                
                // 执行请求
                try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                    HttpEntity entity = response.getEntity();
                    String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                    
                    // 解析响应
                    JSONObject jsonResponse = JSON.parseObject(responseBody);
                    LlmResponse llmResponse = parseSiliconFlowResponse(jsonResponse);
                    logger.info("SiliconFlow响应成功：{}", llmResponse.getId());
                    return llmResponse;
                }
            }
        } catch (Exception e) {
            logger.error("SiliconFlow请求失败", e);
            LlmResponse errorResponse = new LlmResponse();
            errorResponse.setContent("服务调用失败：" + e.getMessage());
            return errorResponse;
        }
    }
    
    @Override
    public Flux<String> streamChat(LlmRequest request) {
        request.setStream(true);
        
        WebClient webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
                
        Map<String, Object> requestMap = JSON.parseObject(JSON.toJSONString(request));
        
        return webClient.post()
                .bodyValue(requestMap)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }
    
    private LlmResponse parseSiliconFlowResponse(JSONObject jsonResponse) {
        LlmResponse response = new LlmResponse();
        response.setId(jsonResponse.getString("id"));
        
        // 获取choices数组中的第一个元素
        JSONObject firstChoice = jsonResponse.getJSONArray("choices").getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        
        response.setContent(message.getString("content"));
        response.setFinishReason(firstChoice.getString("finish_reason"));
        response.setModel(jsonResponse.getString("model"));
        
        // 解析token使用情况
        JSONObject usage = jsonResponse.getJSONObject("usage");
        if (usage != null) {
            response.setTokenUsage(usage.getInteger("total_tokens"));
        }
        
        return response;
    }
}
