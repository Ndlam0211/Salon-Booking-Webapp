package com.lamnd.factory;

import com.lamnd.enums.PaymentMethod;
import com.lamnd.service.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PaymentProcessorFactory {

    private final Map<PaymentMethod, PaymentProcessor> processorMap;

    public PaymentProcessorFactory(List<PaymentProcessor> processors) {
        if (processors == null || processors.isEmpty()) {
            log.error("No payment processors found");
            throw new IllegalArgumentException("At least one payment processor must be provided");
        }

        this.processorMap = processors.stream()
                .collect(Collectors.toMap(
                        PaymentProcessor::getPaymentMethod,
                        Function.identity()
                ));

        log.info("PaymentProcessorFactory initialized with {} processors: {}",
                processorMap.size(), processorMap.keySet());
    }

    public PaymentProcessor getProcessor(PaymentMethod method) {
        if (method == null) {
            log.error("Payment method is null");
            throw new IllegalArgumentException("Payment method cannot be null");
        }

        PaymentProcessor processor = processorMap.get(method);

        if (processor == null) {
            log.error("No processor found for payment method: {}", method);
            throw new RuntimeException("Unsupported payment method: " + method);
        }

        return processor;
    }
}