document.addEventListener('DOMContentLoaded', loadData);

async function loadData (){
    fetch(API_URL +"teams")
        .then(response => response.json())
        .then((data)=>{
            parseTeams(data);
        });
}

function parseTeams(data){
    const tableBody = document.querySelector('.my-teams tbody');

    tableBody.innerHTML ="";


    data.forEach(teamData => {
        const row = document.createElement('tr');

        row.dataset.id = teamData.team.id;

        const teamNameCell = document.createElement('td');
        teamNameCell.textContent = teamData.team.name;
        row.appendChild(teamNameCell);

        const playersCell = document.createElement('td');
        playersCell.textContent = teamData.players;
        row.appendChild(playersCell);

        const gamesCell = document.createElement('td');
        gamesCell.textContent = teamData.games;
        row.appendChild(gamesCell);

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

        const anchorCell = document.createElement('td');

        const teamAnchor = document.createElement('a');
        teamAnchor.href = '/teams/'+teamData.team.id;
        teamAnchor.textContent = 'Přejít na tým';
        anchorCell.appendChild(teamAnchor);

        row.appendChild(anchorCell);

        tableBody.appendChild(row);
    })
}

function openDeleteDialog(button){
    const teamId = button.parentNode.parentNode.dataset.id;
    document.querySelector("input[name=delete-team-id]").value = teamId;
    showDialog("delete-team") ;
}

