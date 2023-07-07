package org.ams.grpc.services;

import org.ams.grpc.GRpcService;
import org.grpc.protobuf.GreeterGrpc;
import org.grpc.protobuf.GreetingResponse;

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
