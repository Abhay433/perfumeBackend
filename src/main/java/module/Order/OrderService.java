package module.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comman.exceptions.BadRequestException;
import comman.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import module.Address.AddressRepository;
import module.Cart.CartEntity;
import module.Cart.CartItemEntity;
import module.Cart.CartItemRepository;
import module.Cart.CartRepository;
import module.Product.ProductEntity;
import module.Product.ProductRepository;
import module.User.UserEntity;
import module.User.UserRepository;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AddressRepository addressRepository;

    // 1. Place Order from Cart (Checkout)
    public OrderDto placeOrder(Long userId, CreateOrderRequestDto request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Optional address validation
        if (request != null && request.getAddressId() != null) {
            boolean addressExists = addressRepository.existsByIdAndUserId(request.getAddressId(), userId);
            if (!addressExists) {
                throw new BadRequestException("Invalid delivery address selected for this user");
            }
        }

        // Get user's cart
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException("Cart is empty. Please add items before placing order"));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty. Please add items before placing order");
        }

        // Calculate total amount and prepare order items
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItemEntity cartItem : cart.getCartItems()) {
            ProductEntity product = cartItem.getProduct();
            if (product == null) {
                continue;
            }
            BigDecimal price = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
            int quantity = cartItem.getQuantity() != null ? cartItem.getQuantity() : 0;
            totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(quantity)));
        }

        // Create Order
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        order.setStatus("PLACED");
        order.setTotalAmount(totalAmount);
        order.setOrderItems(new ArrayList<>());
        OrderEntity savedOrder = orderRepository.save(order);

        // Create Order Items
        List<OrderItemEntity> orderItems = new ArrayList<>();
        for (CartItemEntity cartItem : cart.getCartItems()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);

        // Clear user's cart after successful order
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear();

        return OrderDto.fromEntity(savedOrder);
    }

    // 2. Get all orders of logged-in user
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByIdDesc(userId)
                .stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 3. Get single order details
    public OrderDto getUserOrderById(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return OrderDto.fromEntity(order);
    }

    // 4. Cancel Order by User
    public OrderDto cancelOrder(Long userId, Long orderId) {
        OrderEntity order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!"PLACED".equalsIgnoreCase(order.getStatus()) && !"PENDING".equalsIgnoreCase(order.getStatus())) {
            throw new BadRequestException("Order cannot be cancelled because it is already " + order.getStatus());
        }

        // Restock products back to inventory
        if (order.getOrderItems() != null) {
            for (OrderItemEntity item : order.getOrderItems()) {
                ProductEntity product = item.getProduct();
                if (product != null) {
                    Long currentStock = product.getStock_quantity() != null ? product.getStock_quantity() : 0L;
                    product.setStock_quantity(currentStock + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }

        order.setStatus("CANCELLED");
        OrderEntity updated = orderRepository.save(order);
        return OrderDto.fromEntity(updated);
    }

    // 5. Admin: Get all orders
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAllByOrderByIdDesc()
                .stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 6. Admin: Update order status (CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
    public OrderDto updateOrderStatus(Long orderId, String newStatus) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        String oldStatus = order.getStatus();
        String formattedStatus = newStatus.trim().toUpperCase();

        // If newly cancelled by admin, restock products
        if ("CANCELLED".equals(formattedStatus) && !"CANCELLED".equalsIgnoreCase(oldStatus)) {
            if (order.getOrderItems() != null) {
                for (OrderItemEntity item : order.getOrderItems()) {
                    ProductEntity product = item.getProduct();
                    if (product != null) {
                        Long currentStock = product.getStock_quantity() != null ? product.getStock_quantity() : 0L;
                        product.setStock_quantity(currentStock + item.getQuantity());
                        productRepository.save(product);
                    }
                }
            }
        }

        order.setStatus(formattedStatus);
        OrderEntity updated = orderRepository.save(order);
        return OrderDto.fromEntity(updated);
    }

}
