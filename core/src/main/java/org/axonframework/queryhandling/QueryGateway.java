/*
 * Copyright (c) 2010-2017. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.axonframework.queryhandling;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Interface towards the Query Handling components of an application. This interface provides a friendlier API toward
 * the query bus.
 *
 * @author Marc Gathier
 * @author Allard Buijze
 * @since 3.1
 */
public interface QueryGateway {

    /**
     * sends given query to the query bus and expects a result of type resultClass. Execution may be asynchronous.
     *
     * @param query        the query
     * @param responseType the expected result type
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a completable future that contains the first result of the query.
     * @throws NullPointerException when query is null
     */
    default <R, Q> CompletableFuture<Collection<R>> query(Q query, Class<R> responseType) {
        return query(query.getClass().getName(), query, responseType);
    }

    /**
     * sends given query to the query bus and expects a result with name resultName. Execution may be asynchronous.
     *
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @param queryName    the name of the query
     * @param query        the query
     * @param responseType the expected response type
     * @return a completable future that contains the first result of the query.
     */
    <R, Q> CompletableFuture<Collection<R>> query(String queryName, Q query, Class<R> responseType);

    /**
     * sends given query to the query bus and expects a result of type resultClass. Execution may be asynchronous.
     *
     * @param query        the query
     * @param responseType the expected result type
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a completable future that contains the first result of the query.
     * @throws NullPointerException when query is null
     */
    default <R, Q> CompletableFuture<R> querySingle(Q query, Class<R> responseType) {
        return querySingle(query.getClass().getName(), query, responseType);
    }

    /**
     * sends given query to the query bus and expects a result with name resultName. Execution may be asynchronous.
     *
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @param queryName    the name of the query
     * @param query        the query
     * @param responseType the expected response type
     * @return a completable future that contains the first result of the query.
     */
    default <R, Q> CompletableFuture<R> querySingle(String queryName, Q query, Class<R> responseType) {
        return query(queryName, query, responseType).thenApply(c -> c.isEmpty() ? null : c.iterator().next());
    }

    /**
     * sends given query to the query bus and expects a stream of results with name resultName. The stream is completed
     * when a timeout occurs or when all results are received.
     *
     * @param <R>         The type of result expected from query execution
     * @param <Q>         The query class
     * @param queryName   the name of the query
     * @param query       the query
     * @param resultClass type type of result
     * @param timeout     timeout for the request
     * @param timeUnit    unit for the timeout
     * @return a stream of results
     */
    <R, Q> Stream<Collection<R>> scatterGather(String queryName, Q query, Class<R> resultClass, long timeout, TimeUnit timeUnit);

    /**
     * sends given query to the query bus and expects a stream of results with type responseType. The stream is completed when a timeout occurs
     * or when all results are received.
     *
     * @param query        the query
     * @param responseType the expected result type
     * @param timeout      timeout for the request
     * @param timeUnit     unit for the timeout
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a stream of results
     * @throws NullPointerException when query is null
     */
    default <R, Q> Stream<Collection<R>> scatterGather(Q query, Class<R> responseType, long timeout, TimeUnit timeUnit) {
        return scatterGather(query.getClass().getName(), query, responseType, timeout, timeUnit);
    }

    /**
     * sends given query to the query bus and expects a result of type resultClass. Execution may be asynchronous.
     *
     * @param query        the query
     * @param queryName    the name of the query
     * @param responseType the expected result type
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a completable future that contains the first result of the query.
     * @throws NullPointerException when query is null
     * @deprecated use {@link #query(String, Object, Class)} instead.
     */
    @Deprecated
    default <R, Q> CompletableFuture<R> send(String queryName,Q query, Class<R> responseType) {
        return query(queryName, query, responseType).thenApply(s -> s.isEmpty() ?  null : s.iterator().next());
    }

    /**
     * sends given query to the query bus and expects a result of type resultClass. Execution may be asynchronous.
     *
     * @param query        the query
     * @param responseType the expected result type
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a completable future that contains the first result of the query.
     * @throws NullPointerException when query is null
     * @deprecated use {@link #query(Object, Class)} instead
     */
    @Deprecated
    default <R, Q> CompletableFuture<R> send(Q query, Class<R> responseType) {
        return querySingle(query.getClass().getName(), query, responseType);
    }

    /**
     * sends given query to the query bus and expects a stream of results with type responseType. The stream is completed when a timeout occurs
     * or when all results are received.
     *
     * @param query        the query
     * @param responseType the expected result type
     * @param timeout      timeout for the request
     * @param timeUnit     unit for the timeout
     * @param <R>          The type of result expected from query execution
     * @param <Q>          The query class
     * @return a stream of results
     * @throws NullPointerException when query is null
     * @deprecated use {@link #scatterGather(Object, Class, long, TimeUnit)} instead
     */
    @Deprecated
    default <R, Q> Stream<R> send(Q query, Class<R> responseType, long timeout, TimeUnit timeUnit) {
        return scatterGather(query.getClass().getName(), query, responseType, timeout, timeUnit).flatMap(Collection::stream);
    }

    /**
     * sends given query to the query bus and expects a stream of results with name resultName. The stream is completed when a timeout occurs
     * or when all results are received.
     *
     * @param query       the query
     * @param queryName   the name of the query
     * @param resultClass type type of result
     * @param timeout     timeout for the request
     * @param timeUnit    unit for the timeout
     * @param <R>         The type of result expected from query execution
     * @param <Q>         The query class
     * @return a stream of results
     * @deprecated use {@link #scatterGather(String, Object, Class, long, TimeUnit)} instead
     */
    @Deprecated
    default <R, Q> Stream<R> send(Q query, String queryName, Class<R> resultClass, long timeout, TimeUnit timeUnit) {
        return scatterGather(queryName, query, resultClass, timeout, timeUnit).flatMap(Collection::stream);
    }
}
