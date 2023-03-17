const findCourseListArea = document.getElementById("placesList");
const startArea = document.getElementById("startChoice");
const secondArea = document.getElementById("resChoice");
const thirdArea = document.getElementById("cafeChoice");
const addDateListButton = document.getElementsByClassName("addDateListButton");
const choiceComplete = document.getElementById("choiceComplete");
const zzim = document.getElementById("zzim");

const heretoken = sessionStorage.getItem("jwt");
const heretokenHead = "Bearer " + heretoken;

// 위치
let startPosition, secondPosition, thirdPosition;
// 장소
let rId, cId;
let restJson, cafeJson;

//마커를 담을 배열
var markers = [];
let polylines = [];
let customoverlays = [];

let readyStr = "나만의 코스 만들기";
addInfoItems(findCourseListArea, readyStr);

choiceComplete.addEventListener("click", () => {
    // 마커, 커스텀오버레이, 리스트 아이템 제거
    removeMarker();
    removePolyline();
    removeCustomOverlayee();
    removeAllChildNods(findCourseListArea);
    // 마커, 커스텀오버레이, 리스트 아이템 제거

    // 마커, 폴리라인 추가
    addMarker(startPosition, null);
    addMarker(secondPosition, null);
    addMarker(thirdPosition, null);
    addPolyline(startPosition, secondPosition, thirdPosition);
    let completeStr = "코스완성!"
    addInfoItems(findCourseListArea, completeStr);
    // 마커, 폴리라인 추가

    if (token !== null) {
        const arrReq = [];
        arrReq.push(restJson);
        arrReq.push(cafeJson);

        console.log(arrReq);
        fetch("/myCourse/saveCourse", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": heretokenHead,
            },
            body: JSON.stringify(arrReq),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log(data);
                if (data.flag === 1) {
                    // 찜아이콘 변경
                    zzim.src = 'http://localhost/images/pullheart.png';
                } else {
                    zzim.src = 'http://localhost/images/heart.png';
                }
            })
    } else {
        alert("매칭완료");
    }
})

var mapContainer = document.getElementById("map"), // 지도를 표시할 div
    mapOption = {
        center: new kakao.maps.LatLng(37.56646, 126.98121), // 지도의 중심좌표
        level: 3, // 지도의 확대 레벨
    };

// 지도를 생성한다
var map = new kakao.maps.Map(mapContainer, mapOption);

//현재 위치 가져오기
function locationLoadSuccess(pos) {
    // 현재 위치 받아오기
    var currentPos = new kakao.maps.LatLng(
        pos.coords.latitude,
        pos.coords.longitude
    );

    startPosition = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);

    // 지도 이동(기존 위치와 가깝다면 부드럽게 이동)
    map.panTo(currentPos);

    // 마커를 생성하고 지도에 표시합니다
    var placePosition = new kakao.maps.LatLng(
            pos.coords.latitude,
            pos.coords.longitude
        ),
        marker = addMarker(placePosition, null); // 검색 결과 항목 Element를 생성합니다

    // 기존에 마커가 있다면 제거
    marker.setMap(null);

    // 이상하게 뜨는 건 여전함
    // marker.setMap(map);

    // ================================== 현재위치를 기준으로 검색을 해서 선택할 수 있게 보여줘야함 ==================================
    removeAllChildNods(startArea);
    locationItems(startArea);
    let choiceRes = "식당을 선택하세요!";
    addInfoItems(findCourseListArea, choiceRes);
    // ================================== 현재위치를 기준으로 검색을 해서 선택할 수 있게 보여줘야함 ==================================

    // 내위치 기준으로 선택했을 경우의 레스토랑, 카페 검색 이벤트 시작
    let resButton = document.getElementsByClassName("res-button");
    let cafeButton = document.getElementsByClassName("cafe-button");

    // 레스토랑 검색
    for (let j = 0; j < resButton.length; j++) {
        resButton[j].addEventListener("click", () => {
            var places = new kakao.maps.services.Places();

            // 카테고리로 은행을 검색합니다
            ps.categorySearch('FD6', secondPlacesSearchCB, {
                useMapBounds: true,
                location: new kakao.maps.LatLng(
                    pos.coords.latitude,
                    pos.coords.longitude
                ),
                radius: cafeButton[j].value
            });
        });
    }
    // 카페 검색
    for (let k = 0; k < cafeButton.length; k++) {
        cafeButton[k].addEventListener("click", () => {
            var places = new kakao.maps.services.Places();

            // 카테고리로 은행을 검색합니다
            ps.categorySearch('CE7', thirdPlacesSearchCB, {
                useMapBounds: true,
                location: new kakao.maps.LatLng(
                    pos.coords.latitude,
                    pos.coords.longitude
                ),
                radius: cafeButton[k].value
            });
        });
    }


    // 내위치 기준으로 선택했을 경우의 레스토랑, 카페 검색 이벤트 끝
}

