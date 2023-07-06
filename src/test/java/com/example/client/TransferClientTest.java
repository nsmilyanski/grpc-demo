package com.example.client;

import com.example.models.BankServiceGrpc;
import com.example.models.TransferRequest;
import com.example.models.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {
    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setUp() {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8081)
                .usePlaintext()
                .build();

        this.transferServiceStub = TransferServiceGrpc.newStub(managedChannel);
    }

    @Test
    public void transferTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        TranferStreamingResponse tranferStreamingResponse = new TranferStreamingResponse(countDownLatch);

        StreamObserver<TransferRequest> transfer = this.transferServiceStub.transfer(tranferStreamingResponse);

        for (int i = 0; i < 100; i++) {
            TransferRequest transferRequest = TransferRequest.newBuilder()
                    .setFromAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setToAccount(ThreadLocalRandom.current().nextInt(1, 11))
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 21))
                    .build();

            transfer.onNext(transferRequest);
        }
        transfer.onCompleted();
        countDownLatch.await();
    }
}
