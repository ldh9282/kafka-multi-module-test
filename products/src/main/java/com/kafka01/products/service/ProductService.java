package com.kafka01.products.service;

import com.kafka01.common.event.ProductCreatedEvent;
import com.kafka01.products.dto.CreateProductRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 상품서비스
 */
@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductService(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String createProduct(CreateProductRequest request) {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(MDC.get("logId"), productId, request.title(), request.price(), request.quantity());

        // 비동기실행: topic-name, key, value
        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);

        // 비동기 콜백처리(응답 기다리지 않음 = 넌 블록킹)
        future.whenComplete((result, throwable) -> {
            if (throwable != null) {
                // 비동기 일때는 이미 응답을 리턴해서 GlobalExceptionHandler 가 잡지못함
//                throw new RuntimeException(throwable);
                log.error("Product created event send failed", throwable);
            }

            if (result != null) {
                log.info("Product created event sent successfully with offset: {}", result.getProducerRecord());
            }

        });

        // join 쓰면 동기 콜백처리(응답 기다림 = 블록킹)
//        future.whenComplete((result, throwable) -> {
//            if (throwable != null) {
//                throw new RuntimeException(throwable);
//            }
//
//            if (result != null) {
//                log.info("Product created event sent successfully with offset: {}", result.getProducerRecord());
//            }
//
//        }).join();

        log.info("success");
        return "success";
    }

}

