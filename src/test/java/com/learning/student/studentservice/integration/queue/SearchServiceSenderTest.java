package com.learning.student.studentservice.integration.queue;

import com.learning.student.studentservice.integration.model.search.SearchPayload;
import com.learning.student.studentservice.util.StudentTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SearchServiceSenderTest {

    AmqpTemplate jsonRabbitTemplate;
    SearchServiceSender searchServiceSender;

    SearchPayload searchPayload = StudentTestData.getSearchPayload();

    @BeforeEach
    void setUp() {
        jsonRabbitTemplate = mock(AmqpTemplate.class);
        searchServiceSender = new SearchServiceSender(jsonRabbitTemplate);
    }

    @Test
    void searchPayloadIsConvertedAndSent() {
        searchServiceSender.sendPayload(searchPayload);
        verify(jsonRabbitTemplate).convertAndSend(null, null, searchPayload);
    }
}
