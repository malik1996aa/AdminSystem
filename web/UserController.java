package com.Admin.AdminApp.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
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
import com.Admin.AdminApp.domain.LogIn;
import com.Admin.AdminApp.domain.Permission;
import com.Admin.AdminApp.domain.PermissionRepository;
import com.Admin.AdminApp.domain.PermissionResult;
import com.Admin.AdminApp.domain.Role;
import com.Admin.AdminApp.domain.RoleRepository;
import com.Admin.AdminApp.domain.RoleResult;
import com.Admin.AdminApp.domain.RolesOnHP;
import com.Admin.AdminApp.domain.UserRepository;
import com.Admin.AdminApp.domain.UserResult;
import com.Admin.AdminApp.domain.UserRoleArray;
import com.Admin.AdminApp.domain.Users;
import com.Admin.AdminApp.domain.UsersOnHP;
import com.Admin.AdminApp.domain.UsersRoleArray;
import com.Admin.AdminApp.domain.UsersWithRole;





@RestController
public class UserController {
	@Autowired 
	private UserRepository repository;
	@Autowired 
	private PermissionRepository prepository;
	@Autowired 
	private RoleRepository rolerepository;
//
//	  @RequestMapping("/usersonhp")
//	  public Set<UsersOnHP> getUsersOnHP() 
//	  {
//         return repository.getUsersOnHP();
//      }
      @RequestMapping("/users1")
      public Iterable<Users> getUsers2() {
        return repository.findAll();
      }
//      
//      @RequestMapping("/userrole/{id}")
//      public Set<UserRoleArray> getUsersRoleArray(@PathVariable long id){
//    	  return repository.getUsersRoleArray(id);
//      }
//      
//      @RequestMapping("/userswithrole")
//      public Set<UsersWithRole> getUsersWithRole() {
//        return repository.getUsersWithRole();
//      }
//      /////////////////////////////////////////////////////////////////////////////////////////
      @RequestMapping("/users")
      public ArrayList<UserResult> getUsers()
      {
    	 ArrayList<UserResult> res = new ArrayList<UserResult>();
    	 
    	 ArrayList<Users> users = (ArrayList<Users>) repository.findAll();
    	 for(Users user : users)
    	 {
    		 ArrayList<RoleResult> roles = rolerepository.getRoles(user.getUserId());
   		    UserResult u = new UserResult(user.getUserId(), user.getEmail(), user.getName(), user.getPassword(), user.getPhone(), user.getLogin(),roles);
    		 res.add(u);
    	 }
    	 return res;
      }
      
      @RequestMapping("/roles/{user_id}")
      ArrayList<RoleResult> getRoles(@PathVariable("user_id") long user_id)
      {
    	  return rolerepository.getRoles(user_id);
    	 
      }
      @RequestMapping("/permissions/{user_id}")
      ArrayList<PermissionResult> getPermissionsForUser(@PathVariable("user_id") long user_id)
      {
    	  return repository.getPermissionsForUser(user_id);
    	 
      }
      
//      @RequestMapping(value = "/addUser", method = RequestMethod.POST)
//      public void adds(@RequestBody Users user) {
//    	  
//      repository.save(user);
//      }
      @RequestMapping(value = "/addUser", method = RequestMethod.POST)
      public String adds(@RequestBody UserResult u) {
    	  Users user=new Users();
//    	  long currentID = Users.getCounter();
//    	  currentID = currentID + 1;
    	  user.setUserId(u.getId());
    	  user.setName(u.getName());
    	  user.setPhone(u.getPhone());
    	  user.setEmail(u.getEmail());
    	  user.setPassword(u.getPassword());
    	  user.setLogin("yes");
    	  Set<Role> roleSet=new HashSet<Role>();
    	  Role r;
    	  ArrayList<RoleResult> roleList=u.getRoles();
    	  for(RoleResult role1 : roleList)
    	  {
    		  r=new Role();
    		  r.setRole_name(role1.getRole_name());
    		  r.setDescription(role1.getDescription());
    		  r.setRoleId(role1.getRoleId());   
    		  roleSet.add(r);
    	  }
    	user.setRoles(roleSet);
    	repository.save(user);
    	return "user added";
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
      @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
      public String delete(@PathVariable long id) {
    	  try {
    		  repository.deleteById(id);
//    		  long currentID = Users.getCounter();
//        	  currentID = currentID - 1;
//        	  Users.setCounter(currentID);
    	  }
    	  catch(Exception e) {
    		  return e.getMessage(); }
    	  return "User Deleted";
      }
      
      
    @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable("id") Long id, @RequestBody UserResult user) {
       Optional<Users> uOp= repository.findById(id); 
       if(uOp.isPresent())
       {
      	 
      	 Users user1=uOp.get();
      	  Set<Role> roleSet=new HashSet<Role>();
    	  Role r;
    	  ArrayList<RoleResult> roleList=user.getRoles();
    	  for(RoleResult role1 : roleList)
    	  {
    		  r=new Role();
    		  r.setRole_name(role1.getRole_name());
    		  r.setDescription(role1.getDescription());
    		  r.setRoleId(role1.getRoleId());   
    		  roleSet.add(r);
    	  }
       	 user1.setRoles(roleSet);
      	 user1.setName(user.getName());
      	 user1.setEmail(user.getEmail());
      	 user1.setPhone(user.getPhone());
      	 user1.setPassword(user.getPassword());
      	 user1.setLogin(user.getLogin());
      	 repository.save(user1);
       }
       else
      	 return "There is no user with this id";
    
       return "OK";
    }


    @RequestMapping(value = "/login",method = RequestMethod.PUT)
    public String login(@RequestBody LogIn lg) {

    	String email=lg.getEmail();
    	String pass=lg.getPassword();
    	try {
    		Optional<Users> user= repository.loginAll(email, pass);
    		if(user.isPresent()) {
    			LocalDate lastmodified1 = user.get().getLastmodified();
    			Period period = Period.between(java.time.LocalDate.now(), lastmodified1);
    			int diff = period.getDays();
    			if(diff>=1) {
    				user.get().setLock(false);
    			}
    			int numOA=user.get().getNumOfAttempts();
    			if(user.get().isLock() == false) {
    				if (numOA > 0 ) {
    					return "Log in success";
    				}else{
    					return "user is Lock";
    				}
    			}
    			else {
    				return "user is Lock";
    			}
    		}
    		else{  
    			Optional<Users> user1= repository.loginbyEmail(email);
    			if(user1.isPresent()) {
    				//Users user2=user1.get();
    				int numOA1=user1.get().getNumOfAttempts();
    				numOA1=numOA1-1;
    				user1.get().setNumOfAttempts(1);
    				//repository.save(user1);
    				if(numOA1==0){
    					user1.get().setLock(true);
    					user1.get().setLastmodified(java.time.LocalDate.now());
    					return "Maximum number of attempts exceeded";
    				}

    				return "Invalid passwoerd";
    			}else {
    				return "Invalid user";
    			}
    		}
    	}catch(NullPointerException e){
    		return e.getMessage();
    	}
    }
}