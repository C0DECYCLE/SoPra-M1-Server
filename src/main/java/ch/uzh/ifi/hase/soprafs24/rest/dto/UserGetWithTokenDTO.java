package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserGetWithTokenDTO extends UserGetDTO {

  private String token;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

}