

const teamId = window.location.pathname.split('/').pop();

document.addEventListener('DOMContentLoaded', loadData);

async function loadData (){
    fetch(API_URL +"teams/"+teamId)
        .then(response => response.json())
        .then((data)=>{
            console.log(data);
            parseGames(data.games);
            parsePlayers(data.players);
            parseTeam(data.team)
        });
}

function pasteSettings(){
    let settings = JSON.parse(sessionStorage.getItem("settings"));
    let length = document.querySelector("#game-period-length");
    let periods = document.querySelector("#game-periods");

    length.value = settings.defaultPeriodLength;
    periods.value = settings.defaultPeriods;
}

function parseGames(data){
    const tableBody = document.querySelector('#games tbody');

    tableBody.innerHTML ="";


    data.forEach(game => {
        const row = document.createElement('tr');

        row.dataset.id = game.game.id;

        const opponentCell = document.createElement('td');
        opponentCell.textContent = game.game.opponent;
        row.appendChild(opponentCell);

        const dateCell = document.createElement('td');
        dateCell.textContent = game.game.date ? dateToString(game.game.date):"";
        dateCell.dataset.date = game.game.date;
        row.appendChild(dateCell);

        const timeCell = document.createElement('td');
        timeCell.textContent = game.game.time? game.game.time.substring(0, game.game.time.length-3):"";
        row.appendChild(timeCell);

        const venueCell = document.createElement('td');
        venueCell.textContent = game.game.venue;
        row.appendChild(venueCell);

        const scoreCell = document.createElement('td');
        scoreCell.textContent =`${game.goalsScored}:${game.goalsConceded}`;
        row.appendChild(scoreCell);

        const actionCell = document.createElement('td');

        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openGameEdit(editIcon);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openDeleteGameDialog(deleteIcon);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);

        const anchorCell = document.createElement('td');

        const teamAnchor = document.createElement('a');
        teamAnchor.href = '/games/'+game.game.id;
        teamAnchor.textContent = 'Přejít na zápas';
        anchorCell.appendChild(teamAnchor);

        row.appendChild(anchorCell);

        tableBody.appendChild(row);
    })
}

function parsePlayers(data){
    const tableBody = document.querySelector('#players tbody');

    tableBody.innerHTML ="";


    data.forEach(player => {
        const row = document.createElement('tr');

        row.dataset.id = player.id;
        row.dataset.name = player.name;
        row.dataset.surname = player.surname;

        const numberCell = document.createElement('td');
        numberCell.textContent = player.number??"";
        row.appendChild(numberCell);

        const nameCell = document.createElement('td');
        nameCell.textContent = `${player.name} ${player.surname}`;
        row.appendChild(nameCell);

        const positionCell = document.createElement('td');
        positionCell.textContent = player.position??"";
        row.appendChild(positionCell);



        const actionCell = document.createElement('td');

        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openPlayerEdit(editIcon);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openDeletePlayerDialog(deleteIcon);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);



        tableBody.appendChild(row);
    })
}

function parseTeam(data){
    document.querySelector("#team-name").textContent = data.name;
    document.querySelector("#team-name-title").textContent = data.name;

}

function openDeleteGameDialog(button){
    const gameId = button.parentNode.parentNode.dataset.id;
    document.querySelector("input[name=delete-game-id]").value = gameId;
    showDialog("delete-game") ;
}

function openDeletePlayerDialog(button){
    const playerId = button.parentNode.parentNode.dataset.id;
    document.querySelector("input[name=delete-player-id]").value = playerId;
    showDialog("delete-player") ;
}