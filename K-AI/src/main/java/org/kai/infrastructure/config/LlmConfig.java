package org.kai.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.kai.domain.llm.service.LlmService;
import org.kai.infrastructure.integration.llm.siliconflow.SiliconFlowLlmService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LLM服务配置
 */
@Configuration
public class LlmConfig {

    @Bean
    @ConfigurationProperties(prefix = "llm.provider")
    public ProviderProperties providerProperties() {
        return new ProviderProperties();
    }

   
    @Bean
    public Map<String, LlmService> llmServiceMap(SiliconFlowLlmService siliconFlowLlmService) {
        Map<String, LlmService> serviceMap = new ConcurrentHashMap<>();
        serviceMap.put("siliconflow", siliconFlowLlmService);
        return serviceMap;
    }

    public static class ProviderProperties {
        private String defaultProvider;
        private Map<String, Map<String, Object>> providers = new ConcurrentHashMap<>();

        public String getDefaultProvider() {
            return defaultProvider;
        }

        public void setDefaultProvider(String defaultProvider) {
            this.defaultProvider = defaultProvider;
        }

        public Map<String, Map<String, Object>> getProviders() {
            return providers;
        }

        public void setProviders(Map<String, Map<String, Object>> providers) {
            this.providers = providers;
        }
    }
}
