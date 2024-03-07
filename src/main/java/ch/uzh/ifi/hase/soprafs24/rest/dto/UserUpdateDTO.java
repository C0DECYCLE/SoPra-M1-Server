package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.sql.Date;
import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

public class UserUpdateDTO extends UserAuthenticateByTokenDTO {

  @Getter
  @Setter 
  private Optional<String> username;
  
  @Getter
  @Setter 
  private Optional<Date> birthday;
}
