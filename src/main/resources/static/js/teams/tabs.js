function switchTab(tabName) {
    var i;
    var x = document.getElementsByClassName("tab-content");
    var tabs = document.getElementsByClassName("tab");
    for (i = 0; i < x.length; i++) {
        x[i].classList.remove("active");
    }
    for (i = 0; i < tabs.length; i++) {
        tabs[i].classList.remove("active");
    }
    document.getElementById(tabName).classList.add("active");
    event.currentTarget.classList.add("active");
}