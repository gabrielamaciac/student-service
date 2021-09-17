package com.learning.student.studentservice.exception;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.util.ErrorHandler;

public class CustomErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        if (!(t.getCause() instanceof BusinessException)) {
            throw new AmqpRejectAndDontRequeueException("Service threw exception. Message won't be requeued.", t);
        }
    }
}
