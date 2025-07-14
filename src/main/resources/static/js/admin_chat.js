let currentUser = null;
const urlParams = new URLSearchParams(location.search);

// staffId는 템플릿에서 window.staffId로 전달되어 있다고 가정
console.log("[admin_chat.js] staffId:", staffId);
const ws = new WebSocket("ws://192.168.10.78:8080/chat?userId=" + staffId);
const chatHistory = {}; // { user1: ["msg1", "msg2"], user2: [...] }

function loadUserList() {
    $.get("/chat/rooms", { staff_id: staffId }, function(rooms) {
        $("#userList").html("");
        rooms.forEach(room => {
            // 각 방의 안읽은 메시지 개수 조회
            $.get("/chat/unread-count", { room_id: room.room_id, staff_id: staffId }, function(count) {
                addUserToList(room.user_num, "", count);
            });
        });
    });
}

// 페이지 로드 시 채팅방 리스트 불러오기
$(document).ready(function() {
    loadUserList();
});

// WebSocket 메시지 수신 시에도 유저 목록 갱신
ws.onmessage = function(event) {
    const [sender, msg] = event.data.split(":", 2);
    // sender가 staffId(문자열)이면 오른쪽(관리자), 아니면 왼쪽(사용자)
    const isMine = sender === staffId;
    appendChat(sender, msg, isMine);
    if (!isMine) addUserToList(sender, msg);
    loadUserList(); // 메시지 수신 시 리스트 갱신
};

// 유저 목록 추가 또는 메시지 갱신
function addUserToList(user, lastMsg, unreadCount) {
	if ($("#userList").find(`[data-user='${user}']`).length === 0) {
		$("#userList").append(`
			<div class="user-item" data-user="${user}">
				<div class="user-info"><strong>${user}</strong><br><span>${lastMsg}</span></div>
				<div class="badge">${unreadCount && unreadCount > 0 ? unreadCount : ''}</div>
			</div>
		`);
	} else {
		$(`[data-user='${user}'] .user-info span`).text(lastMsg);
		$(`[data-user='${user}'] .badge`).text(unreadCount && unreadCount > 0 ? unreadCount : '');
	}
}

// 유저 선택 시 DB에서 메시지 내역 불러오기
$(document).on("click", ".user-item", function() {
	currentUser = $(this).data("user");
	$("#chatWith").text(currentUser);
	$("#chatBody").html("");
	// 1. 채팅방(room_id) 조회
	$.get("/chat/room", { user_num: currentUser, chat_type: 0 }, function(room) {
		// 2. 메시지 내역 불러오기
		$.get("/chat/messages", { room_id: room.room_id }, function(messages) {
			messages.forEach(function(msg) {
				// 관리자가 보낸 메시지는 오른쪽, 사용자가 보낸 메시지는 왼쪽
				const isMine = msg.staff_id === staffId;
				const isUser = msg.user_num == currentUser;
				// 관리자가 보낸 메시지는 오른쪽, 사용자가 보낸 메시지는 왼쪽
				appendChat(isMine ? staffId : currentUser, msg.content, isMine);
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
	const msg = $("#messageInput").val();
	if (msg && currentUser) {
		// room_id를 반드시 알아야 함!
		$.get("/chat/room", { user_num: currentUser, chat_type: 0 }, function(room) {
			ws.send(room.room_id + ":" + msg);
			appendChat(staffId, msg, true);
			$("#messageInput").val("");
			loadUserList(); // 메시지 전송 시 리스트 갱신
		});
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
