package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.DTO.userSetting.UserSettingDTO;
import cz.landspa.statsapp.model.UserSetting;
import org.springframework.stereotype.Service;

@Service
public interface UserSettingService {
    UserSettingDTO updateSetting(Long id, UserSetting userSetting);

    UserSettingDTO getSettingByUserId (Long id);
}
