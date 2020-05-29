package com.application.instagramm.mappers;

import com.application.instagramm.dto.AppUserDTO;
import com.application.instagramm.user.AppUser;
import org.springframework.stereotype.Service;

@Service
public class AppUserMapperImpl implements AppUserMapper {
    @Override
    public AppUserDTO appUserToAppUserDTO(AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO(
                appUser.getId(),
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail());
        return appUserDTO;
    }
}
