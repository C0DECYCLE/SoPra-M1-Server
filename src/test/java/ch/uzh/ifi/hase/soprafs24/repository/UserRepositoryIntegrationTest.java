package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUsername_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setCreation_date(new Date(1));
    user.setBirthday(null);

    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByUsername(user.getUsername());

    assertNotNull(found.getId());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getCreation_date(), user.getCreation_date());
    assertEquals(found.getBirthday(), user.getBirthday());
  }
}
