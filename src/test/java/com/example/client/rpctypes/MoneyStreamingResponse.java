package com.example.client.rpctypes;

import com.example.client.metadata.ClientConstants;
import com.example.models.Money;
import com.example.models.WithdrawError;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {
    private final CountDownLatch countDownLatch;

    public MoneyStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Recieved async : " + money.getValue());

    }

    @Override
    public void onError(Throwable throwable) {
        Metadata metadata = Status.trailersFromThrowable(throwable);
        WithdrawError withdrawError = metadata.get(ClientConstants.WITHDRAW_ERROR_KEY);

        System.out.println(withdrawError.getAmount() + " : " + withdrawError.getErrorMessage());
        countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is done!!");
        countDownLatch.countDown();
    }
}
