package com.Admin.AdminApp.web;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.usertype.UserCollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Admin.AdminApp.domain.Emails;
import com.Admin.AdminApp.domain.GetIdByEmail;
import com.Admin.AdminApp.domain.Keys;
import com.Admin.AdminApp.domain.LogIn;
import com.Admin.AdminApp.domain.LogInCircleGraph;
import com.Admin.AdminApp.domain.Permission;
import com.Admin.AdminApp.domain.PermissionRepository;
import com.Admin.AdminApp.domain.PermissionResult;
import com.Admin.AdminApp.domain.Phones;
import com.Admin.AdminApp.domain.Role;
import com.Admin.AdminApp.domain.RoleRepository;
import com.Admin.AdminApp.domain.RoleResult;

import com.Admin.AdminApp.domain.UserRepository;
import com.Admin.AdminApp.domain.UserResult;
import com.Admin.AdminApp.domain.UserRoleArray;
import com.Admin.AdminApp.domain.Users;

@RestController
public class UserController {
	@Autowired
	private UserRepository repository;
	@Autowired
	private PermissionRepository prepository;
	@Autowired
	private RoleRepository rolerepository;

//      /////////////////////////////////////////////////////////////////////////////////////////
	@RequestMapping("/users")
	public ArrayList<UserResult> getUsers() {
		ArrayList<UserResult> res = new ArrayList<UserResult>();

		ArrayList<Users> users = (ArrayList<Users>) repository.findAll();
		for (Users user : users) {
			ArrayList<RoleResult> roles = rolerepository.getRoles(user.getUserId());
			BigInteger bi = BigInteger.valueOf(user.getUserId());
			UserResult u = new UserResult(bi, user.getEmail(), user.getName(), user.getPassword(), user.getPhone(),
					user.getLogin(), roles);
			res.add(u);
		}
		return res;
	}

	@RequestMapping("/LogInCircleGraph")
	public LogInCircleGraph getLogInCircleGraph() {
		LogInCircleGraph details = new LogInCircleGraph();
		int logYes = 0;
		int logNo = 0;
		ArrayList<Users> users = (ArrayList<Users>) repository.findAll();
		for (Users user : users) {
			if (user.getLogin().equalsIgnoreCase("yes")) {
				logYes += 1;
			} else {
				logNo += 1;
			}
		}
		details.setLogInYes(logYes);
		details.setLogInNo(logNo);
		return details;
	}

	@RequestMapping(value = "/emails", method = RequestMethod.GET)
    public ArrayList<Emails> getEmail() {
        ArrayList<Emails> emails = new ArrayList<Emails>();
       
        ArrayList<Users> users = (ArrayList<Users>) repository.findAll();
        for(Users user : users)
        {
            Emails e=new Emails(user.getEmail());
           emails.add(e);
           
        }
        return emails;

    }
	@RequestMapping(value = "/emailByUser/{user_id}", method = RequestMethod.GET)
    public Emails getemailByUser (@PathVariable("user_id") long user_id) {
        
        return repository.getEmailById(user_id);

    }
	@RequestMapping(value = "/phoneByUser/{user_id}", method = RequestMethod.GET)
    public Phones getphoneById (@PathVariable("user_id") long user_id) {
        
        return repository.getPhoneById(user_id);

    }
	@RequestMapping("/roles/{user_id}")
	ArrayList<RoleResult> getRoles(@PathVariable("user_id") long user_id) {
		return rolerepository.getRoles(user_id);

	}

