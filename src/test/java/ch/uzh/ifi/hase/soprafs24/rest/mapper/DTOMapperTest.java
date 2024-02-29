package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetWithTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;

public class DTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("username");
    userPostDTO.setPassword("password");

    UserPostDTO user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    assertEquals(userPostDTO.getUsername(), user.getUsername());
    assertEquals(userPostDTO.getPassword(), user.getPassword());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("password");
    user.setStatus(UserStatus.OFFLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));

    UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getCreation_date(), userGetDTO.getCreation_date());
    assertEquals(user.getBirthday(), userGetDTO.getBirthday());
  }

  @Test
  public void testGetUserWithToken_fromUser_toUserGetDTO_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("password");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));

    UserGetWithTokenDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetWithTokenDTO(user);

    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getToken(), userGetDTO.getToken());
    assertEquals(user.getCreation_date(), userGetDTO.getCreation_date());
    assertEquals(user.getBirthday(), userGetDTO.getBirthday());
  }
}
