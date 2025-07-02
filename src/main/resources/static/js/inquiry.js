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

// WebSocket 연결
/*const userId = "hong";*/
const urlParams = new URLSearchParams(location.search);
const userId = urlParams.get("userId");  // 예: user1
const ws = new WebSocket("ws://192.168.10.78:8080/chat?userId=" + userId);

// 메시지 수신 처리
ws.onmessage = function (event) {
  $(".placeholder-text").hide();

  const [sender, msg] = event.data.split(":", 2);
  const isMine = sender === userId; // 내 메시지인지 여부
  const alignClass = isMine ? "right" : "left";

  const msgElem = $("<div>")
    .addClass("chat-message " + alignClass)
    .text(sender + ": " + msg);

  $("#chatBody").append(msgElem).scrollTop($("#chatBody")[0].scrollHeight);
};


// 문의 시작
$("#startChatBtn").click(function () {
  $("#inputArea").hide();
  $(this).hide();
  $(".placeholder-text").hide();

  $("#chatBody").html(`
    <p style="text-align:center; color:#555;">문의 유형을 선택해주세요</p>
    <div class="chat-options">
      <button class="chat-option" data-type="room">객실 문의</button>
      <button class="chat-option" data-type="dining">다이닝 문의</button>
      <button class="chat-option" data-type="etc">그냥 문의</button>
    </div>
  `);
});

let inquiryType = null;

$(document).on("click", ".chat-option", function () {
  inquiryType = $(this).data("type");

  const typeName = {
    room: "객실 문의",
    dining: "다이닝 문의",
    etc: "일반 문의"
  }[inquiryType];

  $("#chatBody").html(`<p style="text-align:center; color:#555;">[${typeName}] 문의를 시작합니다.</p>`);
  $("#inputArea").css("display", "flex");
});



// 전송 버튼 클릭
$("#sendBtn").click(function () {
  sendMessage();
});

// 엔터 전송
$("#messageInput").keydown(function (e) {
  if (e.key === "Enter") {
    e.preventDefault();
    sendMessage();
  }
});

// 공통 전송 함수
function sendMessage() {
  const msg = $("#messageInput").val().trim();
  if (msg !== "") {
    const msgElem = $("<div>")
      .addClass("chat-message right")
      .text(userId + ": " + msg);
      
    $("#chatBody").append(msgElem).scrollTop($("#chatBody")[0].scrollHeight);
    ws.send(msg);
    $("#messageInput").val("");
  }
}
$(document).ready(function() {
  $(".inquiry_register").click(function() {
    window.location.href = "/inquiry_register";
  });
});


