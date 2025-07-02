let currentUser = null;
const ws = new WebSocket("ws://192.168.10.78:8080/chat?userId=admin");
const chatHistory = {}; // { user1: ["msg1", "msg2"], user2: [...] }

ws.onmessage = function(event) {
	const [sender, msg] = event.data.split(":", 2);

	// 메시지 기록 저장
	if (!chatHistory[sender]) chatHistory[sender] = [];
	chatHistory[sender].push(`${sender}: ${msg}`);

	// 사용자 목록 업데이트 or 새 메시지 알림 처리
	if (sender !== "admin") {
		addUserToList(sender, msg);
		if (sender === currentUser) {
			appendChat(sender, msg, false);
		}
	}
};

function addUserToList(user, lastMsg) {
	if ($("#userList").find(`[data-user='${user}']`).length === 0) {
		$("#userList").append(`
      <div class="user-item" data-user="${user}">
        <div class="user-info"><strong>${user}</strong><br><span>${lastMsg}</span></div>
        <div class="badge">1</div>
      </div>
    `);
	} else {
		$(`[data-user='${user}'] .user-info span`).text(lastMsg);
		
	}
}

// 채팅방 선택
$(document).on("click", ".user-item", function() {
	currentUser = $(this).data("user");
	$("#chatWith").text(currentUser);
	$("#chatBody").html(""); // 기존 메시지 초기화
	// 기록 불러와서 append
	const history = chatHistory[currentUser] || [];
	history.forEach(msg => {
		const isMine = msg.startsWith("admin:");
		appendChat(isMine ? "admin" : currentUser, msg.split(":").slice(1).join(":"), isMine);
	});

	$(this).find(".badge").hide();
});

// 전송 버튼 클릭
$("#sendBtn").click(function() {
	const msg = $("#messageInput").val().trim();
	if (msg && currentUser) {
		ws.send(currentUser + ":" + msg);
		// 기록 저장
		if (!chatHistory["admin"]) chatHistory["admin"] = [];
		if (!chatHistory[currentUser]) chatHistory[currentUser] = [];
		chatHistory[currentUser].push(`admin: ${msg}`);

		appendChat("admin", msg, true);
		$("#messageInput").val("");
	}
});

// 엔터 전송
$("#messageInput").keydown(function(e) {
	if (e.key === "Enter") {
		e.preventDefault();
		sendMessage();
	}
});

// 채팅 출력
function appendChat(user, msg, isMine) {
	const msgElem = $("<div>").text(`${user}: ${msg}`).css("text-align", isMine ? "right" : "left");
	$("#chatBody").append(msgElem);
}
function sendMessage() {
	const msg = $("#messageInput").val().trim();
	if (msg && currentUser) {
		ws.send(currentUser + ":" + msg);
		// 기록 저장
		if (!chatHistory["admin"]) chatHistory["admin"] = [];
		if (!chatHistory[currentUser]) chatHistory[currentUser] = [];
		chatHistory[currentUser].push(`admin: ${msg}`);

		appendChat("admin", msg, true);
		$("#messageInput").val("");
	}
}

