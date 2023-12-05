package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.UserDAO;
import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.UserMapper;
import kz.zhelezyaka.entity.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final Mapper<User, UserDTO> mapper = new UserMapper();

    public UserServiceImpl() {
        this.userDAO = new UserDAO();
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
