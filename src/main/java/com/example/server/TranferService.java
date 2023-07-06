package com.example.server;

import com.example.models.TransferRequest;
import com.example.models.TransferResponse;
import com.example.models.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TranferService extends TransferServiceGrpc.TransferServiceImplBase {
    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TranferStreamingRequest(responseObserver);
    }
}
