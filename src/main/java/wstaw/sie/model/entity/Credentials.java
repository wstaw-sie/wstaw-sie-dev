package wstaw.sie.model.entity;

import org.hibernate.validator.constraints.Email;

public class Credentials {

  @Email
  private String email;
  private String passwordPlain;
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPasswordPlain() {
    return passwordPlain;
  }
  public void setPasswordPlain(String passwordPlain) {
    this.passwordPlain = passwordPlain;
  }
  
  public void release()
  {
    this.email =null;
    this.passwordPlain = null;
  }
  
  public String toString()
  {
    return email+" "+passwordPlain;
  }
  
}
