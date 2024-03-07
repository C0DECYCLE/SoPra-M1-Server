package ch.uzh.ifi.hase.soprafs24.rest.dto;
import java.sql.Date;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {

  @Getter
  @Setter 
  private Long id;

  @Getter
  @Setter 
  private String username;
  
  @Getter
  @Setter 
  private UserStatus status;
  
  @Getter
  @Setter 
  private Date creation_date;
  
  @Getter
  @Setter 
  private Date birthday;
}
