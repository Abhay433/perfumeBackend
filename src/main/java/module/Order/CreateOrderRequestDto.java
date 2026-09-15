package module.Order;

public class CreateOrderRequestDto {

    private Long addressId;
    private String notes;

    public CreateOrderRequestDto() {
    }

    public CreateOrderRequestDto(Long addressId, String notes) {
        this.addressId = addressId;
        this.notes = notes;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