function locationLoadError(pos) {
    alert("위치 정보를 가져오는데 실패했습니다.");
}

// 위치 가져오기 버튼 클릭시
function getCurrentPosBtn() {
    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();

    navigator.geolocation.getCurrentPosition(
        locationLoadSuccess,
        locationLoadError
    );
}

// 현재위치 가져오기 끝

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
var infowindow = new kakao.maps.InfoWindow({zIndex: 1});

// 키워드로 장소를 검색합니다
searchPlaces();

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {
    var keyword = document.getElementById("find-course__searchbar").value;

    if (!keyword.replace(/^\s+|\s+$/g, "")) {
        // alert("키워드를 입력해주세요!");
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch(keyword, placesSearchCB);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);

        // 페이지 번호를 표출합니다
        // 잠깐 숨어있어 형이 너 때문에 눈물이 난다.
        // displayPagination(pagination);
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert("검색 결과가 존재하지 않습니다.");
        return;
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert("검색 결과 중 오류가 발생했습니다.");
        return;
    }

    // 아이템을 클릭했을 때 y, x 좌표가 뜬다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //let items = document.getElementsByClassName("item");

    for (let i = 0; i < addDateListButton.length; i++) {
        addDateListButton[i].addEventListener("click", () => {
            // 첫 번째 아이템 클릭했을 때
            removeAllChildNods(findCourseListArea);
            console.log(data[i].place_name);
            console.log(data[i].road_address_name);
            console.log(data[i].address_name);
            console.log(data[i].y + ", " + data[i].x);
            startPosition = new kakao.maps.LatLng(data[i].y, data[i].x);

            // 시작 영역에 띄울 거임
            removeAllChildNods(startArea);
            addItems(data[i], startArea);
            // 시작 영역에 띄울 거임

            // 마커
            var listEl = document.getElementById("placesList"),
                menuEl = document.getElementsByClassName("find-course-list__container"),
                fragment = document.createDocumentFragment(),
                bounds = new kakao.maps.LatLngBounds(),
                listStr = "";

            // 검색 결과 목록에 추가된 항목들을 제거합니다
            removeAllChildNods(listEl);
            let choiceRes = "식당을 선택하세요!";
            addInfoItems(listEl, choiceRes);
            // 지도에 표시되고 있는 마커를 제거합니다
            removeMarker();

            // 마커를 생성하고 지도에 표시합니다
            var placePosition = new kakao.maps.LatLng(data[i].y, data[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, data[i]); // 검색 결과 항목 Element를 생성합니다

            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
            // LatLngBounds 객체에 좌표를 추가합니다
            bounds.extend(placePosition);
            map.panTo(placePosition);

            let resButton = document.getElementsByClassName("res-button");
            let cafeButton = document.getElementsByClassName("cafe-button");
            // 레스토랑 검색
            for (let j = 0; j < resButton.length; j++) {
                resButton[j].addEventListener("click", () => {
                    removeCustomOverlayee();

                    console.log(data[i].y + ", " + data[i].x);

                    var places = new kakao.maps.services.Places();

                    // 카테고리로 은행을 검색합니다
                    ps.categorySearch('FD6', secondPlacesSearchCB, {
                        useMapBounds: true,
                        location: new kakao.maps.LatLng(data[i].y, data[i].x),
                        radius: cafeButton[j].value
                    });
                });
            }
            // 카페 검색
            for (let k = 0; k < cafeButton.length; k++) {
                cafeButton[k].addEventListener("click", () => {
                    removeCustomOverlayee();

                    console.log(data[i].y + ", " + data[i].x);
                    var places = new kakao.maps.services.Places();

                    // 카테고리로 은행을 검색합니다
                    ps.categorySearch('CE7', thirdPlacesSearchCB, {
                        useMapBounds: true,
                        location: new kakao.maps.LatLng(data[i].y, data[i].x),
                        radius: cafeButton[k].value
                    });
                });
            }
        });
    }
}

// 키워드 검색 완료 시 호출되는 콜백함수 입니다
function secondPlacesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        for (var i = 0; i < data.length; i++) {
            displayPlaces(data);
            // 찜아이콘 변경
            zzim.src = 'http://localhost/images/heart.png';
            //addItems(data[i], secondArea);
        }
    }

    for (let i = 0; i < addDateListButton.length; i++) {
        addDateListButton[i].addEventListener("click", () => {
            // 첫 번째 아이템 클릭했을 때
            removeAllChildNods(findCourseListArea)
            removeAllChildNods(secondArea);
            let choiceCafe = "카페를 선택하세요!";
            addInfoItems(findCourseListArea, choiceCafe);

            // 선택한 식당의 id값
            restJson = data[i];
            rId = data[i].id;
            console.log("식당ID: " + rId);
            console.log(data[i].place_name);
            console.log(data[i].road_address_name);
            console.log(data[i].address_name);
            console.log(data[i].y + ", " + data[i].x);

            secondPosition = new kakao.maps.LatLng(data[i].y, data[i].x);

            // 시작 영역에 띄울 거임
            addItems(data[i], secondArea);
            // 시작 영역에 띄울 거임
        });
    }
}

