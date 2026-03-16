package com.kafka01.notifications.email.handler;

import com.kafka01.common.event.ProductCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 상품 생성 이벤트 컨슈머
 */
@Component
@KafkaListener(topics = "product-created-events-topic") // 여기서 토픽 구독 설정
public class ProductCreatedEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductCreatedEventHandler.class);

    @KafkaHandler // 클래스에 @KafkaListener가 있다면 메서드에는 @KafkaHandler를 써야 합니다.
    public void handle(ProductCreatedEvent event) {
        log.info("ProductCreatedEvent: {}", event);
    }
}
