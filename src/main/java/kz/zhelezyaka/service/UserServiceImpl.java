package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.PurchaseDAO;
import kz.zhelezyaka.dao.UserDAO;
import kz.zhelezyaka.dto.PetDTO;
import kz.zhelezyaka.dto.PurchaseDTO;
import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.UserMapper;
import kz.zhelezyaka.entity.User;
import lombok.AllArgsConstructor;

import java.util.List;


@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private Mapper<User, UserDTO> mapper;

    public UserServiceImpl() {
        this.userDAO = new UserDAO();
        this.mapper = new UserMapper();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userDAO.getAllUsers()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserDTO getUserById(int id) {
        return mapper.toDto(userDAO.getUserById(id));
    }

    @Override
    public UserDTO saveUser(UserDTO user) {
        User sUser = userDAO.saveUser(mapper.toEntity(user));
        return mapper.toDto(sUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        try {
            User uUser = userDAO.updateUser(mapper.toEntity(userDTO));
            return mapper.toDto(uUser);
        } catch (Exception e) {

            throw new RuntimeException("Error while updating user.", e);
        }
    }

    @Override
    public boolean removeUser(int id) {
        return userDAO.removeUser(id);
    }
}
