const findPwId = document.getElementById("findPw-id");
const findPwEmail = document.getElementById("findPw-email");
const findPwBtn = document.getElementById("findPw__button");
const findPwInfoEmail = document.getElementById("findPw-info__email");
const yourPwContainer = document.getElementById("yourPwContainer");
const yourNewPw = document.getElementById("yourNewPw");

findPwBtn.addEventListener("click", () => {
    if (yourPwContainer.style.display === '') {

        const req = {
            u_id: findPwId.value,
            u_email: findPwEmail.value,
        };

        fetch("/login/checkMyInfo", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(req),
        })
            .then((response) => response.json())
            .then((findPwData) => {
                console.log(findPwData);
                if (findPwData === 1) {
                    yourPwContainer.style.display = "flex";
                    findPwInfoEmail.style.display = "none";
                } else {
                    alert("아이디 혹은 이메일을 다시 확인하세요.");
                }
            });

    } else {

        const req = {
            u_id: findPwId.value,
            u_pw: yourNewPw.value,
        };

        fetch("/login/setNewPw", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(req),
        })
            .then((response) => response.json())
            .then((findPwData) => {
                console.log(findPwData);
                if (findPwData === 1) {
                    alert("변경되었습니다.");
                    location.href = "/mainPage";
                } else {
                    alert("오류");
                }
            });

    }
})
