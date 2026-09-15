package module.Cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import comman.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    // 1. Get current logged-in user's cart
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LOGGER.info("Fetching cart for user id: {}", userDetails.getId());
        CartDto cart = cartService.getCart(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cart));
    }

    // 2. Add product to cart
    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AddToCartRequestDto request) {
        LOGGER.info("User {} adding product {} with quantity {}",
                userDetails.getId(), request.getProductId(), request.getQuantity());
        CartDto updatedCart = cartService.addToCart(userDetails.getId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Item added to cart successfully", updatedCart));
    }

    // 3. Update item quantity in cart
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartDto>> updateQuantity(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AddToCartRequestDto request) {
        LOGGER.info("User {} updating product {} to quantity {}",
                userDetails.getId(), request.getProductId(), request.getQuantity());
        CartDto updatedCart = cartService.updateQuantity(userDetails.getId(), request.getProductId(),
                request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Cart updated successfully", updatedCart));
    }

    // 4. Remove a specific product from cart
    @DeleteMapping("/item/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long productId) {
        LOGGER.info("User {} removing product {} from cart", userDetails.getId(), productId);
        CartDto updatedCart = cartService.removeItem(userDetails.getId(), productId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully", updatedCart));
    }

    // 5. Clear all items from cart
    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LOGGER.info("User {} clearing cart", userDetails.getId());
        cartService.clearCart(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }

}
