package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateByTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserUpdateDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private UserAuthenticateDTO userPostDTO;
  private UserAuthenticateByTokenDTO tokenDTO;

  @SuppressWarnings("null")
  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setId(7L);
    testUser.setUsername("testUsername");
    testUser.setPassword("testPassword");
    testUser.setStatus(UserStatus.OFFLINE);
    testUser.setToken("123");
    testUser.setLastStatus(1L);
    testUser.setCreation_date(new Date(1));
    testUser.setBirthday(new Date(1));

    userPostDTO = new UserAuthenticateDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("testPassword");

    tokenDTO = new UserAuthenticateByTokenDTO();
    tokenDTO.setToken("123");

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void getUsers_success() {
    User createdUser = userService.createUser(userPostDTO);

    Mockito.when(userRepository.findAll()).thenReturn(List.of(createdUser));

    User u = userService.getUsers().get(0);

    assertEquals(u.getId(), createdUser.getId());
    assertEquals(u.getUsername(), createdUser.getUsername());
    assertEquals(u.getPassword(), createdUser.getPassword());
    assertEquals(u.getStatus(), createdUser.getStatus());
    assertEquals(u.getLastStatus(), createdUser.getLastStatus());
    assertEquals(u.getCreation_date(), createdUser.getCreation_date());
    assertEquals(u.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void createUser_validInputs_success() {
    User createdUser = userService.createUser(userPostDTO);

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getStatus(), createdUser.getStatus());
    assertEquals(testUser.getLastStatus(), createdUser.getLastStatus());
    assertEquals(testUser.getCreation_date(), createdUser.getCreation_date());
    assertEquals(testUser.getBirthday(), createdUser.getBirthday());
  }

  @Test
  public void createUser_duplicateName_throwsException() {
    userService.createUser(userPostDTO);

    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    assertThrows(ResponseStatusException.class, () -> userService.createUser(userPostDTO));
  }

  @Test
  public void matchingUser_validInput_success() {
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    User m = userService.matchingUser(userPostDTO);

    assertEquals(m.getId(), testUser.getId());
    assertEquals(m.getUsername(), testUser.getUsername());
    assertEquals(m.getPassword(), testUser.getPassword());
    assertEquals(m.getStatus(), testUser.getStatus());
    assertEquals(m.getLastStatus(), testUser.getLastStatus());
    assertEquals(m.getCreation_date(), testUser.getCreation_date());
    assertEquals(m.getBirthday(), testUser.getBirthday());
  }

  @Test
  public void matchingUserByToken_validInput_success() {
    Mockito.when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);

    User m = userService.matchingUserByToken(tokenDTO);

    assertEquals(m.getId(), testUser.getId());
    assertEquals(m.getUsername(), testUser.getUsername());
    assertEquals(m.getPassword(), testUser.getPassword());
    assertEquals(m.getStatus(), testUser.getStatus());
    assertEquals(m.getLastStatus(), testUser.getLastStatus());
    assertEquals(m.getCreation_date(), testUser.getCreation_date());
    assertEquals(m.getBirthday(), testUser.getBirthday());
  }

  @Test
  public void matchingUserById_validInput_success() {
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));

    User m = userService.matchingUserWithId(7L);

    assertEquals(m.getId(), testUser.getId());
    assertEquals(m.getUsername(), testUser.getUsername());
    assertEquals(m.getPassword(), testUser.getPassword());
    assertEquals(m.getStatus(), testUser.getStatus());
    assertEquals(m.getLastStatus(), testUser.getLastStatus());
    assertEquals(m.getCreation_date(), testUser.getCreation_date());
    assertEquals(m.getBirthday(), testUser.getBirthday());
  }

  @Test
  public void secureUpdateUser_validInput_success() {
    Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(testUser));
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

    UserUpdateDTO userUpdate = new UserUpdateDTO();
    userUpdate.setToken("123");
    userUpdate.setUsername(Optional.of("asdf"));
    userUpdate.setBirthday(Optional.of(new Date(2)));

    userService.secureUpdateUser(7L, userUpdate);
  }

}
