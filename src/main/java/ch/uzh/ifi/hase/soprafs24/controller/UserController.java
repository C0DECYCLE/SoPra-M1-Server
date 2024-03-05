package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetWithTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserStatusPingDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserWithTokenPostDTO;
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
  public List<UserGetDTO> getAllUsers() {
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }
  
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetWithTokenDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    User newUser = userService.createUser(userPostDTO);
    return DTOMapper.INSTANCE.convertEntityToUserGetWithTokenDTO(newUser);
  }
  
  @PostMapping("/user")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public UserGetWithTokenDTO authenticateUser(@RequestBody UserPostDTO userPostDTO) {
    User user = userService.matchingUser(userPostDTO);
    return DTOMapper.INSTANCE.convertEntityToUserGetWithTokenDTO(user);
  }

  @PostMapping("/userWithToken")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public UserGetWithTokenDTO authenticateUserWithToken(@RequestBody UserWithTokenPostDTO userWithTokenPostDTO) {
    User user = userService.matchingUserWithToken(userWithTokenPostDTO);
    return DTOMapper.INSTANCE.convertEntityToUserGetWithTokenDTO(user);
  }

  @PostMapping("/userStatusPing")
  @ResponseStatus(HttpStatus.ACCEPTED)
  @ResponseBody
  public void updateUserOnlineStatus(@RequestBody UserStatusPingDTO userStatusPingDTO) {
    userService.updateOnlineStatus(userStatusPingDTO);
  }
}
