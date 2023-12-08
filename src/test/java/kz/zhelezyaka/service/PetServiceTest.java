package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.PetDAO;
import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.PetMapper;
import kz.zhelezyaka.entity.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetDAO petDAOMock;

    @Mock
    private Mapper<Pet, PetDTO> petMapperMock;

    @InjectMocks
    private PetServiceImpl petServiceMock;

    @BeforeEach
    public void setUp() {
        petDAOMock = mock(PetDAO.class);
        petMapperMock = mock(PetMapper.class);
        petServiceMock = new PetServiceImpl(petDAOMock, petMapperMock);
    }

    @Test
    void shouldGetAllPets() {
        List<Pet> mockPets = Arrays.asList(
                new Pet(1, "Dog"),
                new Pet(2, "Cat"));

        when(petDAOMock.getAllPets()).thenReturn(mockPets);

        List<PetDTO> expectedPetDTOs = Arrays.asList(
                new PetDTO(1, "Dog"),
                new PetDTO(2, "Cat")
        );
        when(petMapperMock.toDto(any()))
                .thenReturn(expectedPetDTOs.get(0), expectedPetDTOs.get(1));

        List<PetDTO> result = petServiceMock.getAllPets();

        assertEquals(expectedPetDTOs, result);
        verify(petDAOMock, times(1)).getAllPets();
        verify(petMapperMock, times(2)).toDto(any());
    }

    @Test
    void testGetPetById() {
        int petId = 1;
        Pet mockPet = new Pet(petId, "Dog");
        when(petDAOMock.getPetById(petId)).thenReturn(mockPet);

        PetDTO expectedPetDTO = new PetDTO(petId, "Dog");
        when(petMapperMock.toDto(mockPet)).thenReturn(expectedPetDTO);

        PetDTO result = petServiceMock.getPetById(petId);

        assertEquals(expectedPetDTO, result);
        verify(petDAOMock, times(1)).getPetById(petId);
        verify(petMapperMock, times(1)).toDto(mockPet);
    }

    @Test
    void shouldSavePet() {
        PetDTO petDTO = PetDTO.builder().id(1).name("Mouse").build();

        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Mouse");

        when(petMapperMock.toEntity(petDTO)).thenReturn(pet);
        when(petDAOMock.savePet(any(Pet.class))).thenReturn(pet);
        when(petMapperMock.toDto(pet)).thenReturn(petDTO);

        PetDTO result = petServiceMock.savePet(petDTO);

        assertEquals(petDTO, result);

        verify(petMapperMock, times(1)).toEntity(petDTO);
        verify(petDAOMock, times(1)).savePet(pet);
        verify(petMapperMock, times(1)).toDto(pet);
    }

    @Test
    void shouldUpdatePet() {
        PetDTO petDTO = PetDTO.builder().id(1).name("Murzik").build();
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Murzik");

        when(petMapperMock.toEntity(petDTO)).thenReturn(pet);
        when(petDAOMock.updatePet(any(Pet.class))).thenReturn(pet);
        when(petMapperMock.toDto(pet)).thenReturn(petDTO);

        PetDTO result = petServiceMock.updatePet(petDTO);
        assertEquals(petDTO, result);
    }

    @Test
    void shouldRemovePet() {
        int petId = 1;
        when(petDAOMock.removePet(anyInt())).thenReturn(true);
        boolean result = petServiceMock.removePet(petId);
        assertTrue(result);
    }
}