// 키워드 검색 완료 시 호출되는 콜백함수 입니다
function thirdPlacesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        for (var i = 0; i < data.length; i++) {
            displayPlaces(data);
            // 찜아이콘 변경
            zzim.src = 'http://localhost/images/heart.png';
            //addItems(data[i], secondArea);
        }
    }

    for (let i = 0; i < addDateListButton.length; i++) {
        addDateListButton[i].addEventListener("click", () => {
            // 첫 번째 아이템 클릭했을 때
            removeAllChildNods(findCourseListArea)
            removeAllChildNods(thirdArea);
            let matching = "매칭완료";
            addInfoItems(findCourseListArea, matching);

            // 선택한 카페의 id값
            cafeJson = data[i];
            cId = data[i].id;
            console.log("카페ID: " + cId);
            console.log(data[i].place_name);
            console.log(data[i].road_address_name);
            console.log(data[i].address_name);
            console.log(data[i].y + ", " + data[i].x);

            thirdPosition = new kakao.maps.LatLng(data[i].y, data[i].x);

            // 시작 영역에 띄울 거임
            addItems(data[i], thirdArea);
            // 시작 영역에 띄울 거임
        });
    }
}

// 인포 아이템 띄우기 시작
function addInfoItems(choiceArea, str) {

    let infoEl = document.createElement("div"),
        itemStr =
            "<div>" +
            "<span>" +
            str +
            "</span>" +
            "</div>";

    infoEl.innerHTML = itemStr;
    infoEl.className = "info-item-list";

    choiceArea.appendChild(infoEl);

}

// 인포 아이템 띄우기 시작
function locationItems(choiceArea) {
    let choiceEl = document.createElement("div"),
        itemStr =
            "<div>" +
            "<div class='find-course__description'>" +
            "<h4 class='first'>" +
            "현재위치" +
            "</h4>" +
            "</div>" +
            "</div>";

    choiceEl.innerHTML = itemStr;
    choiceEl.className = "find-course-list";

    choiceArea.appendChild(choiceEl);
    // 아이템 띄우기 끝
}

