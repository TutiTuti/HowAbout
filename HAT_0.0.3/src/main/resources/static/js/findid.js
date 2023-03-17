const findIdEmail = document.getElementById("findId-email")
const findIdBtn = document.getElementById("findId__button")
const yourIdContainer = document.getElementById("yourIdContainer");
const yourId = document.getElementById("yourId");
const token = sessionStorage.getItem('jwt');
const tokenHead = "Bearer " + token;

findIdBtn.addEventListener("click", () => {

    const req = {
        u_email: findIdEmail.value,
    };

    fetch("/login/findMyId", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(req),
    })
        .then((response) => response.json())
        .then((findIdData) => {
            console.log(findIdData);
            yourIdContainer.style.display = "block";
            yourId.value = findIdData.u_id;
        });

})
