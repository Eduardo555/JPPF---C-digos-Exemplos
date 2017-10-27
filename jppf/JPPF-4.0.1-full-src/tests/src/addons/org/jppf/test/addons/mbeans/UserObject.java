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

package org.jppf.test.addons.mbeans;

import java.io.Serializable;

/**
 * Sent as user object in JMX notifications.
 */
public class UserObject implements Serializable
{
  /**
   * Uuid of the node on which the task executed.
   */
  public final String nodeUuid;
  /**
   * Id of the task.
   */
  public final String taskId;

  /**
   * Initialize this user object.
   * @param nodeUuid uuid of the node on which the task executed.
   * @param taskId id of the task.
   */
  public UserObject(final String nodeUuid, final String taskId)
  {
    this.nodeUuid = nodeUuid;
    this.taskId = taskId;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getSimpleName()).append('[');
    sb.append("nodeUuid=").append(nodeUuid);
    sb.append(", taskId=").append(taskId);
    sb.append(']');
    return sb.toString();
  }
}
