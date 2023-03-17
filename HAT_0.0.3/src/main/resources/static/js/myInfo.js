const myInfoSettingButton = document.getElementById("myInfoSettingButton");
const passContainer = document.getElementById("pass_container");
const quit = document.getElementById("quit");
const identification = document.getElementById("userImportant");
const userPw = document.getElementById("userPw");
const userId = document.getElementById("userId");
const userEmail = document.getElementById("userEmail");
const userNick = document.getElementById("userNick");
const userGender = document.getElementById("userGender");
const userBirth = document.getElementById("userBirth");
const loginLogout = document.getElementById("loginLogout");

const token = sessionStorage.getItem('jwt');
const tokenHead = "Bearer " + token;

window.addEventListener("DOMContentLoaded", () => {

    if (token != null) {
        loginLogout.innerText = "로그아웃";
    } else {
        sessionStorage.clear();
    }

    loginLogout.addEventListener("click", () => {
        if (token == null) {
            location.href = "/login/signIn";
        } else {
            const logout = confirm("정말 떠나실 거에요? ㅠㅠ");
            if (logout) {
                sessionStorage.clear();
                location.href = "/mainPage";
            } else {
                alert("히히")
            }
        }
    })

    fetch("/myPage/myInfo/getMyData", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": tokenHead,
        },
    })
        .then((response) => response.json())
        .then((myPageData) => {
            console.log(myPageData);
            let idUser = myPageData.u_id;
            userId.value = idUser;
            let emailUser = myPageData.u_email;
            userEmail.value = emailUser;
            let nickUser = myPageData.u_nick;
            userNick.value = nickUser;
            let birthUser = myPageData.birth;
            userBirth.value = birthUser;
            if (myPageData.gender == 0) {
                userGender.value = "여자"
            } else {
                userGender.value = "남자"
            }
        })
});

myInfoSettingButton.addEventListener("click", () => {
    passContainer.style.display = "flex";
});

quit.addEventListener("click", () => {
    passContainer.style.display = "none";
})

//까아아아아아아아ㅏ아앙아아ㅏ아아아아아악 ~~~~~ 
identification.addEventListener("click", () => {
    const req = {
        u_pw: userPw.value,
    }
    fetch("/myPage/myInfo/CheckPW", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": tokenHead,
        },
        body: JSON.stringify(req),
    })
        .then((response) => response.json())
        .then((userData) => {
            console.log("받은 데이터: " + userData);
            if (Number(userData) == 1) {
                location.href="/myPage/myInfo/webDatasetting"
            } else {
                alert("비밀번호 틀림");
            }
        })

})

