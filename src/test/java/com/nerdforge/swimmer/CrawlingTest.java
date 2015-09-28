package com.nerdforge.swimmer;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.inject.util.Modules;
import com.nerdforge.swimmer.crawlers.Crawler;
import com.nerdforge.unxml.Parsing;
import com.nerdforge.unxml.factory.ParsingFactory;
import com.nerdforge.unxml.parsers.ObjectParser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CrawlingTest {
    @Inject
    private Crawling crawling;

    private Parsing parsing;

    @Before
    public void before(){
        parsing = ParsingFactory.getInstance().create();
        Document document = parsing.xml().document("<bar><id>1</id></bar>");

        WSResponse response = mock(WSResponse.class);
        when(response.asXml()).thenReturn(document);

        WSClient client = mock(WSClient.class, RETURNS_DEEP_STUBS);
        when(client.url(anyString()).get()).thenReturn(Promise.pure(response));

        // Module testModule = Modules.override(new CrawlingModule()).with(BoundFieldModule.of(this));
        Guice.createInjector(new CrawlingModule(client)).injectMembers(this);
    }

    @Test
    public void test(){
        ObjectParser<Bar> parser = parsing.obj("bar").attribute("id").as(Bar.class);
        Foo input = new Foo("http://bar.com");

        Crawler<Foo> fooCrawler = crawling.create(foo -> foo.barUrl, parser, (foo, bar) -> foo.bar = bar);

        Foo foo = fooCrawler.apply(input).get(1000L);
        assertThat(foo.bar.id).isEqualTo("1");
    }

    public static class Foo {
        public String barUrl;
        public Bar bar;

        public Foo(String barUrl){
            this.barUrl = barUrl;
        }
    }

    public static class Bar {
        public String id;
    }
}
