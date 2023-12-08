package kz.zhelezyaka.dto.mapper;

import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class UserMapperTest {
    @Test
    void testToEntity() {
        UserDTO userDTO = UserDTO.builder().id(1).name("Elena").build();
        UserMapper userMapper = new UserMapper();
        User user = userMapper.toEntity(userDTO);

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("Elena", user.getName());
    }

    @Test
    void testToDto() {
        User user = new User(2, "Nikolay");
        UserMapper userMapper = new UserMapper();
        UserDTO userDTO = userMapper.toDto(user);

        assertNotNull(userDTO);
        assertEquals(2, userDTO.getId());
        assertEquals("Nikolay", userDTO.getName());
    }

    @Test
    void testToEntityWithNull() {
        UserDTO userDTO = null;
        UserMapper userMapper = new UserMapper();

        try {
            User user = userMapper.toEntity(userDTO);
            assertNull(user);
        } catch (NullPointerException npe) {
            assertNull(userDTO, "UserDTO should be null");
        }
    }
}