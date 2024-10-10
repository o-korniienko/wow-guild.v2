package com.wowguild.service.token;

import com.wowguild.entity.Token;
import com.wowguild.repos.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TokenManagerImpl implements TokenManager {


    private final TokenRepo tokenRepo;
    private final BattleNetTokenService battleNetTokenService;
    private final WowLogsTokenService wowLogsTokenService;

    @Override
    public String getTokenByTag(String tag) {
        Token tokenFromDB = tokenRepo.findByTag(tag);

        String token = null;
        if (tokenFromDB != null) {

            if (!isTokenExpired(tokenFromDB)) {
                token = tokenFromDB.getAccess_token();
                return token;
            } else {
                if (tag.equals("blizzard")) {
                    Token newToken = battleNetTokenService.getToken();
                    token = newToken.getAccess_token();
                    tokenFromDB.setAccess_token(newToken.getAccess_token());
                    tokenFromDB.setCreateTime(newToken.getCreateTime());
                    tokenFromDB.setExpires_in(newToken.getExpires_in());
                    tokenRepo.save(tokenFromDB);
                    return token;
                }
                if (tag.equals("wow_logs")) {
                    Token newToken = wowLogsTokenService.getToken();
                    token = newToken.getAccess_token();
                    tokenFromDB.setAccess_token(newToken.getAccess_token());
                    tokenFromDB.setCreateTime(newToken.getCreateTime());
                    tokenFromDB.setExpires_in(newToken.getExpires_in());
                    tokenRepo.save(tokenFromDB);
                    return token;
                }
            }
        } else {
            if (tag.equals("blizzard")) {
                Token newToken = battleNetTokenService.getToken();
                token = newToken.getAccess_token();
                tokenRepo.save(newToken);
            }
            if (tag.equals("wow_logs")) {
                Token newToken = wowLogsTokenService.getToken();
                token = newToken.getAccess_token();
                tokenRepo.save(newToken);
            }
        }
        return token;
    }

    private boolean isTokenExpired(Token token) {
        long expiresIn = token.getExpires_in();
        LocalDateTime createTokenDate = token.getCreateTime();
        LocalDateTime dateTokenWorkUntil = createTokenDate.plusSeconds(expiresIn);
        LocalDateTime nowDate = LocalDateTime.now();
        if (nowDate.isAfter(dateTokenWorkUntil)) {
            return true;
        }

        return false;
    }
}
