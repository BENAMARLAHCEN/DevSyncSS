package com.example.devsyncss.service.interfc;

import com.example.devsyncss.entities.Token;
import com.example.devsyncss.entities.User;

public interface ITokenService {
    void addToken(Token token);
    Token getToken(User user);
    Token getTokenByUserId(Long userId);
}
