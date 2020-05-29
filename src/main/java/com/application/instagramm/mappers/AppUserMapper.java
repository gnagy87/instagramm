package com.application.instagramm.mappers;


import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.user.AppUser;
import org.springframework.stereotype.Service;

@Service
public interface AppUserMapper {
    AppUserDTO appUserToAppUserDTO(AppUser appUser);
}
