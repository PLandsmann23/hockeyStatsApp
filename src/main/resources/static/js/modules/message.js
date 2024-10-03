function showMessage(message, type){
    let messageBox = document.querySelector(".message-box");

    if(message != null){
        messageBox.querySelector("h3").innerHTML = message;
    }
    if(type!= null){
        let source= "/svg/";

        switch (type){
            case "success":source+="check.svg";
            break
            case "error": source+="warning.svg";
            break
            default: source+="info.svg";
        }

        messageBox.querySelector("img").setAttribute("src", source);
    }

    messageBox.style.display = "flex";
    setTimeout(()=>{
        messageBox.style.opacity = 1;
    }, 10);

    setTimeout(()=>{
        messageBox.style.opacity = 0;
        messageBox.style.display = "none";
    }, 3000);
}

function showDialog(dialogId){
    let dialog = document.getElementById(dialogId);

    dialog.style.display= "block";
    setTimeout(() => {
        dialog.style.opacity=1;
    }, 10);
}

function closeDialog(dialog, dialogId){
    if(dialog==null){
        dialog = document.getElementById(dialogId);
    }

    dialog.style.opacity=0;
    setTimeout(() => {

        dialog.style.display= "none";
    }, 200);
}