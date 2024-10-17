package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.UserSetting;
import cz.landspa.statsapp.repository.UserSettingRepository;
import cz.landspa.statsapp.service.UserSettingService;
import org.springframework.stereotype.Service;

@Service
public class UserSettingServiceImpl implements UserSettingService {

    private final UserSettingRepository userSettingRepository;

    public UserSettingServiceImpl(UserSettingRepository userSettingRepository) {
        this.userSettingRepository = userSettingRepository;
    }

    @Override
    public UserSetting updateSetting(Long id, UserSetting userSetting) {
        if(userSettingRepository.findById(id).isPresent()) {
            UserSetting foundUserSetting = userSettingRepository.findById(id).get();

            foundUserSetting.setDefaultPeriods(userSetting.getDefaultPeriods());
            foundUserSetting.setDefaultPeriodLength(userSetting.getDefaultPeriodLength());

            return userSettingRepository.save(foundUserSetting);
        } else {
            return null;
        }
    }
}
