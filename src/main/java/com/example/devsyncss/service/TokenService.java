package com.example.devsyncss.service;

import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;
import com.example.devsyncss.repository.TokenRepository;
import com.example.devsyncss.repository.interfc.ITokenRepository;
import com.example.devsyncss.service.interfc.ITokenService;

public class TokenService implements ITokenService {

    private final ITokenRepository tokenRepository;

    public TokenService() {
        tokenRepository = new TokenRepository();
    }

    @Override
    public void addToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public Token getToken(User user) {
        return tokenRepository.getUserTokens(user);
    }

    @Override
    public Token getTokenByUserId(Long userId) {
        return tokenRepository.getToken(userId);
    }
}
