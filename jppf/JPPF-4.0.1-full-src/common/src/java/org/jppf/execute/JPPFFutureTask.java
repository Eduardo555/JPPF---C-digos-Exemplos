/*
 * JPPF.
 * Copyright (C) 2005-2014 JPPF Team.
 * http://www.jppf.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jppf.execute;

import java.util.List;
import java.util.concurrent.*;

/**
 * Future task that notifies transition to done state.
 * @param <V> The result type returned by <code>get</code> method.
 * @author Martin JANDA
 */
public class JPPFFutureTask<V> extends FutureTask<V> implements JPPFFuture<V>
{
  /**
   * List of listeners for this task.
   */
  private final List<Listener> listenerList = new CopyOnWriteArrayList<>();

  /**
   * Creates a <tt>FutureTask</tt> that will, upon running, execute the
   * given <tt>Callable</tt>.
   *
   * @param callable the callable task.
   * @throws NullPointerException if callable is null.
   */
  public JPPFFutureTask(final Callable<V> callable)
  {
    super(callable);
  }

  /**
   *
   * @param runnable the runnable task.
   * @param result the result returned on successful completion.
   * @throws NullPointerException if runnable is null.
   */
  public JPPFFutureTask(final Runnable runnable, final V result)
  {
    super(runnable, result);
  }

  @Override
  protected void done()
  {
    for (Listener listener : listenerList) listener.onDone(this);
  }

  @Override
  public void addListener(final Listener listener)
  {
    if (listener == null) throw new IllegalArgumentException("listener is null");
    if (isDone()) listener.onDone(this);
    else listenerList.add(listener);
  }
}
