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
    let gkhead = document.querySelector('#goalie-stats-head');
    let periods = Number(localStorage.getItem("periods"));

    gkhead.innerHTML = "";

    let hrow1 = document.createElement('tr');
    let hrow2 = document.createElement('tr');

    hrow1.innerHTML = `<tr>
                <th></th>
                <th></th>
                <th colspan="${periods+1}">Zásahy</th>
                <th></th>
                <th></th>
            </tr>`;

    let num = document.createElement('th');
    num.textContent = "Číslo";
    hrow2.appendChild(num);

    let name = document.createElement('th');
    name.textContent = "Jméno";
    hrow2.appendChild(name);



    for(let i = 1; i<=periods; i++){
        let per = document.createElement('th');
        per.textContent = i;
        hrow2.appendChild(per);
    }


    let ot = document.createElement('th');
    ot.textContent = "PR";
    hrow2.appendChild(ot);
    let total = document.createElement('th');
    total.textContent = "Celkem";
    hrow2.appendChild(total);
    let ga = document.createElement('th');
    ga.textContent = "OG";
    hrow2.appendChild(ga);
    let perc = document.createElement('th');
    perc.textContent = "%";
    hrow2.appendChild(perc);


    gkhead.appendChild(hrow1);
    gkhead.appendChild(hrow2);

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

        for(let j = 0; j<=periods+1; j++){
            let periodSaves = document.createElement('td');
            let saves = gk.periodSaves.find((saves)=>saves.period === j+1);
            if(saves!=null){
                periodSaves.textContent = saves.saves;
            }
                row.appendChild(periodSaves);
        }


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