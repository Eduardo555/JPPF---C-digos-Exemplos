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
package org.jppf.server.job;

import org.jppf.execute.ExecutorChannel;
import org.jppf.job.*;
import org.jppf.management.JPPFManagementInfo;
import org.jppf.node.protocol.*;
import org.jppf.server.protocol.BundleParameter;

/**
 * Instances of this class are submitted into an event queue and generate actual
 * job manager events that are then dispatched to registered listeners.
 */
public class JobEventTask implements Runnable
{
  /**
   * The job manager that submits the events.
   */
  private final JobNotificationEmitter jobManager;
  /**
   * The type of event to generate.
   */
  private final JobEventType eventType;
  /**
   * The node, if any, for which the event happened.
   */
  private final ExecutorChannel channel;
  /**
   * The job data.
   */
  private final TaskBundle bundle;
  /**
   * Creation timestamp for this task.
   */
  private final long timestamp = System.currentTimeMillis();

  /**
   * Initialize this job manager event task with the specified parameters.
   * @param jobManager the job manager that submits the events.
   * @param eventType the type of event to generate.
   * @param bundle the job data.
   * @param channel the id of the job source of the event.
   */
  public JobEventTask(final JobNotificationEmitter jobManager, final JobEventType eventType, final TaskBundle bundle, final ExecutorChannel channel)
  {
    this.jobManager = jobManager;
    this.eventType = eventType;
    this.channel = channel;
    this.bundle = bundle;
  }

  /**
   * Execute this task.
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run()
  {
    JobSLA sla = bundle.getSLA();
    JobInformation jobInfo = new JobInformation(bundle.getUuid(), bundle.getName(), bundle.getCurrentTaskCount(),
        bundle.getInitialTaskCount(), sla.getPriority(), sla.isSuspended(), bundle.getParameter(BundleParameter.JOB_PENDING, false));
    jobInfo.setMaxNodes(sla.getMaxNodes());
    JPPFManagementInfo nodeInfo = (channel == null) ? null : channel.getManagementInfo();
    JobNotification event = new JobNotification(eventType, jobInfo, nodeInfo, timestamp);
    if(eventType == JobEventType.JOB_UPDATED)
    {
      int n = bundle.getCurrentTaskCount();
      jobInfo.setTaskCount(n);
    }

    jobManager.fireJobEvent(event);
  }
}
