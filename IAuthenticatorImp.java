package com.hyls.sb.mqtt;

import io.moquette.broker.security.IAuthenticator;
import org.springframework.stereotype.Component;


@Component
public class IAuthenticatorImp implements IAuthenticator {


    @Override
    public boolean checkValid(String s, String s1, byte[] bytes) {
        return true;
    }
}
