package module.Category;

import java.time.LocalDateTime;

public class CategoryDto {

    Long id;

    String name;

    private LocalDateTime createdAt;

    public CategoryDto() {
    }

    public CategoryDto(String name) {
        this.name = name;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "CategoryDto [id=" + id + ", name=" + name + ", createdAt=" + createdAt + "]";
    }

}
