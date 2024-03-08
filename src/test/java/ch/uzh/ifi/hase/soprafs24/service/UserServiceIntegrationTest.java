package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateByTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserUpdateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.Optional;

@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  private UserAuthenticateDTO testUserInput;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();

    testUserInput = new UserAuthenticateDTO();
    testUserInput.setUsername("testUsername");
    testUserInput.setPassword("testPassword");
  }

  @Test
  public void getUsers_success() {
    User createdUser = userService.createUser(testUserInput);
    User u = userService.getUsers().get(0);

    assertEquals(u.getId(), createdUser.getId());
    assertEquals(u.getUsername(), createdUser.getUsername());
    assertEquals(u.getPassword(), createdUser.getPassword());
    assertEquals(u.getStatus(), createdUser.getStatus());
    assertEquals(u.getLastStatus(), createdUser.getLastStatus());
    assertEquals(u.getCreation_date().toString(), createdUser.getCreation_date().toString());
    assertEquals(u.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void createUser_validInputs_success() {
    assertNull(userRepository.findByUsername("testUsername"));

    User createdUser = userService.createUser(testUserInput);

    assertEquals(testUserInput.getUsername(), createdUser.getUsername());
    assertEquals(testUserInput.getPassword(), createdUser.getPassword());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    assertNotNull(createdUser.getCreation_date());
    assertNull(createdUser.getBirthday());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    userService.createUser(testUserInput);

    UserAuthenticateDTO testUserInput2 = new UserAuthenticateDTO();
    testUserInput2.setUsername("testUsername");

    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUserInput2));
  }

  @Test
  public void matchingUser_validInput_success() {
    User createdUser = userService.createUser(testUserInput);
    User m = userService.matchingUser(testUserInput);

    assertEquals(m.getId(), createdUser.getId());
    assertEquals(m.getUsername(), createdUser.getUsername());
    assertEquals(m.getPassword(), createdUser.getPassword());
    assertEquals(m.getStatus(), createdUser.getStatus());
    assertEquals(m.getLastStatus(), createdUser.getLastStatus());
    assertEquals(m.getCreation_date().toString(), createdUser.getCreation_date().toString());
    assertEquals(m.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void matchingUserByToken_validInput_success() {
    User createdUser = userService.createUser(testUserInput);
    
    UserAuthenticateByTokenDTO tokenDTO = new UserAuthenticateByTokenDTO();
    tokenDTO.setToken(createdUser.getToken());

    User m = userService.matchingUserByToken(tokenDTO);

    assertEquals(m.getId(), createdUser.getId());
    assertEquals(m.getUsername(), createdUser.getUsername());
    assertEquals(m.getPassword(), createdUser.getPassword());
    assertEquals(m.getStatus(), createdUser.getStatus());
    assertEquals(m.getLastStatus(), createdUser.getLastStatus());
    assertEquals(m.getCreation_date().toString(), createdUser.getCreation_date().toString());
    assertEquals(m.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void matchingUserById_validInput_success() {
    User createdUser = userService.createUser(testUserInput);
    User m = userService.matchingUserWithId(createdUser.getId());

    assertEquals(m.getId(), createdUser.getId());
    assertEquals(m.getUsername(), createdUser.getUsername());
    assertEquals(m.getPassword(), createdUser.getPassword());
    assertEquals(m.getStatus(), createdUser.getStatus());
    assertEquals(m.getLastStatus(), createdUser.getLastStatus());
    assertEquals(m.getCreation_date().toString(), createdUser.getCreation_date().toString());
    assertEquals(m.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void secureUpdateUser_validInput_success() {
    User createdUser = userService.createUser(testUserInput);

    UserUpdateDTO userUpdate = new UserUpdateDTO();
    userUpdate.setToken(createdUser.getToken());
    userUpdate.setUsername(Optional.of("asdf"));
    userUpdate.setBirthday(Optional.of(new Date(2)));

    userService.secureUpdateUser(createdUser.getId(), userUpdate);

    testUserInput.setUsername("asdf");
    User m = userService.matchingUser(testUserInput);

    assertEquals(m.getId(), createdUser.getId());
    assertEquals(m.getUsername(), "asdf");
    assertEquals(m.getPassword(), createdUser.getPassword());
    assertEquals(m.getStatus(), createdUser.getStatus());
    assertEquals(m.getLastStatus(), createdUser.getLastStatus());
    assertEquals(m.getCreation_date().toString(), createdUser.getCreation_date().toString());
    assertEquals(m.getBirthday().toString(), new Date(2).toString());
  }
}
