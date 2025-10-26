package sit.int204.itbmsbackend.controller.v2;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int204.itbmsbackend.constant.UserRole;
import sit.int204.itbmsbackend.constant.UserType;
import sit.int204.itbmsbackend.dto.order.*;
import sit.int204.itbmsbackend.security.UserPrincipal;
import sit.int204.itbmsbackend.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v2/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // Validate order item before place order
    @PostMapping("/validate")
    public ResponseEntity<OrderValidationResponse> validateCart(@Valid @RequestBody List<CreateOrderRequest> ordersRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.validateOrders(
                userPrincipal.getId(),
                ordersRequest
        ));
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<List<OrderResponse>> createOrder(@Valid @RequestBody List<CreateOrderRequest> ordersRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(
                userPrincipal.getId(),
                ordersRequest
        ));
    }

    // Get a single order detail
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        OrderResponse order = orderService.getOrderById(id, userPrincipal.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/unviewed-count")
    public Map<String, Long> getUnviewedOrderCount(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam UserType userType
    ) {
        long count = orderService.getUnviewedOrderCount(userPrincipal.getId(), userType);
        return Map.of("unviewedOrders", count);
    }
}
