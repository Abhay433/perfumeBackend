package module.Category;

import comman.filters.BaseFilterRequest;

public class CategoryFilterRequest extends BaseFilterRequest {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
