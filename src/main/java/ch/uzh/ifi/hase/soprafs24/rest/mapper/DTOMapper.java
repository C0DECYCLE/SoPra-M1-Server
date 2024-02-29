package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetWithTokenDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserWithTokenPostDTO;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creation_date", target = "creation_date")
  @Mapping(source = "birthday", target = "birthday")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creation_date", target = "creation_date")
  @Mapping(source = "birthday", target = "birthday")
  UserGetWithTokenDTO convertEntityToUserGetWithTokenDTO(User user);
  
  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  UserPostDTO convertUserPostDTOtoEntity(UserPostDTO userPostDTO);
  
  @Mapping(source = "token", target = "token")
  UserWithTokenPostDTO convertUserWithTokenPostDTOtoEntity(UserWithTokenPostDTO userWithTokenPostDTO);
}
