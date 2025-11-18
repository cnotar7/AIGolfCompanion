package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.model.Round;
import jakarta.transaction.Transactional;
import org.springframework.ai.chat.client.ChatClient;
import com.cnotar7.projects.aigolfcompanion.repository.RoundRepository;
import com.cnotar7.projects.aigolfcompanion.util.SummaryPromptBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoundSummaryService {
    private SummaryPromptBuilder summaryPromptBuilder;
    private ChatClient chatClient;

    public String generateAiSummary(Round round) {
        try {
            String prompt = summaryPromptBuilder.buildPrompt(round);
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
