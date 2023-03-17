const locationButton = document.getElementById("location_button");

var mapContainer = document.getElementById("map"), // 지도를 표시할 div
  mapOption = {
    center: new kakao.maps.LatLng(36.815063, 127.1580072), // 지도의 중심좌표
    level: 4, // 지도의 확대 레벨
  };

var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

// 마커 커스텀 인데여;;
var imageSrc = "../images/marker_img.png",
  imageSize = new kakao.maps.Size(64, 69),
  imageOption = { offset: new kakao.maps.Point(27, 69) };

var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imageOption);
// 마커 커스텀 인데여;;

function setCenter() {
  // 이동할 위도 경도 위치를 생성합니다
  var moveLatLon = new kakao.maps.LatLng(33.452613, 126.570888);

  // 지도 중심을 이동 시킵니다
  map.setCenter(moveLatLon);
}

function panTo() {
  // 이동할 위도 경도 위치를 생성합니다
  var moveLatLon = new kakao.maps.LatLng(33.45058, 126.574942);

  // 지도 중심을 부드럽게 이동시킵니다
  // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
  map.panTo(moveLatLon);
}

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

  kakao.maps.event.addListener(marker, "mouseover", () => {
    customOverlay.setMap(map);
  });

  kakao.maps.event.addListener(marker, "mouseout", () => {
    customOverlay.setMap(null);
  });

  // 지도 중심좌표를 접속위치로 변경합니다
  map.setCenter(locPosition);
}
