package module.Address;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import comman.response.ApiResponse;
import comman.security.CustomUserDetails;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    @Autowired
    private AddressService addressService;

    // 1. Get all saved addresses of current user
    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressDto>>> getAllAddresses(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LOGGER.info("Fetching all addresses for user id: {}", userDetails.getId());
        List<AddressDto> addresses = addressService.getUserAddresses(userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success("Addresses retrieved successfully", addresses));
    }

    // 2. Get specific address by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> getAddressById(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        LOGGER.info("Fetching address id {} for user id: {}", id, userDetails.getId());
        AddressDto address = addressService.getAddressById(userDetails.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Address retrieved successfully", address));
    }

    // 3. Add a new address
    @PostMapping
    public ResponseEntity<ApiResponse<AddressDto>> addAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody AddressDto requestDto) {
        LOGGER.info("User {} adding new address", userDetails.getId());
        AddressDto savedAddress = addressService.addAddress(userDetails.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address added successfully", savedAddress));
    }

    // 4. Update an existing address
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressDto>> updateAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @Valid @RequestBody AddressDto requestDto) {
        LOGGER.info("User {} updating address id: {}", userDetails.getId(), id);
        AddressDto updatedAddress = addressService.updateAddress(userDetails.getId(), id, requestDto);
        return ResponseEntity.ok(ApiResponse.success("Address updated successfully", updatedAddress));
    }

    // 5. Delete an address
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        LOGGER.info("User {} deleting address id: {}", userDetails.getId(), id);
        addressService.deleteAddress(userDetails.getId(), id);
        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully", null));
    }

}
