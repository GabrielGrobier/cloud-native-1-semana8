package cl.duoc.cloudnative.producer.service;

import cl.duoc.cloudnative.shared.events.PedidoCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoProducerService {

    private static final Logger log = LoggerFactory.getLogger(PedidoProducerService.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public PedidoProducerService(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publicarPedidoCreado(PedidoCreadoEvent event) {
        log.info("Publicando evento en RabbitMQ. eventId={}, pedidoId={}", event.eventId(), event.pedidoId());
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}
