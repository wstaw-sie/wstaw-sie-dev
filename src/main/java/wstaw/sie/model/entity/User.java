package wstaw.sie.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import wstaw.sie.model.Role;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "salt")
	private String salt;

	@Column(name = "intention", columnDefinition = "TEXT")
	private String intention;

	@Column(name = "pray_for")
	private Integer prayFor;

	@ManyToOne(targetEntity = UploadedFile.class)
	@JoinColumn(name = "filenameWithPath_id", referencedColumnName = "id")
	private UploadedFile photo;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "\"role\"")
	private Role role;
	
	@ManyToOne(targetEntity = Group.class)
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	private Group groupOfUser;
	
	@Column(name = "is_migrated")
	private Boolean isMigrated;
	
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public Integer getPrayFor() {
		return prayFor;
	}

	public void setPrayFor(Integer prayFor) {
		this.prayFor = prayFor;
	}

	public Boolean hasPhoto() {
		return this.photo != null && this.photo.getFilenameWithPath() != "";
	}

	public Group getGroupOfUser() {
		return groupOfUser;
	}

	public void setGroupOfUser(Group groupOfUser) {
		this.groupOfUser = groupOfUser;
	}

	public Boolean getIsMigrated() {
		return isMigrated != null && isMigrated;
	}

	public void setIsMigrated(Boolean isMigrated) {
		this.isMigrated = isMigrated;
	}
	
	public UploadedFile getPhoto() {
		return photo;
	}

	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}
	
	public String getPhotoPath() {
		return photo.getFilenameWithPath();
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", password=" + password
				+ ", salt=" + salt + ", intention=" + intention + ", prayFor="
				+ prayFor + ", photo=" + photo + ", role=" + role
				+ ", groupOfUser=" + groupOfUser + ", isMigrated=" + isMigrated
				+ "]";
	}


}
