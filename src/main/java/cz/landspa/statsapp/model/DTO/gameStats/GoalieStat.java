package cz.landspa.statsapp.model.DTO.gameStats;


import cz.landspa.statsapp.model.DTO.saves.PeriodSaves;
import cz.landspa.statsapp.model.Roster;
import cz.landspa.statsapp.model.Saves;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoalieStat {
    private Roster player;
    private Long saves;
    private List<PeriodSaves> periodSaves = new ArrayList<>();
    private Long saves1st;
    private Long saves2nd;
    private Long saves3rd;
    private Long savesOT;
    private Long goals;
    private Float percentage;


    public GoalieStat(Roster player, Long saves, Long saves1st, Long saves2nd, Long saves3rd, Long savesOT, Long goals) {
        this.player = player;
        this.saves = saves;
        this.saves1st = saves1st;
        this.saves2nd = saves2nd;
        this.saves3rd = saves3rd;
        this.savesOT = savesOT;
        this.goals = goals;
        if(this.saves !=null){
            if(this.saves + this.goals != 0){
                this.percentage = ((float) this.saves / (this.saves+this.goals)) *100;
            } else {
                this.percentage = null;
            }
        } else {
            this.percentage = null;
        }
    }
}
