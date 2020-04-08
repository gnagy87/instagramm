package com.application.instagramm.utils;

import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {

	@Override
	public boolean nameValidator(String name) {
		return name.matches("[a-zA-Z]{2,32}");
	}

	@Override
	public boolean usernameValidator(String username) {
		return username.matches("[a-zA-Z0-9_.-]{4,16}");
	}

	@Override
	public boolean passwordValidator(String password) {
		return password.matches("[-a-zA-Z0-9!?._+]{6,16}");
	}

	@Override
	public boolean emailValidator(String email) {
		return email.matches("[a-z0-9._-]{3,32}@[a-z0-9._-]{2,20}.[a-z]{2,3}");
	}

}
