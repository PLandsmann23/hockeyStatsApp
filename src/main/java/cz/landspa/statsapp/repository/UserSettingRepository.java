package cz.landspa.statsapp.repository;

import cz.landspa.statsapp.model.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {

}
