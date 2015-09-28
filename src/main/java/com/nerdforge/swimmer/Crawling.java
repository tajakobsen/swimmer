package com.nerdforge.swimmer;

import com.nerdforge.swimmer.crawlers.Crawler;
import com.nerdforge.swimmer.crawlers.ListCrawler;
import com.nerdforge.swimmer.factory.SingleCrawlerFactory;
import com.nerdforge.unxml.parsers.ObjectParser;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Crawling {
    private final SingleCrawlerFactory factory;

    @Inject
    public Crawling(SingleCrawlerFactory factory) {
        this.factory = factory;
    }

    public <T, S> Crawler<T> create(BiFunction<WSClient, T, Promise<WSResponse>> response, ObjectParser<S> parser, BiConsumer<T, S> combiner){
        return factory.create(response, parser, combiner);
    }

    public <T, S> Crawler<T> create(Function<T, String> url, ObjectParser<S> parser, BiConsumer<T, S> combiner){
        return factory.create((client, child) -> client.url(url.apply(child)).get(), parser, combiner);
    }

    public <T> Crawler<List<T>> list(Crawler<T> crawler){
        return new ListCrawler<>(crawler);
    }
}