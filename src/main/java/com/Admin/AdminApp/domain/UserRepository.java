package com.Admin.AdminApp.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
//import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;



public interface UserRepository extends CrudRepository <Users, Long> {
//
//	@Query(nativeQuery=true)
//    Set<UsersOnHP> getUsersOnHP();
//	@Query(nativeQuery=true)
//    Set<UserRoleArray> getUsersRoleArray(@Param("UserId") long UserId);
//	@Query(nativeQuery=true)
//    Set<UsersWithRole> getUsersWithRole();
	@Query(nativeQuery=true)
	Users loginAll(@Param("Email") String email,@Param("Password") String password);
	
	@Query(nativeQuery=true)
	Users loginbyEmail(@Param("Email") String email);

	@Query(nativeQuery = true)
	GetIdByEmail findByUserName(String userName);
	
	
	@Query(nativeQuery=true)
    ArrayList<PermissionResult> getPermissionsForUser(@Param("user_id") long user_id);
}
