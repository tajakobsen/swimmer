package com.nerdforge.swimmer;

import com.nerdforge.swimmer.crawlers.Crawler;
import com.nerdforge.swimmer.crawlers.ListCrawler;
import com.nerdforge.swimmer.crawlers.SingleCrawler;
import com.nerdforge.swimmer.util.PromiseHelper;
import com.nerdforge.unxml.parsers.ObjectParser;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Singleton
public class Crawling {
    private final WSClient client;
    private final PromiseHelper promiseHelper;

    @Inject
    public Crawling(@CrawlerClient WSClient client, PromiseHelper promiseHelper) {
        this.client = client;
        this.promiseHelper = promiseHelper;
    }

    public <ParentType, ChildType> Crawler<ParentType> create(
            BiFunction<WSClient, ParentType, Promise<WSResponse>> response,
            ObjectParser<ChildType> parser,
            BiConsumer<ParentType, ChildType> combiner){
        return new SingleCrawler<>(response, parser, combiner, client);
    }

    public <ParentType, ChildType> Crawler<ParentType> create(
            Function<ParentType, String> url,
            ObjectParser<ChildType> parser,
            BiConsumer<ParentType, ChildType> combiner){
        return create((client, child) -> client.url(url.apply(child)).get(), parser, combiner);
    }

    public <ParentType> Crawler<List<ParentType>> list(Crawler<ParentType> crawler){
        return new ListCrawler<>(crawler, promiseHelper);
    }
}