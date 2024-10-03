function openGameEdit(button){
    let row = button.parentNode.parentNode;
    let opponent = row.children[0].innerHTML;
    let date = row.children[1];
    let time = row.children[2].innerHTML;
    let venue = row.children[3].innerHTML;
    let opponentin = document.querySelector("#game-opponent-edit");
    let datein = document.querySelector("#game-date-edit");
    let timein = document.querySelector("#game-time-edit");
    let venuein = document.querySelector("#game-venue-edit");
    let id = document.querySelector("#game-id");


    id.value = row.dataset.id;
    opponentin.value = opponent;
    datein.value = date.dataset.date;
    timein.value = time;
    venuein.value = venue;
    openModal('edit-game');
}

function openGameEditFromRoster(){
    let idin = document.querySelector("#g-id").value;
    let opponent = document.querySelector("#opponent").value;
    let date = document.querySelector("#date").value;
    let time = document.querySelector("#time").value;
    let venue = document.querySelector("#venue").value;
    let opponentin = document.querySelector("#game-opponent-edit");
    let datein = document.querySelector("#game-date-edit");
    let timein = document.querySelector("#game-time-edit");
    let venuein = document.querySelector("#game-venue-edit");
    let id = document.querySelector("#game-id");


    id.value = idin;
    opponentin.value = opponent;
    datein.value = date;
    timein.value = time;
    venuein.value = venue;
    openModal('edit-game');
}

async function editGame(){
    let id = document.querySelector("#game-id").value;
    let opponent = document.querySelector("#game-opponent-edit");
    let date = document.querySelector("#game-date-edit");
    let time = document.querySelector("#game-time-edit");
    let venue = document.querySelector("#game-venue-edit");

    let body = {opponent: opponent.value, date: date.value, time: time.value, venue: venue.value};

    let opponentError = document.querySelector("#e-game-opponent-edit");
    let dateError = document.querySelector("#e-game-date-edit");
    let timeError = document.querySelector("#e-game-time-edit");
    let venueError = document.querySelector("#e-game-venue-edit");

    try {
        const response = await fetch(API_URL + "games/"+id, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Zápas upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("opponent" in e || "date" in e || "time" in e || "venue" in e){
            if("opponent" in e ){
                opponentError.textContent = e.opponent;
                opponent.classList.add("error-input");
                return;
            }
            if("date" in e ){
                dateError.textContent = e.date;
                date.classList.add("error-input");
                return;
            }
            if("time" in e ){
                timeError.textContent = e.time;
                time.classList.add("error-input");
                return;
            }
            if("venue" in e ){
                venueError.textContent = e.venue;
                venue.classList.add("error-input");
                return;
            }
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se upravit zápas: " + message.join(", "), "error");
        }
    }

    opponentError.textContent = "";
    opponent.classList.remove("error-input");

    dateError.textContent = "";
    date.classList.remove("error-input");

    timeError.textContent = "";
    time.classList.remove("error-input");

    venueError.textContent = "";
    venue.classList.remove("error-input");

    await loadData();
    closeModal(null, "edit-game");

}

async function newGame(){
    const teamId = window.location.pathname.split('/').pop();
    let opponent = document.querySelector("#game-opponent");
    let date = document.querySelector("#game-date");
    let time = document.querySelector("#game-time");
    let venue = document.querySelector("#game-venue");
    let periodLength = document.querySelector("#game-period-length");
    let periods = document.querySelector("#game-periods");

    let body = {opponent: opponent.value, date: date.value, time: time.value, venue: venue.value, team:{id: teamId}, periods: periods.value!==""?periods.value:undefined, periodLength: periodLength.value!==""?periodLength.value:undefined};

    let opponentError = document.querySelector("#e-game-opponent");
    let dateError = document.querySelector("#e-game-date");
    let timeError = document.querySelector("#e-game-time");
    let venueError = document.querySelector("#e-game-venue");

    try {
        const response = await fetch(API_URL + "games", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Zápas vytvořen", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("opponent" in e || "date" in e || "time" in e || "venue" in e){
            if("opponent" in e ){
                opponentError.textContent = e.opponent;
                opponent.classList.add("error-input");
                return;
            }
            if("date" in e ){
                dateError.textContent = e.date;
                date.classList.add("error-input");
                return;
            }
            if("time" in e ){
                timeError.textContent = e.time;
                time.classList.add("error-input");
                return;
            }
            if("venue" in e ){
                venueError.textContent = e.venue;
                venue.classList.add("error-input");
                return;
            }
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se vytvořit zápas: " + message.join(", "), "error");
        }
    }

    opponentError.textContent = "";
    opponent.classList.remove("error-input");

    dateError.textContent = "";
    date.classList.remove("error-input");

    timeError.textContent = "";
    time.classList.remove("error-input");

    venueError.textContent = "";
    venue.classList.remove("error-input");

    await loadData();
    closeModal(null, "create-game");

}

async function deleteGame(){
    let gameId = document.querySelector("input[name=delete-game-id]").value;
    try{
        const response = await fetch(API_URL+"games/"+gameId, {  method: "DELETE" });
        if (response.ok) {
            showMessage("Zápas byl úspěšně smazán", "success");
        } else {
            showMessage("Nepodařilo se smazat zápas", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se smazat zápas", "error");
    }
    document.querySelector("input[name=delete-game-id]").value = "";
    await loadData();
    closeDialog(null, 'delete-game');

}