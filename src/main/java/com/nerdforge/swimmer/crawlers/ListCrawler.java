package com.nerdforge.swimmer.crawlers;

import com.google.inject.assistedinject.Assisted;
import play.libs.F.Promise;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListCrawler<T> implements Crawler<List<T>> {
    private final Crawler<T> crawler;

    @Inject
    public ListCrawler(@Assisted Crawler<T> crawler) {
        this.crawler = crawler;
    }

    @Override
    public Promise<List<T>> apply(List<T> list){
        return Promise.sequence(
            list.stream()
                .map(crawler::apply)
                    .collect(toList())
        );
    }
}