package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.model.DTO.userSetting.UserSettingDTO;
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
    public UserSettingDTO updateSetting(Long id, UserSetting userSetting) {
        if(userSettingRepository.findById(id).isPresent()) {
            UserSetting foundUserSetting = userSettingRepository.findById(id).get();

            foundUserSetting.setDefaultPeriods(userSetting.getDefaultPeriods());
            foundUserSetting.setDefaultPeriodLength(userSetting.getDefaultPeriodLength());

            return UserSettingDTO.fromUserSetting(userSettingRepository.save(foundUserSetting));
        } else {
            return null;
        }
    }

    @Override
    public UserSettingDTO getSettingByUserId(Long id) {
        return UserSettingDTO.fromUserSetting(userSettingRepository.findById(id).orElse(null));
    }
}
