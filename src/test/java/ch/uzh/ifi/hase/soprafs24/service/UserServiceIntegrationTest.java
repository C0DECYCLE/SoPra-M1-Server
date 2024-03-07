package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    assertNull(userRepository.findByUsername("testUsername"));

    UserAuthenticateDTO testUserInput = new UserAuthenticateDTO();
    testUserInput.setUsername("testUsername");
    testUserInput.setPassword("testPassword");

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

    UserAuthenticateDTO testUserInput = new UserAuthenticateDTO();
    testUserInput.setUsername("testUsername");
    testUserInput.setPassword("testPassword");
    
    userService.createUser(testUserInput);

    UserAuthenticateDTO testUserInput2 = new UserAuthenticateDTO();
    testUserInput2.setUsername("testUsername");

    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUserInput2));
  }
}
