function openPlayerEdit(button){
    let row = button.parentNode.parentNode;
    let name = row.dataset.name;
    let surname = row.dataset.surname;
    let number = row.children[0].textContent;
    let position = row.children[2].textContent;


    let namein = document.querySelector("#player-name-edit");
    let surnamein = document.querySelector("#player-surname-edit");
    let numberin = document.querySelector("#player-number-edit");
    let positionin = document.querySelector("#player-position-edit");
    let id = document.querySelector("#player-id");


    namein.value = name;
    surnamein.value = surname;
    numberin.value = number;
    id.value = row.dataset.id;

    for (let i = 0; i < positionin.options.length; i++) {
        if (positionin.options[i].value === position) {
            positionin.selectedIndex = i;
            break;
        }
    }

    openModal("edit-player");
}

async function editPlayer(){
    let id = document.querySelector("#player-id").value
    let name = document.querySelector("#player-name-edit");
    let surname = document.querySelector("#player-surname-edit");
    let number = document.querySelector("#player-number-edit");
    let position = document.querySelector("#player-position-edit");

    let body = {name:name.value, surname: surname.value, number:number.value, position: position.value===""?null:position.value};

    let nameError = document.querySelector("#e-player-name-edit");
    let surnameError = document.querySelector("#e-player-surname-edit");
    let numberError = document.querySelector("#e-player-number-edit");
    let positionError = document.querySelector("#e-player-position-edit");

    try {
        const response = await fetch(API_URL + "players/"+id, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Hráč upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("name" in e || "surname" in e || "number" in e || "position" in e){
            if("name" in e ){
                nameError.textContent = e.name;
                name.classList.add("error-input");
                return;
            }
            if("surname" in e ){
                surnameError.textContent = e.surname;
                surname.classList.add("error-input");
                return;
            }
            if("number" in e ){
                numberError.textContent = e.number;
                number.classList.add("error-input");
                return;
            }
            if("position" in e ){
                positionError.textContent = e.position;
                position.classList.add("error-input");
                return;
            }
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se upravit hráče: " + message.join(", "), "error");
        }
    }
    nameError.textContent = "";
    name.classList.remove("error-input");

    surnameError.textContent = "";
    surname.classList.remove("error-input");

    numberError.textContent = "";
    number.classList.remove("error-input");

    positionError.textContent = "";
    position.classList.remove("error-input");

    await loadData();
    closeModal(null, "edit-player");
}

async function newPlayer(){
    const teamId = window.location.pathname.split('/').pop();
    let name = document.querySelector("#player-name");
    let surname = document.querySelector("#player-surname");
    let number = document.querySelector("#player-number");
    let position = document.querySelector("#player-position");

    let body = {name:name.value, surname: surname.value, number:number.value, position: position.value===""?null:position.value};

    let nameError = document.querySelector("#e-player-name");
    let surnameError = document.querySelector("#e-player-surname");
    let numberError = document.querySelector("#e-player-number");
    let positionError = document.querySelector("#e-player-position");

    try {
        const response = await fetch(API_URL +"teams/"+teamId+ "/players", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Hráč přidán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("name" in e || "surname" in e || "number" in e || "position" in e){
            if("name" in e ){
                nameError.textContent = e.name;
                name.classList.add("error-input");
                return;
            }
            if("surname" in e ){
                surnameError.textContent = e.surname;
                surname.classList.add("error-input");
                return;
            }
            if("number" in e ){
                numberError.textContent = e.number;
                number.classList.add("error-input");
                return;
            }
            if("position" in e ){
                positionError.textContent = e.position;
                position.classList.add("error-input");
                return;
            }
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se přidat hráče: " + message.join(", "), "error");
        }
    }
    nameError.textContent = "";
    name.classList.remove("error-input");

    surnameError.textContent = "";
    surname.classList.remove("error-input");

    numberError.textContent = "";
    number.classList.remove("error-input");

    positionError.textContent = "";
    position.classList.remove("error-input");

    await loadData();
    closeModal(null, "create-player");
}

async function deletePlayer(){
    let playerId = document.querySelector("input[name=delete-player-id]").value;
    try{
        const response = await fetch(API_URL+"players/"+playerId, {method: "DELETE" });
        if (response.ok) {
            showMessage("Hráč byl úspěšně smazán", "success");
        } else {
            showMessage("Nepodařilo se smazat hráče", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se smazat hráče", "error");
    }
    document.querySelector("input[name=delete-player-id]").value = "";
    await loadData();
    closeDialog(null, 'delete-player');

}