package module.Category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import comman.exceptions.BadRequestException;
import comman.response.PagedResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Service
public class CategoryService {

    @Transactional
    public PagedResponse<CategoryDto> filterCategory(CategoryFilterRequest request) {
        if (request == null) {
            request = new CategoryFilterRequest();
        }

        String sortDir = (request.getSortDirection() != null && !request.getSortDirection().trim().isEmpty())
                ? request.getSortDirection().trim() : "desc";
        String sortBy = (request.getSortBy() != null && !request.getSortBy().trim().isEmpty())
                ? request.getSortBy().trim() : "id";

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        int page = request.getPage() != null && request.getPage() >= 0 ? request.getPage() : 0;
        int size = request.getSize() != null && request.getSize() > 0 ? request.getSize() : 50;
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CategoryEntity> pageResult = CategoryRepository
                .findAll(CategorySpecification.filter(request), pageable);

        List<CategoryDto> dtoList = pageResult.getContent().stream().map(this::mapToDto)
                .collect(Collectors.toList());

        Page<CategoryDto> dtoPage = new PageImpl<>(dtoList, pageResult.getPageable(), pageResult.getTotalElements());
        return PagedResponse.fromPage(dtoPage, "Category fetched successfully");
    }

    @Autowired
    CategoryRepository CategoryRepository;

    public void addorUpdate(CategoryDto requestCategoryDto) {
        if (requestCategoryDto.getId() == null) {
            createCategory(requestCategoryDto);
        } else {
            updateCategory(requestCategoryDto);
        }

    }

    public CategoryDto createCategory(CategoryDto requestCategoryDto) {

        if (requestCategoryDto.getName() == null || requestCategoryDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Invalid category name provided");
        }

        if (CategoryRepository.existsByName(requestCategoryDto.getName())) {
            throw new BadRequestException("Category name already exists");
        }

        CategoryEntity category = new CategoryEntity();
        category.setName(requestCategoryDto.getName());
        requestCategoryDto.setCreatedAt(category.getCreatedAt());

        CategoryEntity savedCategory = CategoryRepository.save(category);
        return new CategoryDto(savedCategory.getName());
    }

    public CategoryDto updateCategory(CategoryDto requestCategoryDto) {

        CategoryEntity entity = CategoryRepository.findById(requestCategoryDto.getId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        if (requestCategoryDto.getName() == null || requestCategoryDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Invalid category name provided");
        }

        if (CategoryRepository.existsByName(requestCategoryDto.getName())) {
            throw new BadRequestException("Category name already exists");
        }

        entity.setName(requestCategoryDto.getName());
        requestCategoryDto.setCreatedAt(entity.getCreatedAt());
        CategoryEntity savedCategory = CategoryRepository.save(entity);
        return new CategoryDto(savedCategory.getName());
    }

    public List<CategoryDto> getAllCategories() {
        List<CategoryEntity> categories = CategoryRepository.findAll();

        return categories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapToDto(CategoryEntity category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        return dto;
    }

    public void delete(Long id) {
        if (id == null) {
            throw new BadRequestException("Category id is required");
        }
        if (!CategoryRepository.existsById(id)) {
            throw new BadRequestException("Category id is not exists");
        }
        CategoryRepository.deleteById(id);
    }

}
