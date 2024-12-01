const API_URL = "/api/";


const GAME_PARTS = {
    2: "poločas",
    3: "třetina",
    4: "čtvrtina",
    5: "perioda"
}

const GAME_PARTS_SHORT = {
    2: "pol.",
    3: "tř.",
    4: "čtv.",
    5: "per."
}

function getPartName(){
    let noOfParts = localStorage.getItem("periods");
    return GAME_PARTS[noOfParts];
}

function getShortPartName(){
    let noOfParts = localStorage.getItem("periods");
    return GAME_PARTS_SHORT[noOfParts];
}