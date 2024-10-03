async function showStats(){
    try{
        const response = await fetch(API_URL+"games/"+gameId+"/stats");

        if(response.ok){

            parseStatsToTable(await response.json());

            openModal("player-stats");
        }

    } catch (e){
        showMessage("Chyba"+e,"error")
    }

}

function parseStatsToTable(data){
    let goalies = data.goalkeepers;
    let players = data.players;
    let gkbody = document.querySelector("#goalie-stats-body");
    let playerbody = document.querySelector("#player-stats-body");

    gkbody.innerHTML = "";
    playerbody.innerHTML = "";

    goalies.forEach(gk =>{
       let row = document.createElement('tr');

       let numberCell = document.createElement('td');
       numberCell.textContent = gk.player.gameNumber;
       row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.textContent = gk.player.player.name+" "+gk.player.player.surname;
        row.appendChild(nameCell);

        let saves1 = document.createElement('td');
        saves1.textContent = gk.saves1st;
        row.appendChild(saves1);

        let saves2 = document.createElement('td');
        saves2.textContent = gk.saves2nd;
        row.appendChild(saves2);

        let saves3 = document.createElement('td');
        saves3.textContent = gk.saves3rd;
        row.appendChild(saves3);

        let savesOT = document.createElement('td');
        savesOT.textContent = gk.savesOT;
        row.appendChild(savesOT);

        let saves = document.createElement('td');
        saves.textContent = gk.saves;
        row.appendChild(saves);

        let goals = document.createElement('td');
        goals.textContent = gk.goals;
        row.appendChild(goals);

        let percentage;

        if(gk.percentage!=null) {
             percentage = gk.percentage.toFixed(2);
        } else {
             percentage = "--"
        }

        let pct = document.createElement('td');
        pct.textContent = percentage;
        row.appendChild(pct);

        gkbody.appendChild(row);
    });

    players.forEach(player =>{

        let row = document.createElement('tr');


        let numberCell = document.createElement('td');
        numberCell.textContent = player.player.gameNumber;
        row.appendChild(numberCell);

        let nameCell = document.createElement('td');
        nameCell.textContent = player.player.player.name+" "+player.player.player.surname;
        row.appendChild(nameCell);

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
        let pm = (player.plus - player.minus)
        plusMinus.textContent = pm.toString();
        row.appendChild(plusMinus);

        let penalty = document.createElement('td');
        penalty.textContent = player.penaltyMinutes?? "0";
        row.appendChild(penalty);

        playerbody.appendChild(row);

    });

}