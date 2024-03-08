package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.util.Optional;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findById_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setLastStatus(1L);
    user.setToken("1");
    user.setCreation_date(new Date(1));
    user.setBirthday(null);

    entityManager.persist(user);
    entityManager.flush();

    @SuppressWarnings("null")
    Optional<User> found = userRepository.findById(user.getId());

    assert(found.isPresent());
    User u = found.get();
    assertNotNull(u.getId());
    assertEquals(u.getUsername(), user.getUsername());
    assertEquals(u.getPassword(), user.getPassword());
    assertEquals(u.getStatus(), user.getStatus());
    assertEquals(u.getLastStatus(), user.getLastStatus());
    assertEquals(u.getToken(), user.getToken());
    assertEquals(u.getCreation_date(), user.getCreation_date());
    assertEquals(u.getBirthday(), user.getBirthday());
  }

  @Test
  public void findById_fail() {
    Optional<User> found = userRepository.findById(5L);
    assert(found.isEmpty());
  }

  @Test
  public void findByUsername_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setLastStatus(1L);
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
    assertEquals(found.getLastStatus(), user.getLastStatus());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getCreation_date(), user.getCreation_date());
    assertEquals(found.getBirthday(), user.getBirthday());
  }

  @Test
  public void findByUsername_fail() {
    User found = userRepository.findByUsername("firstname@lastname");
    assertNull(found);
  }

  @Test
  public void findByToken_success() {
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setPassword("testPassword");
    user.setStatus(UserStatus.OFFLINE);
    user.setLastStatus(1L);
    user.setToken("123");
    user.setCreation_date(new Date(1));
    user.setBirthday(null);

    entityManager.persist(user);
    entityManager.flush();

    User found = userRepository.findByToken(user.getToken());

    assertNotNull(found.getId());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getStatus(), user.getStatus());
    assertEquals(found.getLastStatus(), user.getLastStatus());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getCreation_date(), user.getCreation_date());
    assertEquals(found.getBirthday(), user.getBirthday());
  }

  @Test
  public void findByToken_fail() {
    User found = userRepository.findByToken("123");
    assertNull(found);
  }
}
