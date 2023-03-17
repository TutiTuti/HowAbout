const loginId = document.getElementById("loginid");
const loginPw = document.getElementById("loginpw");
const loginBtn = document.getElementById("loginbtn");

// 로그인 버튼 클릭시
// window.onload = () => {
//   const el = document.getElementById("loginBtn");
//   el.onclick = loginBtn;
//   ``;
// };

//로그인 버튼 클릭시
loginBtn.addEventListener("click", (e) => {
  e.preventDefault();

  const req = {
    u_id: loginId.value,
    u_pw: loginPw.value,
  };

  console.log(req);
  console.log(JSON.stringify(req));

  if (loginId.value == "") {
    alert("아이디를 입력해주세요");
  } else if (loginPw.value == "") {
    alert("비밀번호를 입력해주세요");
  } else {
    console.log("로그인 성공");

    fetch("/login/signIn", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(req),
    })
      .then((response) => response.json())
      .then((data) => {
        //fetch 이후 작동할 코드
        console.log("response: " + data + "TYPE: " + typeof data);
        console.log(data);
        if (data.success == 1) {
          // form.submit();
          alert(data.msg+"님 환영합니다");
          sessionStorage.setItem('jwt', data.token);
          location.href = "/mainPage";
        } else {
          alert(data.msg);
        }
      });
  }
});