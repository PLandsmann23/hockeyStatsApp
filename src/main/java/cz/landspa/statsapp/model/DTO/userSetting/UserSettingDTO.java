package cz.landspa.statsapp.model.DTO.userSetting;


import cz.landspa.statsapp.model.UserSetting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserSettingDTO {
    Integer defaultPeriodLength;
    Integer defaultPeriods;


    public UserSettingDTO(Integer defaultPeriodLength, Integer defaultPeriods) {
        this.defaultPeriodLength = defaultPeriodLength;
        this.defaultPeriods = defaultPeriods;
    }

    public static UserSettingDTO fromUserSetting (UserSetting userSetting){
        if(userSetting!=null){
            return new UserSettingDTO(
                    userSetting.getDefaultPeriodLength(),
                    userSetting.getDefaultPeriods()
            );
        } else {
            return null;
        }
    }
}
