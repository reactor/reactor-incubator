/*
 * Copyright (c) 2011-2016 Pivotal Software Inc., Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.groovy.ext

import groovy.transform.CompileStatic
import org.reactivestreams.Processor
import org.reactivestreams.Subscriber
import reactor.core.publisher.Mono
import reactor.fn.BiFunction
import reactor.fn.Consumer
import reactor.fn.Function
import reactor.fn.Predicate
import reactor.rx.Fluxion
import reactor.rx.Promise
import reactor.rx.subscriber.InterruptableSubscriber

//import reactor.io.codec.Codec
//import reactor.rx.IOStreams
/**
 * Glue for Groovy closures and operator overloading applied to Stream, Composable,
 * Promise and Deferred.
 * Also gives convenient Deferred handling by providing map/filter/consume operations
 *
 * @author Jon Brisbin
 * @author Stephane Maldini
 */
@CompileStatic
class FluxionExtensions {

  /**
   * Alias
   */

  // IO Streams
  /*static <SRC, IN> Fluxion<IN> decode(final Publisher<? extends SRC> publisher, Codec<SRC, IN, ?> codec) {
    IOStreams.decode(codec, publisher)
  }*/

  /**
   * Operator overloading
   */
  static <T> Mono<T> mod(final Fluxion<T> selfType, final BiFunction<T, T, T> other) {
    selfType.reduce other
  }

  //Mapping
  static <O, E extends Subscriber<? super O>> E or(final Fluxion<O> selfType, final E other) {
    selfType.broadcastTo(other)
  }

  static <T, V> Fluxion<V> or(final Fluxion<T> selfType, final Function<T, V> other) {
    selfType.map other
  }

  static <T, V> Fluxion<V> or(final Promise<T> selfType, final Function<T, V> other) {
    selfType.stream().map(other)
  }

  //Filtering
  static <T> Fluxion<T> and(final Fluxion<T> selfType, final Predicate<T> other) {
    selfType.filter other
  }

  static <T> Fluxion<T> and(final Promise<T> selfType, final Predicate<T> other) {
    selfType.stream().filter(other)
  }

  //Consuming
  static <T> InterruptableSubscriber<?> leftShift(final Fluxion<T> selfType, final Consumer<T> other) {
    selfType.consume other
  }

  static <T> Mono<T> leftShift(final Promise<T> selfType, final Consumer<T> other) {
    selfType.doOnSuccess other
  }

  static <T> List<T> rightShift(final Fluxion<T> selfType, final List<T> other) {
    selfType.collect ({ it }, { it }, { a, b -> a.add(b)}).get()
  }

  //Consuming
  static <T, P extends Processor<?, T>> P leftShift(final P selfType, final T value) {
    selfType.onNext(value)
    selfType
  }

}