const gameId = window.location.pathname.split('/')[2];

document.addEventListener("DOMContentLoaded", loadData);

async function loadData(){

   await fetch(API_URL +"games/"+gameId)
        .then(response => response.json())
        .then(async (data) => {

            parseEvents(data.events);

        });

}




function parseEvents(events){
    let table = document.querySelector(".game-events table");

    table.innerHTML = "";

    let periodLength = Number(localStorage.getItem("periodLength"));

    for(let i = 0; i<= Number(localStorage.getItem("periods")); i++){
        let head = document.createElement('thead');
        let b = document.createElement('tbody');
        let r = document.createElement('tr');
        let h = document.createElement('th');
        if(i<Number(localStorage.getItem("periods"))){
            h.textContent = (i + 1) + "." + getPartName();
        } else {
            h.textContent = "Prodloužení";
        }

        r.appendChild(h);
        head.appendChild(r);

        table.appendChild(head);
        table.appendChild(b);
    }

    let bodies = document.querySelectorAll(".game-events tbody");


    for(let p = 0; p < bodies.length; p++){


        let periodEvents = Array();

        for(let ev of events){
            if(ev.time > p*periodLength*60 && ev.time <= (p+1)*periodLength*60 && p<Number(localStorage.getItem("periods"))){
                periodEvents.push(ev);
            } else if (ev.time > p*periodLength*60 && p===Number(localStorage.getItem("periods"))){
                periodEvents.push(ev);
            }
        }

        for(let event of periodEvents){
            let row;
            if(event.type === "GoalScored"){
                row = scoredGoalToRow(event);
            }
            if(event.type === "GoalConceded"){
                row = concededGoalToRow(event);
            }
            if(event.type === "Penalty"){
                row = penaltyToRow(event);
            }
            if(event.type === "OpponentPenalty"){
                row = opponentPenaltyToRow(event);
            }
            bodies[p].appendChild(row);
        }

    }
}




function scoredGoalToRow(goal){
    let onIceNumbers =[];
    let assists = [];
    let i = 0;
    for(let assist of goal.assists){
        assists[i] = assist.gameNumber;
        i++;
    }
    i=0;
    for(let onIcePlayer of goal.onIce){
        onIceNumbers[i] = onIcePlayer.gameNumber;
        i++;
    }

    // Vytvoření hlavní tr struktury
    const tr = document.createElement('tr');
    tr.classList.add('goal', 'scored');
    tr.dataset.id = goal.id;
    tr.onclick = () =>openEditScoredGoal(goal.id);

    // První buňka s časem
    const timeTd = document.createElement('td');
    timeTd.textContent = secondsToMinutes(goal.time);

    // Druhá buňka s typem události
    const eventTd = document.createElement('td');
    eventTd.textContent = 'Vstřelený gól';




    const players = document.createElement('td');
    players.innerHTML = `G: &nbsp;${goal.scorer?.gameNumber || '--'} &nbsp;|&nbsp; A:&nbsp; ${assists ? assists.join(', ') : '--'} &nbsp;|&nbsp; +:&nbsp; ${onIceNumbers.join(', ')}`;



    const situationTd = document.createElement('td');
    situationTd.textContent = goal.situation;

    tr.appendChild(timeTd);
    tr.appendChild(eventTd);
    tr.appendChild(players);
    tr.appendChild(situationTd);

    return tr;
}

