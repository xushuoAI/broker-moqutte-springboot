package com.hyls.sb.mqtt;

import io.moquette.broker.security.IAuthorizatorPolicy;
import io.moquette.broker.subscriptions.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PermitAllAuthorizator implements IAuthorizatorPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(PermitAllAuthorizator.class);
    @Override
    public boolean canWrite(Topic topic, String s, String s1) {
        LOG.info(topic.toString());
        LOG.info("==权限鉴定s===="+s);
        LOG.info("==权限鉴定s1===="+s1);
        return true;
    }

    @Override
    public boolean canRead(Topic topic, String s, String s1) {

        return true;
    }
}