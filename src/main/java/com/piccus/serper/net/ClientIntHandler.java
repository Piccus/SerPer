package com.piccus.serper.net;

import com.piccus.serper.tools.Converter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by Piccus on 2016/12/6.
 */
public class ClientIntHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = Logger.getLogger(ClientIntHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("ClientIntHandler.channelActive");
        Person person = new Person();
        person.setName("Mitsuha");
        person.setSex("female");
        person.setAge(17);
        String str = Converter.toJsonString(person);
        ctx.writeAndFlush(str);
        ctx.close();
    }
}
