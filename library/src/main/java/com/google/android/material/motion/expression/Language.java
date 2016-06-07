/*
 * Copyright (C) 2016 The Material Motion Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.material.motion.expression;

import android.support.annotation.Keep;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A {@link Language} defines the {@link Term Terms} that can be used in an {@link Expression}
 * chain. A Language instance begins the {@link Expression} chain and allows a Term to be chained.
 * Like all Expressions, a Language is immutable.
 * <p>
 * <p>
 * To define a Term that can be chained to this Language, create a method that returns a new
 * instance of that Term. Be sure to pass <code>this</code> instance into the Term's constructor.
 * The return value must be the most specific type possible, and must be a generic type if possible,
 * to enable chaining. Call this method to continue the Expression chain.
 * <p>
 * <code>Language &larr;<sub>new</sub>&larr; Term</code>
 * <p>
 * <p>
 * A Language also acts as a bridge between Terms on the Expression chain.
 * A Term can use the Language instance at {@link Term#and} to continue the Expression chain.
 * <p>
 * <code>Term &larr;<sub>and</sub>&larr; Language</code>
 * <p>
 * <p>
 * A Language does not intrinsically define any {@link Intention Intentions}.
 * Its {@link #intentions()} are the previous chained Term's full set of {@link Term#intentions()}.
 */
public abstract class Language<L extends Language> extends Expression {

  /**
   * The full set of {@link Intention Intentions} from the previous chained {@link Term}.
   */
  private final Work work;

  /**
   * The initializing constructor.
   * <p>
   * <p>
   * Subclasses should call this from their own initializing constructor.
   */
  public Language() {
    this.work = Work.EMPTY;
  }

  /**
   * The chaining constructor.
   * <p>
   * <p>
   * Subclasses should call this from their own chaining constructor, which must be annotated with
   * {@link Keep} and have the same parameter types.
   *
   * @param work You must directly pass in the {@link Work} object that was passed into the
   *             subclass's constructor.
   */
  @Keep
  protected Language(Work work) {
    this.work = work;
  }

  final L chain(Work work) {
    return newInstance(work);
  }

  @SuppressWarnings({"TryWithIdenticalCatches", "unchecked"}) // Cast to Class<L>
  private L newInstance(Work work) {
    try {
      Class<L> klass = (Class<L>) getClass();

      Constructor<L> constructor = klass.getDeclaredConstructor(Work.class);
      constructor.setAccessible(true);

      return constructor.newInstance(work);
    } catch (NoSuchMethodException e) {
      throw new BadImplementationException(this, BadImplementationException.MISSING_CONSTRUCTOR, e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e.getCause());
    }
  }

  @Override
  public final Intention[] intentions() {
    return work.work();
  }
}