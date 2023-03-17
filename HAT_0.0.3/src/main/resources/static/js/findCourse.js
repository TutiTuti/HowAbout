// 반경 설정
const radiusButton = document.querySelectorAll("button");
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

// 마커 커스텀 인데여;;
var imageSrc = "../images/marker_img.png",
    imageSize = new kakao.maps.Size(64, 69),
    imageOption = {offset: new kakao.maps.Point(27, 69)};

var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
// 마커 커스텀 인데여;;

// 지도에 마커와 인포윈도우를 표시하는 함수입니다
function displayMarker(locPosition, message) {
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: locPosition,
        image: markerImage,
    });

    var content =
        '<div class="custom_content">' + "<span>" + message + "</span>" + "</div>";

    // 커스텀 오버레이가 표시될 위치입니다
    var customOverlay = new kakao.maps.CustomOverlay({
        map: map,
        position: locPosition,
        content: content,
        yAnchor: 1,
    });
    customOverlay.setMap(null);

    // 마커가 최상단에서만 마우스를 인식함,,,
    kakao.maps.event.addListener(marker, "click", () => {
        customOverlay.setMap(map);
    });

    kakao.maps.event.addListener(marker, "mouseout", () => {
        customOverlay.setMap(null);
    });

    // 지도 중심좌표를 접속위치로 변경합니다
    map.setCenter(locPosition);
}