package module.Order;

import jakarta.validation.constraints.NotBlank;

public class UpdateOrderStatusRequestDto {

    @NotBlank(message = "Order status is required")
    private String status;

    public UpdateOrderStatusRequestDto() {
    }

    public UpdateOrderStatusRequestDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
