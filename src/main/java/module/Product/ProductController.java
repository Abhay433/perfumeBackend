package module.Product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/admin/products", "/api/products"})
public class ProductController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @PostMapping("/addorUpdate")
    public ResponseEntity<ApiResponse<ProductDto>> addorUpdate(@Valid @RequestBody ProductDto requestProductDto) {
        LOGGER.info("Product addorUpdate attempt for product: {}", requestProductDto.getName());
        productService.addorUpdate(requestProductDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product addorUpdate successfully", requestProductDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> delete(@PathVariable Long id) {
        LOGGER.info("Product deletion attempt for id: {}", id);
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Product deleted successfully", null));
    }

    @GetMapping({"", "/getAllProducts"})
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        LOGGER.info("Product getAllProducts attempt");
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Products retrieved successfully", products));
    }

}
