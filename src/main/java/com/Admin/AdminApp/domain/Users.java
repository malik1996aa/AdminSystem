package com.Admin.AdminApp.domain;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@SqlResultSetMapping(name = "allloginnMapping", classes = { @ConstructorResult(targetClass = Users.class, columns = {
		@ColumnResult(name = "USER_ID", type = long.class), @ColumnResult(name = "NAME", type = String.class),
		@ColumnResult(name = "EMAIL", type = String.class), @ColumnResult(name = "PHONE", type = String.class),
		@ColumnResult(name = "PASSWORD", type = String.class), @ColumnResult(name = "LOGIN", type = String.class),
		@ColumnResult(name = "NUM_OF_ATTEMPTS", type = int.class),
		@ColumnResult(name = "LASTMODIFIED", type = LocalDate.class),
		@ColumnResult(name = "LOCK", type = boolean.class) }) })
@NamedNativeQuery(name = "Users.loginAll", query = " SELECT USERS.USER_ID , USERS.NAME , USERS.EMAIL , USERS.PHONE "
		+ ", USERS.PASSWORD , USERS.LOGIN " + " , USERS.NUM_OF_ATTEMPTS , USERS.LASTMODIFIED , USERS.LOCK "
		+ " from USERS " + " WHERE ((USERS.EMAIL) =:Email) AND "
		+ " ((USERS.PASSWORD) =:Password) ", resultSetMapping = "allloginnMapping")

////////////////////////////////////////////

@SqlResultSetMapping(name = "loginnbyEmailMapping", classes = {
		@ConstructorResult(targetClass = Users.class, columns = { @ColumnResult(name = "USER_ID", type = long.class),
				@ColumnResult(name = "NAME", type = String.class), @ColumnResult(name = "EMAIL", type = String.class),
				@ColumnResult(name = "PHONE", type = String.class),
				@ColumnResult(name = "PASSWORD", type = String.class),
				@ColumnResult(name = "LOGIN", type = String.class),
				@ColumnResult(name = "NUM_OF_ATTEMPTS", type = int.class),
				@ColumnResult(name = "LASTMODIFIED", type = LocalDate.class),
				@ColumnResult(name = "LOCK", type = boolean.class) }) })
@NamedNativeQuery(name = "Users.loginbyEmail", query = " SELECT USERS.USER_ID , USERS.NAME , USERS.EMAIL , USERS.PHONE "
		+ ", USERS.PASSWORD , USERS.LOGIN " + " , USERS.NUM_OF_ATTEMPTS , USERS.LASTMODIFIED , USERS.LOCK "
		+ " from USERS " + " WHERE ((USERS.EMAIL) =:Email) ", resultSetMapping = "loginnbyEmailMapping")

///////////////////////////////////////
@SqlResultSetMapping(name = "UserPermissionsMapping", classes = {
		@ConstructorResult(targetClass = PermissionResult.class, columns = {
				@ColumnResult(name = "PER_ID", type = BigInteger.class),
				@ColumnResult(name = "PER_NAME", type = String.class) }) })
@NamedNativeQuery(name = "Users.getPermissionsForUser", query = " select distinct p.PER_ID ,p.PER_NAME "
		+ " from PERMISSION p join ROLE_PERMISSION rp on p.PER_ID = rp.PERMISSION_ID "
		+ " JOIN  ROLE r on r.ROLE_ID =rp.ROLE_ID " + " JOIN USER_ROLE  ur ON r.ROLE_ID=ur.ROLE_ID "
		+ " JOIN USERS u on u.USER_ID=ur.USER_ID "
		+ " where ((u.USER_ID)=:user_id) ", resultSetMapping = "UserPermissionsMapping")


///////////////////////////////////////
@SqlResultSetMapping(name = "findByUserNameMapping", classes = {
		@ConstructorResult(targetClass = GetIdByEmail.class, columns = {
				@ColumnResult(name = "USER_ID", type = BigInteger.class)})})
				
@NamedNativeQuery(name = "Users.findByUserName", query = " select u.user_id from users u where u.email = ?1 ", resultSetMapping = "findByUserNameMapping")
///////////////////////
@SqlResultSetMapping(name = "getEmailByIdMapping", classes = {
		@ConstructorResult(targetClass = Emails.class, columns = {
				@ColumnResult(name = "EMAIL", type = String.class)})})
				
@NamedNativeQuery(name = "Users.getEmailById", query = " select u.EMAIL from users u where ((u.USER_ID)=:user_id) ", resultSetMapping = "getEmailByIdMapping")
///////////////////////
@SqlResultSetMapping(name = "getPhoneByIdMapping", classes = {
@ConstructorResult(targetClass = Phones.class, columns = {
@ColumnResult(name = "PHONE", type = String.class)})})

@NamedNativeQuery(name = "Users.getPhoneById", query = " select u.Phone from users u where ((u.USER_ID)=:user_id) ", resultSetMapping = "getPhoneByIdMapping")
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long UserId;
	private String Name;
	@Column(unique = true)
	private String Email;
	private String Phone;
	private String Password;
	private String login="No";
	private int NumOfAttempts = 4;
	private LocalDate lastmodified = LocalDate.now().minusDays(3);
	private boolean lock = false;
	private int key=0;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "User_Role", joinColumns = @JoinColumn(name = "User_Id", referencedColumnName = "UserId"), inverseJoinColumns = @JoinColumn(name = "Role_id", referencedColumnName = "RoleId"))
	@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
	@JsonIgnore
	private Set<Role> roles;

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Users() {
		super();
	}

	public long getUserId() {
		return UserId;
	}

	public void setUserId(long userId) {
		UserId = userId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getNumOfAttempts() {
		return NumOfAttempts;
	}

	public void setNumOfAttempts(int numOfAttempts) {
		NumOfAttempts = numOfAttempts;
	}

	public LocalDate getLastmodified() {
		return lastmodified;
	}

	public void setLastmodified(LocalDate lastmodified) {
		this.lastmodified = lastmodified;
	}

	public boolean isLock() {
		return lock;
	}

	public void setLock(boolean lock) {
		this.lock = lock;
	}
	
	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Users(String name, String email, String phone, String password, String login) {
		super();
//        counter++;
//        this.UserId = counter;
		this.Name = name;
		this.Email = email;
		this.Phone = phone;
		this.Password = password;
		this.login = login;
	}

	public Users(String name, String email, String phone, String password, String login, Set<Role> roles) {
		super();
//        counter++;
//        this.UserId = counter;
		this.Name = name;
		this.Email = email;
		this.Phone = phone;
		this.Password = password;
		this.login = login;
		this.roles = roles;
	}

	public Users(String name, String email, String phone, String password, String login, int numOfAttempts,
			LocalDate lastmodified, boolean lock) {
		super();
		Name = name;
		Email = email;
		Phone = phone;
		Password = password;
		this.login = login;
		NumOfAttempts = numOfAttempts;
		this.lastmodified = lastmodified;
		this.lock = lock;
	}

	public Users(long userId, String name, String email, String phone, String password, String login, int numOfAttempts,
			LocalDate lastmodified, boolean lock) {
		super();
		UserId = userId;
		Name = name;
		Email = email;
		Phone = phone;
		Password = password;
		this.login = login;
		NumOfAttempts = numOfAttempts;
		this.lastmodified = lastmodified;
		this.lock = lock;
	}

	public boolean equals1(Optional<Users> user) {
		return this.UserId == user.get().UserId;
	}
	

}
