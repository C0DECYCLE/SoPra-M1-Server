package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateByTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserAuthenticateDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserUpdateDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@SuppressWarnings("null")
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().registerModule(new Jdk8Module()).writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));

    List<User> allUsers = Collections.singletonList(user);

    given(userService.getUsers()).willReturn(allUsers);

    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(user.getId())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$[0].creation_date", is(user.getCreation_date().toString())))
        .andExpect(jsonPath("$[0].birthday", is(user.getBirthday().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
    userAuthenticateDTO.setUsername("testUsername");
    userAuthenticateDTO.setPassword("testPassword");

    given(userService.createUser(Mockito.any())).willReturn(user);

    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.token", is(user.getToken())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$.creation_date", is(user.getCreation_date().toString())))
        .andExpect(jsonPath("$.birthday", is(user.getBirthday().toString())));
  }

  @Test
  public void createUser_invalidInput_fail() throws Exception  {
    UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
    userAuthenticateDTO.setUsername("testUsername");
    userAuthenticateDTO.setPassword("testPassword");

    given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest).andExpect(status().isConflict());
  }

  @Test
  public void authenticateUser_validInput() throws Exception {
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
    userAuthenticateDTO.setUsername("testUsername");
    userAuthenticateDTO.setPassword("testPassword");

    given(userService.matchingUser(Mockito.any())).willReturn(user);

    MockHttpServletRequestBuilder postRequest = post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.token", is(user.getToken())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$.creation_date", is(user.getCreation_date().toString())))
        .andExpect(jsonPath("$.birthday", is(user.getBirthday().toString())));
  }

  @Test
  public void authenticateUser_validInput_wrong() throws Exception {
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    UserAuthenticateDTO userAuthenticateDTO = new UserAuthenticateDTO();
    userAuthenticateDTO.setUsername("testUsername");
    userAuthenticateDTO.setPassword("wrongPassword");

    given(userService.matchingUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));


    MockHttpServletRequestBuilder postRequest = post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest).andExpect(status().isConflict());
  }

  @Test
  public void authenticateUserWithToken_validInput() throws Exception {
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("123");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    UserAuthenticateByTokenDTO userAuthenticateDTO = new UserAuthenticateByTokenDTO();
    userAuthenticateDTO.setToken("123");

    given(userService.matchingUserByToken(Mockito.any())).willReturn(user);

    MockHttpServletRequestBuilder postRequest = post("/userWithToken")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest)
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.token", is(user.getToken())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$.creation_date", is(user.getCreation_date().toString())))
        .andExpect(jsonPath("$.birthday", is(user.getBirthday().toString())));
  }

  @Test
  public void authenticateUserWithToken_validInput_wrong() throws Exception {
    User user = new User();
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    UserAuthenticateByTokenDTO userAuthenticateDTO = new UserAuthenticateByTokenDTO();
    userAuthenticateDTO.setToken("321");

    given(userService.matchingUserByToken(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));


    MockHttpServletRequestBuilder postRequest = post("/userWithToken")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userAuthenticateDTO));

    mockMvc.perform(postRequest).andExpect(status().isConflict());
  }

  @Test
  public void getUserById_validInput() throws Exception {
    User user = new User();
    user.setId(7L);
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("123");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    given(userService.matchingUserWithId(Mockito.any())).willReturn(user);

    MockHttpServletRequestBuilder getRequest = get("/users/7");

    mockMvc.perform(getRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$.creation_date", is(user.getCreation_date().toString())))
        .andExpect(jsonPath("$.birthday", is(user.getBirthday().toString())));
  }

  @Test
  public void getUserById_validInput_unkown() throws Exception {
    User user = new User();
    user.setId(7L);
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("123");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
    
    given(userService.matchingUserWithId(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

    MockHttpServletRequestBuilder getRequest = get("/users/3");

    mockMvc.perform(getRequest).andExpect(status().isNotFound());
  }

  @Test
  public void updateUser_validInput() throws Exception {
    User user = new User();
    user.setId(7L);
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("123");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
  
    UserUpdateDTO userUpdate = new UserUpdateDTO();
    userUpdate.setToken("123");
    userUpdate.setUsername(Optional.of("changedUsername"));
    userUpdate.setBirthday(Optional.of(new Date(2)));

    MockHttpServletRequestBuilder putRequest = put("/users/7")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userUpdate));

    mockMvc.perform(putRequest).andExpect(status().isNoContent());
  }

  @Test
  public void updateUser_invalidInput() throws Exception {
    User user = new User();
    user.setId(7L);
    user.setUsername("testUsername");
    user.setPassword("testPassword");
    user.setToken("123");
    user.setStatus(UserStatus.ONLINE);
    user.setCreation_date(new Date(1));
    user.setBirthday(new Date(1));
  
    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userService).secureUpdateUser(Mockito.anyLong(),Mockito.any());

    UserUpdateDTO userUpdate = new UserUpdateDTO();
    userUpdate.setToken("321");

    MockHttpServletRequestBuilder putRequest = put("/users/6")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userUpdate));

    mockMvc.perform(putRequest).andExpect(status().isNotFound());
  }

}