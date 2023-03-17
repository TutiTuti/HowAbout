const myPageLocation = document.getElementById("myPageLocation");
const myCourseContainerLocation = document.getElementById("myPageContainerLocation");
const myFavoriteCourseLink = document.getElementById("myFavoriteCourseLink");
const myInfoLink = document.getElementById("myInfoLink");
const loginLogout = document.getElementById("loginLogout");

const token = sessionStorage.getItem('jwt');

window.addEventListener("DOMContentLoaded", () => {
	if (token != null) {
		myFavoriteCourseLink.style.display = "inline";
		myInfoLink.style.display = "inline";
		loginLogout.innerText = "로그아웃";
	} else {
		sessionStorage.clear();
	}
})

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


// myPageLocation.addEventListener("click", () => {
//
// 	const req = "leehj"
//
// 	fetch("/myPage/myInfo/webData", {
// 		method: "POST",
// 		headers: { "Content-Type": "application/json" },
// 		body: req,
// 	})
// 		.then((response) => response.text())
// 		.then((html) => {
// 			let parser = new DOMParser();
// 			let doc = parser.parseFromString(html, 'text/html');
//
// 			console.log(doc);
// 			window.document.innterHTML = doc;
//
// 		})
// 		.catch((err) => {
// 			console.log("Failed to fetch page: ", err)
// 		})
//
// })