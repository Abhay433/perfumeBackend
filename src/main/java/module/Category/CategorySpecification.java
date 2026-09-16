package module.Category;

import org.springframework.data.jpa.domain.Specification;

import comman.filters.SpecificationBuilder;

public class CategorySpecification {

    private CategorySpecification() {
    }

    public static Specification<CategoryEntity> filter(CategoryFilterRequest filterRequest) {
        if (filterRequest == null) {
            return null;
        }
        return new SpecificationBuilder<CategoryEntity>()
                .with(nameLike(filterRequest.getName()))
                .build();
    }

    public static Specification<CategoryEntity> nameLike(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.trim().toLowerCase() + "%");
        };
    }

}