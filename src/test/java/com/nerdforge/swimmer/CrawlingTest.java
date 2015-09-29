package com.nerdforge.swimmer;

import com.google.inject.Guice;
import com.nerdforge.swimmer.crawlers.Crawler;
import com.nerdforge.swimmer.util.TestWSClientBuilder;
import com.nerdforge.unxml.Parsing;
import com.nerdforge.unxml.factory.ParsingFactory;
import com.nerdforge.unxml.parsers.ObjectParser;
import org.junit.Before;
import org.junit.Test;
import play.libs.ws.WSClient;
import javax.inject.Inject;

import static org.fest.assertions.Assertions.assertThat;

public class CrawlingTest {
    @Inject
    private Crawling crawling;

    @Before
    public void before(){
        WSClient client = TestWSClientBuilder.create()
                .response("http://bar.com", "<bar><id>1</id></bar>")
                .build();

        Guice.createInjector(new CrawlingModule(client)).injectMembers(this);
    }

    @Test
    public void test(){
        Parsing parsing = ParsingFactory.getInstance().create();
        ObjectParser<Bar> parser = parsing.obj("bar").attribute("id").as(Bar.class);
        Foo input = new Foo("http://bar.com");

        Crawler<Foo> crawler = crawling.create(foo -> foo.barUrl, parser, (foo, bar) -> foo.bar = bar);

        Foo foo = crawler.apply(input).get(1000L);

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
