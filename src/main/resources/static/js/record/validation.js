//document.querySelectorAll("#scored-goal input").forEach()

function checkTimeKey(input){
    let modal = input.closest(".modal");
    let saveButton = modal.querySelector("button.save");
    let errorInput = modal.querySelectorAll(".error-input");
    if(input.value.length!=4 || errorInput.size>0){
        saveButton.disabled = true;
    }else {
        saveButton.disabled = false;
    }
}

function checkTimeFocus(input){
    let modal = input.closest(".modal");
    let saveButton = modal.querySelector("button.save");
    let errorInput = modal.querySelectorAll(".error-input");
    if(input.value.length!=4  || errorInput.size>0){
        saveButton.disabled = true;
        showMessage("Čas musí být zadán ve formátu mmss","error")
    } else {
        saveButton.disabled = false;
    }
}

function validateInput(input){
    let modal = input.closest(".modal");
    let saveButton = modal.querySelector("button.save");
    let timeInput = modal.querySelector("input[type=text]");

    /*setTimeout(()=>{*/if(input.value===""|| findInRoster(input.value)){
        if(input.value!== ""){
            modal.querySelector("#"+input.id+"-id").value = findInRoster(input.value);
        } else{
            modal.querySelector("#"+input.id+"-id").value = "";

        }
        input.classList.remove("error-input");
        input.style.background= "none";
        if(checkTimeKey(timeInput)) {
            saveButton.disabled = false;
        }
    } else {
        modal.querySelector("#"+input.id+"-id").value = "";
        input.classList.add("error-input");
        saveButton.disabled = true;
        showMessage("Hráč nebyl nalezen","error")
    }/*},10);*/

}



function findInRoster(gameNumber){
    let roster = JSON.parse(localStorage.getItem("roster"));
    for(let player of roster){
        if(player.gameNumber == gameNumber){
            return player.id;
        }
    }

    return null;
}