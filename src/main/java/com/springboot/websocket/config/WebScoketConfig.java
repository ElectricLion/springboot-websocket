package com.springboot.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author:tangh
 * @since 1.0
 */
@Configuration
public class WebScoketConfig {
        /**
         *服务端点导出器
         * @return
         */
        @Bean
        public ServerEndpointExporter serverEndpointExporter() {
            return new ServerEndpointExporter();
        }


}
