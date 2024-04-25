package com.fdm.barbieBank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResetPasswordController {

	@GetMapping("/resetPassword")
	public String goToResetPasswordPage() {

		return "resetPassword";

	}
	
}
