// ===================== FAQ 토글 기능 =====================
// FAQ 항목 클릭 시 답변을 토글 (회원/비회원 모두 동작)
$(document).ready(function() {
  $(".faq-item").off('click').on('click', function() {
    const content = $(this).next(".faq-answer");
    $(".faq-answer").not(content).slideUp();
    content.slideToggle();
  });
});

// ===================== 채팅/문의 기능 =====================
$(document).ready(function() {
  // 사용자 정보 fetch (JWT 쿠키 기반): /api/user/me 호출해 user_num 받아옴
  function fetchCurrentUser() {
    return fetch('/api/user/me', { credentials: 'include' })
      .then(res => res.ok ? res.json() : null)
      .then(user => {
        if (user && user.user_num) {
          userNum = user.user_num;
          // 사용자 정보 콘솔 출력
          console.log('[사용자 로그인 정보] user_num:', user.user_num, 'email:', user.email,'이름:',user.name);
        } else {
          userNum = null;
          console.log('[사용자 로그인 정보] 로그인하지 않음');
        }
        checkLoginStatus(); // 로그인 상태 UI 갱신
        return userNum;
      });
  }

  // 로그인 상태 체크: user_num이 있으면 true, 없으면 false
  function checkLoginStatus() {
    if (!userNum) {
      $("#loginCheck").show();
      $("#chatOptions").hide();
      $("#chatMessages").hide();
      $("#inputArea").hide();
      return false;
    } else {
      $("#loginCheck").hide();
      $("#chatOptions").show();
      return true;
    }
  }

  // 시간 포맷 함수 (메시지 시간 표시용)
  function getCurrentTime() {
    const now = new Date();
    const h = now.getHours().toString().padStart(2, '0');
    const m = now.getMinutes().toString().padStart(2, '0');
    return `${h}:${m}`;
  }

  // 채팅 타입 이름 반환 (문의 유형)
  function getChatTypeName(chatType) {
    switch(chatType) {
      case 0: return "객실";
      case 1: return "다이닝";
      case 2: return "일반";
      default: return "일반";
    }
  }

  // 시간 포맷팅 (DB timestamp)
  function formatTime(timestamp) {
    const date = new Date(timestamp);
    const h = date.getHours().toString().padStart(2, '0');
    const m = date.getMinutes().toString().padStart(2, '0');
    return `${h}:${m}`;
  }

  // ===== 채팅 관련 변수 =====
  let isOpen = false; // 채팅창 열림/닫힘 상태
  let currentRoomId = null; // 현재 채팅방 ID
  let currentChatType = null; // 현재 문의 유형
  let ws = null; // WebSocket 객체
  let userNum = null; // 로그인한 사용자 번호
  let messageTimestamps = []; // 도배 방지용 타임스탬프
  let isMaximized = false; // 채팅창 최대화 상태

  // WebSocket 연결: userNum을 쿼리로 넘겨 연결
  function connectWebSocket() {
    if (!userNum) return;
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/chat?userId=${userNum}`;
    ws = new WebSocket(wsUrl);
    ws.onopen = function() {
      console.log('WebSocket 연결됨');
    };
    ws.onmessage = function(event) {
      // 메시지 수신 시 화면에 출력
      const [sender, msg] = event.data.split(":", 2);
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
      setTimeout(connectWebSocket, 3000); // 연결 끊기면 재연결 시도
    };
    ws.onerror = function(error) {
      console.error('WebSocket 오류:', error);
    };
  }

  // 채팅방 생성/입장: chat_type별로 채팅방 생성 또는 조회
  function enterChatRoom(chatType) {
    currentChatType = chatType;
    $.ajax({
      url: '/api/chat/room',
      method: 'POST',
      contentType: 'application/json',
      data: JSON.stringify({ chat_type: chatType.toString() }),
      xhrFields: { withCredentials: true }, // JWT 쿠키 자동 전송
      success: function(room) {
        currentRoomId = room.room_id;
        $("#chatTitle").text(getChatTypeName(chatType) + " 문의");
        $("#chatOptions").hide();
        $("#chatMessages").show();
        $("#inputArea").show();
        loadChatMessages(room.room_id);
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

  // 채팅 메시지 불러오기: room_id로 메시지 목록 조회
  function loadChatMessages(roomId) {
    $.ajax({
      url: '/api/chat/messages',
      method: 'GET',
      data: { room_id: roomId },
      xhrFields: { withCredentials: true }, // JWT 쿠키 자동 전송
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

  // ===== 메시지 전송 함수 =====
  // 메시지 전송 및 도배 방지, 300자 제한 등
  function sendMessage() {
    const msg = $("#messageInput").val();
    if (msg.replace(/\s/g, '') === "") return; // 공백/엔터만 있는 경우 차단
    if (msg.length > 300) {
      showError("⚠️ 300자 초과로 전송할 수 없습니다.");
      return;
    }
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

  // 오류 메시지 표시 (5초 후 자동 제거)
  function showError(msg) {
    if ($("#errorBox").length === 0) {
      $("#inputArea").prepend(`<div id="errorBox" style="color:red; margin-bottom:4px;"></div>`);
    }
    $("#errorBox").text(msg);
    setTimeout(() => $("#errorBox").fadeOut(300, function() { $(this).remove(); }), 5000);
  }

  // 전송 버튼 비활성화 (도배 방지)
  function disableSendBtn() {
    $("#sendBtn").prop("disabled", true).css({
      "background-color": "#ccc",
      "cursor": "not-allowed"
    });
    setTimeout(() => {
      $("#sendBtn").prop("disabled", false).css({
        "background-color": "",
        "cursor": ""
      });
      $("#errorBox").remove();
      messageTimestamps = [];
    }, 30000);
  }

  // ====== fetchCurrentUser 완료 후 채팅/문의 바인딩 ======
  fetchCurrentUser().then(() => {
    // 채팅 팝업은 항상 닫힌 상태로 시작
    $("#chatBox").hide();
    isOpen = false;

    // 채팅 팝업 버튼 바인딩: 회원(userNum 있으면)만 열림, 비회원은 로그인 안내
    $("#chatToggleBtn").off('click').on('click', function() {
      if (!userNum) {
        alert("로그인이 필요한 서비스입니다.\n로그인 후 이용해 주세요.");
        return;
      }
      isOpen = !isOpen;
      $("#chatBox").fadeToggle();
      $(this).html(isOpen ? "✖" : "💬");
    });

    // 문의 시작 버튼 클릭 시 입력창 활성화
    $("#startChatBtn").off('click').on('click', function() {
      $("#inputArea").css("display", "flex");
      $(this).hide();
      $("#chatBody").html('');
    });

    // 문의 유형 버튼 클릭 시 반드시 POST로 채팅방 생성 (GET 금지)
    // (a, form, location.href 등 GET 요청 유발 코드가 있으면 절대 사용하지 말 것)
    $(document).off('click', '.chat-option').on('click', '.chat-option', function(e) {
      e.preventDefault(); // 혹시라도 a/form 등으로 GET이 발생하는 경우 방지
      if (!userNum) {
        alert('로그인이 필요합니다.');
        return;
      }
      const chatType = $(this).data('type');
      enterChatRoom(chatType); // 반드시 POST로만 호출
    });

    // 전송 버튼 클릭 (로그인 안 되어 있어도 안내만)
    $("#sendBtn").off('click').on('click', function() {
      if (!userNum) {
        alert("로그인이 필요합니다.");
        return;
      }
      sendMessage();
    });

    // 엔터 전송 (shift+enter는 줄바꿈)
    $("#messageInput").off('keydown').on('keydown', function(e) {
      if (e.key === "Enter") {
        if (e.shiftKey) return;
        e.preventDefault();
        sendMessage();
      }
    });

    // 최대화 버튼 클릭 시 채팅창 크기 토글
    $("#maximizeBtn").off('click').on('click', function() {
      $("#chatBox").toggleClass("maximized");
      isMaximized = !isMaximized;
    });
    // 뒤로가기 버튼 클릭 시 채팅방 나가기
    $("#backBtn").off('click').on('click', function() {
      $("#inputArea").hide();
      $("#chatMessages").hide();
      $("#chatOptions").show();
      $("#chatTitle").text("1:1 채팅");
      currentRoomId = null;
      currentChatType = null;
    });
  });
});
