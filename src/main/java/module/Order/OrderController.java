package module.Order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import comman.security.CustomUserDetails;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    // 1. Checkout (Place Order from Cart)
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDto>> checkout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody(required = false) CreateOrderRequestDto request) {
        LOGGER.info("User {} placing order", userDetails.getId());
        OrderDto order = orderService.placeOrder(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", order));
    }

    // 2. Get all orders for current user
    @GetMapping("/my-orders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LOGGER.info("Fetching orders for user {}", userDetails.getId());
        List<OrderDto> orders = orderService.getUserOrders(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
    }

    // 3. Get single order details
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId) {
        LOGGER.info("Fetching order {} for user {}", orderId, userDetails.getId());
        OrderDto order = orderService.getUserOrderById(userDetails.getId(), orderId);
        return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order));
    }

    // 4. Cancel order
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId) {
        LOGGER.info("User {} cancelling order {}", userDetails.getId(), orderId);
        OrderDto order = orderService.cancelOrder(userDetails.getId(), orderId);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", order));
    }

}
