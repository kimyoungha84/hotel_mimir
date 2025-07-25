/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
  loadCalendar(new Date().getFullYear(), new Date().getMonth() + 1);
	
  const btnScheduler = document.querySelector(".tab-btn-scheduler");
  const btnMenu = document.querySelector(".tab-btn-menu");
  const schedulerContent = document.getElementById("scheduler");
  const menuContent = document.getElementById("menu");
  const tabTitle = document.getElementById("title");
  const schedulerSection = document.querySelector(".scheduler-content-wrap");
  const selectDayTxt = document.querySelector(".select-day-wrap");
  const inputAdult = document.getElementById("inputAdult");
  const inputChild = document.getElementById("inputChild");
  const inputTime = document.getElementById("inputTime");
  const nextBtn = document.querySelector(".next-btn");
  
  inputAdult.value = document.querySelector(".cnt-adult-txt").textContent.trim() || "1";
  inputChild.value = document.querySelector(".cnt-child-txt").textContent.trim() || "0";
  
  document.querySelector(".next-btn").addEventListener("click", function(e) {
    if (this.classList.contains("disabled")) {
      e.preventDefault();
    }
  });
  
  // 탭 전환 이벤트
  btnScheduler.addEventListener("click", () => {
    btnScheduler.classList.add("active");
    btnMenu.classList.remove("active");
    schedulerContent.style.display = "block";
	if (menuContent) menuContent.style.display = "none";
    menuContent.style.display = "none";
    tabTitle.textContent = "예약일 선택";
    
    document.querySelectorAll("td.selected-date").forEach(td => td.classList.remove("selected-date"));
    
    if (!inputTime.value) {
        schedulerSection.style.display = "none";
      }
      selectDayTxt.style.display = "block";
  });

if (btnMenu && menuContent) {
  btnMenu.addEventListener("click", () => {
    btnMenu.classList.add("active");
    btnScheduler.classList.remove("active");
    menuContent.style.display = "block";
    schedulerContent.style.display = "none";
    tabTitle.textContent = "대표 메뉴";
    schedulerSection.style.display = "none";
    selectDayTxt.style.display = "none";
  });
}
  // 초기 상태 설정
  btnScheduler.classList.add("active");
  schedulerContent.style.display = "block";
  menuContent.style.display = "none";
  tabTitle.textContent = "예약일 선택";

  // 다이닝 선택 팝업 열기
  document.querySelector(".dining-name-btn").addEventListener("click", () => {
	  
    document.querySelectorAll(".dining-popup .dining-item.selected").forEach(item => {
      item.classList.remove("selected");
	});
	
	if (selectDiningId) {
	  const selectedItemElem = document.querySelector(`.dining-popup .dining-item[data-id='${selectDiningId.toString()}']`);
	  if (selectedItemElem) {
	    selectedItemElem.classList.add("selected");
	    selectedItem = selectedItemElem;
	  }
	}
	  
    document.querySelector(".popup-overlay01").style.display = "block";
    document.querySelector(".dining-popup").style.display = "block";
  });

  // 다이닝 선택 팝업 닫기 및 선택 처리
  let selectedItem = null;

  document.querySelectorAll(".dining-item").forEach(item => {
    item.addEventListener("click", function () {
      if (selectedItem) selectedItem.classList.remove("selected");
      this.classList.add("selected");
      selectedItem = this;
    });
  });

  document.querySelector(".dining-select-btn").addEventListener("click", () => {
    if (selectedItem) {
      const name = selectedItem.querySelector(".title-txt")?.textContent.trim();
	  const id = selectedItem.getAttribute("data-id");
	    if (name && id) {
	      const newUrl = `${location.pathname}?diningId=${id}`;
	      location.href = newUrl;
	    }
	  }
    document.querySelector(".popup-overlay01").style.display = "none";
    document.querySelector(".dining-popup").style.display = "none";
  });

  // 인원수 선택 후 반영 및 팝업 닫기
  document.querySelector(".cntBtn").addEventListener("click", () => {
	const currentAdult = document.querySelector(".cnt-adult-txt")?.textContent.trim() || "1";
	const currentChild = document.querySelector(".cnt-child-txt")?.textContent.trim() || "0";

	document.getElementById("adultCnt").textContent = currentAdult;
	document.getElementById("childCnt").textContent = currentChild;

    document.querySelector(".popup-overlay02").style.display = "none";
    document.querySelector(".cnt-popup").style.display = "none";
  });

  // 예약 시간 버튼 클릭 이벤트
  document.querySelectorAll(".time-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".time-btn").forEach(b => b.classList.remove("selected"));
      btn.classList.add("selected");
      
      const isLunch = btn.closest(".time-lunch") !== null;
      const mealType = isLunch ? "Lunch" : "Dinner";
	  
	  if (!inputAdult.value) inputAdult.value = "1";
	  if (!inputChild.value) inputChild.value = "0";
      
      // form 데이터 저장
      if (!document.getElementById("inputMeal")) {
        const mealInput = document.createElement("input");
        mealInput.type = "hidden";
        mealInput.name = "meal";
        mealInput.id = "inputMeal";
        document.getElementById("reservationForm").appendChild(mealInput);
      }
      document.getElementById("inputMeal").value = mealType;

      // 선택된 시간값 form hidden input에 저장
      inputTime.value = btn.textContent.trim();
      
   	  // 날짜값 hidden input에 저장
      if (!document.getElementById("inputDate")) {
        const dateInput = document.createElement("input");
        dateInput.type = "hidden";
        dateInput.name = "date";
        dateInput.id = "inputDate";
        document.getElementById("reservationForm").appendChild(dateInput);
      }
      document.getElementById("inputDate").value = selectedDateStr;

      // 다음 버튼 활성화
      nextBtn.classList.remove("disabled");
      nextBtn.removeAttribute("aria-disabled");
    });
  });

});

