package com.nerdforge.swimmer.util;

import com.nerdforge.unxml.Parsing;
import com.nerdforge.unxml.factory.ParsingFactory;
import org.w3c.dom.Document;
import play.libs.F;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestWSClientBuilder {
    private WSClient client;

    private TestWSClientBuilder(){
        client = mock(WSClient.class, RETURNS_DEEP_STUBS);
    }

    public static TestWSClientBuilder create(){
        return new TestWSClientBuilder();
    }

    public TestWSClientBuilder response(String url, String xml){
        WSResponse response = mock(WSResponse.class);

        when(response.asXml()).thenReturn(document(xml));
        when(client.url(contains(url)).get()).thenReturn(F.Promise.pure(response));

        return this;
    }

    public WSClient build(){
        return client;
    }

    private Document document(String xml){
        Parsing parsing = ParsingFactory.getInstance().create();
        return parsing.xml().document(xml);
    }
}
