package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserWithTokenDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

public class DTOMapperTest {

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("password");
    user.setStatus(UserStatus.OFFLINE);
    user.setLastStatus(1L);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));

    UserDTO userDTO = DTOMapper.INSTANCE.convertEntityToUserDTO(user);

    assertEquals(user.getId(), userDTO.getId());
    assertEquals(user.getUsername(), userDTO.getUsername());
    assertEquals(user.getStatus(), userDTO.getStatus());
    assertEquals(user.getCreation_date(), userDTO.getCreation_date());
    assertEquals(user.getBirthday(), userDTO.getBirthday());
  }

  @Test
  public void testGetUserWithToken_fromUser_toUserGetDTO_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("password");
    user.setStatus(UserStatus.OFFLINE);
    user.setLastStatus(1L);
    user.setToken("1");
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));

    UserWithTokenDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserWithTokenDTO(user);

    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getToken(), userGetDTO.getToken());
    assertEquals(user.getCreation_date(), userGetDTO.getCreation_date());
    assertEquals(user.getBirthday(), userGetDTO.getBirthday());
  }
}
