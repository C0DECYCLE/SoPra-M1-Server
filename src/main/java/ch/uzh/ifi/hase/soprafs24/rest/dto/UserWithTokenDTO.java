package ch.uzh.ifi.hase.soprafs24.rest.dto;
import lombok.Getter;
import lombok.Setter;

public class UserWithTokenDTO extends UserDTO {

  @Getter
  @Setter 
  private String token;

}
