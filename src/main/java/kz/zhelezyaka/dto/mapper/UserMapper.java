package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.entity.User;

public class UserMapper implements Mapper<User, UserDTO> {

    @Override
    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        return user;
    }

    @Override
    public UserDTO toDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
