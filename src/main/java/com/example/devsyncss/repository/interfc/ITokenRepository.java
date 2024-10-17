package com.example.devsyncss.repository.interfc;

import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;

import java.util.List;

public interface ITokenRepository {
    void createToken(Token token);
    Token getToken(Long id);
    void updateToken(Token token);
    boolean deleteToken(Long id);
    void deleteAllTokensPassResetDate();
    Token getUserTokens(User user);
    List<Token> getAllTokens();
    void save(Token token);

    Token getTokenByUserId(Long userId);
}
