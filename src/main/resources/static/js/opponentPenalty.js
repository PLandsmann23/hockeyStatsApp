async function newOpponentPenalty(){
    let time = timeToSeconds(document.querySelector("#opponent-penalty-time-real").value);
    let minutes = document.querySelector("#opponent-penalty-length").value;
    let coincidental = document.querySelector("#opponent-coincidental").checked;



    let body = {time:time ,game: {id: gameId}, minutes: minutes, coincidental: coincidental};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/opponentPenalties", {headers: {"Content-Type": "application/json"
            },method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Trest uložen", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se uložit trest: " + message.join(", "), "error");

    }

    closeModal(null, "opponent-penalty");
    await loadData();
}

async function editOpponentPenalty(){
    let eventId = document.querySelector("#opponent-penalty-id").value;
    let time = timeToSeconds(document.querySelector("#opponent-penalty-time-real-edit").value);
    let minutes = document.querySelector("#opponent-penalty-length-edit").value;
    let coincidental = document.querySelector("#opponent-coincidental-edit").checked;



    let body = {time:time ,game: {id: gameId}, minutes: minutes, coincidental: coincidental};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/opponentPenalties/"+eventId, {headers: {"Content-Type": "application/json"
            },method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Trest upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se upravit trest: " + message.join(", "), "error");

    }

    closeModal(null, "opponent-penalty-edit");
    await loadData();
}


async function deleteOpponentPenalty(){
    let eventId = document.querySelector("input[name=delete-opponent-penalty-id]").value;
    try {
        const response = await fetch(API_URL + "games/"+gameId+"/opponentPenalties/"+eventId, {headers: {"Content-Type": "application/json"
            },method: "DELETE"})
        if(response.ok){
            showMessage("Trest smazán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){

        let message = Object.values(e);

        showMessage("Nepodařilo se smazat trest: " + message.join(", "), "error");

    }

    closeDialog(null, 'delete-opponent-penalty');
    closeModal(null, "opponent-penalty-edit");
    await loadData();
}

async function openEditOpponentPenalty(eventId){
    try{
        const response = await fetch(API_URL+"games/"+gameId+"/events/"+eventId);
        if(response.ok){
            opponentPenaltyToEdit(await response.json());
            openModal("opponent-penalty-edit");


        } else {
            throw await response.json();
        }
    } catch (e) {
        showMessage("Chyba: " + e, "error");

    }
}