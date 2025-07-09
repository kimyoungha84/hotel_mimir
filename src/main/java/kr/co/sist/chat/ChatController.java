package kr.co.sist.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    /**
     * ✅ 채팅방 입장 또는 생성 (프론트에서 최초 문의 버튼 누를 때)
     * @param session 세션에서 userNum 고정값 사용
     * @param chatType 채팅 유형 (0:객실, 1:다이닝, 2:일반)
     * @return roomId (채팅방 ID)
     */
    @PostMapping("/enter")
    public int enterChat(HttpSession session, @RequestParam String chatType) {
        // 로그인 기능 미구현 상태이므로 고정 userNum 사용
        Long userNum = (Long) session.getAttribute("userNum");
        if (userNum == null) {
            userNum = 10001L;
            session.setAttribute("userNum", userNum);
        }
        session.setAttribute("chatType", chatType);

        return chatService.getOrCreateRoom(userNum, chatType);
    }

    /**
     * ✅ 이전 메시지 불러오기 (사용자가 다시 해당 채팅 유형 클릭했을 때)
     * @param session
     * @return 메시지 목록
     */
    @PostMapping("/load")
    public List<ChatMessageDTO> loadMessages(HttpSession session) {
        Long userNum = (Long) session.getAttribute("userNum");
        String chatType = (String) session.getAttribute("chatType");

        if (userNum == null || chatType == null) {
            return List.of(); // 오류 또는 채팅 미진입 상태
        }

        return chatService.loadMessages(userNum, chatType);
    }
}
