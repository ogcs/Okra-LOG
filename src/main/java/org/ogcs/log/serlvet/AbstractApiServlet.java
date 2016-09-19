package org.ogcs.log.serlvet;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.HashMap;
import java.util.Map;


/**
 * 内置response用于返回页面json信息
 * @author TinyZ
 * @date 2016-09-17.
 */
public abstract class AbstractApiServlet implements ApiServlet {

    public HttpResponse response(final int state) {
        return response(state, null);
    }

    public HttpResponse response(final int state, final Object data) {
        return response(new HashMap<String, Object>() {{
            put("state", state);
            if (data != null)
                put("data", data);
        }});
    }

    public HttpResponse response(Map<String, Object> params) {
        return response(JSON.toJSONString(params));
    }

    public HttpResponse response(String json) {
        return response(Unpooled.copiedBuffer(json.getBytes()));
    }

    public HttpResponse response(ByteBuf byteBuf) {
        return response(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
    }

    public HttpResponse response(HttpVersion httpVersion, HttpResponseStatus status) {
        return new DefaultFullHttpResponse(httpVersion, status);
    }

    public HttpResponse response(HttpVersion httpVersion, HttpResponseStatus status, ByteBuf byteBuf) {
        return new DefaultFullHttpResponse(httpVersion, status, byteBuf);
    }

}
