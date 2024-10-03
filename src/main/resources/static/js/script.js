document.addEventListener('DOMContentLoaded', loadData);


async function loadData (){
    fetch(API_URL +"info")
        .then(response => response.json())
        .then((data)=>{
            parseGames(data.recentGames);
            parseTeams(data.userTeams)
            parseNumbers(data);
        });
}

function parseGames(data){

    const tableBody = document.querySelector("#next-games table tbody");

    tableBody.innerHTML ="";


    data.forEach(game => {
        const row = document.createElement('tr');


        // Tým
        const teamCell = document.createElement('td');
        teamCell.textContent = game.team.name;
        row.appendChild(teamCell);

        // X - Placeholder pro výsledek nebo status
        const statusCell = document.createElement('td');
        statusCell.textContent = 'X'; // Zde můžete vložit výsledek zápasu nebo jiný symbol
        row.appendChild(statusCell);

        // Soupeř
        const opponentCell = document.createElement('td');
        opponentCell.textContent = game.opponent;
        row.appendChild(opponentCell);

        // Meta data (datum, čas, místo)
        const metaCell = document.createElement('td');
        const metaTable = document.createElement('table');
        metaTable.classList.add('game-meta');

        // První řádek s datem a časem
        const metaRow1 = document.createElement('tr');

        const dateCell = document.createElement('td');
        const dateIcon = document.createElement('img');
        dateIcon.src = '/svg/calendar.svg';
        dateCell.appendChild(dateIcon);
        dateCell.innerHTML += ` ${dateToString(game.date)}`;
        metaRow1.appendChild(dateCell);

        const timeCell = document.createElement('td');
        const timeIcon = document.createElement('img');
        timeIcon.src = '/svg/clock.svg';
        timeCell.appendChild(timeIcon);
        timeCell.innerHTML += ` ${game.time.substring(0,game.time.length-3)}`;
        metaRow1.appendChild(timeCell);

        metaTable.appendChild(metaRow1);

        // Druhý řádek s místem
        const metaRow2 = document.createElement('tr');
        const locationCell = document.createElement('td');
        locationCell.colSpan = 2;

        const locationIcon = document.createElement('img');
        locationIcon.src = '/svg/place.svg';
        locationCell.appendChild(locationIcon);
        locationCell.innerHTML += `&nbsp; ${game.venue}`;

        metaRow2.appendChild(locationCell);
        metaTable.appendChild(metaRow2);

        // Přidání meta tabulky do buňky
        metaCell.appendChild(metaTable);
        row.appendChild(metaCell);

        // Přidání řádku do tabulky
        tableBody.appendChild(row);
    });
}

function parseTeams(myTeams){
    const tableBody = document.querySelector('#my-teams tbody');

    tableBody.innerHTML ="";


    myTeams.forEach(teamData => {
        const row = document.createElement('tr');

        row.dataset.id = teamData.team.id;

        const teamNameCell = document.createElement('td');
        teamNameCell.textContent = teamData.team.name;
        row.appendChild(teamNameCell);

        const playersCell = document.createElement('td');
        playersCell.textContent = teamData.players;
        row.appendChild(playersCell);

        const actionCell = document.createElement('td');

        const editIcon = document.createElement('img');
        editIcon.src = '/svg/pencil.svg';
        editIcon.onclick = () => openTeamEdit(editIcon);
        actionCell.appendChild(editIcon);

        const deleteIcon = document.createElement('img');
        deleteIcon.src = '/svg/bin.svg';
        deleteIcon.onclick = () => openDeleteDialog(deleteIcon);
        actionCell.appendChild(deleteIcon);

        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

function parseNumbers(data){
    let games = data.noGames;
    let teams = data.noTeams;
    let players = data.noPlayers;

    document.querySelector("#games-count").innerHTML = games;
    document.querySelector("#players-count").innerHTML = players;
    document.querySelector("#teams-count").innerHTML = teams;
}

function openDeleteDialog(button){
    const teamId = button.parentNode.parentNode.dataset.id;
    document.querySelector("input[name=delete-team-id]").value = teamId;
    showDialog("delete-team") ;
}

