package ch.uzh.ifi.hase.soprafs24.rest.dto;
import lombok.Getter;
import lombok.Setter;

public class UserPostDTO {

  @Getter
  @Setter 
  private String username;

  @Getter
  @Setter 
  private String password;
}
