package com.nerdforge.swimmer.util;

import play.libs.F.Promise;

import java.util.ArrayList;
import java.util.List;

public class TestPromiseHelper implements PromiseHelper {
    @Override
    public <A> Promise<List<A>> sequence(Iterable<Promise<A>> promises) {
        List<A> result = new ArrayList<>();

        for(Promise<A> promise : promises){
            result.add(promise.get(1000L));
        }

        return Promise.pure(result);
    }
}
