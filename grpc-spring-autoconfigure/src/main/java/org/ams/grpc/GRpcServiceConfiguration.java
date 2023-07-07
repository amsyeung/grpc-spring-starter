package org.ams.grpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * Enable your custom annotation: Finally, you need to enable your custom
 * annotation and make it discoverable by Spring.
 * 
 * @author amsyeung
 * @since 1.0.0
 */
@Configuration
public class GRpcServiceConfiguration {
    @Bean
    public static GRpcServiceRegister gRpcServiceRegister() {
        return new GRpcServiceRegister();
    }
}
