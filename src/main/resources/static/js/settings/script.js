document.addEventListener("DOMContentLoaded",()=>{
    let settings = JSON.parse(sessionStorage.getItem("settings"));

    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');

    newLength.value = settings.defaultPeriodLength;
    newPeriods.value = settings.defaultPeriods;

});

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

function validateSettings(){
    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');
    let newLengthError = document.querySelector('#e-defaultPeriodLength');
    let newPeriodsError = document.querySelector('#e-defaultPeriods');
    let submit = document.querySelector("#change-settings-button");

    if(newLength.value.length > 0){
        newLength.classList.remove("error-input");
        newLengthError.innerText = "";
    } else {
        newLength.classList.add("error-input");
        newLengthError.innerText = "Délka nesmí být prázdná";
    }

    if(newPeriods.value.length > 0){
        newPeriods.classList.remove("error-input");
        newPeriodsError.innerText = "";
    } else {
        newPeriods.classList.add("error-input");
        newPeriodsError.innerText = "Délka nesmí být prázdná";
    }



    if(newLength.value.length > 0 && newPeriods.value.length > 0){
        submit.disabled = false;

    }   else {
        submit.disabled = true;
    }
}

document.querySelector(".settings-form:first-child form").addEventListener("submit", async (ev )=>{
    ev.preventDefault();

    let oldPassword = document.querySelector('#old-password').value;
    let newPassword = document.querySelector('#new-password').value;
    let newPassRepeat = document.querySelector('#new-password-repeat');

    let form = document.querySelector('.settings-form form:first-child');

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

document.querySelector(".settings-form:nth-child(2) form").addEventListener("submit", async (ev )=>{
    ev.preventDefault();

    let newLength = document.querySelector('#defaultPeriodLength');
    let newPeriods = document.querySelector('#defaultPeriods');


    let data = {
        defaultPeriodLength: newLength.value,
        defaultPeriods: newPeriods.value
    }

    try {
        const response = await fetch("/api/users/settings", {headers: {"Content-Type": "application/json"},
            method: "POST",
            body: JSON.stringify(data)
        });

        if (response.ok) {
            showMessage("Nastavení bylo změněno", "success");
            sessionStorage.setItem("settings", JSON.stringify(await response.json()));
        } else {

            throw await response.json();
        }
    } catch (e){
        showMessage(e.message, "error");
    }
});