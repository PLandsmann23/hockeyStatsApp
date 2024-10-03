async function addShot() {
    let period = localStorage.getItem("currentPeriod");

        await fetch(API_URL + "games/" + gameId + "/shots/period/" + period+"/add", {headers: {"Content-Type": "application/json"},method: "PUT"})
            .then(response => response.json())
            .then(data => {
                shotRecordToInput(data);
            })

}

async function removeShot() {
    let period = localStorage.getItem("currentPeriod");

        await fetch(API_URL + "games/" + gameId + "/shots/period/" + period+"/remove", {headers: {"Content-Type": "application/json"},method: "PUT"})
            .then(response => response.json())
            .then(data => {
                shotRecordToInput(data);
            })

}