let selectedDateStr = "";

// 부모 윈도우로부터 선택 날짜 메시지 받으면 예약시간 선택 영역 보이기
window.addEventListener("message", function(event) {
  if (event.data && event.data.type === "dateSelected") {
	  
	selectedDateStr = event.data.date;
	
	const rawDate = new Date(selectedDateStr);
	const yyyy = rawDate.getFullYear();
	const mm = String(rawDate.getMonth() + 1).padStart(2, '0');
	const dd = String(rawDate.getDate()).padStart(2, '0');
	selectedDateStr = `${yyyy}-${mm}-${dd}`;

	// 요청 URL 로그 삽입
	const diningId = document.getElementById("inputDining").value;
	console.log("🔵 요청 URL:", `/api/remainingSeats?diningId=${diningId}&date=${selectedDateStr}`);

	// 좌석 AJAX 호출 삽입
	fetch(`/api/remainingSeats?diningId=${diningId}&date=${selectedDateStr}`)
	  .then(response => {
	    if (!response.ok) throw new Error("API 요청 실패");
	    return response.json();
	  })
	  .then(data => {
	    console.log("✅ 잔여좌석 데이터:", data);

	    // 각 버튼에 좌석 수 매핑
		  document.querySelectorAll(".time-btn").forEach(btn => {
		    const span = btn.querySelector("span");
		    const timeText = span.textContent.replace("오후 ", "").trim();
		    const seatCount = data[timeText];

		    let seatTag = btn.querySelector("small.remain-seat");

		    if (!seatTag) {
		      seatTag = document.createElement("small");
		      seatTag.classList.add("remain-seat");
		      btn.appendChild(seatTag);
		    }

		    if (seatCount !== undefined) {
		      seatTag.textContent = `잔여좌석: ${seatCount}석`;
		    } else {
		      seatTag.textContent = "잔여좌석: -석";
		    }
		  });
		})
	
    const schedulerSection = document.querySelector(".scheduler-content-wrap");
    if (schedulerSection) schedulerSection.style.display = "block";
    
    // 시간 초기화
    const inputTime = document.getElementById("inputTime");
    inputTime.value = "";

    // 시간 버튼 선택 해제
    document.querySelectorAll(".time-btn.selected").forEach(btn => btn.classList.remove("selected"));

    // 다음 버튼 비활성화
    const nextBtn = document.querySelector(".next-btn");
    if (nextBtn) {
      nextBtn.classList.add("disabled");
      nextBtn.setAttribute("aria-disabled", "true");
    }
  }
});

window.addEventListener("pageshow", function (event) {
  if (event.persisted) {
    // 뒤로가기로 돌아온 경우: 초기화
	document.getElementById("inputDining").value = selectDiningId;
	document.getElementById("inputAdult").value = "";
	document.getElementById("inputChild").value = "";
	document.getElementById("inputTime").value = "";
	document.getElementById("inputDate").value = "";
	document.getElementById("inputMeal").value = "";

	// 표시된 텍스트도 초기화
	document.querySelector(".dining-name-txt").textContent = "스테이, 모던 레스토랑";
	document.querySelector(".cnt-adult-txt").textContent = "1";
	document.querySelector(".cnt-child-txt").textContent = "0";

	// 선택된 시간 버튼 초기화
	document.querySelectorAll(".time-btn.selected").forEach(btn => btn.classList.remove("selected"));
	
	// 시간 버튼 영역 숨김
    const schedulerSection = document.querySelector(".scheduler-content-wrap");
    if (schedulerSection) schedulerSection.style.display = "none";

    // 달력 선택 날짜 제거
    document.querySelectorAll("td.selected-date").forEach(td => td.classList.remove("selected-date"));
    
	// 다음 버튼 비활성화
	const nextBtn = document.querySelector(".next-btn");
	if (nextBtn) {
	nextBtn.classList.add("disabled");
	nextBtn.setAttribute("aria-disabled", "true");
	}
  }
});

// 지도 팝업
function openMapPopup() {
  document.querySelector('.popup-overlay04').style.display = 'flex';
}