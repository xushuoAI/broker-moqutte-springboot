package com.hyls.sb.mqtt;

import groovy.util.logging.Slf4j;
import io.moquette.interception.AbstractInterceptHandler;
import io.moquette.interception.messages.InterceptAcknowledgedMessage;
import io.moquette.interception.messages.InterceptConnectMessage;
import io.moquette.interception.messages.InterceptConnectionLostMessage;
import io.moquette.interception.messages.InterceptPublishMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component("safetyInterceptHandler")
public class SafetyInterceptHandler extends AbstractInterceptHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MoquetteServer.class);
    @Override
    public String getID() {
        return SafetyInterceptHandler.class.getName();
    }

    @Override
    public void onConnect(InterceptConnectMessage msg) {
        super.onConnect(msg);
        LOG.info("==啥玩意连接上了====");
    }

    @Override
    public void onConnectionLost(InterceptConnectionLostMessage msg) {
        super.onConnectionLost(msg);

        LOG.info("==哦豁，有人掉了ID:===="+msg.getClientID());
        LOG.info("==哦豁，有人掉了Name:===="+msg.getUsername());
    }


    @Override
    public void onPublish(InterceptPublishMessage msg) {
        super.onPublish(msg);
    }


    @Override
    public void onMessageAcknowledged(InterceptAcknowledgedMessage msg) {
        super.onMessageAcknowledged(msg);
    }

}