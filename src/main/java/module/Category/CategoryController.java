package module.Category;

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
@RequestMapping({"/api/admin/categories", "/api/categories"})
public class CategoryController {

    @Autowired
    CategoryService CategoryService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @PostMapping("/addorUpdate")
    public ResponseEntity<ApiResponse<CategoryDto>> addorUpdate(@Valid @RequestBody CategoryDto requestDto) {
        LOGGER.info("Category addorUpdate attempt for category: {}", requestDto.getName());
        CategoryService.addorUpdate(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category addorUpdate successfully", requestDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        LOGGER.info("Category getAllCategories attempt");
        List<CategoryDto> categoryDtot = CategoryService.getAllCategories();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Category get successfully", categoryDtot));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> delete(@PathVariable Long id) {
        LOGGER.info("Category deletion attempt for id: {}", id);
        CategoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Category deleted successfully", null));
    }

}
