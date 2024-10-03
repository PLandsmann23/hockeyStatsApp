
function dateToString(string){
    let date = new Date(string).toLocaleDateString("cs-CZ", {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });

    return date;
}

function secondsToMinutes(timeSeconds){
    let minutes = Math.floor(timeSeconds /60);
    let seconds = timeSeconds - (minutes*60);
    if(seconds<10){
        seconds = "0"+seconds;
    }
    return minutes+":"+seconds;
}

function secondsToInput(timeSeconds){
    let minutes = Math.floor(timeSeconds /60);
    let seconds = timeSeconds - (minutes*60);
    if(seconds<10){
        seconds = "0"+seconds;
    }
    if(minutes<10){
        minutes = "0"+minutes;
    }
    minutes = minutes.toString();
    seconds = seconds.toString();
    return minutes+seconds;
}

function timeToSeconds(timeStr){
    let minutes = Number.parseInt(timeStr.slice(0,2));
    let seconds = Number.parseInt(timeStr.slice(2));
    return (minutes*60) + seconds;
}

function check_duplicate_in_id (input_array)  {
    let unique = new Set();
    let duplicated_element = [];
    for (let i = 0; i < input_array.length; i++) {
        if (unique.has(input_array[i].id)) {
            duplicated_element.push(input_array[i]);
        }
        unique.add(input_array[i]);
    }
    return Array.from(new Set(duplicated_element));
}

function checkForActiveGoalie(roster) {
    for (let rosterPlayer of roster) {
        if (rosterPlayer.activeGk) {
            return rosterPlayer;
        }
    }

    return false;
}

function  findPlayerFromRoster(playerId){
    const player = JSON.parse(localStorage.getItem("roster")).filter(record => record.id == playerId);

    return player[0];
}

function openMenu(){
    if(window.innerWidth < 1440){
        document.querySelector(".menu").style.display = "flex";
    }
}

function closeMenu(){
    if(window.innerWidth < 1440){
        document.querySelector(".menu").style.display = "none";
    }
}
