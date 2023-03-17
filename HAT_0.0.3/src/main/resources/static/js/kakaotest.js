const loginid = document.getElementById('loginid')
const loginpw = document.getElementById('loginpw')
const searchbtn = document.getElementById('searchbtn')
//const cafebtn = document.getElementById('cafebtn')


function getmap(){
	fetch("/asendMap", {
	
    method: "POST",
    headers: { "Content-Type": "application/json" },

  })
    .then((response) => response.json())
    .then((data) => {
	  //ArrayList<JSONObject> 표현하기
      console.log(JSON.stringify(data));
 
    });
}



function mapTest() {
	const req = {
    first: "127.1304019",
    second: "36.8352557",
  };
	
	fetch("/testmap", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(req),
  })
    

 
	
}


function cafeUp() { 
  const req = {
    x: "127.1304019",
    y: "36.8352557",
    radius : "500",
  };
  var kakaolist = [];
  kakaolist.push(req); 
  fetch("/myCourse/cafe", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(kakaolist),
  })
    .then((response) => response.json())
    .then((data) => {
	  //ArrayList<JSONObject> 표현하기
      console.log(JSON.stringify(data));
      // 위에줄에서 첫번째에 있는 place_name 뽑아보기 
      console.log(JSON.stringify(data[0]["documents"][0]["place_name"]));

    });
}


function courseUp(){
	const json1 = {
		"u_id" : "testman",
		"unknown" : 1
		
	};
	const json2 = {
		"address_name": "충남 천안시 동남구 대흥동 95",
      "category_group_code": "FD6",
      "category_group_name": "음식점",
      "category_name": "음식점 > 아시아음식 > 인도음식",
      "distance": "130",
      "id": "13110298",
      "phone": "041-555-3612",
      "place_name": "마운틴피시텔 레스토랑",
      "place_url": "http://place.map.kakao.com/13110298",
      "road_address_name": "충남 천안시 동남구 버들로 4",
      "x": "127.148423612077",
      "y": "36.8090572575193"
	};
	const json3 = {
      "address_name": "충남 천안시 동남구 문화동 179",
      "category_group_code": "CE7",
      "category_group_name": "카페",
      "category_name": "음식점 > 카페 > 테마카페 > 키즈카페",
      "distance": "351",
      "id": "836565346",
      "phone": "",
      "place_name": "볼베어파크 천안점",
      "place_url": "http://place.map.kakao.com/836565346",
      "road_address_name": "충남 천안시 동남구 옛시청길 29",
      "x": "127.150594233861",
      "y": "36.8078722715644"
	};
	var courselist = [];
	courselist.push(json1);
	courselist.push(json2);
	courselist.push(json3);
	fetch("/findCourse/saveCourse", {
		method: "POST",
    	headers: { "Content-Type": "application/json" },
    	body: JSON.stringify(courselist),
	})
}




