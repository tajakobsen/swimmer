package com.nerdforge.swimmer.util;

import play.libs.F.Promise;
import java.util.List;

public interface PromiseHelper {
    <A> Promise<List<A>> sequence(Iterable<Promise<A>> promises);
}
