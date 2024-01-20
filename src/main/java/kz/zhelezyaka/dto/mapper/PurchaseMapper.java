package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.dto.PurchaseDTO;
import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.entity.Pet;
import kz.zhelezyaka.entity.Purchase;
import kz.zhelezyaka.entity.User;

public class PurchaseMapper implements Mapper<Purchase, PurchaseDTO> {

    @Override
    public Purchase toEntity(PurchaseDTO dto) {
        Purchase purchase = new Purchase();
        purchase.setId(dto.getId());
        return purchase;
    }

    @Override
    public PurchaseDTO toDto(Purchase purchase) {
        UserDTO userDTO = mapToUserDTO(purchase.getUser());
        PetDTO petDTO = mapToPetDTO(purchase.getPet());
        return PurchaseDTO.builder()
                .id(purchase.getId())
                .user(userDTO)
                .pet(petDTO)
                .build();
    }

    public UserDTO mapToUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public PetDTO mapToPetDTO(Pet pet) {
        if (pet == null) {
            return null;
        }

        return PetDTO.builder()
                .id(pet.getId())
                .name(pet.getName())
                .build();
    }
}
