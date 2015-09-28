package com.nerdforge.swimmer.crawlers;

import play.libs.F.Promise;

import java.util.function.Function;

public interface Crawler<T> extends Function<T, Promise<T>> {}