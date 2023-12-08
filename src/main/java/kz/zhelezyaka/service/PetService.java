package kz.zhelezyaka.service;

import kz.zhelezyaka.dto.PetDTO;

import java.util.List;

public interface PetService {
    List<PetDTO> getAllPets();

    PetDTO getPetById(int id);

    PetDTO savePet(PetDTO pet);

    PetDTO updatePet(PetDTO petDTO);

    boolean removePet(int id);
}
