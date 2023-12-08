package kz.zhelezyaka.service;

import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(int id);

    UserDTO saveUser(UserDTO user);

    UserDTO updateUser(UserDTO userDTO);

    boolean removeUser(int id);
}
