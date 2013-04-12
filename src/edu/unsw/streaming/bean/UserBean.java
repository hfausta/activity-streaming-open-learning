package edu.unsw.streaming.bean;

import java.io.Serializable;
import edu.unsw.streaming.shared.Validate;
/**
 * 
 * @author Charagh Jethnani
 *Represents a User of the website. Administrators are determined by their isAdmin status. 
 *Lecturers and Students are determined by the role they possess in each Group. 
 *That is the purpose of the Belong class
 *
 */
@SuppressWarnings("serial")
public class UserBean extends ParticipantBean implements Serializable {
	private String email;
	private String password;
	private Boolean isAdmin;
	private String name;
	
	public UserBean() {
		super();
	}
	
	public UserBean(Integer id, String name,
			String email, String password) throws Exception {
		super(id);
		this.email = email;
		this.isAdmin = false;
		this.name = name;
		Validate.notNull(name,"User must have a name");
		
		Validate.notNull(password,"User must have password");
		/*MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
		mdEnc.update(password.getBytes(), 0, password.length());
		this.password= new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string*/
		this.password = password;
		
		Validate.notNull(email,"User must have email");
		if(!Validate.isEmail(email)){
			throw new Exception("Invalid email");
		}
	}

	public String getName() {
		return name;
	}



	public void setName(String name) throws Exception {
		Validate.notNull(name,"User must have a name");
		this.name = name;
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws Exception {
		Validate.notNull(email,"User must have email");
		Validate.isEmail(email);		
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws Exception {
		Validate.notNull(password,"User must have password");
		/*MessageDigest mdEnc = MessageDigest.getInstance("MD5"); // Encryption algorithm
		mdEnc.update(password.getBytes(), 0, password.length());
		this.password= new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted string*/
		this.password = password;
		
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) throws Exception {
		Validate.notNull(isAdmin, "Admin status can't be set to null");
		this.isAdmin = isAdmin;
	}

}
