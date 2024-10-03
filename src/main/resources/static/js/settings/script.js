function validate(){
    let newPass = document.querySelector('#new-password');
    let newPassRepeat = document.querySelector('#new-password-repeat');
    let newPassError = document.querySelector('#e-new-password');
    let newPassRepeatError = document.querySelector('#e-new-password-repeat');
    let submit = document.querySelector("#change-pass-button");

    if(newPass.value.length > 0){
        newPass.classList.remove("error-input");
        newPassError.innerText = "";
    } else {
        newPass.classList.add("error-input");
        newPassError.innerText = "Heslo nesmí být prázdné";
    }

    if(newPass.value === newPassRepeat.value){
        newPassRepeat.classList.remove("error-input");
        newPassRepeatError.innerText = "";
    } else {
        newPassRepeat.classList.add("error-input");
        newPassRepeatError.innerText = "Hesla se neshodují";
    }

    if(newPass.value.length > 0 && newPass.value === newPassRepeat.value){
        submit.disabled = false;

    }   else {
        submit.disabled = true;
    }
}

document.querySelector(".change-password form").addEventListener("submit", async (ev )=>{
    ev.preventDefault();

    let oldPassword = document.querySelector('#old-password').value;
    let newPassword = document.querySelector('#new-password').value;
    let newPassRepeat = document.querySelector('#new-password-repeat');

    let form = document.querySelector('.change-password form');

    let formData = new FormData(form);

    let oldPasswordValue = formData.get("old-password");
    let newPasswordValue = formData.get("new-password");

    try {
        const response = await fetch("/api/users/changepassword", {headers: {"Content-Type": "application/json"},
            method: "POST",
            body: JSON.stringify({
                oldPassword: oldPasswordValue,
                newPassword: newPasswordValue
            })
        });

        if (response.ok) {
            showMessage("Heslo bylo změněno", "success");
        } else {

            throw await response.json();
        }
    } catch (e){
        showMessage(e.message, "error");
    }
});