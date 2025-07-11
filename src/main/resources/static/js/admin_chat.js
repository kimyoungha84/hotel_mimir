let currentUser = null;
const urlParams = new URLSearchParams(location.search);

// staffId는 템플릿에서 window.staffId로 전달되어 있다고 가정
console.log("[admin_chat.js] staffId:", staffId);
const ws = new WebSocket("ws://192.168.10.78:8080/chat?userId=" + staffId);
const chatHistory = {}; // { user1: ["msg1", "msg2"], user2: [...] }

// WebSocket 메시지 수신
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

// 유저 목록 추가 또는 메시지 갱신
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

// 유저 선택 시 DB에서 메시지 내역 불러오기
$(document).on("click", ".user-item", function() {
	currentUser = $(this).data("user");
	$("#chatWith").text(currentUser);
	$("#chatBody").html("");
	// 1. 채팅방(room_id) 조회 (staff_id는 'mimir_267801'로 고정)
	$.get("/test/chat/room", { user_num: currentUser, chat_type: 0 }, function(room) {
		// 2. 메시지 내역 불러오기
		$.get("/test/chat/messages", { room_id: room.room_id }, function(messages) {
			messages.forEach(function(msg) {
				const isMine = msg.staff_id === 'mimir_267801';
				appendChat(isMine ? 'admin' : currentUser, msg.content, isMine);
			});
		});
	});
	$(this).find(".badge").hide();
});

// 전송 버튼 클릭 시
$("#sendBtn").click(function() {
	sendMessage();
});

// 엔터 입력 시
$("#messageInput").keydown(function(e) {
	if (e.key === "Enter") {
		if (e.shiftKey) return; // 줄바꿈 허용
		e.preventDefault();
		sendMessage();
	}
});

// 메시지 전송 로직
function sendMessage() {
	const msg = $("#messageInput").val();// 🔥 trim 제거 → 줄바꿈 보존됨
	if (msg && currentUser) {
		ws.send(currentUser + ":" + msg);

		if (!chatHistory["admin"]) chatHistory["admin"] = [];
		if (!chatHistory[currentUser]) chatHistory[currentUser] = [];
		chatHistory[currentUser].push(`admin: ${msg}`);

		appendChat("admin", msg, true);
		$("#messageInput").val("");
	}
}

// 채팅 출력
function appendChat(user, msg, isMine) {
	const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

		const block = $("<div>").addClass("message-block").addClass(isMine ? "right" : "left");
		//const sender = $("<div>").addClass("sender-name").text(user);
		const message = $("<div>").addClass("chat-message").addClass(isMine ? "right" : "left").text(msg);
		const timestamp = $("<div>").addClass("message-time").text(time);

		block/*.append(sender)*/.append(message).append(timestamp);

		$("#chatBody").append(block);
		$("#chatBody").scrollTop($("#chatBody")[0].scrollHeight);
}
