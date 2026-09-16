package module.Product;

import org.springframework.data.jpa.domain.Specification;

import comman.filters.SpecificationBuilder;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<ProductEntity> filter(ProductFilterRequest filterRequest) {
        return new SpecificationBuilder<ProductEntity>()

                .with(nameEqual(filterRequest.getName()))
                .with(categoryEqual(filterRequest.getCategoryId()))
                .with(priceBetween(filterRequest.getMinPrice(), filterRequest.getMaxPrice()))
                .build();
    }

    public static Specification<ProductEntity> nameEqual(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

    public static Specification<ProductEntity> categoryEqual(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<ProductEntity> priceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

}
