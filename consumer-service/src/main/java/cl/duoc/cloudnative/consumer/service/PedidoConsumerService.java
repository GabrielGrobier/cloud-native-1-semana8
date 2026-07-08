package cl.duoc.cloudnative.consumer.service;

import cl.duoc.cloudnative.shared.events.PedidoCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoConsumerService {

    private static final Logger log = LoggerFactory.getLogger(PedidoConsumerService.class);
    private static final String PRODUCTO_ERROR = "ERROR";

    @Value("${app.consumer.simulate-error-for-product:false}")
    private boolean simulateErrorForProduct;

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void procesarPedido(PedidoCreadoEvent event) {
        log.info("Mensaje recibido desde RabbitMQ. eventId={}, pedidoId={}, usuarioId={}",
                event.eventId(), event.pedidoId(), event.usuarioId());

        if (simulateErrorForProduct && event.producto() != null && event.producto().equalsIgnoreCase(PRODUCTO_ERROR)) {
            throw new IllegalStateException("Error simulado para probar reintentos y Dead Letter Queue");
        }

        log.info("Generando guia de despacho para pedido {}", event.pedidoId());
        log.info("Producto={}, cantidad={}, email={}, roles={}",
                event.producto(), event.cantidad(), event.email(), event.roles());
        log.info("Pedido {} procesado correctamente", event.pedidoId());
    }
}
