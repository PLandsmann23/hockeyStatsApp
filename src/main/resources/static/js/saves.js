async function addSave() {
    let activeGoalie = checkForActiveGoalie(JSON.parse(localStorage.getItem("roster")));
    let period = localStorage.getItem("currentPeriod");
    if (activeGoalie) {
        await fetch(API_URL + "games/" + gameId + "/saves/" + activeGoalie.id + "/period/" + period+"/add", {headers: {"Content-Type": "application/json"},method: "PUT"})
            .then(response => response.json())
            .then(data => {
                savesToInput(data);
            })
    }
}

async function removeSave() {
    let activeGoalie = checkForActiveGoalie(JSON.parse(localStorage.getItem("roster")));
    let period = localStorage.getItem("currentPeriod");
    if (activeGoalie) {
        await fetch(API_URL + "games/" + gameId + "/saves/" + activeGoalie.id + "/period/" + period+"/remove", {headers: {"Content-Type": "application/json"},method: "PUT"})
            .then(response => response.json())
            .then(data => {
                savesToInput(data);
            })
    }
}

async function createSaves(){
    let activeGoalie = checkForActiveGoalie(JSON.parse(localStorage.getItem("roster")));
    let period = localStorage.getItem("currentPeriod");
    if (activeGoalie) {
        await fetch(API_URL + "games/" + gameId + "/saves/" + activeGoalie.id + "/period/" + period+"", {headers: {"Content-Type": "application/json"},method: "POST"})
            .then(response => response.json())
            .then(data => {
                savesToInput(data);
            })
    }
}