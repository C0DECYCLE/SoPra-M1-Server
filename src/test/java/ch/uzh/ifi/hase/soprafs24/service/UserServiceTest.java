package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;
  private UserPostDTO userPostDTO;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    testUser = new User();
    testUser.setUsername("testUsername");
    testUser.setPassword("testPassword");
    testUser.setStatus(UserStatus.OFFLINE);
    testUser.setLastStatus(1L);
    testUser.setCreation_date(new Date(1));
    testUser.setBirthday(new Date(1));


    userPostDTO = new UserPostDTO();
    userPostDTO.setUsername("testUsername");
    userPostDTO.setPassword("testPassword");

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    User createdUser = userService.createUser(userPostDTO);

    //Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
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

}
