async function handleRegister() {
     // Zastaví odeslání formuláře


    const body = {
        username: document.querySelector("#username").value,
            email:document.querySelector("#email").value,
        password:document.querySelector("#password").value
    }

    const userError = document.querySelector("#username-error");
    const emailError = document.querySelector('#email-error');
    const passwordError = document.querySelector("#password-error");

    const response  = await fetch("api/users/register", {
        headers: {"Content-Type": "application/json"},
        method: 'POST',
        body: JSON.stringify(body)
    });


        if(response.status === 200){
            window.location.href = '/login?register';
     /*       data = await response.json();
            if (data.token) {
                // Uložit JWT do sessionStorage
                sessionStorage.setItem('jwtToken', data.token);
               // Přesměrování po přihlášení
            }

        } else{
            data = await response.json();
            if (data.username) {
                userError.innerText = data.username;
            }
            if (data.password) {
                passwordError.innerText = data.password;
            } */
        } else{
            let data = await response.json();

            if(data.username){
                userError.innerText = data.username;
            }

            if(data.email){
                emailError.innerText = data.email;
            }
        }



}

document.querySelector('#username').addEventListener("focusout", (event)=>{
            let errorSpan = document.querySelector('#username-error');
            if(event.target.value <1){
                errorSpan.innerText = "Uživatelské jméno musí být vyplněno";
            } else {
                errorSpan.innerText = "";

            }
        });


document.querySelector('#email').addEventListener("focusout", (event)=>{
    let errorSpan = document.querySelector('#email-error');
    let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if(event.target.value <1){
        errorSpan.innerText = "Email musí být vyplněn";
    } else {
        errorSpan.innerText = "";
    }
    if(!event.target.value.match(mailformat)){
        errorSpan.innerText = "Email není ve správném formátu";

    } else {
        errorSpan.innerText = "";

    }

});

document.querySelector('#password').addEventListener("focusout", (event)=>{
    let errorSpan = document.querySelector('#password-error');
    if(event.target.value <1){
        errorSpan.innerText = "Heslo musí být vyplněno";
    } else if(event.target.value <6) {
        errorSpan.innerText = "Heslo musí mít alespoň 6 znaků";
    }else
     {
        errorSpan.innerText = "";
    }
});

document.querySelector('#password-repeat').addEventListener("keyup", (event)=>{
    let errorSpan = document.querySelector('#password-repeat-error');
    let password = document.querySelector('#password').value;
    if(event.target.value === password){
        errorSpan.innerText = "";
    } else {
        errorSpan.innerText = "Hesla se musí shodovat";
    }
});


document.querySelectorAll('input').forEach(
    input =>{
        input.addEventListener("keyup",()=>{
            let button = document.querySelector('button#register');
            let username = document.querySelector('#username');
            let email = document.querySelector('#email');
            let password = document.querySelector('#password');
            let passwordrepeat = document.querySelector('#password-repeat');
            let mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;

            if(username.value !== "" && email.value !== "" && email.value.match(mailformat)&& password.value.length >= 6 && password.value === passwordrepeat.value){
                button.disabled = false;
            } else {
                button.disabled = true;
            }


        })
    }
)


document.addEventListener('DOMContentLoaded',(event)=>{

    if(new URLSearchParams(window.location.search).has("logout")){
        showMessage("Uživatel byl odhlášen", "success")
    }
})

document.addEventListener('keypress',(event)=>{
    let button = document.querySelector('button#register');
    if(event.key === "Enter" && button.disabled === false){
        handleRegister();
    }

})