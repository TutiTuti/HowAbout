const userEmail = document.getElementById("user_email");
const userId = document.getElementById("user_id");
const userPw = document.getElementById("user_pw");
const userPwRe = document.getElementById("user_pw_re");
const userNick = document.getElementById("user_nick");
const birthYear = document.getElementById("birth-year");
const birthMonth = document.getElementById("birth-month");
const birthDay = document.getElementById("birth-day");
const form = document.getElementById("signup-form");
const email_error = document.getElementById("email_error");
const id_error = document.getElementById("id_error");
const pw_error = document.getElementById("pw_error");
const pw_re_error = document.getElementById("pw_re_error");
const nick_error = document.getElementById("nick_error");

function goReplace(str) {
  location.replace(str);
}

// 회원가입 버튼 클릭시
window.onload = () => {
  const el = document.getElementById("signupBtn");
  el.onclick = signup;
  ``;
};

const aaa = document.getElementById("user_id");

const signup = () => {
  const req = {
    u_email: userEmail.value,
    u_id: userId.value,
    u_pw: userPw.value,
    u_nick: userNick.value,
    birth: birthYear.value + "-" + birthMonth.value + "-" + birthDay.value,
    gender: document.querySelector('input[name="gender"]:checked').value,
  };

  console.log(req);
  console.log(JSON.stringify(req));
  fetch("/login/signUp", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("response : " + data + " TYPE : " + typeof data);
      if (Number(data) == 1) {
        alert("가입완료");
        location.href="/mainPage";
      } else {
        alert("다시 확인해!!!!");
      }
    });
  //});
};

//이메일 입력창 start
userEmail.addEventListener("blur", () => {
  if (userEmail.value == "") {
    email_error.innerHTML = "필수 입력 값입니다.";
    email_error.classList.add("error-design");
  } else {
    email_error.classList.remove("error-design");
    const req = {
      u_email: userEmail.value,
    };

    // console.log(req);
    console.log(JSON.stringify(req));
    fetch("/login/signUp/emailCheck", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(req),
    })
      .then((response) => response.json())
      .then((uEmail) => {
        console.log("response : " + uEmail + "TYPE : " + typeof data);
        if (Number(uEmail) == 1) {
          email_error.innerHTML = "사용할 수 있는 이메일입니다.";
        } else {
          //alert("중복인 것 같은데요?");
        }
      });
  }
});

// 아이디 입력창 start
userId.addEventListener("blur", () => {
  if (userId.value == "") {
    id_error.innerHTML = "필수 입력 값입니다.";
    id_error.classList.add("error-design");
  } else {
    id_error.classList.remove("error-design");
    const req = {
      u_id: userId.value,
    };

    // console.log(req);
    console.log(JSON.stringify(req));
    fetch("/login/signUp/idCheck", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(req),
    })
      .then((response) => response.json())
      .then((uId) => {
        console.log("response : " + uId + "TYPE : " + typeof data);
        if (Number(uId) == 1) {
          id_error.innerHTML = "사용할 수 있는 아이디입니다.";
        } else {
          alert("중복인 것 같은데요?");
        }
      });
  }
});

userId.addEventListener("focus", () => {
  document.getElementById("id_error").innerHTML = "";
  id_error.classList.remove("error-design");
});
// 아이디 입력창 end

// 비밀번호 입력창 start
userPw.addEventListener("blur", () => {
  if (userPw.value == "") {
    pw_error.innerHTML = "필수 입력 값입니다.";
    pw_error.classList.add("error-design");
  }
});

userPw.addEventListener("focus", () => {
  document.getElementById("pw_error").innerHTML = "";
  pw_error.classList.remove("error-design");
});
// 비밀번호 입력창 end

// 비밀번호 확인 입력창 start
userPwRe.addEventListener("blur", () => {
  if (userPw.value != userPwRe.value) {
    console.log("틀렸어요.");
    pw_re_error.innerHTML = "비밀번호가 일치하지 않습니다.";
    pw_re_error.classList.add("error-design");
    userPw.classList.add("invalid");
    userPwRe.classList.add("invalid");
  } else if (userPwRe.value == "") {
    pw_re_error.innerHTML = "필수 입력 값입니다.";
    pw_re_error.classList.add("error-design");
  } else {
    console.log("맞았어요.");
    userPw.classList.remove("invalid");
    userPwRe.classList.remove("invalid");
  }
});

userPwRe.addEventListener("focus", () => {
  document.getElementById("pw_re_error").innerHTML = "";
  pw_re_error.classList.remove("error-design");
});
// 비밀번호 확인 입력창 end

// 닉네임 입력창 start
userNick.addEventListener("blur", () => {
  if (userNick.value == "") {
    nick_error.innerHTML = "필수 입력 값입니다.";
    nick_error.classList.add("error-design");
  } else {
    const req = {
      u_nick: userNick.value,
    };

    console.log(JSON.stringify(req));
    fetch("/login/signUp/nickCheck", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(req),
    })
      .then((response) => response.json())
      .then((uNick) => {
        console.log("response : " + uNick + "TYPE : " + typeof data);
        if (Number(uNick) == 1) {
          nick_error.innerHTML = "멋진 닉네임이네요.";
        } else {
          alert("중복인 것 같은데요?");
        }
      });
  }
});

userNick.addEventListener("focus", () => {
  document.getElementById("nick_error").innerHTML = "";
  nick_error.classList.remove("error-design");
  userNick.classList.remove("invalid");
});
// 닉네임 입력창 end

// select -> year
const birthYearEl = document.querySelector("#birth-year");
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
const birthMonthEl = document.querySelector("#birth-month");
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
const birthDayEl = document.querySelector("#birth-day");
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