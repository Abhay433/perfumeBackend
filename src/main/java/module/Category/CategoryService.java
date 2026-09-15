package module.Category;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comman.exceptions.BadRequestException;

@Service
public class CategoryService {

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
