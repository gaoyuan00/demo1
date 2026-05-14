package com.example.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import com.example.dto.ChatRequestDTO;
import com.example.vo.ChatResponseVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatServiceImpl implements ChatService {
    
    private final StringRedisTemplate stringRedisTemplate;
    private final ChatClient chatClient;

    private static final String REDIS_KEY_PREFIX = "chat:session:";
    private static final long SESSION_TTL_MINUTES = 30;
    private static final int MAX_HISTORY_ROUNDS = 3;

    public ChatServiceImpl(ChatClient.Builder chatClientBuilder, StringRedisTemplate stringRedisTemplate) {
        this.chatClient = chatClientBuilder
                .defaultSystem("你是一名专业、友好、简洁的中文智能助手,请结合历史上下文回答问题")
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        String sessionId = requestDTO.getSessionId();
        String message = requestDTO.getMessage();
        
        String redisKey = REDIS_KEY_PREFIX + sessionId;

        List<String> records = stringRedisTemplate.opsForList().range(redisKey, 0, -1);
        String historyText = "";
        if (records != null && !records.isEmpty()) {
            historyText = String.join("\n", records);
        }

        String finalPrompt = String.format("""
                以下是历史对话:
                %s
                当前用户问题:
                %s
                """, historyText, message);

        String answer = chatClient.prompt(finalPrompt)
                .call()
                .content();

        String userRecord = "用户:" + message;
        String assistantRecord = "助手:" + answer;
        stringRedisTemplate.opsForList().rightPush(redisKey, userRecord);
        stringRedisTemplate.opsForList().rightPush(redisKey, assistantRecord);

        Long size = stringRedisTemplate.opsForList().size(redisKey);
        int maxRecords = MAX_HISTORY_ROUNDS * 2;
        if (size != null && size > maxRecords) {
            stringRedisTemplate.opsForList().trim(redisKey, size - maxRecords, size - 1);
        }

        stringRedisTemplate.expire(redisKey, SESSION_TTL_MINUTES, TimeUnit.MINUTES);

        return new ChatResponseVO(message, answer);
    }
}
