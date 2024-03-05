package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserStatusPingDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserWithTokenPostDTO;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

  //private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(UserPostDTO newUserInput) {
    checkIfUsernameExists(newUserInput.getUsername());
    long now = System.currentTimeMillis();
    User newUser = new User();
    newUser.setUsername(newUserInput.getUsername());
    newUser.setPassword(newUserInput.getPassword());
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.OFFLINE);
    newUser.setLastStatus(now);
    newUser.setCreation_date(new Date(now));
    newUser.setBirthday(null);
    newUser = userRepository.save(newUser);
    userRepository.flush();
    return newUser;
  }

  private void checkIfUsernameExists(String username) {
    User userByUsername = userRepository.findByUsername(username);
    String errorMessage = "The username provided already exists. Please choose another one.";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
    }
  }

  public User matchingUser(UserPostDTO userInput) {
    User userByUsername = userRepository.findByUsername(userInput.getUsername());
    String unknownMessage = "Username doesn't exist. Please try again.";
    String wrongMessage = "Password is not correct. Please try again.";
    if (userByUsername == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, unknownMessage);
    }
    if (!userByUsername.getPassword().trim().equals(userInput.getPassword().trim())) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, wrongMessage);
    }
    return userByUsername;
  }

  public User matchingUserWithToken(UserWithTokenPostDTO userInput) {
    User user = userRepository.findByToken(userInput.getToken());
    String errorMessage = "Invalid token.";
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
    }
    return user;
  }

  public void updateOnlineStatus(UserStatusPingDTO userStatusPingDTO) {
    User user = userRepository.findByToken(userStatusPingDTO.getToken());
    if (user == null) {
      return;
    }
    long now = System.currentTimeMillis();
    user.setStatus(UserStatus.ONLINE);
    user.setLastStatus(now);
  }
}
