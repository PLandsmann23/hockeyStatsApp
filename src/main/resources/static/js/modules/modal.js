
    function openModal(modalName){
    let modal = document.getElementById(modalName);
    modal.style.display= "block";
    setTimeout(() => {
    modal.style.opacity=1;
}, 10);
}

    function closeModal(modal, modalId){
    if(modal==null){
    modal = document.getElementById(modalId);
}

    for(let input of modal.querySelectorAll("input")){
        input.value = "";
        input.classList.remove("error-input");
        }
    for (let errorSpan of modal.querySelectorAll("span.error-message")){
        errorSpan.innerHTML = "";
    }
    modal.style.opacity=0;
    setTimeout(() => {

    modal.style.display= "none";
}, 300);
}



    document.addEventListener("click", (evt)=>{
    document.querySelectorAll('.modal').forEach(function(modal) {
        if (evt.target === modal) {
            closeModal(modal);
        }
    });
});

    window.addEventListener('keydown', function(event) {
    if (event.key === "Escape") {
    document.querySelectorAll('.modal').forEach(function(modal) {
    if (modal.style.display === "block") {
    closeModal(modal);
}
});
}
});