function addItems(dateInfo, choiceArea) {
    let choiceEl = document.createElement("div"),
        itemStr =
            "<div>" +
            "<div class='find-course__description'>" +
            "<h4 class='first'>" +
            dateInfo.place_name +
            "</h4>" +
            "<div class='first__item'>" +
            "<span class='first__description'>" +
            dateInfo.road_address_name +
            "</span>" +
            "<span>" +
            dateInfo.address_name +
            "</span>";

    if (dateInfo.r_phone) {
        itemStr += "<span class='tel'>" + dateInfo.r_phone + "</span>";
    }

    itemStr += "</div>" + "</div>" + "</div>";

    choiceEl.innerHTML = itemStr;
    choiceEl.className = "find-course-list";

    choiceArea.appendChild(choiceEl);
    // 아이템 띄우기 끝
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {
    var listEl = document.getElementById("placesList"),
        menuEl = document.getElementsByClassName("find-course-list__container"),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds(),
        listStr = "";

    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();

    // 찜아이콘 변경
    zzim.src = 'http://localhost/images/heart.png';

    for (var i = 0; i < places.length; i++) {
        // 마커를 생성하고 지도에 표시합니다
        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
            marker = addMarker(placePosition, i),
            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(placePosition);

        // 마커와 검색결과 항목에 mouseover 했을때
        // 해당 장소에 인포윈도우에 장소명을 표시합니다
        // mouseout 했을 때는 인포윈도우를 닫습니다
        (function (marker, title) {
            let message = title;
            let first = places[i].y;
            let second = places[i].x;
            let markerPosition = new kakao.maps.LatLng(first, second);
            let roadAddress = places[i].road_address_name;
            let address = places[i].address_name;
            var content =
                '<div class="custom_content">' +
                '<div class="place_name">' +
                "<span>" +
                message +
                "</span>" +
                "</div>" +
                "<div class='address_name'>" +
                "<span>" +
                roadAddress +
                "</span>" +
                ", " +
                "<span>" +
                address +
                "</span>" +
                "</div>" +
                "</div>";

            // // 커스텀 오버레이가 표시될 위치입니다
            function customOverlayee() {
                var customOverlay = new kakao.maps.CustomOverlay({
                    map: map,
                    clickable: true,
                    position: markerPosition,
                    content: content,
                    yAnchor: 1,
                });
                customOverlay.setMap(map);
                customoverlays.push(customOverlay);
            }

            function removeCustomOverlayee() {
                for (let i = 0; i < customoverlays.length; i++) {
                    customoverlays[i].setMap(null);
                }
                customoverlays = [];
            }

            kakao.maps.event.addListener(marker, "click", function () {
                // customOverlay.setMap(map);
                customOverlayee();
            });

            kakao.maps.event.addListener(map, "click", function () {
                // customOverlay.setMap(null);
                removeCustomOverlayee();
            });

            itemEl.onclick = function () {
                // displayInfowindow(marker, title);
                removeCustomOverlayee();
                customOverlayee();
                // customOverlay.setMap(map);
                // 지도 중심을 이동 시킵니다
                map.panTo(markerPosition);
            };
        })(marker, places[i].place_name);

        fragment.appendChild(itemEl);
    }

    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
    listEl.appendChild(fragment);
    menuEl.scrollTop = 0;

    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

function removeCustomOverlayee() {
    for (let i = 0; i < customoverlays.length; i++) {
        customoverlays[i].setMap(null);
    }
    customoverlays = [];
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, places) {
    var el = document.createElement("li"),
        itemStr =
            '<span class="markerbg marker_' +
            (index + 1) +
            '"></span>' +
            '<div class="info">' +
            "   <h5 id='title'>" +
            places.place_name +
            "</h5>";

    if (places.road_address_name) {
        itemStr +=
            "    <span class='road_address'>" +
            places.road_address_name +
            "</span>" +
            " / " +
            '   <span class="jibun gray">' +
            places.address_name +
            "</span>";
    } else {
        itemStr += "    <span>" + places.address_name + "</span>";
    }

    itemStr += '  <span class="tel">' + places.phone + "</span>" +
        "<button class='addDateListButton'>" +
        "선택" +
        "</button>" +
        "</div>";

    el.innerHTML = itemStr;
    el.className = "item";

    return el;
}


// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = "../images/marker_img.png",
        imageSize = new kakao.maps.Size(52, 57),
        imageOption = {offset: new kakao.maps.Point(25, 55)};

    var markerImage = new kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imageOption
    );
    marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage,
    });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker); // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById("pagination"),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
    }

    for (i = 1; i <= pagination.last; i++) {
        var el = document.createElement("a");
        el.href = "#";
        el.innerHTML = i;

        if (i === pagination.current) {
            el.className = "on";
        } else {
            el.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i);
                };
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px; z-index:1;">' + title + "</div>";

    infowindow.setContent(content);
    infowindow.open(map, marker);
    // const moveLatLon = new window.kakao.maps.LatLng(places[i].y, places[i].x);
    // map.panTo(moveLatLon); //지도를 부드럽게 이동
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}

// 지도에 표시할 선을 생성합니다
function addPolyline(firstPosition, secondPosition, thirdPosition, idx) {
    linePath = [firstPosition, secondPosition, thirdPosition];

    let polyline = new kakao.maps.Polyline({
        path: linePath, // 선을 구성하는 좌표배열 입니다
        strokeWeight: 5, // 선의 두께 입니다
        strokeColor: "#FF5757", // 선의 색깔입니다
        strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
        strokeStyle: "solid", // 선의 스타일입니다
    });
    polyline.setMap(map);
    polylines.push(polyline);

    return polyline;
}

function removePolyline() {
    for (let i = 0; i < polylines.length; i++) {
        polylines[i].setMap(null);
    }
    markers = [];
}

// 모든 자식 요소를 제거하는 함수
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}

zzim.addEventListener("click", () => {
    if (token !== null) {
        const req = {
            r_id: rId,
            c_id: cId,
        }

        fetch("/myCourse/courseDibs", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": heretokenHead,
            },
            body: JSON.stringify(req),
        })
            .then((response) => response.json())
            .then((data) => {
                if (Number(data) === 1) {
                    if (zzim.src === "http://localhost/images/pullheart.png") {
                        zzim.src = "http://localhost/images/heart.png";
                        console.log("flag: " + 0);
                        alert("찜해제");
                    } else {
                        zzim.src = 'http://localhost/images/pullheart.png';
                        alert("찜 😍")
                        console.log("res: " + rId + ", cafe: " + cId);
                        console.log("flag: " + 1);
                        console.log("성공");
                    }
                } else {
                    console.log("실패");
                }
            })
    } else {
        alert("로그인 후 이용해주세요 :)");
        location.href = "/login/signIn";
    }
})