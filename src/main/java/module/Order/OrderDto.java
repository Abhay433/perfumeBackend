package module.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDto {

    private Long orderId;
    private Long userId;
    private String userEmail;
    private String userName;
    private BigDecimal totalAmount;
    private String status;
    private List<OrderItemDto> items;

    public OrderDto() {
    }

    public OrderDto(Long orderId, Long userId, String userEmail, String userName, BigDecimal totalAmount, String status, List<OrderItemDto> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public static OrderDto fromEntity(OrderEntity order) {
        if (order == null) {
            return null;
        }

        List<OrderItemDto> itemDtos = new ArrayList<>();
        if (order.getOrderItems() != null) {
            for (OrderItemEntity item : order.getOrderItems()) {
                BigDecimal price = item.getPrice() != null ? item.getPrice() : BigDecimal.ZERO;
                int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(qty));

                itemDtos.add(new OrderItemDto(
                        item.getId(),
                        item.getProduct() != null ? item.getProduct().getId() : null,
                        item.getProduct() != null ? item.getProduct().getName() : null,
                        price,
                        qty,
                        itemTotal
                ));
            }
        }

        return new OrderDto(
                order.getId(),
                order.getUser() != null ? order.getUser().getId() : null,
                order.getUser() != null ? order.getUser().getEmail() : null,
                order.getUser() != null ? order.getUser().getName() : null,
                order.getTotalAmount(),
                order.getStatus(),
                itemDtos
        );
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

}
