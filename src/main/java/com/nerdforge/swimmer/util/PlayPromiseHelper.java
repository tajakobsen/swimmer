package com.nerdforge.swimmer.util;

import play.libs.F;

import java.util.List;

public class PlayPromiseHelper implements PromiseHelper {
    @Override
    public <A> F.Promise<List<A>> sequence(Iterable<F.Promise<A>> promises) {
        return F.Promise.sequence(promises);
    }
}
