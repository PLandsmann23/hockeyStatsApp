async function newPlayerToRoster(button){
    let gameId = document.querySelector("#g-id").value;
    let playerId = button.parentNode.parentNode.dataset.id;
    let conflict = isNumberConflict(button.parentNode.parentNode.children[0].innerHTML)

    if(conflict.conflict){
        document.querySelector("#roster-player-id").value = conflict.id;
        document.querySelector("#roster-player-number").value = button.parentNode.parentNode.children[0].innerHTML;
        document.querySelector("#new-player-id").value = playerId;
        document.querySelector("#roster-player-name").textContent = conflict.name;
        document.querySelector("#new-player-name").textContent = button.parentNode.parentNode.children[1].textContent;
        document.querySelector("#conflict-number").textContent =    button.parentNode.parentNode.children[0].innerHTML;

        openModal("number-conflict");
        return;
    }

    let body = {game:{id: gameId}, player: {id: playerId}};

    try {
        const response = await fetch(API_URL + "games/"+gameId+"/roster", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Hráč přidán do soupisky", "success");

        } else {
            throw await response.json();
        }
    } catch (e) {
        showMessage(e.message, "error");
    }

    await loadData();
}

async function deletePlayerFromRoster(){
    let rosterId = document.querySelector("input[name=delete-roster-id]").value;
    let gameId = document.querySelector("#g-id").value;


    try{
        const response = await fetch(API_URL+"games/"+gameId+"/roster/"+rosterId, {method: "DELETE" });
        if (response.ok) {
            showMessage("Hráč byl odebrán ze soupisky", "success");
        } else {
            showMessage("Nepodařilo se odebrat hráče ze soupisky", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se odebrat hráče ze soupisky", "error");
    }
    document.querySelector("input[name=delete-roster-id]").value = "";
    await loadData();
    closeDialog(null, 'delete-roster');
}

async function editPlayerInRoster(button){
    let input = button.parentNode.querySelector("input");
    let rosterId = button.parentNode.parentNode.parentNode.dataset.id;

    if(isNumberConflict(input.value).conflict){
        if(isNumberConflict(input.value).id == rosterId){
            let img = button.querySelector("img");
            img.src = '/svg/pencil.svg';
            input.disabled = true;
            button.onclick = ()=> numberEdit(button);
            return;
        } else {
            showMessage("Hráč s tímto číslem se už v soupisce nachází", "error");
            return;
        }
    }

    //let select = button.parentNode.parentNode.querySelector("select");

    let body = {gameNumber:input.value /*, select.value*/};

    try{
        const response = await fetch(API_URL+"games/"+gameId+"/roster/"+rosterId, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)});
        if (response.ok) {
            showMessage("Hráč upraven", "success");
        } else {
            showMessage("Nepodařilo se upravit hráče na soupisce", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se upravit hráče na soupisce", "error");
    }

    let img = button.querySelector("img");
    img.src = '/svg/pencil.svg';
    input.disabled = true;
    button.onclick = ()=> numberEdit(button);
    await loadData();
}

async function editAndNewRoster(){
    let rosterId = document.querySelector("#roster-player-id").value;
    let editGameNumber = document.querySelector("#roster-player-number").value;

    //let select = button.parentNode.parentNode.querySelector("select");

    let bodyEdit = {gameNumber:editGameNumber /*, select.value*/};

    try{
        const responseEdit = await fetch(API_URL+"games/"+gameId+"/roster/"+rosterId, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(bodyEdit)});
        if (responseEdit.ok) {
            showMessage("Hráč upraven", "success");
        } else {
            showMessage("Nepodařilo se upravit hráče na soupisce", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se upravit hráče na soupisce", "error");
    }

    let playerId = document.querySelector("#new-player-id").value;
    let newGameNumber = document.querySelector("#new-player-number").value;

    let bodyNew = {game: {id: gameId}, player:{id: playerId}, gameNumber: newGameNumber};

    try {
        const responseNew = await fetch(API_URL + "games/"+gameId+"/roster", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(bodyNew)})
        if(responseNew.ok){
            showMessage("Hráč přidán do soupisky", "success");

        } else {
            throw await responseNew.json();
        }
    } catch (e) {
        showMessage(e, "error");
    }

    await loadData();
    closeModal(null, "number-conflict");
}

function isNumberConflict(playerNumber){
    let roster = JSON.parse(localStorage.getItem("roster"));

    for (let player of roster){
        if(player.gameNumber == playerNumber){
            return {conflict: true, id: player.id, name: player.player.name +" "+player.player.surname};
        }
    }

    return {conflict: false};

}

async function markAsActive(goalie){
    goalie.activeGk = true;
    try {
        const responseNew = await fetch(API_URL + "games/"+gameId+"/roster/"+goalie.id, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(goalie)})
        if(responseNew.ok){
            showMessage("Brankář označen", "success");

        } else {
            throw await responseNew.json();
        }
    } catch (e) {
        showMessage(e, "error");
    }
    await loadData()
}

async function selectGoalie(){
    if(onRadioChecked()) {
        let playerId = document.querySelector('#select-active-goalie input[name="goalie"]:checked').value;

        let player = findPlayerFromRoster(playerId);
        await markAsActive(player);
        closeModal(null, "select-active-goalie");
    }
}

