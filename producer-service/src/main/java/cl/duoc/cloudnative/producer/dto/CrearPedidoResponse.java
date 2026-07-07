package cl.duoc.cloudnative.producer.dto;

public record CrearPedidoResponse(
        String mensaje,
        String pedidoId,
        String eventId
) {
}
