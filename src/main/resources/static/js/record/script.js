const gameId = window.location.pathname.split('/')[2];

document.addEventListener("DOMContentLoaded", loadData);

async function loadData(){
    await fetch(API_URL +"games/"+gameId+"/roster")
        .then(response => response.json())
        .then((data)=>{
            localStorage.setItem("roster",JSON.stringify(data.roster));

        });
   await fetch(API_URL +"games/"+gameId)
        .then(response => response.json())
        .then(async (data) => {
            localStorage.setItem("periods", data.game.periods);
            localStorage.setItem("periodLength", data.game.periodLength);
            parseGameData(data);
            parseEvents(data.events);
            shotsToInput(data.shots);
            let activeGoalie = checkForActiveGoalie(JSON.parse(localStorage.getItem("roster")));
            if (activeGoalie) {

            } else {
                const goalies = JSON.parse(localStorage.getItem("roster")).filter(record => record.player.position === "GK");
                if(goalies.length ===0){
                    // nedělat nic
                }
                if(goalies.length ===1){
                    // označit golmana jako active
                    await markAsActive(goalies[0]);
                    return;
                }
                if(goalies.length ===2){
                    // zeptat se který bude active
                    openSelectGoalies(goalies);
                }
            }

            await parseSaves(activeGoalie);
        });

}


function parseGameData(data){
    const table = document.querySelector(".game-header table tbody");


    let game = data.game;

    localStorage.setItem("currentPeriod", game.currentPeriod);


    table.innerHTML = "";

    let row = document.createElement("tr");

    let teamsCell = document.createElement("td");
    teamsCell.innerHTML = `${game.team.name} &nbsp;X&nbsp;  ${game.opponent}`;
    row.appendChild(teamsCell);

    let dateTimeCell = document.createElement("td");
    dateTimeCell.textContent = `${game.date ? dateToString(game.date):""}, ${game.time? game.time.substring(0, game.time.length-3):""}`;
    row.appendChild(dateTimeCell);


    let scoreCell = document.createElement("td");
    if(game.currentPeriod === 1) {
        scoreCell.textContent = `${data.goalsScored}:${data.goalsConceded} (${data.goals[0].team}:${data.goals[0].opponent} , -:-, -:-)`;
    }
    if(game.currentPeriod === 2) {
        scoreCell.textContent = `${data.goalsScored}:${data.goalsConceded} (${data.goals[0].team}:${data.goals[0].opponent} , ${data.goals[1].team}:${data.goals[1].opponent}, -:-)`;
    }
    if(game.currentPeriod >= 3) {
        scoreCell.textContent = `${data.goalsScored}:${data.goalsConceded} (${data.goals[0].team}:${data.goals[0].opponent} , ${data.goals[1].team}:${data.goals[1].opponent}, ${data.goals[2].team}:${data.goals[2].opponent})`;
    }
    row.appendChild(scoreCell);

    let periodCell = document.createElement("td");
    periodCell.textContent = `${game.currentPeriod<=localStorage.getItem("periods")?game.currentPeriod+'. '+getPartName():'Prodloužení'}`;
    row.appendChild(periodCell)

    table.appendChild(row);
}


