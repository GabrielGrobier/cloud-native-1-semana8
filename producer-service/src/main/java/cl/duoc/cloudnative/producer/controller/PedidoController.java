package cl.duoc.cloudnative.producer.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import cl.duoc.cloudnative.producer.dto.CrearPedidoRequest;
import cl.duoc.cloudnative.producer.dto.CrearPedidoResponse;
import cl.duoc.cloudnative.producer.service.PedidoProducerService;
import cl.duoc.cloudnative.shared.events.PedidoCreadoEvent;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoProducerService pedidoProducerService;

    public PedidoController(PedidoProducerService pedidoProducerService) {
        this.pedidoProducerService = pedidoProducerService;
    }

    @PostMapping
    public ResponseEntity<CrearPedidoResponse> crearPedido(
            @Valid @RequestBody CrearPedidoRequest request,
            JwtAuthenticationToken authentication
    ) {
        Jwt jwt = authentication.getToken();

        String eventId = UUID.randomUUID().toString();
        String usuarioId = jwt.getSubject();
        String email = extractEmail(jwt);
        List<String> roles = extractRoles(jwt);
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : "";

        PedidoCreadoEvent event = new PedidoCreadoEvent(
                eventId,
                request.pedidoId(),
                request.producto(),
                request.cantidad(),
                usuarioId,
                email,
                roles,
                issuer,
                Instant.now().toString()
        );

        pedidoProducerService.publicarPedidoCreado(event);

        return ResponseEntity.accepted().body(new CrearPedidoResponse(
                "Pedido recibido y enviado a procesamiento asincrono",
                request.pedidoId(),
                eventId
        ));
    }

    private String extractEmail(Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        if (email != null && !email.isBlank()) {
            return email;
        }

        List<String> emails = jwt.getClaimAsStringList("emails");
        if (emails != null && !emails.isEmpty()) {
            return emails.getFirst();
        }

        return "sin-email";
    }

    private List<String> extractRoles(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null && !roles.isEmpty()) {
            return roles;
        }

        String role = jwt.getClaimAsString("role");
        if (role != null && !role.isBlank()) {
            return List.of(role);
        }

        String extensionRole = jwt.getClaimAsString("extension_role");
        if (extensionRole != null && !extensionRole.isBlank()) {
            return List.of(extensionRole);
        }

        return List.of();
    }
}
