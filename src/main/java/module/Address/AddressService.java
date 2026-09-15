package module.Address;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import comman.exceptions.BadRequestException;
import comman.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import module.User.UserEntity;
import module.User.UserRepository;

@Service
@Transactional
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    // 1. Get all saved addresses of a user
    public List<AddressDto> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(AddressDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. Get specific address by ID
    public AddressDto getAddressById(Long userId, Long addressId) {
        AddressEntity address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));
        return AddressDto.fromEntity(address);
    }

    // 3. Add new address
    public AddressDto addAddress(Long userId, AddressDto dto) {
        if (dto.getAddressLine() == null || dto.getAddressLine().trim().isEmpty()) {
            throw new BadRequestException("Address line cannot be empty");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        AddressEntity entity = new AddressEntity();
        entity.setAddressLine(dto.getAddressLine().trim());
        entity.setCity(dto.getCity().trim());
        entity.setPincode(dto.getPincode().trim());
        entity.setUser(user);

        AddressEntity saved = addressRepository.save(entity);
        return AddressDto.fromEntity(saved);
    }

    // 4. Update address
    public AddressDto updateAddress(Long userId, Long addressId, AddressDto dto) {
        AddressEntity address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        address.setAddressLine(dto.getAddressLine().trim());
        address.setCity(dto.getCity().trim());
        address.setPincode(dto.getPincode().trim());

        AddressEntity updated = addressRepository.save(address);
        return AddressDto.fromEntity(updated);
    }

    // 5. Delete address
    public void deleteAddress(Long userId, Long addressId) {
        AddressEntity address = addressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        addressRepository.delete(address);
    }

}
