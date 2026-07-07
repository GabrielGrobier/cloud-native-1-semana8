package cl.duoc.cloudnative.producer.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearPedidoRequest(
        @NotBlank(message = "pedidoId es obligatorio")
        String pedidoId,

        @NotBlank(message = "producto es obligatorio")
        String producto,

        @NotNull(message = "cantidad es obligatoria")
        @Min(value = 1, message = "cantidad debe ser mayor o igual a 1")
        Integer cantidad
) {
}
