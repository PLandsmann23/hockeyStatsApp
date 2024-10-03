async function loadData(){
    const response = await fetch("/api/teams");

    if(response.ok){
        let data = await response.json()

        let select = document.querySelector('#team');
        let options = teamsToSelect(data);

        for(let option of options){
            select.appendChild(option);
        }


    }

}


function teamsToSelect(teams) {
    let options = [];

    for (let team of teams) {
        let option = document.createElement('option');

        option.innerText = team.team.name;
        option.value = team.team.id;

        options.push(option); // Uložit vytvořené <option> do pole
    }

    return options; // Vrátí pole <option> elementů
}

async function getTeamStats(){
    let select = document.querySelector('#team');
    let teamName;

    if(select.value !== "") {
        let url = "/api/stats/" + select.value;

        for (let option of select.children) {
            if (option.value === select.value) {
                teamName = option.innerText;
                break;
            }
        }

        if (teamName != null) {
            document.querySelector(".stats-header h1").innerText = "Statistiky - " + teamName;
        }

        if (document.querySelector("#range").value === "last5") {
            url += "?range=last5"
        }

        if (select.value !== "") {
            const response = await fetch(url);

            if (response.ok) {
                const data = await response.json();

                teamStatsToGraph(data.team);
                parseStatsToTable(data);
            }


        }
    }

}

function teamStatsToGraph(data){
    let ppPerc = document.querySelector('#pp-eff .stats-value');
    let shPerc = document.querySelector('#sh-eff .stats-value');
    let shotPerc = document.querySelector('#shots-eff .stats-value');
    let savePerc = document.querySelector('#saves-eff .stats-value');
    let avgScored = document.querySelector('#avg-goals-scored');
    let avgConceded = document.querySelector('#avg-goals-conceded');

    let ppPercCircle = document.querySelector('#pp-eff .stats-circle');
    let shPercCircle = document.querySelector('#sh-eff .stats-circle');
    let shotPercCircle = document.querySelector('#shots-eff .stats-circle');
    let savePercCircle = document.querySelector('#saves-eff .stats-circle');
    let avgScoredLine = document.querySelector('.scored-bar');
    let avgConcededLine = document.querySelector('.conceded-bar');

    if(data.ppPercentage != null){
        ppPerc.innerText = (data.ppPercentage*100).toFixed(2)+" %";
        ppPercCircle.style.background = `conic-gradient(var(--violet) ${360 * data.ppPercentage}deg, var(--lgrey) 0deg)`;
    } else {
        ppPerc.innerText = "--";
    }
    if(data.shPercentage != null){
        shPerc.innerText = (data.shPercentage*100).toFixed(2)+" %";
        shPercCircle.style.background = `conic-gradient(var(--violet) ${360 * data.shPercentage}deg, var(--lgrey) 0deg)`;
    } else {
        shPerc.innerText = "--";
    }
    if(data.shotEfficiency != null){
        shotPerc.innerText = (data.shotEfficiency*100).toFixed(2)+" %";
        shotPercCircle.style.background = `conic-gradient(var(--violet) ${360 * data.shotEfficiency}deg, var(--lgrey) 0deg)`;
    } else {
        shotPerc.innerText = "--";
    }
    if(data.saveEfficiency != null){
        savePerc.innerText = (data.saveEfficiency*100).toFixed(2)+" %";
        savePercCircle.style.background = `conic-gradient(var(--violet) ${360 * data.saveEfficiency}deg, var(--lgrey) 0deg)`;
    } else {
        savePerc.innerText = "--";
    }
    if(data.avgScored != null){
        avgScored.innerText = data.avgScored.toFixed(2);
    } else {
        avgScored.innerText = "--"
    }
    if(data.avgConceded != null){
        avgConceded.innerText = data.avgConceded.toFixed(2);
    } else {
        avgConceded.innerText = "--"
    }

    if(data.avgScored != null && data.avgConceded != null && (data.avgScored+data.avgConceded)>0){
        avgScoredLine.style.width = (data.avgScored/(data.avgScored+data.avgConceded)*100)+"%";
        avgConcededLine.style.width = (data.avgConceded/(data.avgScored+data.avgConceded)*100)+"%";

    } else {

    }


}

function parseStatsToTable(data){
    document.querySelector(".stats-table").style.display = "block";
    let goalies = data.goalkeepers;
    let players = data.players;
    let gkbody = document.querySelector("#goalie-stats-body");
    let playerbody = document.querySelector("#player-stats-body");

    gkbody.innerHTML = "";
    playerbody.innerHTML = "";

    goalies.forEach(gk =>{
        let row = document.createElement('tr');

        let numberCell = document.createElement('td');
        numberCell.textContent = gk.player.number;
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.textContent = gk.player.name+" "+gk.player.surname;
        row.appendChild(nameCell);

        let gamesPlayed = document.createElement('td');
        gamesPlayed.textContent = gk.gamesPlayed;
        row.appendChild(gamesPlayed);

        let goalsConceded = document.createElement('td');
        goalsConceded.textContent = gk.goalsConceded;
        row.appendChild(goalsConceded);

        let saves = document.createElement('td');
        saves.textContent = gk.saves;
        row.appendChild(saves);



        let avgGoals = document.createElement('td');
        avgGoals.textContent = gk.avgGoals!=null? gk.avgGoals.toFixed(2) : "--";
        row.appendChild(avgGoals);


        let percentage = document.createElement('td');
        percentage.textContent = gk.savePercentage!=null? (gk.savePercentage*100).toFixed(2) : "--";
        row.appendChild(percentage);



        gkbody.appendChild(row);
    });




    players.forEach(player =>{

        let row = document.createElement('tr');


        let numberCell = document.createElement('td');
        numberCell.textContent = player.player.number;
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.textContent = player.player.name+" "+player.player.surname;
        row.appendChild(nameCell);

        let gp = document.createElement('td');
        gp.textContent = player.gamesPlayed;
        row.appendChild(gp);

        let goals = document.createElement('td');
        goals.textContent = player.goals;
        row.appendChild(goals);

        let assists = document.createElement('td');
        assists.textContent = player.assists;
        row.appendChild(assists);

        let points = document.createElement('td');
        points.textContent = player.goals + player.assists;
        row.appendChild(points);

        let plusMinus = document.createElement('td');
        let pm = (player.plusMinus)
        plusMinus.textContent = pm.toString();
        row.appendChild(plusMinus);

        let penalty = document.createElement('td');
        penalty.textContent = player.penaltyMinutes?? "0";
        row.appendChild(penalty);

        playerbody.appendChild(row);

    });

}

document.addEventListener("DOMContentLoaded", loadData);