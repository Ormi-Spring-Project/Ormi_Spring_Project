document.addEventListener("DOMContentLoaded", function() {

    if (sessionStorage.getItem("scrollPosition")) {
        window.scrollTo(0, parseInt(sessionStorage.getItem("scrollPosition")));
        sessionStorage.removeItem("scrollPosition");
    }

    document.querySelectorAll("button").forEach(button => {
        button.addEventListener('click', function() {
            sessionStorage.setItem("scrollPosition", window.scrollY.toString());
        });
    });
});
