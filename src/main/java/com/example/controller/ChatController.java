package com.example.controller;

import com.example.common.Result;
import com.example.dto.ChatRequestDTO;
import com.example.service.ChatService;
import com.example.vo.ChatResponseVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Result<ChatResponseVO> chat (@RequestBody ChatRequestDTO requestDTO) {
        ChatResponseVO responseVO = chatService.chat (requestDTO);
        return Result. success(responseVO);
    }
}
