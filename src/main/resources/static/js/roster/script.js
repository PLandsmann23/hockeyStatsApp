const gameId = window.location.pathname.split('/').pop();

document.addEventListener("DOMContentLoaded", loadData);

async function loadData(){
    fetch(API_URL +"games/"+gameId+"/roster")
        .then(response => response.json())
        .then((data)=>{
            parsePlayers(data.notInRoster);
            parseRoster(data.roster);
            parseGame(data.game);
            localStorage.setItem("roster", JSON.stringify(data.roster));
        });
}

function parsePlayers(data){
    const tableBody = document.querySelector('#not-in-roster tbody');

    tableBody.innerHTML ="";


    data.forEach(player => {
        const row = document.createElement('tr');

        row.dataset.id = player.id;

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

        const rosterAnchor = document.createElement('button');
        rosterAnchor.onclick = () => newPlayerToRoster(rosterAnchor) ;
        rosterAnchor.textContent = 'Přidat';
        actionCell.appendChild(rosterAnchor);

        row.appendChild(actionCell);



        tableBody.appendChild(row);
    });
}

function parseRoster(data){
    const tableBody = document.querySelector('#roster tbody');

    tableBody.innerHTML ="";

    data.forEach((item, index) => {
        // Vytvoř nový řádek tabulky
        const tr = document.createElement('tr');

        tr.dataset.id = item.id;

        // První buňka: input s číslem a tlačítko pro editaci
        const numberCell = document.createElement('td');
        const div = document.createElement('div');
        const input = document.createElement('input');
        input.type = 'number';
        input.value = item.gameNumber || "";
        input.disabled = true;


        const buttonEdit = document.createElement('button');
        buttonEdit.classList.add('edit-roster');
        const img = document.createElement('img');
        img.src = '/svg/pencil.svg';
        buttonEdit.appendChild(img);
        buttonEdit.onclick= ()=> numberEdit(buttonEdit);
        div.appendChild(input);
        div.appendChild(buttonEdit);
        numberCell.appendChild(div);

        // Druhá buňka: Jméno a příjmení
        const nameCell = document.createElement('td');
        nameCell.textContent = `${item.player.name} ${item.player.surname}`;

        // Třetí buňka: Číslo hráče
        const positionCell = document.createElement('td');
        positionCell.textContent = item.player.position || '';

        // Čtvrtá buňka: Výběr (select) pro pozici
        const lineCell = document.createElement('td');
        const select = document.createElement('select');

        // Vytvoření jednotlivých options
        const options = [
            { value: 0, text: '--' },
            { value: 1, text: '1' },
            { value: 2, text: '2' },
            { value: 3, text: '3' },
            { value: 4, text: '4' },
            { value: 5, text: '5' }
        ];

        options.forEach(opt => {
            const option = document.createElement('option');
            option.value = opt.value;
            option.text = opt.text;
            select.appendChild(option);
        });

        select.disabled = true;
        lineCell.appendChild(select);

        // Pátá buňka: Tlačítko pro odebrání hráče
        const deleteCell = document.createElement('td');
        const buttonDelete = document.createElement('button');
        buttonDelete.textContent = 'Odebrat';
        buttonDelete.onclick = () =>
            showRosterDeleteDialog(buttonDelete);
        deleteCell.appendChild(buttonDelete);

        // Přidej všechny buňky do řádku
        tr.appendChild(numberCell);
        tr.appendChild(nameCell);
        tr.appendChild(positionCell);
        tr.appendChild(lineCell);
        tr.appendChild(deleteCell);

        // Přidej řádek do tabulky
        tableBody.appendChild(tr);
    });
}

function parseGame(data){
    let gameInfo = document.querySelector("#game-info");

    gameInfo.innerHTML = `Zápas: ${data.team.name} &nbsp;X&nbsp;  ${data.opponent}`;

    let idIn = document.querySelector("#g-id");
    let opponentIn = document.querySelector("#opponent");
    let dateIn = document.querySelector("#date");
    let timeIn = document.querySelector("#time");
    let venueIn = document.querySelector("#venue");

    idIn.value = data.id;
    opponentIn.value = data.opponent;
    dateIn.value = data.date;
    timeIn.value = data.time;
    venueIn.value = data.venue;
}

function showRosterDeleteDialog(button){
    const rosterId = button.parentNode.parentNode.dataset.id;
    document.querySelector("input[name=delete-roster-id]").value = rosterId;
    showDialog("delete-roster") ;
}

function numberEdit(button){
    let input = button.parentNode.querySelector("input");
let img = button.querySelector("img");
    img.src = '/svg/tick.svg';
    input.disabled = false;
    button.onclick = ()=> editPlayerInRoster(button);
}


