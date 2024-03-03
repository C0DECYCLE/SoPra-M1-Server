package ch.uzh.ifi.hase.soprafs24.rest.dto;
import lombok.Getter;
import lombok.Setter;

public class UserGetWithTokenDTO extends UserGetDTO {

  @Getter
  @Setter 
  private String token;

}
