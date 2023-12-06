package kz.zhelezyaka.service;

import kz.zhelezyaka.dao.UserDAO;
import kz.zhelezyaka.dto.UserDTO;
import kz.zhelezyaka.dto.mapper.Mapper;
import kz.zhelezyaka.dto.mapper.UserMapper;
import kz.zhelezyaka.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO userDAOMock;
    @Mock
    private Mapper<User, UserDTO> userMapperMock;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        userMapperMock = mock(UserMapper.class);
        userService = new UserServiceImpl(userDAOMock, userMapperMock);
    }

    @Test
    void shouldGetAllUsers() {
        User user1 = new User();
        user1.setId(1);
        user1.setName("User1");

        User user2 = new User();
        user2.setId(2);
        user2.setName("User2");

        List<User> userList = Arrays.asList(user1, user2);

        when(userDAOMock.getAllUsers()).thenReturn(userList);

        UserDTO userDto1 = UserDTO.builder().id(1).name("User1").build();
        UserDTO userDto2 = UserDTO.builder().id(2).name("User2").build();

        when(userMapperMock.toDto(user1)).thenReturn(userDto1);
        when(userMapperMock.toDto(user2)).thenReturn(userDto2);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals(userDto1, result.get(0));
        assertEquals(userDto2, result.get(1));
    }

    @Test
    void shouldGetUserById() {
        int userId = 1;

        User user = new User();
        user.setId(userId);
        user.setName("AnotherUser");

        when(userDAOMock.getUserById(userId)).thenReturn(user);

        UserDTO userDto = UserDTO.builder().id(userId).name("AnotherUser").build();
        when(userMapperMock.toDto(user)).thenReturn(userDto);

        UserDTO result = userService.getUserById(userId);

        assertEquals(userDto, result);
    }

    @Test
    void shouldGetUserByIdNotFound() {
        int userId = 2;

        when(userDAOMock.getUserById(userId)).thenReturn(null);

        UserDTO result = userService.getUserById(userId);

        assertNull(result);
    }

    @Test
    void testSaveUser() {
        UserDTO userDTO = UserDTO.builder().id(1).name("AnotherSaveUser").build();

        User user = new User();
        user.setId(1);
        user.setName("AnotherSaveUser");

        when(userMapperMock.toEntity(userDTO)).thenReturn(user);
        when(userDAOMock.saveUser(any(User.class))).thenReturn(user);
        when(userMapperMock.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.saveUser(userDTO);

        assertEquals(userDTO, result);

        verify(userMapperMock).toEntity(userDTO);
        verify(userDAOMock).saveUser(user);
        verify(userMapperMock).toDto(user);
    }

    @Test
    void testUpdateUser() {
        UserDTO userDTO = UserDTO.builder().id(1).name("UpdatedUser").build();
        User user = new User();
        user.setId(1);
        user.setName("UpdatedUser");

        when(userMapperMock.toEntity(userDTO)).thenReturn(user);
        when(userDAOMock.updateUser(any(User.class))).thenReturn(user);
        when(userMapperMock.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(userDTO);

        assertEquals(userDTO, result);
    }

    @Test
    void testRemoveUser() {
        int userId = 1;

        when(userDAOMock.removeUser(anyInt())).thenReturn(true);

        boolean result = userService.removeUser(userId);

        assertTrue(result);
    }
}