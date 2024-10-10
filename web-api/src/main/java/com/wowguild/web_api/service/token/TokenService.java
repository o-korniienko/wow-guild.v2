package com.wowguild.web_api.service.token;

import com.wowguild.common.entity.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {

    Token getToken();

}
