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

package org.jppf.node.event;

import java.util.EventListener;

/**
 * Interface for all listeners to the node's life cycle events.
 * @author Laurent Cohen
 */
public interface NodeLifeCycleListener extends EventListener
{
  /**
   * Called when the node has finished initializing, and before it starts processing jobs.
   * @param event encapsulates information about the node.
   */
  void nodeStarting(NodeLifeCycleEvent event);

  /**
   * Called when the node is terminating.
   * @param event encapsulates information about the node.
   */
  void nodeEnding(NodeLifeCycleEvent event);

  /**
   * Called when the node has loaded a job header and before the <code>DataProvider</code> or any of the tasks has been loaded.
   * <br>Note that <code>event.getTasks()</code> will return <code>null</code> at this point.
   * @param event encapsulates information about the job.
   */
  void jobHeaderLoaded(NodeLifeCycleEvent event);

  /**
   * Called before the node starts processing a job.
   * @param event encapsulates information about the job.
   */
  void jobStarting(NodeLifeCycleEvent event);

  /**
   * Called after the node finishes processing a job.
   * @param event encapsulates information about the job.
   */
  void jobEnding(NodeLifeCycleEvent event);
}
