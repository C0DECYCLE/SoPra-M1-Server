package ch.uzh.ifi.hase.soprafs24.entity;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "USER")
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  @Getter
  @Setter 
  private Long id;

  @Column(nullable = false, unique = true)
  @Getter
  @Setter 
  private String username;

  @Column(nullable = false)
  @Getter
  @Setter 
  private String password;

  @Column(nullable = false, unique = true)
  @Getter
  @Setter 
  private String token;

  @Column(nullable = false)
  @Getter
  @Setter 
  private UserStatus status;

  @Column(nullable = false)
  @Getter
  @Setter 
  private Long lastStatus;

  @Column(nullable = false)
  @Getter
  @Setter 
  private Date creation_date;

  @Column()
  @Getter
  @Setter 
  private Date birthday;
}
