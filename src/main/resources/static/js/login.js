/*
async function handleLogin(event) {
    event.preventDefault(); // Zastaví odeslání formuláře

    const form = event.target;
    const formData = new FormData(form);

    const userError = document.querySelector("#username-error");
    const passwordError = document.querySelector("#password-error");

    const response  = await fetch(form.action, {
        method: 'POST',
        body: new URLSearchParams(formData),
    });
        let data;

        if(response.ok){
            data = await response.json();
            if (data.token) {
                // Uložit JWT do sessionStorage
                sessionStorage.setItem('jwtToken', data.token);
                window.location.href = '/'; // Přesměrování po přihlášení
            }

        } else{
            data = await response.json();
            if (data.username) {
                userError.innerText = data.username;
            }
            if (data.password) {
                passwordError.innerText = data.password;
            }
        }



}
*/

document.addEventListener('DOMContentLoaded',(event)=>{

    if(new URLSearchParams(window.location.search).has("logout")){
        showMessage("Uživatel byl odhlášen", "success")
    }

    if(new URLSearchParams(window.location.search).has("register")){
        showMessage("Registrace byla úspěšná, nyní se můžete přihlásit", "success")
    }

    if(new URLSearchParams(window.location.search).has("error")){
        if(new URLSearchParams(window.location.search).get("error")==="un"){
            document.querySelector("#username-error").innerText = "Uživatelské jméno nebo email nejsou správné";
        }
        if(new URLSearchParams(window.location.search).get("error")==="p"){
            document.querySelector("#password-error").innerText = "Heslo není správné";
        }
        if(new URLSearchParams(window.location.search).get("error")==="ne"){
            document.querySelector("#username-error").innerText = "Účet nebyl ověřen, nejprve účet ověřte pomocí emailu";
        }


    }
})