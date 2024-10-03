package cz.landspa.statsapp.model.DTO.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamStats {
    Float ppPercentage;

    Float shPercentage;

    Float shotEfficiency;

    Float saveEfficiency;

    Float avgScored;

    Float avgConceded;

    public TeamStats(Long ppGoals, Long ppMajorGoals, Long ppNumber, Long shGoals, Long shMajorGoals, Long shNumber, Long totalShots, Long totalSaves, Long games, Long scoredGoals, Long concededGoals, Long concededGoalsNoEmpty){
        ppMajorGoals = 0L;
        shMajorGoals = 0L;

        if(totalShots == null){
            totalShots = 0L;
        }
        if(totalSaves == null){
            totalSaves = 0L;
        }


        if(ppMajorGoals + ppNumber >0){
            this.ppPercentage = (float) ppGoals / (ppMajorGoals + ppNumber);
        } else {
            this.ppPercentage = null;
        }

        if(shMajorGoals + shNumber >0){
            this.shPercentage = (float) (shNumber-shGoals) / (shMajorGoals + shNumber);
        } else {
            this.shPercentage = null;
        }

        if(totalShots >0 && totalShots != null){
            this.shotEfficiency = (float) scoredGoals/totalShots;
        } else {
            this.shotEfficiency = null;
        }

        if(totalSaves + concededGoalsNoEmpty >0 && totalSaves != null){
            this.saveEfficiency = (float) totalSaves/(totalSaves + concededGoalsNoEmpty);
        } else {
            this.saveEfficiency = null;
        }

        if(games >0){
            this.avgScored = (float) scoredGoals/games;
        } else {
            this.avgScored = null;
        }

        if(games >0){
            this.avgConceded = (float) concededGoals/games;
        } else {
            this.avgConceded = null;
        }
    }
}
