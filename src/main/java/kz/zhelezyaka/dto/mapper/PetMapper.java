package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.entity.Pet;

public class PetMapper implements Mapper<Pet, PetDTO>{
    @Override
    public Pet toEntity(PetDTO dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setName(dto.getName());
        return pet;
    }

    @Override
    public PetDTO toDto(Pet pet) {
        return PetDTO.builder()
                .id(pet.getId())
                .name(pet.getName())
                .build();
    }
}