function concededGoalToRow(goal){
    let onIceNumbers =[];
    let i = 0;
    for(let onIcePlayer of goal.onIce){
        onIceNumbers[i] = onIcePlayer.gameNumber;
        i++;
    }

    // Vytvoření hlavní tr struktury
    const tr = document.createElement('tr');
    tr.classList.add('goal', 'conceded');
    tr.dataset.id = goal.id;
    tr.onclick = () =>openEditConcededGoal(goal.id);

    // První buňka s časem
    const timeTd = document.createElement('td');
    timeTd.textContent = secondsToMinutes(goal.time);

    // Druhá buňka s typem události
    const eventTd = document.createElement('td');
    eventTd.textContent = 'Obdržený gól';

    // Třetí buňka s vnořenou tabulkou
    const tableTd = document.createElement('td');
    const innerTable = document.createElement('table');
    const tbody = document.createElement('tbody');

    // První vnořený řádek s informacemi o střelci a asistentech
    const InnerTr = document.createElement('tr');

    const onIceTd = document.createElement('td');
    onIceTd.textContent = `-: ${onIceNumbers.join(', ')}`;

    const situationTd = document.createElement('td');
    situationTd.textContent = goal.situation;




    // Přidání všech buněk do hlavního řádku
    tr.appendChild(timeTd);
    tr.appendChild(eventTd);
    tr.appendChild(onIceTd);
    tr.appendChild(situationTd);

    return tr;
}

function penaltyToRow(penalty){
    const tr = document.createElement('tr');
    tr.classList.add('penalty');
    tr.dataset.id = penalty.id;
    tr.onclick = () =>openEditPenalty(penalty.id);

    // První buňka s časem
    const timeTd = document.createElement('td');
    timeTd.textContent = secondsToMinutes(penalty.time);

    // Druhá buňka s typem události
    const eventTd = document.createElement('td');
    eventTd.textContent = 'Vyloučení';

    // Třetí buňka s vnořenou tabulkou
    const tableTd = document.createElement('td');
    const innerTable = document.createElement('table');
    const tbody = document.createElement('tbody');

    // Vytvoření vnořeného řádku s informacemi o vyloučeném hráči
    const innerTr = document.createElement('tr');

    const playerTd = document.createElement('td');
    playerTd.textContent = `#${penalty.player?.gameNumber || '--'} (${penalty.player?.player.surname || '--'})`;

    const minutesTd = document.createElement('td');
    minutesTd.textContent = `${penalty.minutes} min`;

    const reasonTd = document.createElement('td');
    reasonTd.textContent = penalty?.reason || "---";

    // Přidání buněk do vnořeného řádku
    innerTr.appendChild(playerTd);
    innerTr.appendChild(minutesTd);
    innerTr.appendChild(reasonTd);

    // Sestavení vnořené tabulky
    tbody.appendChild(innerTr);
    innerTable.appendChild(tbody);
    tableTd.appendChild(innerTable);
    tableTd.setAttribute('colspan', '2');

    // Přidání všech buněk do hlavního řádku
    tr.appendChild(timeTd);
    tr.appendChild(eventTd);
    tr.appendChild(tableTd);

    return tr;
}

function opponentPenaltyToRow(penalty) {
    const tr = document.createElement('tr');
    tr.classList.add('penalty', 'opponent');
    tr.dataset.id = penalty.id;
    tr.onclick = () => openEditOpponentPenalty(penalty.id);

    // První buňka s časem
    const timeTd = document.createElement('td');
    timeTd.textContent = secondsToMinutes(penalty.time);

    // Druhá buňka s typem události
    const eventTd = document.createElement('td');
    eventTd.textContent = 'Vyloučení soupeře';

    // Třetí buňka s vnořenou tabulkou
    const tableTd = document.createElement('td');
    const innerTable = document.createElement('table');
    const tbody = document.createElement('tbody');

    // Vytvoření vnořeného řádku s informacemi o vyloučeném hráči
    const innerTr = document.createElement('tr');


    const minutesTd = document.createElement('td');
    minutesTd.textContent = `${penalty.minutes} min`;

    // Přidání buněk do vnořeného řádku
    innerTr.appendChild(minutesTd);

    // Sestavení vnořené tabulky
    tbody.appendChild(innerTr);
    innerTable.appendChild(tbody);
    tableTd.appendChild(innerTable);
    tableTd.setAttribute('colspan', '2');


    // Přidání všech buněk do hlavního řádku
    tr.appendChild(timeTd);
    tr.appendChild(eventTd);
    tr.appendChild(tableTd);

    return tr;
}


