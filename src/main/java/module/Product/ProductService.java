package module.Product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import comman.response.PagedResponse;
import module.Category.CategoryEntity;
import module.Category.CategoryRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public PagedResponse<ProductDto> filterProduct(ProductFilterRequest request) {
        if (request == null) {
            request = new ProductFilterRequest();
        }

        String sortDir = (request.getSortDirection() != null && !request.getSortDirection().trim().isEmpty())
                ? request.getSortDirection().trim() : "desc";
        String sortBy = (request.getSortBy() != null && !request.getSortBy().trim().isEmpty())
                ? request.getSortBy().trim() : "id";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 20;
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ProductEntity> pageResult = productRepository
                .findAll(ProductSpecification.filter(request), pageable);

        List<ProductDto> dtoList = pageResult.getContent().stream().map(this::mapToDto)
                .collect(Collectors.toList());

        Page<ProductDto> dtoPage = new PageImpl<>(dtoList, pageResult.getPageable(), pageResult.getTotalElements());
        return PagedResponse.fromPage(dtoPage, "Product fetched successfully");
    }

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
        product.setConcentration(requestProductDto.getConcentration());
        product.setGender(requestProductDto.getGender());
        product.setImageUrl(requestProductDto.getImageUrl());
        product.setNotes(requestProductDto.getNotes());
        product.setVolume(requestProductDto.getVolume());

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
        product.setConcentration(requestProductDto.getConcentration());
        product.setGender(requestProductDto.getGender());
        product.setImageUrl(requestProductDto.getImageUrl());
        product.setNotes(requestProductDto.getNotes());
        product.setVolume(requestProductDto.getVolume());

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
        return productRepository.findAll().stream().map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProductDto mapToDto(ProductEntity product) {
        ProductDto dto = new ProductDto(
                product.getId(),
                product.getName(),
                product.getCategory() != null ? product.getCategory().getId() : null,
                product.getPrice(),
                product.getStock_quantity());
        dto.setConcentration(product.getConcentration());
        dto.setGender(product.getGender());
        dto.setImageUrl(product.getImageUrl());
        dto.setNotes(product.getNotes());
        dto.setVolume(product.getVolume());
        return dto;
    }

}
