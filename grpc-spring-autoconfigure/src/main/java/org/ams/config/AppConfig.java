package org.ams.config;

import org.ams.grpc.GRpcServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 
 * @author amsyeung
 * @since 1.0.0
 */
@Configuration
@Import(GRpcServiceConfiguration.class)
@ComponentScan(basePackages = "org.ams.grpc.services")
public class AppConfig {

}
