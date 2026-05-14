package com.example.service;

import com.example.dto. ChatRequestDTO;
import com.example.vo.ChatResponseVO;
public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDT0);
}
