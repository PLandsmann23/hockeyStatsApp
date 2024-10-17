package cz.landspa.statsapp.service;

import cz.landspa.statsapp.model.UserSetting;
import org.springframework.stereotype.Service;

@Service
public interface UserSettingService {
    UserSetting updateSetting(Long id, UserSetting userSetting);

}
