package com.poly.controller.api.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.LoginDTO;
import com.poly.dto.ResponseDTO;
import com.poly.services.UserService;
import com.poly.utilites.SessionUtilities;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	UserService userService;

	@PostMapping("/login")
	public ResponseDTO login(@RequestBody LoginDTO info) {
		if(this.userService.adminLogin(info)) {
			return new ResponseDTO("Thành công", SessionUtilities.getAdmin());
		}
		return new ResponseDTO("Thông tin đăng nhập không hợp lệ", null);
	}

	@GetMapping("/logout")
	public ResponseDTO logout() {
		this.userService.adminLogout();
		return new ResponseDTO("Đăng xuất thành công",null);
	}

}
