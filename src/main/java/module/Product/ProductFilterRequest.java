package module.Product;

import comman.filters.BaseFilterRequest;

public class ProductFilterRequest extends BaseFilterRequest {
    private String name;
    private Long categoryId;
    private Double minPrice;
    private Double maxPrice;

    public ProductFilterRequest() {
        super();
    }

    public ProductFilterRequest(String name, Long categoryId, Double minPrice, Double maxPrice) {
        this.name = name;
        this.categoryId = categoryId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
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

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    @Override
    public String toString() {
        return "ProductFilterRequest [name=" + name + ", categoryId=" + categoryId + ", minPrice=" + minPrice
                + ", maxPrice=" + maxPrice + "]";
    }

}
