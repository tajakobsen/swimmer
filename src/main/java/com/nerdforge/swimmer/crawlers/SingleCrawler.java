package com.nerdforge.swimmer.crawlers;

import com.google.inject.assistedinject.Assisted;
import com.nerdforge.swimmer.CrawlerClient;
import com.nerdforge.unxml.parsers.ObjectParser;
import play.libs.F.Promise;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import javax.inject.Inject;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Creates a function that consumes a Parent object, invokes a url found there,
 * parses child xml, adds the result to the parent object, and returns a Promise of
 * the parent object.
 * @param <T> Parent class
 * @param <S> Child class
 */
public class SingleCrawler<T, S> implements Crawler<T> {
    private final BiFunction<WSClient, T, Promise<WSResponse>> response;
    private final ObjectParser<S> parser;
    private final BiConsumer<T, S> combiner;
    private final WSClient client;

    @Inject
    public SingleCrawler(@Assisted BiFunction<WSClient, T, Promise<WSResponse>> response,
                         @Assisted ObjectParser<S> parser,
                         @Assisted BiConsumer<T, S> combiner,
                         @CrawlerClient WSClient client) {
        this.response = response;
        this.parser = parser;
        this.combiner = combiner;
        this.client = client;
    }

    public Promise<T> apply(T parent){
        return response.apply(client, parent)
                .map(WSResponse::asXml)
                .map(parser::apply)
                .map(child -> {
                    combiner.accept(parent, child); // adds child to parent
                    return parent;
                });
    }

    public Crawler<T> identity() {
        return Promise::pure;
    }
}
