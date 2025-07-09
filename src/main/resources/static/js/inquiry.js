// FAQ toggle 개선
$(".faq-item").click(function () {
  const content = $(this).next(".faq-answer");

  // 모든 답변 닫고, 선택한 항목만 toggle
  $(".faq-answer").not(content).slideUp();
  content.slideToggle();
});

// 채팅창 토글
let isOpen = true;
$("#chatToggleBtn").click(function() {
  isOpen = !isOpen;
  $("#chatBox").fadeToggle();
  $(this).html(isOpen ? "✖" : "💬");
});

// 문의 시작 → 입력창 열기
$("#startChatBtn").click(function() {
  $("#inputArea").css("display", "flex");
  $(this).hide();
  $("#chatBody").html('');
});

// WebSocket 객찰
const urlParams = new URLSearchParams(location.search);
const userId = urlParams.get("userId");  // 예: user1
const ws = new WebSocket("ws://192.168.10.78:8080/chat?userId=" + userId);

// 메시지 수신 처리
ws.onmessage = function (event) {
  $(".placeholder-text").hide();

  const [sender, msg] = event.data.split(":", 2);
  const isMine = sender === userId;
  const alignClass = isMine ? "right" : "left";
  const formattedMsg = msg.replace(/\n/g, "<br>"); // 🔥 줄바꿈 처리

  const messageBlock = $("<div>").addClass("message-block " + alignClass);

  // 관리자 메시지일 경우 이름과 시간 표시
  if (!isMine) {
    //const nameElem = $("<div>").addClass("sender-name").text(sender);
    const timeElem = $("<div>").addClass("message-time").text(getCurrentTime());
    const msgElem = $("<div>").addClass("chat-message left").html(formattedMsg); // 🔥 줄바꿈 반영
    messageBlock.append( msgElem, timeElem);
  } else {
    // 사용자 메시지는 이름 없이 메시지만
    const msgElem = $("<div>").addClass("chat-message right").html(formattedMsg); // 🔥 줄바꿈 반영
    messageBlock.append(msgElem);
  }

  $("#chatBody").append(messageBlock).scrollTop($("#chatBody")[0].scrollHeight);
};

// 시간 포맷 함수
function getCurrentTime() {
  const now = new Date();
  const h = now.getHours().toString().padStart(2, '0');
  const m = now.getMinutes().toString().padStart(2, '0');
  return `${h}:${m}`;
}


// 문의 시작 버튼 클릭
$("#startChatBtn").click(function () {
  $("#inputArea").hide();
  $(this).hide();
  $(".placeholder-text").hide();

  $("#chatBody").html(`
    <p style="text-align:center; color:#555;">문의 유형을 선택해주세요</p>
    <div class="chat-options">
      <button class="chat-option" data-type="room">객실 문의</button>
      <button class="chat-option" data-type="dining">다이닝 문의</button>
      <button class="chat-option" data-type="etc">기타 문의</button>
    </div>
  `);
});

let inquiryType = null;

$(document).on("click", ".chat-option", function () {
  inquiryType = $(this).data("type");

  const typeName = {
    room: "거실 문의",
    dining: "다이닝 문의",
    etc: "일반 문의"
  }[inquiryType];

  $("#chatBody").html(`<p style="text-align:center; color:#555;">[${typeName}] 문의를 시작합니다.</p>`);
  $("#inputArea").css("display", "flex");
});

// 보낸 타임스템프 저장
let messageTimestamps = [];

// 전송 버튼 클릭
$("#sendBtn").click(function () {
  sendMessage();
});

// 엔터 전송
$("#messageInput").keydown(function (e) {
  if (e.key === "Enter") {
	if (e.shiftKey) return; // 줄바꿈 허용
    e.preventDefault();
    sendMessage();
  }
});

// 공통 전송 함수
function sendMessage() {
  const msg = $("#messageInput").val().trim();

  // 1. 300자 차례 검사
  if (msg.length > 300) {
    showError("⚠️ 300자 초과로 전송할 수 없습니다.");
    return;
  }

  // 2. 30초 안에 10개 이상 전송이면 도배 처리
  const now = Date.now();
  messageTimestamps = messageTimestamps.filter(ts => now - ts <= 30000);
  if (messageTimestamps.length >= 7) {
    disableSendBtn();
    showError("⚠️ 도배 방지를 위해 30초간 전송이 제한됩니다.");
    return;
  }

  if (msg !== "") {
	const msgElem = $("<div>").addClass("message-block right");
	
	//const message = $("<div>").addClass("chat-message right").text(msg);
	const formattedMsg = msg.replace(/\n/g, "<br>");
	const message = $("<div>").addClass("chat-message right").html(formattedMsg);
	const timeElem = $("<div>").addClass("message-time").text(getCurrentTime());
	msgElem.append(message,timeElem);

    $("#chatBody").append(msgElem).scrollTop($("#chatBody")[0].scrollHeight);
    ws.send(msg);
    $("#messageInput").val("");
    messageTimestamps.push(now);
  }
}

// 오류 메시지 표시
function showError(msg) {
  if ($("#errorBox").length === 0) {
    $("#inputArea").prepend(`<div id="errorBox" style="color:red; margin-bottom:4px;"></div>`);
  }
  $("#errorBox").text(msg);

  // 5초 후 제거
  setTimeout(() => $("#errorBox").fadeOut(300, function () { $(this).remove(); }), 30000);
}

// 보낸 비활성화 버튼 통제
function disableSendBtn() {
  $("#sendBtn").prop("disabled", true).css({
    "background-color": "#ccc",
    "cursor": "not-allowed"
  });

  // 30초 후 복구
  setTimeout(() => {
    $("#sendBtn").prop("disabled", false).css({
      "background-color": "",
      "cursor": ""
    });
    $("#errorBox").remove();
    messageTimestamps = []; // 처음복귀
  }, 30000);
}

// 문의 등록 페이지 이동
$(document).ready(function() {
  $(".inquiry_register").click(function() {
    window.location.href = "/inquiry_register";
  });
});

// 최대화 & 뒤로가기 버튼 이벤트
let isMaximized = false;

$("#maximizeBtn").click(function () {
  $("#chatBox").toggleClass("maximized");
  isMaximized = !isMaximized;
});

$("#backBtn").click(function () {
  $("#inputArea").hide();
  $("#chatBody").html(`
   <p style="text-align:center; color: #0000FF;">상담 시간 10:00 ~ 17:00 </p>
   <p style="text-align:center; font-size : 13px; color: #FF0000;">※욕설 금지 = 관리자도 누군가의 자녀이자 부모님이다.※ </p>
    <p style="text-align:center; color: #aaa;">문의 유형을 선택해주세요</p>
    <div class="chat-options">
      <button class="chat-option" data-type="room">객실 문의</button>
      <button class="chat-option" data-type="dining">다이닝 문의</button>
      <button class="chat-option" data-type="etc">기타 문의</button>
    </div>
  `);
});