function parseEvents(events){

    let table = document.querySelector(".gm-center table");

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

    let bodies = document.querySelectorAll(".gm-center tbody");


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

async function changeGoalkeeper(){
    let roster = JSON.parse(localStorage.getItem("roster"));
    const goalies = roster.filter(record => record.player.position === "GK");

    if(goalies.length === 2){
        await fetch(API_URL+"games/"+gameId+"/goalkeeperChange", {method: "POST"})
            .then(response => response.json())
            .then(data =>{})
        await loadData()
    } else {
        showMessage("Na soupisce se nachází pouze jeden brankář","error")
    }
}

async function parseSaves(activeGoalie){
    let period = localStorage.getItem("currentPeriod");
    try {
        const response = await fetch(API_URL + "games/" + gameId + "/saves/" + activeGoalie.id + "/period/" + period)
        if(response.ok){
            savesToInput(await response.json());
        }

    }catch {

    }
}

function showRoster(inputId, modalId) {
    const modal = document.getElementById(modalId); // Získá správný modal podle ID
    const roster = modal.querySelector('.roster'); // Najde soupisku pouze v daném modalu
    roster.innerHTML = "";
    for(let button of rosterToButtons()){
        roster.appendChild(button);
    }
    roster.classList.add('visible'); // Zobrazí soupisku
}

// Funkce pro skrytí soupisky
function hideRoster(modalId) {
    const modal = document.getElementById(modalId); // Získá správný modal podle ID
    const roster = modal.querySelector('.roster'); // Najde soupisku pouze v daném modalu
    roster.classList.remove('visible'); // Skryje soupisku
    const inputs = modal.querySelectorAll('input');
    inputs.forEach(input => {
        input.style.border = ''; // Vrátí border na původní hodnotu
    });
}

// Přidání události pro zobrazení soupisky při kliknutí na input
document.querySelectorAll('input[type=number]').forEach(input => {
    input.addEventListener('focus', (event) => {
        const modal = input.closest('.modal'); // Najde správný modal, kde input je
   //     showRoster(event.target.id, modal.id); // Zobrazí soupisku pouze pro daný modal
    });

});

document.addEventListener('click', (event) => {
    document.querySelectorAll('.modal').forEach(modal => {
        const roster = modal.querySelector('.roster');

        // Zkontroluj, zda event.target je uvnitř rosteru nebo je input element
        const isInsideRoster = event.target.closest('.roster');
        const isInput = event.target.type === 'number' && event.target.tagName === 'input';

        // Pokud klik není uvnitř rosteru ani na inputu, skryj roster
        if (!isInsideRoster && !isInput) {
            hideRoster(modal.id); // Skryje soupisku pro daný modal
        }
    });
});


let activeInput = null;  // Uchovává aktuálně vybraný input

document.querySelectorAll('input[type=number]').forEach(input => {
    input.addEventListener('click', (event) => {
        // Pokud existuje aktivní input, resetuj jeho border a background
        if (activeInput && activeInput !== event.target) {
            activeInput.style.border = "";  // Odstraní border u předchozího inputu
            activeInput.style.background = "";  // Resetuje background u předchozího inputu
        }

        // Nastav nový aktivní input a zvýrazni jeho border a background
        activeInput = event.target;
        activeInput.style.border = "2px solid var(--violet)";  // Změní border aktuálního inputu
        activeInput.style.background = "var(--lblue)";  // Změní background aktuálního inputu
    });
});

// Při kliknutí mimo input
document.addEventListener('click', (event) => {
    if (activeInput && !event.target.closest('input[type=number]')) {
        activeInput.style.border = "";  // Resetuje border, pokud klikneš mimo input
        activeInput.style.background = "";  // Resetuje background, pokud klikneš mimo input
        activeInput = null;
    }
});


function playerToInput(button)  {
    if (activeInput) {
        const number = button.dataset.number;  // Předpokládáme, že číslo hráče je uloženo v data-number
        const id = button.dataset.id;  // Předpokládáme, že ID hráče je uloženo v data-id

        let playerExists = false;

        const div = activeInput.closest("div");
        div.querySelectorAll("input[type= number]").forEach(input =>{
            if(input.value ===number){
                showMessage("Zadaný hráč už se v záznamu nachází","error");
                playerExists = true;
                return;
            }
        });

        if(playerExists){
            hideRoster(button.closest(".modal").id);
            return;
        }

        activeInput.value = number;  // Nastav číslo hráče do aktivního inputu

        // Najdi skrytý input s odpovídajícím ID
        const hiddenInputId = activeInput.id + '-id';
        const hiddenInput = document.getElementById(hiddenInputId);
        if (hiddenInput) {
            hiddenInput.value = id;  // Nastav ID hráče do skrytého inputu
        }
    }
    hideRoster(button.closest(".modal").id);
}


function rosterToButtons (){
    let roster = JSON.parse(localStorage.getItem("roster"));
    let buttons = [];

    for (let player of roster){
        let button = document.createElement("button");
        button.dataset.number = player.gameNumber;
        button.dataset.id = player.id;
        button.innerHTML = `${player.gameNumber}<br>${player.player.surname}`;
        button.onclick = ()=> playerToInput(button);
    buttons.push(button);
    }
    return buttons;
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

function opponentPenaltyToRow(penalty){
    const tr = document.createElement('tr');
    tr.classList.add('penalty', 'opponent');
    tr.dataset.id = penalty.id;
    tr.onclick = () =>openEditOpponentPenalty(penalty.id);

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

function goalScoredToEdit(goal){
    document.querySelector("#scored-goal-id").value = goal.id;
    document.querySelector("#scored-goal-time-real-edit").value = secondsToInput(goal.time);
    let select = document.querySelector("#scored-goal-situation-edit")
    for(let op of select.children){
        if(goal.situation === op.value){
            op.selected = true;
        }
    }
    document.querySelector("#goal-edit-id").value = goal.scorer?.id ? goal.scorer.id : "";
    document.querySelector("#goal-edit").value = goal.scorer?.gameNumber ? goal.scorer.gameNumber : "";
    document.querySelector("#assist1-edit-id").value = goal.assists[0]?.id ? goal.assists[0].id : "";
    document.querySelector("#assist2-edit-id").value = goal.assists[1]?.id ? goal.assists[1].id : "";
    document.querySelector("#assist1-edit").value = goal.assists[0]?.gameNumber ? goal.assists[0].gameNumber : "";
    document.querySelector("#assist2-edit").value = goal.assists[1]?.gameNumber ? goal.assists[1].gameNumber : "";
    document.querySelector("#scored-goal-onIce1-edit-id").value = goal.onIce[0]?.id ? goal.onIce[0].id : "";
    document.querySelector("#scored-goal-onIce2-edit-id").value = goal.onIce[1]?.id ? goal.onIce[1].id : "";
    document.querySelector("#scored-goal-onIce3-edit-id").value = goal.onIce[2]?.id ? goal.onIce[2].id : "";
    document.querySelector("#scored-goal-onIce4-edit-id").value = goal.onIce[3]?.id ? goal.onIce[3].id : "";
    document.querySelector("#scored-goal-onIce5-edit-id").value = goal.onIce[4]?.id ? goal.onIce[4].id : "";
    document.querySelector("#scored-goal-onIce6-edit-id").value = goal.onIce[5]?.id ? goal.onIce[5].id : "";
    document.querySelector("#scored-goal-onIce1-edit").value = goal.onIce[0]?.gameNumber ? goal.onIce[0].gameNumber : "";
    document.querySelector("#scored-goal-onIce2-edit").value = goal.onIce[1]?.gameNumber ? goal.onIce[1].gameNumber : "";
    document.querySelector("#scored-goal-onIce3-edit").value = goal.onIce[2]?.gameNumber ? goal.onIce[2].gameNumber : "";
    document.querySelector("#scored-goal-onIce4-edit").value = goal.onIce[3]?.gameNumber ? goal.onIce[3].gameNumber : "";
    document.querySelector("#scored-goal-onIce5-edit").value = goal.onIce[4]?.gameNumber ? goal.onIce[4].gameNumber : "";
    document.querySelector("#scored-goal-onIce6-edit").value = goal.onIce[5]?.gameNumber ? goal.onIce[5].gameNumber : "";

}

function goalConcededToEdit(goal){
    document.querySelector("#conceded-goal-id").value = goal.id;
    document.querySelector("#conceded-goal-time-real-edit").value = secondsToInput(goal.time);
    let select = document.querySelector("#conceded-goal-situation-edit")
    for(let op of select.children){
        if(goal.situation === op.value){
            op.selected = true;
        }
    }


    document.querySelector("#conceded-goal-onIce1-edit-id").value = goal.onIce[0]?.id ? goal.onIce[0].id : "";
    document.querySelector("#conceded-goal-onIce2-edit-id").value = goal.onIce[1]?.id ? goal.onIce[1].id : "";
    document.querySelector("#conceded-goal-onIce3-edit-id").value = goal.onIce[2]?.id ? goal.onIce[2].id : "";
    document.querySelector("#conceded-goal-onIce4-edit-id").value = goal.onIce[3]?.id ? goal.onIce[3].id : "";
    document.querySelector("#conceded-goal-onIce5-edit-id").value = goal.onIce[4]?.id ? goal.onIce[4].id : "";
    document.querySelector("#conceded-goal-onIce6-edit-id").value = goal.onIce[5]?.id ? goal.onIce[5].id : "";
    document.querySelector("#conceded-goal-onIce1-edit").value = goal.onIce[0]?.gameNumber ? goal.onIce[0].gameNumber : "";
    document.querySelector("#conceded-goal-onIce2-edit").value = goal.onIce[1]?.gameNumber ? goal.onIce[1].gameNumber : "";
    document.querySelector("#conceded-goal-onIce3-edit").value = goal.onIce[2]?.gameNumber ? goal.onIce[2].gameNumber : "";
    document.querySelector("#conceded-goal-onIce4-edit").value = goal.onIce[3]?.gameNumber ? goal.onIce[3].gameNumber : "";
    document.querySelector("#conceded-goal-onIce5-edit").value = goal.onIce[4]?.gameNumber ? goal.onIce[4].gameNumber : "";
    document.querySelector("#conceded-goal-onIce6-edit").value = goal.onIce[5]?.gameNumber ? goal.onIce[5].gameNumber : "";

}

function penaltyToEdit(penalty){
    document.querySelector("#penalty-id").value = penalty.id;
    document.querySelector("#penalty-time-real-edit").value = secondsToInput(penalty.time);
    let select = document.querySelector("#penalty-length-edit");
    for(let op of select.children){
        if(penalty.minutes == op.value){
            op.selected = true;
        }
    }
    document.querySelector("#penalty-player-edit-id").value = penalty.player?.id ? penalty.player.id :"";
    document.querySelector("#penalty-player-edit").value = penalty.player?.gameNumber ? penalty.player.gameNumber :"";
    document.querySelector("#reason-edit").value = penalty.reason? penalty.reason : "";
    document.querySelector("#coincidental-edit").checked = penalty.coincidental;
}

function opponentPenaltyToEdit(penalty){
    document.querySelector("#opponent-penalty-id").value = penalty.id;
    document.querySelector("#opponent-penalty-time-real-edit").value = secondsToInput(penalty.time);
    document.querySelector("#opponent-coincidental-edit").checked = penalty.coincidental;
    let select = document.querySelector("#opponent-penalty-length-edit");
    for(let op of select.children){
        if(penalty.minutes == op.value){
            op.selected = true;
        }
    }
}

function openDeleteScoredGoalDialog(){
    document.querySelector("input[name=delete-goal-scored-id]").value = document.querySelector("#scored-goal-id").value;
    showDialog("delete-goal-scored");

}

function openDeleteConcededGoalDialog(){
    document.querySelector("input[name=delete-goal-conceded-id]").value = document.querySelector("#conceded-goal-id").value;
    showDialog("delete-goal-conceded");

}

function openDeletePenaltyDialog(){
    document.querySelector("input[name=delete-penalty-id]").value = document.querySelector("#penalty-id").value;
    showDialog("delete-penalty");

}

function openDeleteOpponentPenaltyDialog(){
    document.querySelector("input[name=delete-opponent-penalty-id]").value = document.querySelector("#opponent-penalty-id").value;
    showDialog("delete-opponent-penalty");

}

function savesToInput(saves){

    let periods = Number(localStorage.getItem("periods"));

    if(saves) {
        document.querySelector("#saves-table").style.visibility = "inherit";
        document.querySelector("#saves").innerText = saves.saves;
        document.querySelector("#saves-header").innerHTML = `Zásahy brankáře - ${saves.period<=periods?saves.period+'. '+getShortPartName():'PR'}`;
        document.querySelector("#saves-header-name").innerHTML = `#${saves.goalkeeper.gameNumber} ${saves.goalkeeper.player.name} ${saves.goalkeeper.player.surname}`;
    } else {
        document.querySelector("#saves-table").style.visibility = "hidden";

    }
}

function shotsToInput(shotsArray){
    let period = localStorage.getItem("currentPeriod");
    let periods = Number(localStorage.getItem("periods"));


    let shots;
    for (let record of shotsArray){
        if(record.period == period){
            shots = record;
            break;
        }
    }
        document.querySelector("#shots").innerText = shots.shots;
        document.querySelector("#shots-header").innerHTML = `Střely na branku - ${shots.period<=periods?shots.period+'. '+getShortPartName():'PR'}`;
}

function shotRecordToInput(shots){
    let periods = Number(localStorage.getItem("periods"));


    document.querySelector("#shots").innerText = shots.shots;
    document.querySelector("#shots-header").innerHTML = `Střely na branku - ${shots.period<=periods?shots.period+'. '+getShortPartName():'PR'}`;
}

async function addPeriod(){
    try{
        const response = await fetch(API_URL+"games/"+gameId+"/addPeriod", {method: "POST"})

        if(response.ok){
            await loadData();
        } else {
            throw await response
        }

    } catch (e){
        showMessage(e, "error")
    }
}

async function removePeriod(){
    try{
        const response = await fetch(API_URL+"games/"+gameId+"/removePeriod", {method: "POST"})

        if(response.ok){
            await loadData();
        } else {
            throw await response
        }

    } catch (e){
        showMessage(e, "error")
    }
}

function openSelectGoalies(goalies){
    document.querySelector("#goalie1").value = goalies[0].id;
    document.querySelector("#goalie2").value = goalies[1].id;
    document.querySelector("label[for=goalie1]").innerText = goalies[0].player.name +" "+goalies[0].player.surname;
    document.querySelector("label[for=goalie2]").innerText = goalies[1].player.name +" "+goalies[1].player.surname;
    openModal("select-active-goalie");
}

function onRadioChecked(){
    let radios = document.querySelectorAll("#select-active-goalie input[type=radio]");
    for(let input of radios){
        if(input.checked == true){
            return true;
            break;
        }
    }
    return false;
}


