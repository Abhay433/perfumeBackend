package module.Address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddressDto {

    private Long id;

    @NotBlank(message = "Address line is required")
    private String addressLine;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pincode must be a valid 6-digit postal code")
    private String pincode;

    public AddressDto() {
    }

    public AddressDto(Long id, String addressLine, String city, String pincode) {
        this.id = id;
        this.addressLine = addressLine;
        this.city = city;
        this.pincode = pincode;
    }

    public static AddressDto fromEntity(AddressEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AddressDto(
                entity.getId(),
                entity.getAddressLine(),
                entity.getCity(),
                entity.getPincode()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public String toString() {
        return "AddressDto [id=" + id + ", addressLine=" + addressLine + ", city=" + city + ", pincode=" + pincode
                + "]";
    }

}
