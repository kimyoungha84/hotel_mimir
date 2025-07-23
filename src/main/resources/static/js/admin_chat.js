let currentUser = null;
let currentRoomId = null;
let ws = null;

// staffId는 템플릿에서 window.staffId로 전달되어 있다고 가정
console.log("[admin_chat.js] staffId:", staffId);

// WebSocket 연결
function connectWebSocket() {
    if (!staffId || !currentRoomId) {
        console.warn('WebSocket 연결에 필요한 staffId 또는 currentRoomId가 없습니다.');
        return;
    }
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    // roomId를 반드시 포함
    const wsUrl = `${protocol}//${window.location.host}/chat?userId=${staffId}&roomId=${currentRoomId}`;
    ws = new WebSocket(wsUrl);
    ws.onopen = function() {
        console.log('관리자 WebSocket 연결됨', wsUrl);
    };
    ws.onmessage = function(event) {
        let handled = false;
        try {
            const data = JSON.parse(event.data);
            if ((data.type === "typing" || data.type === "typing_end") && data.userId !== staffId) {
                if (data.type === "typing") {
                    $("#typing-indicator").text("상대방이 입력 중입니다...");
                } else {
                    $("#typing-indicator").text("");
                }
                handled = true;
            }
        } catch(e) {}
        if (!handled) {
            // 기존 텍스트 메시지 처리
            const [roomId, sender, msg] = event.data.split(":", 3);
            if (roomId == currentRoomId) {
                const isMine = sender === staffId;
                appendChat(sender, msg, isMine);
            }
            if (sender !== staffId) {
                addUserToList(sender, msg);
            }
            loadUserList();
        }
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
                addUserToList(room.user_num, "", 0, room.room_id, room.chat_type);
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
    // #chatBody 아래에 typing-indicator가 없으면 추가
    if ($("#typing-indicator").length === 0) {
        $("#chatBody").after('<div id="typing-indicator" style="min-height:20px;color:#888;font-size:13px;padding:2px 10px;"></div>');
    }
    // ===== 타이핑 인디케이터 기능 =====
    let typingTimeout;
    $("#messageInput").on("keyup", function() {
        clearTimeout(typingTimeout);
        if (ws && ws.readyState === WebSocket.OPEN) {
            ws.send(JSON.stringify({
                type: "typing",
                roomId: currentRoomId,
                userId: staffId
            }));
        }
        typingTimeout = setTimeout(() => {
            if (ws && ws.readyState === WebSocket.OPEN) {
                ws.send(JSON.stringify({
                    type: "typing_end",
                    roomId: currentRoomId,
                    userId: staffId
                }));
            }
        }, 500);
    });
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
                <div class="badge"></div>
            </div>
        `);
    } else {
        $(`[data-user='${userKey}'] .last-message`).text(lastMsg);
        $(`[data-user='${userKey}'] .badge`).text("");
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
    // 채팅방 선택 시마다 WebSocket 재연결
    if (ws) { ws.close(); }
    connectWebSocket();
    // 메시지 내역 불러오기
    $.ajax({
        url: '/api/admin/chat/messages',
        method: 'GET',
        data: { room_id: currentRoomId },
        success: function(messages) {
            messages.forEach(function(msg) {
                const isMine = msg.is_from_user === 'N';
                appendChat(isMine ? staffId : msg.user_num, msg.content, isMine, msg.send_time, undefined, msg.message_id);
            });
            $("#chatBody").scrollTop($("#chatBody")[0].scrollHeight);
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
        appendChat(staffId, msg, true, undefined, undefined, undefined);
        $("#messageInput").val("");
        loadUserList();
    } else if (!currentRoomId) {
        alert('채팅방이 선택되지 않았습니다.');
    } else if (!ws || ws.readyState !== WebSocket.OPEN) {
        alert('채팅 연결이 끊어졌습니다. 다시 시도해주세요.');
    }
}

// HTML 태그를 escape하는 함수 (XSS 방지)
function escapeHtml(str) {
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/\"/g, "&quot;")
    .replace(/'/g, "&#39;");
}

// 채팅 출력
function appendChat(user, msg, isMine, timestamp, isRead, messageId) {
    const time = timestamp ? formatTime(timestamp) : new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    // HTML escape 적용
    const formattedMsg = escapeHtml(msg).replace(/\n/g, "<br>");
    const block = $("<div>").addClass("message-block").addClass(isMine ? "right" : "left").attr("id", messageId ? `message-${messageId}` : undefined);
    const message = $("<div>").addClass("chat-message").addClass(isMine ? "right" : "left").html(formattedMsg);
    // 읽음/안읽음 뱃지 표시 부분 삭제
    const timeElem = $("<div>").addClass("message-time").text(time);
    block.append(message).append(timeElem);
    $("#chatBody").append(block).scrollTop($("#chatBody")[0].scrollHeight);
}

// 시간 포맷팅 (DB timestamp)
function formatTime(timestamp) {
    const date = new Date(timestamp);
    const h = date.getHours().toString().padStart(2, '0');
    const m = date.getMinutes().toString().padStart(2, '0');
    return `${h}:${m}`;
}

// ws.onmessage 내부에 아래 코드 추가(기존 텍스트 메시지 처리와 병합)
const origOnMessage = ws && ws.onmessage;
function adminChatOnMessage(event) {
    let handled = false;
    try {
        const data = JSON.parse(event.data);
        if ((data.type === "typing" || data.type === "typing_end") && data.userId !== staffId) {
            if (data.type === "typing") {
                $("#typing-indicator").text("상대방이 입력 중입니다...");
            } else {
                $("#typing-indicator").text("");
            }
            handled = true;
        }
    } catch(e) {}
    if (!handled) {
        // 기존 메시지 처리
        if (typeof origOnMessage === 'function') origOnMessage.call(ws, event);
        else {
            // 기존 ws.onmessage 코드 복사
            const [roomId, sender, msg] = event.data.split(":", 3);
            if (roomId == currentRoomId) {
                const isMine = sender === staffId;
                appendChat(sender, msg, isMine);
            }
            if (sender !== staffId) {
                addUserToList(sender, msg);
            }
            loadUserList();
        }
    }
}
if (ws) ws.onmessage = adminChatOnMessage;
