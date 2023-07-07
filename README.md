# grpc-spring-starter
This starter helps you to create the gRPC project with Spring framework.

## Prerequisite
- [x] Java SE 17

- [x] wildfly-27.0.1.Final or latest

- [x] apache-maven

## Usage
Run the following command to build the war package.
```bash
mvn clean package
```

Copy `grpc-server/target/grpc-server.war` into your wildlfy deployment folder `wildfly-27.0.1.Final\standalone\deployments`. Then, run the following command to start the server. Please noted that the gRPC will be started using the Spring container instead of Wildlfy container. The default port is `6565`.
```bash
sh ./standalone.sh
```

## Service Implementation
Mark `@GRpcService` annotation on your service class. The annotated class will be discoverable when initializes the gRPC server. Simple implementation can be referenced as follows:

```java
@GRpcService
public class GreeterServiceImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void greeting(org.grpc.protobuf.GreetingRequest request,
            io.grpc.stub.StreamObserver<org.grpc.protobuf.GreetingResponse> responseObserver) {
        responseObserver.onNext(GreetingResponse.newBuilder().setGreeting("Hello " +
                request.getName()).build());
        responseObserver.onCompleted();
    }
}
```

## Is Spring 5 compatable with this project?
You still able to use Spring 5 for this project. However, you need to change to use `javax.servlet-api` instead of `jakarta.servlet-api` in `grpc-spring-autoconfigure` project.

```xml
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.5</version>
    <scope>provided</scope>
</dependency>
```

Change the java compile version to 11 and make sure the `spring.version` using Spring 5.
```xml
<maven.compiler.source>11</maven.compiler.source>
<maven.compiler.target>11</maven.compiler.target>
<java.version>11</java.version>
<spring.version>5.3.28</spring.version>
```

Change to use `javax.servlet.ServletContext` and `javax.servletServletException` in `WebAppInitializer.class`
```java
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(AppConfig.class);
        container.addListener(new ContextLoaderListener(context));
    }
}
```

## Copy grpc-server.war to wildfly deployment automatically when build
I usually use `maven-antrun-plugin` to copy the war application to wildfly deployment folder for convenience. You may add into `grpc-server` project build profile.
```xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>1.8</version>
    <executions>
        <execution>
            <phase>package</phase>
            <configuration>
                <tasks>
                    <copy file="target/${project.build.finalName}.war" todir="${wildfly.deployment.folder}" />
                </tasks>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```