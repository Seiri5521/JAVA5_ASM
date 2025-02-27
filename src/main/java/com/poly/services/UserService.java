package com.poly.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.poly.dto.ChangePasswordDTO;
import com.poly.dto.LoginDTO;
import com.poly.dto.RegisterDTO;
import com.poly.dto.UpdateUserDTO;
import com.poly.dto.UserDTO;
import com.poly.model.User;

public interface UserService {
	public Page<UserDTO> findAllUser(String sdt,String email,String ho_ten, Pageable pageable);
	public User findUserById(Long id);
	public User findUserByUsername(String username);
	public boolean saveUser(User user);
	public boolean login(LoginDTO user);
	public boolean register(RegisterDTO user);
	public boolean updateUser(UpdateUserDTO updateUserDTO);
	public boolean deleteUserById(Long id);
	public boolean checkLogin();
	public boolean changePassword(ChangePasswordDTO changePasswordDTO);
	public boolean updateUserByAdmin(UpdateUserDTO updateUserDTO,Long id);
	public boolean adminLogin(LoginDTO user);
	public boolean checkAdminLogin();
	public void adminLogout();
	public boolean resetPass(Long id);
}
