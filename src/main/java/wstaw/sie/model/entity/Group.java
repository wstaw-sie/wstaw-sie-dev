package wstaw.sie.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@ManyToOne(targetEntity = UploadedFile.class)
	@JoinColumn(name = "filenameWithPath_id", referencedColumnName = "id")
	private UploadedFile photo;
	

	@OneToMany(mappedBy = "groupOfUser", targetEntity = User.class, cascade = CascadeType.ALL)
	private Set<User> usersOfGroup = new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsersOfGroup() {
		return usersOfGroup;
	}

	public void setUsersOfGroup(Set<User> usersOfGroup) {
		this.usersOfGroup = usersOfGroup;
	}

	public Boolean hasPhoto() {
		return this.photo != null && this.photo.getFilenameWithPath() != "";
	}

	public UploadedFile getPhoto() {
		return photo;
	}
	
	public String getPhotoPath() {
		return photo.getFilenameWithPath();
	}

	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}

}
