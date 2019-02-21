package com.Admin.AdminApp.web;

import java.math.BigInteger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.math.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.Admin.AdminApp.domain.*;

@RestController
public class RolesController {
	@Autowired
	private RoleRepository rolerepository;

	@Autowired
	private PermissionRepository perRepository;
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/permissions/{role_id}")
	ArrayList<PermissionResult> getperForRole(@PathVariable("role_id") long role_id) {
		return perRepository.getPermissionsForRole(role_id);
	}

	@RequestMapping("/rolesWithPermissions")
	public ArrayList<RoleResultWithPermissions> getRoles() {
		ArrayList<RoleResultWithPermissions> res = new ArrayList<RoleResultWithPermissions>();

		ArrayList<Role> roles = (ArrayList<Role>) rolerepository.findAll();
		for (Role role : roles) {
			ArrayList<PermissionResult> permissions = perRepository.getPermissionsForRole(role.getRoleId());
			BigInteger bi = BigInteger.valueOf(role.getRoleId());
			RoleResultWithPermissions r = new RoleResultWithPermissions(bi, role.getRole_name(), role.getDescription(),
					permissions);
			res.add(r);
		}
		return res;
	}

	@RequestMapping("/roles")
	public Set<RoleResult> getAllRoles() {
		return rolerepository.getAllRoles();
	}

	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public String adds(@RequestBody RoleResultWithPermissions rp) {
		Role role = new Role();
		role.setRole_name(rp.getRole_name());
		role.setDescription(rp.getDescription());
		Set<Permission> permissionSet = new HashSet<Permission>();
		Permission p;
		ArrayList<PermissionResult> permissionList = rp.getPermissions();
		for (PermissionResult permission1 : permissionList) {
			p = new Permission();
			p.setPerId(permission1.getPerId().longValue());
			p.setPer_name(permission1.getPer_name());
			permissionSet.add(p);
		}
		role.setPermissions(permissionSet);
		rolerepository.save(role);
		return "role added";
	}

	@RequestMapping(value = "/deleteRole/{roleId}", method = RequestMethod.DELETE)
	public String delete(@PathVariable long roleId) {
		try {

			ArrayList<Users> user = (ArrayList<Users>) userRepository.findAll();
			for (Users user1 : user) {
				Users u = new Users();
				Set<Role> role = user1.getRoles();
				for (Role role1 : role) {
					if (role1.getRoleId() == roleId) {

						role.remove(role1);
					}
				}
				u.setUserId(user1.getUserId());
				u.setEmail(user1.getEmail());
				u.setLogin(user1.getLogin());
				u.setName(user1.getName());
				u.setPassword(user1.getPassword());
				u.setPhone(user1.getPhone());
				u.setRoles(role);

			}
			rolerepository.deleteById(roleId);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "Role Deleted";
	}

	@RequestMapping(value = "/updateRole/{id}", method = RequestMethod.PUT)
	public String updateRole(@PathVariable("id") long id, @RequestBody Role role) {
		Optional<Role> uOp = rolerepository.findById(id);
		if (uOp.isPresent()) {

			Role role1 = uOp.get();
			role1.setRole_name(role.getRole_name());
			role1.setDescription(role.getDescription());
			role1.setPermissions(role.getPermissions());
			rolerepository.save(role1);
		} else
			return "There is no role with this id";

		return "OK";
	}
}
