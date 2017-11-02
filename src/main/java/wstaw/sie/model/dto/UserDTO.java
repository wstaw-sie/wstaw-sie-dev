package wstaw.sie.model.dto;

import wstaw.sie.model.entity.Group;
import wstaw.sie.model.Role;

public class UserDTO {
  
  private Integer id;
  private Group groupOfUser;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String salt;
  private String intention;
  private Role role;
  
  public Group getGroupOfUser() {
	return groupOfUser;
}
public void setGroupOfUser(Group groupOfUser) {
	this.groupOfUser = groupOfUser;
}
public Role getRole() {
	return role;
}
public void setRole(Role role) {
	this.role = role;
}
public Integer getPrayFor() {
	return prayFor;
}
public void setPrayFor(Integer prayFor) {
	this.prayFor = prayFor;
}

private Integer prayFor;
  private String photo;

  public Integer getId() {
    return id;
  }
  public void setId(Integer id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public String getLastName() {
    return lastName;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getSalt() {
    return salt;
  }
  public void setSalt(String salt) {
    this.salt = salt;
  }
  public String getIntention() {
    return intention;
  }
  public void setIntention(String intention) {
    this.intention = intention;
  }
  public String getPhoto() {
    return photo;
  }
  public void setPhoto(String photo) {
    this.photo = photo;
  }
  
  public Boolean hasPhoto()
  {
	  return this.photo!= null && this.photo!= "";
  }

}
