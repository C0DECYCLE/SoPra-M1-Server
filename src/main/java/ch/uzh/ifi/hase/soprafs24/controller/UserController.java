package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserWithTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserUpdateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserStatusPingDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateByTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserDTO> getAllUsers() {
    List<User> users = userService.getUsers();
    List<UserDTO> userDTOs = new ArrayList<>();
    for (User user : users) {
      userDTOs.add(DTOMapper.INSTANCE.convertEntityToUserDTO(user));
    }
    return userDTOs;
  }
  
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserWithTokenDTO createUser(@RequestBody UserAuthenticateDTO userAuthenticate) {
    User newUser = userService.createUser(userAuthenticate);
    return DTOMapper.INSTANCE.convertEntityToUserWithTokenDTO(newUser);
  }
  
  @PostMapping("/user")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public UserWithTokenDTO authenticateUser(@RequestBody UserAuthenticateDTO userAthenticate) {
    User user = userService.matchingUser(userAthenticate);
    return DTOMapper.INSTANCE.convertEntityToUserWithTokenDTO(user);
  }

  @PostMapping("/userWithToken")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public UserWithTokenDTO authenticateUserByToken(@RequestBody UserAuthenticateByTokenDTO userAuthenticate) {
    User user = userService.matchingUserByToken(userAuthenticate);
    return DTOMapper.INSTANCE.convertEntityToUserWithTokenDTO(user);
  }

  @PostMapping("/userStatusPing")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUserOnlineStatus(@RequestBody UserStatusPingDTO userStatusPingDTO) {
    userService.updateOnlineStatus(userStatusPingDTO);
  }

  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserDTO getUserById(@PathVariable Long userId) {
    User user = userService.matchingUserWithId(userId);
    return DTOMapper.INSTANCE.convertEntityToUserDTO(user);
  }

  @PutMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void updateUser(@PathVariable Long userId, @RequestBody UserUpdateDTO userUpdate) {
    userService.secureUpdateUser(userId, userUpdate);
  }

  @PutMapping("/users/unbirthday/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void unbirthdayUser(@PathVariable Long userId, @RequestBody UserAuthenticateByTokenDTO userAuthenticate) {
    userService.secureUnbirthdayUser(userId, userAuthenticate);
  }
}
