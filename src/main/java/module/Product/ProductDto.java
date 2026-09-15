package module.Product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductDto {

    private Long id;

    @NotNull(message = "Product name is not null")
    @Size(max = 255, message = "Product name must be less than 255 characters")
    private String name;

    @NotNull(message = "Category ID is required")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Positive(message = "Stock quantity must be positive")
    private Long stock_quantity;

    public ProductDto() {
    }

    public ProductDto(Long id, String name, Long categoryId, BigDecimal price, Long stock_quantity) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getStock_quantity() {
        return stock_quantity;
    }

    public void setStock_quantity(Long stock_quantity) {
        this.stock_quantity = stock_quantity;
    }

    @Override
    public String toString() {
        return "ProductDto [id=" + id + ", name=" + name + ", categoryId=" + categoryId + ", price=" + price
                + ", stock_quantity=" + stock_quantity + "]";
    }

}
