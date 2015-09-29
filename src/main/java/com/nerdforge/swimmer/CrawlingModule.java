package com.nerdforge.swimmer;

import com.google.inject.PrivateModule;
import com.nerdforge.swimmer.factory.CrawlingFactory;
import com.nerdforge.swimmer.util.PlayPromiseHelper;
import com.nerdforge.swimmer.util.PromiseHelper;
import play.libs.ws.WSClient;

public class CrawlingModule extends PrivateModule {
    private WSClient client;

    public CrawlingModule(WSClient client){
        this.client = client;
    }

    @Override
    protected void configure() {
        bind(WSClient.class).annotatedWith(CrawlerClient.class).toInstance(client);

        bind(CrawlingFactory.class);
        expose(CrawlingFactory.class);

        bind(PromiseHelper.class).to(PlayPromiseHelper.class);

        // utility class
        bind(Crawling.class);
        expose(Crawling.class);
    }
}
