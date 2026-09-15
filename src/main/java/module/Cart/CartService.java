package module.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comman.exceptions.BadRequestException;
import comman.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import module.Product.ProductEntity;
import module.Product.ProductRepository;
import module.User.UserEntity;
import module.User.UserRepository;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Get or Create User's Cart
    public CartDto getCart(Long userId) {
        CartEntity cart = getOrCreateCart(userId);
        return mapToDto(cart);
    }

    // 2. Add item to cart
    public CartDto addToCart(Long userId, Long productId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than zero");
        }

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        CartEntity cartEntity = getOrCreateCart(userId);

        if (cartEntity.getCartItems() == null) {
            cartEntity.setCartItems(new ArrayList<>());
        }

        // Check if item already exists in cart
        Optional<CartItemEntity> existingItem = cartEntity.getCartItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
                .findFirst();

        int requestedQuantity = existingItem.map(item -> item.getQuantity() + quantity)
                .orElse(quantity);

        // Stock check
        Long availableStock = product.getStock_quantity() != null ? product.getStock_quantity() : 0L;
        if (availableStock < requestedQuantity) {
            throw new BadRequestException(
                    "Only " + availableStock + " units available for " + product.getName());
        }

        product.setStock_quantity(availableStock - quantity);
        productRepository.save(product);

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(requestedQuantity);
            cartItemRepository.save(item);
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cartEntity);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartEntity.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        return mapToDto(cartEntity);
    }

    public CartDto updateQuantity(Long userId, Long productId, Integer quantity) {
        if (quantity == null) {
            throw new BadRequestException("Quantity cannot be null");
        }

        CartEntity cartEntity = getOrCreateCart(userId);

        if (cartEntity.getCartItems() == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        CartItemEntity item = cartEntity.getCartItems().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        ProductEntity product = item.getProduct();
        Long currentStock = product.getStock_quantity() != null ? product.getStock_quantity() : 0L;
        int oldQuantity = item.getQuantity();

        if (quantity <= 0) {
            // Item remove ho raha hai — jitna reserved tha, utna stock wapas add karo
            product.setStock_quantity(currentStock + oldQuantity);
            productRepository.save(product);

            cartEntity.getCartItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            int diff = quantity - oldQuantity; // positive = zyada chahiye, negative = kam chahiye

            if (diff > 0 && currentStock < diff) {
                throw new BadRequestException(
                        "Only " + currentStock + " units available for " + product.getName());
            }

            // Stock ko diff ke hisaab se adjust karo (agar diff negative hai toh stock
            // wapas badhega)
            product.setStock_quantity(currentStock - diff);
            productRepository.save(product);

            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        return mapToDto(cartEntity);
    }

    // 4. Remove Item from Cart
    public CartDto removeItem(Long userId, Long productId) {
        CartEntity cartEntity = getOrCreateCart(userId);

        if (cartEntity.getCartItems() == null) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        CartItemEntity item = cartEntity.getCartItems().stream()
                .filter(i -> i.getProduct() != null && i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

        cartEntity.getCartItems().remove(item);
        cartItemRepository.delete(item);

        return mapToDto(cartEntity);
    }

    // 5. Clear entire cart
    public void clearCart(Long userId) {
        CartEntity cartEntity = getOrCreateCart(userId);
        if (cartEntity.getCartItems() != null && !cartEntity.getCartItems().isEmpty()) {
            cartItemRepository.deleteAll(cartEntity.getCartItems());
            cartEntity.getCartItems().clear();
        }
    }

    // Helper: Find or create Cart for user
    private CartEntity getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>());
                    return cartRepository.save(newCart);
                });
    }

    // Helper: Map Entity to CartDto
    private CartDto mapToDto(CartEntity cart) {
        List<CartItemDto> itemDtos = new ArrayList<>();
        double totalAmount = 0.0;

        if (cart.getCartItems() != null) {
            for (CartItemEntity item : cart.getCartItems()) {
                double price = (item.getProduct() != null && item.getProduct().getPrice() != null)
                        ? item.getProduct().getPrice().doubleValue()
                        : 0.0;
                int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                double itemTotal = price * qty;
                totalAmount += itemTotal;

                itemDtos.add(new CartItemDto(
                        item.getProduct() != null ? item.getProduct().getId() : null,
                        item.getProduct() != null ? item.getProduct().getName() : null,
                        price,
                        qty,
                        itemTotal));
            }
        }

        return new CartDto(cart.getId(), itemDtos, totalAmount);
    }
}
