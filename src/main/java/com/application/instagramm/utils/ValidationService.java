package com.application.instagramm.utils;

public interface ValidationService {
	boolean nameValidator(String name);

	boolean usernameValidator(String username);

	boolean passwordValidator(String password);

	boolean emailValidator(String email);
}
