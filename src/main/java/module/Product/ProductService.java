package module.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import module.Category.CategoryEntity;
import module.Category.CategoryRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public void addorUpdate(ProductDto requestProductDto) {
        if (requestProductDto.getId() == null) {
            createProduct(requestProductDto);
        } else {
            updateProduct(requestProductDto);
        }
    }

    public void createProduct(ProductDto requestProductDto) {

        CategoryEntity category = categoryRepository
                .findById(requestProductDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductEntity product = new ProductEntity();

        product.setName(requestProductDto.getName());
        product.setPrice(requestProductDto.getPrice());
        product.setStock_quantity(requestProductDto.getStock_quantity());

        product.setCategory(category);

        productRepository.save(product);
    }

    public void updateProduct(ProductDto requestProductDto) {

        ProductEntity product = productRepository.findById(requestProductDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CategoryEntity category = categoryRepository
                .findById(requestProductDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setName(requestProductDto.getName());
        product.setPrice(requestProductDto.getPrice());
        product.setStock_quantity(requestProductDto.getStock_quantity());

        product.setCategory(category);

        productRepository.save(product);
    }

    public void delete(Long id) {

        if (id == null) {
            throw new RuntimeException("Product id is required");
        }

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.delete(product);
    }

    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(product -> new ProductDto(product.getId(),
                product.getName(), product.getCategory().getId(), product.getPrice(), product.getStock_quantity()))
                .collect(Collectors.toList());
    }

}
