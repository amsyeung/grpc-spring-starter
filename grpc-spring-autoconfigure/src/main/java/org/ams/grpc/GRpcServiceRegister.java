package org.ams.grpc;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 *
 * register the service with GRpc server
 * 
 * @author amsyeung
 * @since 1.0.0
 */
public class GRpcServiceRegister implements ApplicationContextAware, InitializingBean, SmartLifecycle {
    private final static Logger log = LogManager.getLogger(GRpcServiceRegister.class);
    private final static int GRPC_PORT = 6565;

    private ApplicationContext applicationContext;
    private Server grpcServer;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        registerCustomGRpcServices();
    }

    private void registerCustomGRpcServices() {
        if (isRunning()) {
            return;
        }

        Map<String, Object> customGRpcServices = applicationContext.getBeansWithAnnotation(GRpcService.class);
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(GRPC_PORT);

        for (Object serviceBean : customGRpcServices.values()) {
            if (serviceBean instanceof BindableService) {
                BindableService bindableService = (BindableService) serviceBean;

                log.info("bindableService.getClass(): " + bindableService.getClass());

                serverBuilder.addService(bindableService);
            } else {
                throw new IllegalArgumentException(
                        "Service " + serviceBean.getClass().getName() + " does not implement BindableService");
            }
        }

        grpcServer = serverBuilder.build();

        start();
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }

    @Override
    public void start() {
        try {
            grpcServer.start();
            isRunning.set(true);
            log.info("gRPC Server started, listening on port {}.", GRPC_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
    }

    @Override
    public void stop() {
        Optional.ofNullable(grpcServer).ifPresent(s -> {
            log.info("Shutting down gRPC server ...");
            try {
                s.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e);
            }

            log.info("gRPC server stopped.");
        });
    }
}
