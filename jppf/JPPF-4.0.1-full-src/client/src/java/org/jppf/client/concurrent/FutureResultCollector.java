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

package org.jppf.client.concurrent;

import java.util.*;

import org.jppf.client.*;
import org.jppf.client.event.TaskResultEvent;
import org.jppf.node.protocol.Task;
import org.slf4j.*;

/**
 * 
 * @author Laurent Cohen
 * @exclude
 */
class FutureResultCollector extends JPPFResultCollector
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(FutureResultCollector.class);
  /**
   * Determines whether debug-level logging is enabled.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * A lst of the listeners to this results collector.
   */
  private List<FutureResultCollectorListener> listeners = new LinkedList<>();
  /**
   * The uuid of the corresponding job.
   */
  private String jobUuid = null;

  /**
   * Initialize this collector with a specified number of tasks.
   * @param job the job to execute.
   */
  FutureResultCollector(final JPPFJob job)
  {
    super(job);
    this.jobUuid = job.getUuid();
  }

  /**
   * Get the task at the specified position.
   * @param position the position of the task in the job it is a part of.
   * @return the task whose results were received, or null if the results were not received.
   */
  synchronized Task<?> getTask(final int position)
  {
    return job.getResults().getResultTask(position);
  }

  /**
   * Wait for the execution results of the specified task to be received.
   * @param position the position of the task in the job it is a part of.
   * @return the task whose results were received.
   */
  synchronized Task<?> waitForTask(final int position)
  {
    return waitForTask(position, Long.MAX_VALUE);
  }

  /**
   * Wait for the execution results of the specified task to be received.
   * @param position the position of the task in the job it is a part of.
   * @param millis maximum number of milliseconds to wait.
   * @return the task whose results were received, or null if the timeout expired before it was received.
   */
  synchronized Task<?> waitForTask(final int position, final long millis)
  {
    long start = System.currentTimeMillis();
    long elapsed = 0L;
    boolean taskReceived = isTaskReceived(position);
    while ((elapsed < millis) && !taskReceived)
    {
      try
      {
        wait(millis - elapsed);
      }
      catch(InterruptedException e)
      {
        log.error(e.getMessage(), e);
      }
      elapsed = System.currentTimeMillis() - start;
      taskReceived = isTaskReceived(position);
      if ((elapsed >= millis) && !taskReceived) return null;
    }
    return job.getResults().getResultTask(position);
  }

  /**
   * Determine whether the results of the specified task have been received.
   * @param position the position of the task in the job it is a part of.
   * @return true if the results of the task have been received, false otherwise.
   */
  boolean isTaskReceived(final int position)
  {
    return job.getResults().hasResult(position);
  }

  /**
   * Called to notify that the results of a number of tasks have been received from the server.
   * @param event a notification of completion for a set of submitted tasks.
   * @see org.jppf.client.JPPFResultCollector#resultsReceived(org.jppf.client.event.TaskResultEvent)
   */
  @Override
  public synchronized void resultsReceived(final TaskResultEvent event)
  {
    super.resultsReceived(event);
    fireResultsReceived(event.getTasks());
  }

  @Override
  protected void onComplete() {
    super.onComplete();
    fireResultsComplete();
  }

  /**
   * Register a listener with this results collector.
   * @param listener the listener to register.
   */
  synchronized void addListener(final FutureResultCollectorListener listener)
  {
    listeners.add(listener);
  }

  /**
   * Remove a listener form the list of listeners registered this results collector.
   * @param listener the listener to remove.
   */
  synchronized void removeListener(final FutureResultCollectorListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Notify all listeners that all results have been received by this collector.
   * @param results the results that were received.
   */
  synchronized void fireResultsReceived(final List<Task<?>> results)
  {
    FutureResultCollectorEvent event = new FutureResultCollectorEvent(this, results);
    for (FutureResultCollectorListener listener: listeners) listener.resultsReceived(event);
  }

  /**
   * Notify all listeners that all results have been received by this collector.
   */
  synchronized void fireResultsComplete()
  {
    FutureResultCollectorEvent event = new FutureResultCollectorEvent(this, null);
    for (FutureResultCollectorListener listener: listeners) listener.resultsComplete(event);
  }

  /**
   * Get the uuid of the corresponding job.
   * @return the uuid as a string.
   */
  String getJobUuid()
  {
    return jobUuid;
  }
}
