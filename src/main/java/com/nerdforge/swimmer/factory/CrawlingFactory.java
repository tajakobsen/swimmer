package com.nerdforge.swimmer.factory;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nerdforge.swimmer.Crawling;
import com.nerdforge.swimmer.CrawlingModule;
import play.libs.ws.WSClient;

import javax.inject.Inject;
import javax.inject.Provider;

public class CrawlingFactory {
    private final Provider<Crawling> crawling;

    @Inject
    private CrawlingFactory(Provider<Crawling> crawling){
        this.crawling = crawling;
    }

    /**
     * Returns an instance of the factory
     * @return An instance of this factory
     */
    public static CrawlingFactory getInstance(WSClient client){
        Injector injector = Guice.createInjector(new CrawlingModule(client));
        return injector.getInstance(CrawlingFactory.class);
    }

    /**
     * Returns a new Parsing object with all the utility methods to create parsers.
     * @return An instance of Parsing
     */
    public Crawling create(){
        return crawling.get();
    }
}
