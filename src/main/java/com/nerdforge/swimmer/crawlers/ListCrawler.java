package com.nerdforge.swimmer.crawlers;

import com.nerdforge.swimmer.util.PromiseHelper;
import play.libs.F.Promise;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ListCrawler<ParentType> implements Crawler<List<ParentType>> {
    private final Crawler<ParentType> crawler;
    private final PromiseHelper helper;

    public ListCrawler(Crawler<ParentType> crawler, PromiseHelper helper) {
        this.crawler = crawler;
        this.helper = helper;
    }

    @Override
    public Promise<List<ParentType>> apply(List<ParentType> list){
        return helper.sequence(list.stream().map(crawler::apply).collect(toList()));
    }
}