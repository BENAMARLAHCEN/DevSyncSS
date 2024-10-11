package com.example.devsyncss.scheduler;

import com.example.devsyncss.entities.Token;
import com.example.devsyncss.service.TokenService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final TokenService tokenService = new TokenService();

    public void startTokenScheduler() {
        scheduler.scheduleAtFixedRate(this::resetTokens, 0, 1, TimeUnit.SECONDS);
    }

    private void resetTokens() {
        List<Token> tokens = tokenService.getAllTokens();
        for (Token token : tokens) {
            if (token.getLastResetDate().plusDays(1).isBefore(LocalDateTime.now())) {
                token.setModificationTokens(token.getResetModificationTokens() + token.getModificationTokens());
                token.setLastResetDate(LocalDateTime.now().plusDays(1));
                token.setResetModificationTokens(0);
                tokenService.updateToken(token);
            }
            if (LocalDateTime.now().getDayOfMonth() == 1) {
                token.setDeletionTokens(token.getResetDeletionTokens() + token.getDeletionTokens());
                token.setResetDeletionTokens(0);
                tokenService.updateToken(token);
            }
        }
    }
}
