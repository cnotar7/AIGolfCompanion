package com.cnotar7.projects.aigolfcompanion.service;

import com.cnotar7.projects.aigolfcompanion.dto.PlayerStatsDTO;
import com.cnotar7.projects.aigolfcompanion.model.*;
import org.springframework.ai.chat.client.ChatClient;
import com.cnotar7.projects.aigolfcompanion.util.SummaryPromptBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoundSummaryService {
    private SummaryPromptBuilder summaryPromptBuilder;
    private ChatClient chatClient;

    public String generateAiSummary(Round round, PlayerStatsDTO playerStatsDTO) {
        try {

            System.out.println("sdfgdfgfdghgfd");
            String prompt = summaryPromptBuilder.buildPrompt(round, playerStatsDTO);
            System.out.println("Fake Round Prompt: \n" + prompt);
            System.out.println("Before AI API Call");
            String response = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            System.out.println("Fake Round Response: \n" + response);
            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
