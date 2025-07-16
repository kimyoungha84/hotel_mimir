let currentUser = null;
let currentRoomId = null;
let ws = null;

// staffId는 템플릿에서 window.staffId로 전달되어 있다고 가정
console.log("[admin_chat.js] staffId:", staffId);

// WebSocket 연결
function connectWebSocket() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/chat?userId=${staffId}`;
    
    ws = new WebSocket(wsUrl);
    
    ws.onopen = function() {
        console.log('관리자 WebSocket 연결됨');
    };
    
    ws.onmessage = function(event) {
        const [sender, msg] = event.data.split(":", 2);
        // sender가 staffId(문자열)이면 오른쪽(관리자), 아니면 왼쪽(사용자)
        const isMine = sender === staffId;
        appendChat(sender, msg, isMine);
        if (!isMine) {
            // 사용자로부터 메시지를 받았을 때 해당 사용자 목록 갱신
            addUserToList(sender, msg);
        }
        loadUserList(); // 메시지 수신 시 리스트 갱신
    };
    
    ws.onclose = function() {
        console.log('관리자 WebSocket 연결 종료');
        // 재연결 시도
        setTimeout(connectWebSocket, 3000);
    };
    
    ws.onerror = function(error) {
        console.error('관리자 WebSocket 오류:', error);
    };
}

function loadUserList() {
    $.ajax({
        url: '/api/admin/chat/rooms',
        method: 'GET',
        data: { staff_id: staffId },
        success: function(rooms) {
            $("#userList").html("");
            rooms.forEach(room => {
                // 각 방의 안읽은 메시지 개수 조회
                $.ajax({
                    url: '/api/admin/chat/unread-count',
                    method: 'GET',
                    data: { room_id: room.room_id, staff_id: staffId },
                    success: function(response) {
                        const count = response.count;
                        addUserToList(room.user_num, "", count, room.room_id, room.chat_type);
                    },
                    error: function() {
                        addUserToList(room.user_num, "", 0, room.room_id, room.chat_type);
                    }
                });
            });
        },
        error: function() {
            console.error('채팅방 목록 조회 실패');
        }
    });
}

// 페이지 로드 시 채팅방 리스트 불러오기
$(document).ready(function() {
    connectWebSocket();
    loadUserList();
    currentRoomId = null; // 반드시 null로 초기화
    $("#chatBody").html(""); // 채팅창 비우기
    $("#chatWith").text("선택된 유저 없음");
});

// 유저 목록 추가 또는 메시지 갱신
function addUserToList(user, lastMsg, unreadCount, roomId, chatType) {
    const chatTypeName = getChatTypeName(chatType);
    const userKey = `user_${user}_${chatType}`;
    
    if ($("#userList").find(`[data-user='${userKey}']`).length === 0) {
        $("#userList").append(`
            <div class="user-item" data-user="${userKey}" data-room-id="${roomId}" data-chat-type="${chatType}">
                <div class="user-info">
                    <strong>사용자 ${user}</strong><br>
                    <span class="chat-type">${chatTypeName} 문의</span><br>
                    <span class="last-message">${lastMsg}</span>
                </div>
                <div class="badge">${unreadCount && unreadCount > 0 ? unreadCount : ''}</div>
            </div>
        `);
    } else {
        $(`[data-user='${userKey}'] .last-message`).text(lastMsg);
        $(`[data-user='${userKey}'] .badge`).text(unreadCount && unreadCount > 0 ? unreadCount : '');
    }
}

// 채팅 타입 이름 반환
function getChatTypeName(chatType) {
    switch(chatType) {
        case "0": return "객실";
        case "1": return "다이닝";
        case "2": return "일반";
        default: return "일반";
    }
}

// 유저 선택 시 DB에서 메시지 내역 불러오기
$(document).on("click", ".user-item", function() {
    const userKey = $(this).data("user");
    currentRoomId = $(this).data("room-id");
    const chatType = $(this).data("chat-type");
    const chatTypeName = getChatTypeName(chatType);
    
    currentUser = userKey;
    $("#chatWith").text(`사용자 ${userKey.split('_')[1]} - ${chatTypeName} 문의`);
    $("#chatBody").html("");
    
    // 메시지 내역 불러오기
    $.ajax({
        url: '/api/admin/chat/messages',
        method: 'GET',
        data: { room_id: currentRoomId },
        success: function(messages) {
            messages.forEach(function(msg) {
                // is_from_user가 'N'이면 오른쪽(관리자), 'Y'이면 왼쪽(사용자)
                const isMine = msg.is_from_user === 'N';
                console.log('[관리자 채팅] staffId:', staffId, 'msg.staff_id:', msg.staff_id, 'is_from_user:', msg.is_from_user, 'isMine:', isMine, 'content:', msg.content);
                appendChat(isMine ? staffId : msg.user_num, msg.content, isMine, msg.send_time);
            });
            
            // 스크롤을 맨 아래로
            $("#chatBody").scrollTop($("#chatBody")[0].scrollHeight);
            
            // 읽음 처리
            $.ajax({
                url: '/api/admin/chat/read',
                method: 'POST',
                data: { room_id: currentRoomId, staff_id: staffId }
            });
        },
        error: function() {
            console.error('메시지 조회 실패');
        }
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
    if (msg && currentRoomId && ws && ws.readyState === WebSocket.OPEN) {
        ws.send(currentRoomId + ":" + msg);
        appendChat(staffId, msg, true);
        $("#messageInput").val("");
        loadUserList(); // 메시지 전송 시 리스트 갱신
    } else if (!currentRoomId) {
        alert('채팅방이 선택되지 않았습니다.');
    } else if (!ws || ws.readyState !== WebSocket.OPEN) {
        alert('채팅 연결이 끊어졌습니다. 다시 시도해주세요.');
    }
}

// 채팅 출력
function appendChat(user, msg, isMine, timestamp) {
    const time = timestamp ? formatTime(timestamp) : new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    const formattedMsg = msg.replace(/\n/g, "<br>");

    const block = $("<div>").addClass("message-block").addClass(isMine ? "right" : "left");
    const message = $("<div>").addClass("chat-message").addClass(isMine ? "right" : "left").html(formattedMsg);
    const timeElem = $("<div>").addClass("message-time").text(time);

    block.append(message).append(timeElem);

    $("#chatBody").append(block);
    $("#chatBody").scrollTop($("#chatBody")[0].scrollHeight);
}

// 시간 포맷팅 (DB timestamp)
function formatTime(timestamp) {
    const date = new Date(timestamp);
    const h = date.getHours().toString().padStart(2, '0');
    const m = date.getMinutes().toString().padStart(2, '0');
    return `${h}:${m}`;
}
