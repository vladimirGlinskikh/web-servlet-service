package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.entity.Pet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetMapperTest {
    @Test
    void testToEntity() {
        PetDTO petDTO = PetDTO.builder().id(1).name("Zamira").build();
        PetMapper petMapper = new PetMapper();
        Pet pet = petMapper.toEntity(petDTO);

        assertNotNull(pet);
        assertEquals(1, pet.getId());
        assertEquals("Zamira", pet.getName());
    }

    @Test
    void testToDto() {
        Pet pet = new Pet(2, "Zamira");
        PetMapper petMapper = new PetMapper();
        PetDTO petDTO = petMapper.toDto(pet);

        assertNotNull(petDTO);
        assertEquals(2, petDTO.getId());
        assertEquals("Zamira", petDTO.getName());
    }

    @Test
    void testToEntityWithNull() {
        PetDTO petDTO = null;
        PetMapper petMapper = new PetMapper();

        try {
            Pet pet = petMapper.toEntity(petDTO);
            assertNull(pet);
        } catch (NullPointerException npe) {
            assertNull(petDTO, "PetDTO should be null");
        }
    }
}