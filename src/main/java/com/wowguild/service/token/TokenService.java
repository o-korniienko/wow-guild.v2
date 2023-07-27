package com.wowguild.service.token;

import com.wowguild.entity.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    Token getToken();

}
