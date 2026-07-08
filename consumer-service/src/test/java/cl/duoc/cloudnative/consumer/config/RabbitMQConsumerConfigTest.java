package cl.duoc.cloudnative.consumer.config;

import org.aopalliance.aop.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.rabbitmq.listener.simple.auto-startup=false")
class RabbitMQConsumerConfigTest {

    @Autowired
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @Test
    void shouldDisableRequeueAndConfigureRetryInterceptor() {
        Object defaultRequeueRejected = ReflectionTestUtils.getField(
                rabbitListenerContainerFactory,
                "defaultRequeueRejected"
        );
        Object adviceChain = ReflectionTestUtils.getField(rabbitListenerContainerFactory, "adviceChain");

        assertThat(defaultRequeueRejected).isEqualTo(Boolean.FALSE);
        assertThat(adviceChain).isInstanceOf(Advice[].class);
        assertThat((Advice[]) adviceChain).isNotEmpty();
    }
}
