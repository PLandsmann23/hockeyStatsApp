async function newTeam(){
    let name = document.querySelector("#team-name");
    let body = {name: name.value};
    let nameError = document.querySelector("#e-team-name");

    try {
        const response = await fetch(API_URL + "teams", {headers: {"Content-Type": "application/json"},method: "POST", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Tým přidán", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("name" in e){
            nameError.textContent = e.name;
            name.classList.add("error-input");
            return;
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se přidat tým: " + message.join(", "), "error");
        }
    }

    nameError.textContent = "";
    name.classList.remove("error-input");
    await loadData();
    closeModal(null, "create-team");
}

function openTeamEdit(button){
    let row = button.parentNode.parentNode;
    let name = row.children[0].innerHTML;
    let input = document.querySelector("#team-name-edit");
    let id = document.querySelector("#team-id");



    id.value = row.dataset.id;
    input.value = name;
    openModal('edit-team');
}

async function editTeam(){
    let id = document.querySelector("#team-id").value;
    let name = document.querySelector("#team-name-edit");
    let body = {name: name.value};
    let nameError = document.querySelector("#e-team-name-edit");

    try {
        const response = await fetch(API_URL + "teams/"+id, {headers: {"Content-Type": "application/json"},method: "PUT", body: JSON.stringify(body)})
        if(response.ok){
            showMessage("Tým upraven", "success");

        } else {
            throw await response.json();
        }
    } catch (e){
        if ("name" in e){
            nameError.textContent = e.name;
            name.classList.add("error-input");
            return;
        } else
        {
            let message = Object.values(e);

            showMessage("Nepodařilo se přidat tým: " + message.join(", "), "error");
        }
    }

    nameError.textContent = "";
    name.classList.remove("error-input");
    await loadData();
    closeModal(null, "edit-team");
}

async function deleteTeam(){
    let teamId = document.querySelector("input[name=delete-team-id]").value;
    try{
        const response = await fetch(API_URL+"teams/"+teamId, {  method: "DELETE" });
        if (response.ok) {
            showMessage("Tým byl úspěšně smazán", "success");
        } else {
            showMessage("Nepodařilo se smazat tým", "error");
        }
    }catch(e){
        showMessage("Nepodařilo se smazat tým", "error");
    }
    document.querySelector("input[name=delete-team-id]").value = "";
    await loadData();
    closeDialog(null, 'delete-team');

}

