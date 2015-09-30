package com.nerdforge.swimmer.crawlers;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.google.inject.util.Modules;
import com.nerdforge.swimmer.Crawling;
import com.nerdforge.swimmer.CrawlingModule;
import com.nerdforge.swimmer.util.PromiseHelper;
import com.nerdforge.swimmer.util.TestPromiseHelper;
import com.nerdforge.swimmer.util.TestWSClientBuilder;
import com.nerdforge.unxml.Parsing;
import com.nerdforge.unxml.factory.ParsingFactory;
import com.nerdforge.unxml.parsers.ObjectParser;
import org.junit.Before;
import org.junit.Test;
import play.libs.F.Promise;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class ListCrawlerTest {
    @Inject
    private Crawling crawling;

    @Bind
    private PromiseHelper helper = new TestPromiseHelper();

    @Before
    public void before(){
        WSClient client = TestWSClientBuilder.create()
                .response("http://bar.com/0", "<bar><id>0</id></bar>")
                .response("http://bar.com/1", "<bar><id>1</id></bar>")
                .build();

        Module testModule = Modules.override(new CrawlingModule(client)).with(BoundFieldModule.of(this));
        Guice.createInjector(testModule).injectMembers(this);
    }

    @Test
    public void testListCrawling(){
        Parsing parsing = ParsingFactory.getInstance().create();
        ObjectParser<Bar> parser = parsing.obj("bar").attribute("id").as(Bar.class);

        List<Foo> fooList = new ArrayList<>();
        fooList.add(new Foo("http://bar.com/0"));
        fooList.add(new Foo("http://bar.com/1"));

        // create crawler
        Crawler<List<Foo>> crawler = crawling.list(crawling.create(foo -> foo.barUrl, parser, (foo, bar) -> foo.bar = bar));

        // apply crawler
        List<Foo> result = Promise.pure(fooList)
                .flatMap(crawler::apply) // Use flatMap with the crawler
                .get(1000L);

        assertThat(result.get(0).bar.id).isEqualTo("0");
        assertThat(result.get(1).bar.id).isEqualTo("1");
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
