package cl.duoc.cloudnative.shared.events;

import java.util.List;

public record PedidoCreadoEvent(
        String eventId,
        String pedidoId,
        String producto,
        Integer cantidad,
        String usuarioId,
        String email,
        List<String> roles,
        String issuer,
        String fechaCreacion
) {
}
