// FAQ toggle 개선
$(".faq-item").click(function() {
	const content = $(this).next(".faq-answer");
	// 모든 답변 닫고, 선택한 항목만 toggle
	$(".faq-answer").not(content).slideUp();
	content.slideToggle();
});

// 채팅 관련 변수
let isOpen = true;
let currentRoomId = null;
let currentChatType = null;
let ws = null;
let userNum = null;

// JWT 토큰에서 사용자 정보 추출
function getCurrentUser() {
	const token = localStorage.getItem('jwt_token');
	if (!token) return null;
	
	try {
		const payload = JSON.parse(atob(token.split('.')[1]));
		return payload;
	} catch (e) {
		console.error('JWT 토큰 파싱 오류:', e);
		return null;
	}
}

// 로그인 상태 체크
function checkLoginStatus() {
	const user = getCurrentUser();
	if (!user || !user.user_num) {
		$("#loginCheck").show();
		$("#chatOptions").hide();
		$("#chatMessages").hide();
		$("#inputArea").hide();
		return false;
	} else {
		userNum = user.user_num;
		$("#loginCheck").hide();
		$("#chatOptions").show();
		return true;
	}
}

// 채팅창 토글
$("#chatToggleBtn").click(function() {
	if (!checkLoginStatus()) {
		$("#chatBox").fadeIn();
		return;
	}
	
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
function connectWebSocket() {
	if (!userNum) return;
	
	const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
	const wsUrl = `${protocol}//${window.location.host}/chat?userId=${userNum}`;
	
	ws = new WebSocket(wsUrl);
	
	ws.onopen = function() {
		console.log('WebSocket 연결됨');
	};
	
	ws.onmessage = function(event) {
		const [sender, msg] = event.data.split(":", 2);
		// sender가 userNum(숫자)이면 오른쪽(사용자), 아니면 왼쪽(관리자)
		const isMine = sender == userNum;
		const alignClass = isMine ? "right" : "left";
		const formattedMsg = msg.replace(/\n/g, "<br>");
		
		const messageBlock = $("<div>").addClass("message-block " + alignClass);
		
		if (!isMine) {
			const timeElem = $("<div>").addClass("message-time").text(getCurrentTime());
			const msgElem = $("<div>").addClass("chat-message left").html(formattedMsg);
			messageBlock.append(msgElem, timeElem);
		} else {
			const msgElem = $("<div>").addClass("chat-message right").html(formattedMsg);
			messageBlock.append(msgElem);
		}
		
		$("#chatBody").append(messageBlock).scrollTop($("#chatBody")[0].scrollHeight);
	};
	
	ws.onclose = function() {
		console.log('WebSocket 연결 종료');
		// 재연결 시도
		setTimeout(connectWebSocket, 3000);
	};
	
	ws.onerror = function(error) {
		console.error('WebSocket 오류:', error);
	};
}

// 시간 포맷 함수
function getCurrentTime() {
	const now = new Date();
	const h = now.getHours().toString().padStart(2, '0');
	const m = now.getMinutes().toString().padStart(2, '0');
	return `${h}:${m}`;
}

// 채팅방 생성/입장
function enterChatRoom(chatType) {
	currentChatType = chatType;
	
	// API 호출하여 채팅방 생성/조회
	$.ajax({
		url: '/api/chat/room',
		method: 'POST',
		contentType: 'application/json',
		headers: {
			'Authorization': 'Bearer ' + localStorage.getItem('jwt_token')
		},
		data: JSON.stringify({ chat_type: chatType.toString() }),
		success: function(room) {
			currentRoomId = room.room_id;
			$("#chatTitle").text(getChatTypeName(chatType) + " 문의");
			
			// 채팅 메시지 영역 표시
			$("#chatOptions").hide();
			$("#chatMessages").show();
			$("#inputArea").show();
			
			// 이전 메시지 불러오기
			loadChatMessages(room.room_id);
			
			// WebSocket 연결
			if (!ws || ws.readyState !== WebSocket.OPEN) {
				connectWebSocket();
			}
		},
		error: function(xhr) {
			if (xhr.status === 401) {
				alert('로그인이 필요합니다.');
				location.href = '/member/loginFrm';
			} else {
				alert('채팅방 생성 중 오류가 발생했습니다.');
			}
		}
	});
}

// 채팅 메시지 불러오기
function loadChatMessages(roomId) {
	$.ajax({
		url: '/api/chat/messages',
		method: 'GET',
		headers: {
			'Authorization': 'Bearer ' + localStorage.getItem('jwt_token')
		},
		data: { room_id: roomId },
		success: function(messages) {
			$("#chatMessages").empty();
			
			messages.forEach(function(msg) {
				// 사용자가 보낸 메시지는 오른쪽, 관리자가 보낸 메시지는 왼쪽
				const isMine = msg.user_num == userNum;
				const alignClass = isMine ? "right" : "left";
				const messageBlock = $("<div>").addClass("message-block " + alignClass);
				const formattedMsg = msg.content.replace(/\n/g, "<br>");
				const message = $("<div>").addClass("chat-message " + alignClass).html(formattedMsg);
				
				if (!isMine) {
					const timeElem = $("<div>").addClass("message-time").text(formatTime(msg.send_time));
					messageBlock.append(message, timeElem);
				} else {
					messageBlock.append(message);
				}
				
				$("#chatMessages").append(messageBlock);
			});
			
			// 스크롤을 맨 아래로
			$("#chatMessages").scrollTop($("#chatMessages")[0].scrollHeight);
		},
		error: function(xhr) {
			if (xhr.status === 401) {
				alert('로그인이 필요합니다.');
				location.href = '/member/loginFrm';
			} else {
				alert('메시지 조회 중 오류가 발생했습니다.');
			}
		}
	});
}

// 시간 포맷팅 (DB timestamp)
function formatTime(timestamp) {
	const date = new Date(timestamp);
	const h = date.getHours().toString().padStart(2, '0');
	const m = date.getMinutes().toString().padStart(2, '0');
	return `${h}:${m}`;
}

// 채팅 타입 이름 반환
function getChatTypeName(chatType) {
	switch(chatType) {
		case 0: return "객실";
		case 1: return "다이닝";
		case 2: return "일반";
		default: return "일반";
	}
}

// 문의 유형 선택 시
$(document).on("click", ".chat-option", function() {
	const chatType = $(this).data("type");
	enterChatRoom(chatType);
});

// 보낸 타임스템프 저장
let messageTimestamps = [];

// 전송 버튼 클릭
$("#sendBtn").click(function() {
	sendMessage();
});

// 엔터 전송
$("#messageInput").keydown(function(e) {
	if (e.key === "Enter") {
		if (e.shiftKey) return; // 줄바꿈 허용
		e.preventDefault();
		sendMessage();
	}
});

// 공통 전송 함수
function sendMessage() {
	const msg = $("#messageInput").val();
	
	if (msg.replace(/\s/g, '') === "") return;  // 공백/엔터만 있는 경우 차단

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

	if (msg !== "" && currentRoomId && ws && ws.readyState === WebSocket.OPEN) {
		const msgElem = $("<div>").addClass("message-block right");
		const formattedMsg = msg.replace(/\n/g, "<br>");
		const message = $("<div>").addClass("chat-message right").html(formattedMsg);
		const timeElem = $("<div>").addClass("message-time").text(getCurrentTime());
		msgElem.append(message, timeElem);

		$("#chatMessages").append(msgElem).scrollTop($("#chatMessages")[0].scrollHeight);
		ws.send(currentRoomId + ':' + msg);
		$("#messageInput").val("");
		messageTimestamps.push(now);
	} else if (!currentRoomId) {
		showError('채팅방이 선택되지 않았습니다.');
	} else if (!ws || ws.readyState !== WebSocket.OPEN) {
		showError('채팅 연결이 끊어졌습니다. 다시 시도해주세요.');
	}
}

// 오류 메시지 표시
function showError(msg) {
	if ($("#errorBox").length === 0) {
		$("#inputArea").prepend(`<div id="errorBox" style="color:red; margin-bottom:4px;"></div>`);
	}
	$("#errorBox").text(msg);

	// 5초 후 제거
	setTimeout(() => $("#errorBox").fadeOut(300, function() { $(this).remove(); }), 5000);
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

// 최대화 & 뒤로가기 버튼 이벤트
let isMaximized = false;

$("#maximizeBtn").click(function() {
	$("#chatBox").toggleClass("maximized");
	isMaximized = !isMaximized;
});

$("#backBtn").click(function() {
	$("#inputArea").hide();
	$("#chatMessages").hide();
	$("#chatOptions").show();
	$("#chatTitle").text("1:1 채팅");
	currentRoomId = null;
	currentChatType = null;
});

// 페이지 로드 시 로그인 상태 체크
$(document).ready(function() {
	checkLoginStatus();
});
