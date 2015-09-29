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
 * @param <ParentType> Parent class
 * @param <ChildType> Child class
 */
public class SingleCrawler<ParentType, ChildType> implements Crawler<ParentType> {
    private final BiFunction<WSClient, ParentType, Promise<WSResponse>> response;
    private final ObjectParser<ChildType> parser;
    private final BiConsumer<ParentType, ChildType> combiner;
    private final WSClient client;

    @Inject
    public SingleCrawler(@Assisted BiFunction<WSClient, ParentType, Promise<WSResponse>> response,
                         @Assisted ObjectParser<ChildType> parser,
                         @Assisted BiConsumer<ParentType, ChildType> combiner,
                         @CrawlerClient WSClient client) {
        this.response = response;
        this.parser = parser;
        this.combiner = combiner;
        this.client = client;
    }

    public Promise<ParentType> apply(ParentType parent){
        return response.apply(client, parent)
                .map(WSResponse::asXml)
                .map(parser::apply)
                .map(child -> {
                    combiner.accept(parent, child); // adds child to parent
                    return parent;
                });
    }
}
