async function newGoalScored(){
    let time = timeToSeconds(document.querySelector("#scored-goal-time-real").value);
    let situation = document.querySelector("#scored-goal-situation").value;
    let goalScorer = document.querySelector("#goal-id").value;
    let assist = [];
    assist[0] = document.querySelector("#assist1-id").value;
    assist[1] = document.querySelector("#assist2-id").value;
    let plus = [];
    plus[0] = document.querySelector("#scored-goal-onIce1-id").value;
    plus[1] = document.querySelector("#scored-goal-onIce2-id").value;
    plus[2] = document.querySelector("#scored-goal-onIce3-id").value;
    plus[3] = document.querySelector("#scored-goal-onIce4-id").value;
    plus[4] = document.querySelector("#scored-goal-onIce5-id").value;
    plus[5] = document.querySelector("#scored-goal-onIce6-id").value;
    let scorer = goalScorer!==""?{id: goalScorer}:null;
    let assists = [];
    for(let id of assist){
        if(id!=="") {
            assists.push({id: id});
        }
    }
    let onIce = [];
    for (let id of plus){
        if(id!="") {
            onIce.push({id: id});
        }
    }


    let body = {time:time ,game: {id: gameId}, scorer: scorer, assists: assists, onIce:onIce, situation:situation};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsScored", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Gól uložen", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

            let message = Object.values(e);

            showMessage("Nepodařilo se uložit gól: " + message.join(", "), "error");

    }

    closeModal(null, "scored-goal");
    await loadData();
}

async function editGoalScored(){
    let eventId = document.querySelector("#scored-goal-id").value;
    let time = timeToSeconds(document.querySelector("#scored-goal-time-real-edit").value);
    let situation = document.querySelector("#scored-goal-situation-edit").value;
    let goalScorer = document.querySelector("#goal-edit-id").value;
    let assist = [];
    assist[0] = document.querySelector("#assist1-edit-id").value;
    assist[1] = document.querySelector("#assist2-edit-id").value;
    let plus = [];
    plus[0] = document.querySelector("#scored-goal-onIce1-edit-id").value;
    plus[1] = document.querySelector("#scored-goal-onIce2-edit-id").value;
    plus[2] = document.querySelector("#scored-goal-onIce3-edit-id").value;
    plus[3] = document.querySelector("#scored-goal-onIce4-edit-id").value;
    plus[4] = document.querySelector("#scored-goal-onIce5-edit-id").value;
    plus[5] = document.querySelector("#scored-goal-onIce6-edit-id").value;
    let scorer = goalScorer!==""?{id: goalScorer}:null;
    let assists = [];
    for(let id of assist){
        if(id!=="") {
            assists.push({id: id});
        }
    }
    let onIce = [];
    for (let id of plus){
        if(id!="") {
            onIce.push({id: id});
        }
    }


    let body = {time:time ,game: {id: gameId}, scorer: scorer, assists: assists, onIce:onIce, situation:situation};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsScored/"+eventId, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Gól upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se upravit gól: " + message.join(", "), "error");

    }

    closeModal(null, "scored-goal-edit");
    await loadData();
}

async function deleteGoalScored(){
    let eventId = document.querySelector("input[name=delete-goal-scored-id]").value;
    try {
        const response = await fetch(API_URL + "games/"+gameId+"/goalsScored/"+eventId, {headers: {"Content-Type": "application/json"},method: "DELETE"})
        if(response.ok){
            showMessage("Gól smazán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se smazat gól: " + message.join(", "), "error");

    }

    closeDialog(null, 'delete-goal-scored');
    closeModal(null, "scored-goal-edit");
    await loadData();
}

async function openEditScoredGoal(eventId){
  try{
        const response = await fetch(API_URL+"games/"+gameId+"/events/"+eventId);
        if(response.ok){
             goalScoredToEdit(await response.json());
            openModal("scored-goal-edit");


        } else {
            throw await response.json();
        }
    } catch (e) {
        showMessage("Chyba: " + e, "error");

    }
}

