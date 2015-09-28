package com.nerdforge.swimmer.factory;

import com.google.inject.assistedinject.Assisted;
import com.nerdforge.swimmer.crawlers.SingleCrawler;
import com.nerdforge.unxml.parsers.ObjectParser;
import play.libs.F;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface SingleCrawlerFactory {
    <T, S> SingleCrawler<T, S> create(BiFunction<WSClient, T, F.Promise<WSResponse>> response, ObjectParser<S> parser, BiConsumer<T, S> combiner);
}
