async function newPenalty(){
    let time = timeToSeconds(document.querySelector("#penalty-time-real").value);
    let minutes = document.querySelector("#penalty-length").value;
    let player = document.querySelector("#penalty-player-id").value;
    let reason = document.querySelector("#reason").value;
    let coincidental = document.querySelector("#coincidental").checked;

    let playerObject = player!==""?{id:player}:null;

    let body = {time:time ,game: {id: gameId}, player: playerObject, minutes: minutes, reason:reason, coincidental:coincidental};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/penalties", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Trest uložen", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se uložit trest: " + message.join(", "), "error");

    }

    closeModal(null, "penalty");
    await loadData();
}

async function editPenalty(){
    let eventId = document.querySelector("#penalty-id").value;
    let time = timeToSeconds(document.querySelector("#penalty-time-real-edit").value);
    let minutes = document.querySelector("#penalty-length-edit").value;
    let player = document.querySelector("#penalty-player-edit-id").value;
    let reason = document.querySelector("#reason-edit").value;
    let coincidental = document.querySelector("#coincidental-edit").checked;


    let playerObject = player!==""?{id:player}:null;

    let body = {time:time ,game: {id: gameId}, player: playerObject, minutes: minutes, reason:reason, coincidental:coincidental};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/penalties/"+eventId, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Trest upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se upravit trest: " + message.join(", "), "error");

    }

    closeModal(null, "penalty-edit");
    await loadData();
}

async function deletePenalty(){
    let eventId = document.querySelector("input[name=delete-penalty-id]").value;
    try {
        const response = await fetch(API_URL + "games/"+gameId+"/penalties/"+eventId, {headers: {"Content-Type": "application/json"},method: "DELETE"})
        if(response.ok){
            showMessage("Trest smazán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se smazat trest: " + message.join(", "), "error");

    }

    closeDialog(null, 'delete-penalty');
    closeModal(null, "penalty-edit");
    await loadData();
}



async function openEditPenalty(eventId){
   // try{
        const response = await fetch(API_URL+"games/"+gameId+"/events/"+eventId);
        if(response.ok){
            penaltyToEdit(await response.json());
            openModal("penalty-edit");


        } else {
            throw await response.json();
        }
 //   } catch (e) {
 //       showMessage("Chyba: " + e, "error");

 //   }
}

