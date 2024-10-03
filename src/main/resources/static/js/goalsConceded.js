async function newGoalConceded(){
    let time = timeToSeconds(document.querySelector("#conceded-goal-time-real").value);
    let situation = document.querySelector("#conceded-goal-situation").value;

    let minus = [];
    minus[0] = document.querySelector("#conceded-goal-onIce1-id").value;
    minus[1] = document.querySelector("#conceded-goal-onIce2-id").value;
    minus[2] = document.querySelector("#conceded-goal-onIce3-id").value;
    minus[3] = document.querySelector("#conceded-goal-onIce4-id").value;
    minus[4] = document.querySelector("#conceded-goal-onIce5-id").value;
    minus[5] = document.querySelector("#conceded-goal-onIce6-id").value;

    let emptyNet = document.querySelector("#empty-net").checked;

    let inGoal

    if(!emptyNet){
        inGoal = checkForActiveGoalie(JSON.parse(localStorage.getItem("roster")));
        if(!inGoal){
            inGoal = null;
                }
    }

    let onIce = [];
    for (let id of minus){
        if(id!="") {
            onIce.push({id: id});
        }
    }




    let body = {time:time ,game: {id: gameId}, onIce:onIce, situation:situation, inGoal: inGoal};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsConceded", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Gól uložen", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

            let message = Object.values(e);

            showMessage("Nepodařilo se uložit gól: " + message.join(", "), "error");

    }
    closeModal(null,"conceded-goal");
    await loadData();
}

async function editConcededGoal(){

    let eventId = document.querySelector("#conceded-goal-id").value;
    let time = timeToSeconds(document.querySelector("#conceded-goal-time-real-edit").value);
    let situation = document.querySelector("#conceded-goal-situation-edit").value;

    let minus = [];
    minus[0] = document.querySelector("#conceded-goal-onIce1-edit-id").value;
    minus[1] = document.querySelector("#conceded-goal-onIce2-edit-id").value;
    minus[2] = document.querySelector("#conceded-goal-onIce3-edit-id").value;
    minus[3] = document.querySelector("#conceded-goal-onIce4-edit-id").value;
    minus[4] = document.querySelector("#conceded-goal-onIce5-edit-id").value;
    minus[5] = document.querySelector("#conceded-goal-onIce6-edit-id").value;


    let onIce = [];
    for (let id of minus){
        if(id!="") {
            onIce.push({id: id});
        }
    }


    let body = {time:time ,game: {id: gameId}, onIce:onIce, situation:situation};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsConceded/"+eventId, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Gól upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se upravit gól: " + message.join(", "), "error");

    }
    closeModal(null,"conceded-goal-edit");
    await loadData();
}

async function deleteGoalConceded(){
    let eventId = document.querySelector("input[name=delete-goal-conceded-id]").value;
    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsConceded/"+eventId, {headers: {"Content-Type": "application/json"},method: "DELETE"})
        if(response.ok){
            showMessage("Gól smazán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se smazat gól: " + message.join(", "), "error");

    }

    closeDialog(null, 'delete-goal-conceded');
    closeModal(null, "conceded-goal-edit");
    await loadData();
}

async function openEditConcededGoal(eventId){
    try{
        const response = await fetch(API_URL+"games/"+gameId+"/events/"+eventId);
        if(response.ok){
            goalConcededToEdit(await response.json());
            openModal("conceded-goal-edit");


        } else {
            throw await response.json();
        }
    } catch (e) {
       showMessage("Chyba: " + e, "error");

    }
}


