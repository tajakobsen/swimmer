package com.nerdforge.swimmer;

import com.google.inject.PrivateModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.nerdforge.swimmer.crawlers.SingleCrawler;
import com.nerdforge.swimmer.factory.CrawlingFactory;
import com.nerdforge.swimmer.factory.SingleCrawlerFactory;
import com.nerdforge.unxml.UnXmlModule;
import play.libs.ws.WSClient;

public class CrawlingModule extends PrivateModule {
    private WSClient client;

    public CrawlingModule(WSClient client){
        this.client = client;
    }

    @Override
    protected void configure() {
        install(new UnXmlModule());

        bind(WSClient.class).annotatedWith(CrawlerClient.class).toInstance(client);

        // Generate Factories
        install(new FactoryModuleBuilder()
                .implement(SingleCrawler.class, SingleCrawler.class)
                .build(SingleCrawlerFactory.class));

        bind(CrawlingFactory.class);
        expose(CrawlingFactory.class);

        // utility class
        bind(Crawling.class);
        expose(Crawling.class);
    }
}
