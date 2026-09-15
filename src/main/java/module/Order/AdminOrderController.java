package module.Order;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminOrderController.class);

    @Autowired
    private OrderService orderService;

    // 1. Admin: View all orders across all customers
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders() {
        LOGGER.info("Admin fetching all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success("All orders retrieved successfully", orders));
    }

    // 2. Admin: Update order status (CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequestDto request) {
        LOGGER.info("Admin updating order {} status to {}", orderId, request.getStatus());
        OrderDto updatedOrder = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Order status updated successfully", updatedOrder));
    }

}