	@RequestMapping("/permissionsUser/{user_id}")
	ArrayList<PermissionResult> getPermissionsForUser(@PathVariable("user_id") long user_id) {
		return repository.getPermissionsForUser(user_id);
	}

	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public String adds(@RequestBody UserResult u) {
		Users user = new Users();
		user.setName(u.getName());
		user.setPhone(u.getPhone());
		user.setEmail(u.getEmail());
		user.setPassword(u.getPassword());
		
		Set<Role> roleSet = new HashSet<Role>();
		Role r;
		ArrayList<RoleResult> roleList = u.getRoles();
		for (RoleResult role1 : roleList) {
			r = new Role();
			r.setRole_name(role1.getRole_name());
			r.setDescription(role1.getDescription());
			r.setRoleId(role1.getRoleId().longValue());
			roleSet.add(r);
		}
		user.setRoles(roleSet);
		repository.save(user);
		return "user added";
	}

	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable long id) {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "User Deleted";
	}

	@RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT)
	public String updateUser(@PathVariable("id") Long id, @RequestBody UserResult user) {
		Optional<Users> uOp = repository.findById(id);
		if (uOp.isPresent()) {

			Users user1 = uOp.get();
			Set<Role> roleSet = new HashSet<Role>();
			Role r;
			ArrayList<RoleResult> roleList = user.getRoles();
			for (RoleResult role1 : roleList) {
				r = new Role();
				r.setRole_name(role1.getRole_name());
				r.setDescription(role1.getDescription());
				r.setRoleId(role1.getRoleId().longValue());
				roleSet.add(r);
			}
			user1.setRoles(roleSet);
			user1.setName(user.getName());
			user1.setEmail(user.getEmail());
			user1.setPhone(user.getPhone());
			user1.setPassword(user.getPassword());
			repository.save(user1);
		} else
			return "There is no user with this id";

		return "OK";
	}
//

	@RequestMapping(value = "/login", method = RequestMethod.PUT)
	public Keys login(@RequestBody LogIn lg) {
		String email = lg.getEmail();
		String pass = lg.getPassword();
		Keys k=new Keys();
		Random rn = new Random();
		int key1 = rn.nextInt(100) + 1;
		
		try {
			Users u = repository.loginAll(email, pass);
			if (u != null) {
				Optional<Users> u1=repository.findById(u.getUserId());
				if (u1.isPresent()) {
					Users user = u1.get();		
		     		LocalDate lastmodified1 = user.getLastmodified();
				    Period period = Period.between(java.time.LocalDate.now(), lastmodified1);
				    int diff = period.getDays();
				    if (diff >= 1) {
					    user.setLock(false);
					    repository.save(user);
				    }			
		     		int numOA = user.getNumOfAttempts();
			    	if (user.isLock() == false) {
				     	if (numOA > 0) {
						   user.setLogin("Yes");
						   user.setKey(key1);
						   k.setKey(key1);
					        repository.save(user);
					        return k;
					} else {
						k.setKey(-1);
						return k;}
					
			    	} else {
					    k.setKey(-1);
				     	return k;}
				}
				
			  } else {
			    	Users uu = repository.loginbyEmail(email);
				    if (uu != null) {
		         	Optional<Users> u2=repository.findById(uu.getUserId());
					if (u2.isPresent()) {
					Users user1 = u2.get();
					LocalDate lastmodified1 = user1.getLastmodified();
					Period period = Period.between(java.time.LocalDate.now(), lastmodified1);
					int diff = period.getDays();
					if (diff >= 1) {
						user1.setLock(false);
						user1.setNumOfAttempts(3);
						repository.save(user1);
					}

					int numOA1 = user1.getNumOfAttempts();

					if (numOA1 != 0)
						numOA1 = numOA1 - 1;
					user1.setNumOfAttempts(numOA1);

					
					repository.save(user1);
					if (numOA1 == 0) {
						user1.setLock(true);
						user1.setLastmodified(java.time.LocalDate.now());
						k.setKey(-2);
						
						repository.save(user1);
						

						return k;
					}
					

					k.setKey(-3);
					return k;
				} else {
					k.setKey(-4);
					return k;
				}
			}
			}
		    return k;

		} catch (NullPointerException e) {
			k.setKey(-5);
			return k;
		}

	}
	

	@RequestMapping(value = "/getUser/{key}")
	public ArrayList<PermissionResult> getuser(@PathVariable("key") int key) {
		Iterable<Users> users = repository.findAll();
		for (Users user : users) {
			if (user.getKey() == key) {
				return repository.getPermissionsForUser(user.getUserId());

			}

		}
		return null;

	}
	
	@RequestMapping("/getUserIdByEmail/{email}")
	   public GetIdByEmail getUserIdByEmail(@PathVariable("email") String email) {
	       return repository.findByUserName(email);
       
	}

}
