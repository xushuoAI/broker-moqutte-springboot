package com.hyls.sb.mqtt;

import groovy.util.logging.Slf4j;
import io.moquette.broker.ClientDescriptor;
import io.moquette.broker.Server;
import io.moquette.broker.config.ClasspathResourceLoader;
import io.moquette.broker.config.IConfig;
import io.moquette.broker.config.IResourceLoader;
import io.moquette.broker.config.ResourceLoaderConfig;
import io.moquette.broker.security.IAuthorizatorPolicy;
import io.moquette.interception.InterceptHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


@Slf4j
@Service
public class MoquetteServer {
    @Value("${mqtt-server.config-path}")
    private String configFilePath;

    @Autowired
    private IAuthenticatorImp iAuthenticatorImp;

    @Autowired
    private PermitAllAuthorizator authorizatorPolicy;
    private static final Logger LOG = LoggerFactory.getLogger(MoquetteServer.class);
    /**
     * Safety相关的拦截器，如果有其它业务，可以再去实现一个拦截器处理其它业务
     */
    @Autowired
    @Qualifier("safetyInterceptHandler")
    private InterceptHandler safetyinterceptHandler;


    private Server mqttServer;

    public void startServer() throws IOException {
        IResourceLoader configFileResourceLoader = new ClasspathResourceLoader(configFilePath);
        final IConfig config = new ResourceLoaderConfig(configFileResourceLoader);

        mqttServer = new Server();

        /**添加处理Safety相关的拦截器，如果有其它业务，可以再去实现一个拦截器处理其它业务，然后也添加上即可*/
        List<InterceptHandler> interceptHandlers = Arrays.asList(safetyinterceptHandler);
        /**
         * Authenticator 不显示设置，Server会默认以password_file创建一个ResourceAuthenticator
         * 如果需要更灵活的连接验证方案，可以继承IAuthenticator接口,自定义实现
         */
        mqttServer.startServer(config, interceptHandlers, null, iAuthenticatorImp, authorizatorPolicy);


    }


    public void stop() {
        mqttServer.stopServer();
    }

}
