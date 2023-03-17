const user_id = document.getElementById("user_id");
const new_password = document.getElementById("new_password");
const re_new_password = document.getElementById("re-new_password");
const new_nickname = document.getElementById("new-nickname");
const save_data = document.getElementById("save_data");
const new_birth_year = document.getElementById("birth-box__year");
const new_birth_month = document.getElementById("birth-box__month");
const new_birth_day = document.getElementById("birth-box__day");
const loginLogout = document.getElementById("loginLogout");
const women = document.getElementById("women");
const men = document.getElementById("men");
const token = sessionStorage.getItem('jwt');
const tokenHead = "Bearer " + token;
let userNick;

// let passPass = false;
// let nickPass = false;

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
        .then((infoData) => {
            console.log(infoData);
            let idUser = infoData.u_id;
            user_id.value = idUser;
            let nickUser = infoData.u_nick;
            userNick = infoData.u_nick;
            console.log(userNick);
            new_nickname.value = nickUser;
            let birthUser = infoData.birth;
            const word = birthUser.split("-");
            console.log(word[0]);
            console.log(word[1]);
            console.log(word[2]);
            new_birth_year.options[new_birth_year.selectedIndex].text = word[0];
            new_birth_month.options[new_birth_month.selectedIndex].text = word[1];
            new_birth_day.options[new_birth_day.selectedIndex].text = word[2];
            if (infoData.gender == 0) {
                women.checked = true;
            } else {
                men.checked = true;
            }
        });
});

// new password checked
re_new_password.addEventListener("blur", () => {
    if (re_new_password.value === "" && new_password.value === "") {
        console.log("기존비번");
    } else if (re_new_password.value != new_password.value) {
        console.log("비밀번호 불일치");
        console.log("비밀번호: " + new_password.value + " 비밀번호 재입력: " + re_new_password.value);
    } else {
        console.log("비밀번호 일치");
        passPass = true;
    }
});
// new password checked


// new nickname checked
new_nickname.addEventListener("blur", () => {
    if (new_nickname.value === userNick) {
        alert("기존 닉네임");
    } else {
        const new_nick = {u_nick: new_nickname.value,};

        console.log(new_nick);
        console.log(JSON.stringify(new_nick));
        fetch("/login/signUp/nickCheck", {
            method: "POST",
            headers: {"Content-type": "application/json"},
            body: JSON.stringify(new_nick),
        })
            .then((response) => response.json())
            .then((newnickdata) => {
                console.log("newnickdata : " + newnickdata);
                if (Number(newnickdata) == 1) {
                    alert("사용할 수 있는 닉네임입니다.");
                    nickPass = true;
                } else if (Number(newnickdata) == 0) {
                    alert("중복입니다. 다시 입력해주세요. ");
                } else if (Number(newnickdata) == 2) {
                    alert("서버 오류");
                }
            });
    }
});
// new nickname checked

// user info saved

save_data.addEventListener("click", () => {
    // if (passPass == true && nickPass == true) {
    if (re_new_password.value === "" && new_password.value === "") {
        const req = {
            u_nick: new_nickname.value,
            gender: document.querySelector('input[name="new-gender"]:checked').value,
            birth:
                new_birth_year.value +
                "-" +
                new_birth_month.value +
                "-" +
                new_birth_day.value,
        };
        // const arrReq = [];
        // arrReq.push(req);

        console.log(req);
        console.log(JSON.stringify(req));
        fetch("/myPage/myInfo/updateInfo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": tokenHead,
            },
            body: JSON.stringify(req),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log("data: " + data.jwt);
                alert("변경되었습니다.");
                location.href = "/myPage/myInfo/webData";
            });
        // }
    } else {
        const req = {
            u_nick: new_nickname.value,
            u_pw: new_password.value,
            gender: document.querySelector('input[name="new-gender"]:checked').value,
            birth:
                new_birth_year.value +
                "-" +
                new_birth_month.value +
                "-" +
                new_birth_day.value,
        };
        // const arrReq = [];
        // arrReq.push(req);

        console.log(req);
        console.log(JSON.stringify(req));
        fetch("/myPage/myInfo/updateInfo", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": tokenHead,
            },
            body: JSON.stringify(req),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log("data: " + data.jwt);
                alert("변경되었습니다.");
                location.href = "/myPage/myInfo/webData";
            });
        // }
    }
});
// user info saved

// select -> year
const birthYearEl = document.querySelector("#birth-box__year");
isYearOptionExisted = false;
birthYearEl.addEventListener("focus", () => {
    if (!isYearOptionExisted) {
        isYearOptionExisted = true;
        for (let i = 1940; i <= 2022; i++) {
            const YearOption = document.createElement("option");
            YearOption.setAttribute("value", i);
            YearOption.innerText = i;
            birthYearEl.appendChild(YearOption);
        }
    }
});

// select -> month
const birthMonthEl = document.querySelector("#birth-box__month");
isMonthOptionExisted = false;
birthMonthEl.addEventListener("focus", () => {
    if (!isMonthOptionExisted) {
        isMonthOptionExisted = true;
        for (let i = 1; i <= 12; i++) {
            const MonthOption = document.createElement("option");
            if (i < 10) {
                MonthOption.setAttribute("value", "0" + i);
            } else {
                MonthOption.setAttribute("value", i);
            }
            MonthOption.innerText = i;
            birthMonthEl.appendChild(MonthOption);
        }
    }
});

// select -> day
const birthDayEl = document.querySelector("#birth-box__day");
isDayOptionExisted = false;
birthDayEl.addEventListener("focus", () => {
    if (!isDayOptionExisted) {
        isDayOptionExisted = true;
        for (let i = 1; i <= 31; i++) {
            const DayOption = document.createElement("option");
            if (i < 10) {
                DayOption.setAttribute("value", "0" + i);
            } else {
                DayOption.setAttribute("value", i);
            }
            DayOption.innerText = i;
            birthDayEl.appendChild(DayOption);
        }
    }
});