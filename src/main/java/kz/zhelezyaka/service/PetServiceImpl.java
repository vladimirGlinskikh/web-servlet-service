package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.PetDAO;
import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.PetMapper;
import kz.zhelezyaka.entity.Pet;
import kz.zhelezyaka.exception.DataAccessException;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetDAO petDAO;
    private Mapper<Pet, PetDTO> mapper;

    public PetServiceImpl() {
        this.petDAO = new PetDAO();
        this.mapper = new PetMapper();
    }

    @Override
    public List<PetDTO> getAllPets() {
        return petDAO.getAllPets()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public PetDTO getPetById(int id) {
        return mapper.toDto(petDAO.getPetById(id));
    }

    @Override
    public PetDTO savePet(PetDTO pet) {
        Pet sPet = petDAO.savePet(mapper.toEntity(pet));
        return mapper.toDto(sPet);
    }

    @Override
    public PetDTO updatePet(PetDTO petDTO) {
        try {
            Pet uPet = petDAO.updatePet(mapper.toEntity(petDTO));
            return mapper.toDto(uPet);
        } catch (Exception e) {
            throw new DataAccessException("Error while updating pet.", e);
        }
    }

    @Override
    public boolean removePet(int id) {
        return petDAO.removePet(id);
    }
}
