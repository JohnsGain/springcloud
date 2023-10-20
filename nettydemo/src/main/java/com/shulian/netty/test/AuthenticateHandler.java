package com.shulian.netty.test;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

/**
 * @author Lee HN
 * @date 2019/6/19 9:46
 * tcp登陆认证handler
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class AuthenticateHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private TcpChannelGroup channelGroup;

//    @Autowired
//    private PassengerRepository passengerRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String token = (String) msg;
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
        if (oAuth2AccessToken.isExpired()) throw new IllegalStateException("expired token");
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        String username = oAuth2Authentication.getName();
//        String idCache = redisTemplate.opsForValue().get(PassengerService.getIdKeyPrefix + username);
        String idCache = "";
//        Long userId = StringUtils.isBlank(idCache) ? passengerRepository.getIdByUsername(username) : Long.valueOf(idCache);
        Long userId = 1L;
        Channel channel = ctx.channel();
        channel.attr(AttributeKey.valueOf("username")).set(username);
        channel.attr(AttributeKey.valueOf("id")).set(userId);
        channelGroup.add(userId, channel);
        ctx.pipeline().remove(this);
        log.info("用户 " + username + " socket验证连接成功");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String message = cause.getMessage();
        ctx.writeAndFlush(message);
        ctx.close();
        log.error("=== " + ctx.channel().remoteAddress() + " is closed === " + message);
    }

}
