package com.pfe.service.mapper;

import com.pfe.domain.User;
import com.pfe.service.dto.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

class UserMapperTest {


    @Mock
    private RoleMapper roleMapper;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("TestUser");
        user.setEmail("test@example.com");
        user.setIsActive(true);

        UserDTO userDTO = this.userMapper.toDto(user);

        Assertions.assertEquals(user.getId(), userDTO.getId());
        Assertions.assertEquals(user.getUsername(), userDTO.getUsername());
        Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        Assertions.assertEquals(user.getIsActive(), userDTO.getIsActive());
    }

    @Test
    void testToEntity() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setUsername("DTOUser");
        userDTO.setEmail("dto@example.com");
        userDTO.setIsActive(false);

        User user = this.userMapper.toEntity(userDTO);

        Assertions.assertEquals(userDTO.getId(), user.getId());
        Assertions.assertEquals(userDTO.getUsername(), user.getUsername());
        Assertions.assertEquals(userDTO.getEmail(), user.getEmail());
        Assertions.assertEquals(userDTO.getIsActive(), user.getIsActive());
    }
}
