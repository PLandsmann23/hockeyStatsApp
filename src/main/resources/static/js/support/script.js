function validate(){
    let subject = document.querySelector('#subject');
    let message = document.querySelector('#message');
    let subjectError = document.querySelector('#e-subject');
    let messageError = document.querySelector('#e-message');
    let submit = document.querySelector("#send-message-button");

    if(subject.value.length > 0){
        subject.classList.remove("error-input");
        subjectError.innerText = "";
    } else {
        subject.classList.add("error-input");
        subjectError.innerText = "Předmět musí být vyplněn";
    }

    if(message.value.length > 0){
        message.classList.remove("error-input");
        messageError.innerText = "";
    } else {
        message.classList.add("error-input");
        messageError.innerText = "Zpráva musí být vyplněna";
    }



    submit.disabled = !(subject.value.length > 0 && message.value.length > 0);
}

document.querySelector(".send-support-message form").addEventListener("submit", async (ev )=>{
    ev.preventDefault();

    let subject = document.querySelector('#subject');
    let message = document.querySelector('#message');

    let form = document.querySelector('.send-support-message form');

    let formData = new FormData(form);

    let subjectValue = formData.get("subject");
    let messageValue = formData.get("message");

    try {
        const response = await fetch("/api/support/send", {headers: {"Content-Type": "application/json"},
            method: "POST",
            body: JSON.stringify({
                subject: subjectValue,
                message: messageValue
            })
        });

        if (response.ok) {
            showMessage("Zpráva byla odeslána", "success");
            subject.value = "";
            message.value = "";
        } else {

            throw await response.json();
        }
    } catch (e){
        showMessage(e.message, "error");
    }
});