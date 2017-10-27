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
package org.jppf.ui.monitoring.data;

import org.jppf.utils.LocalizationUtils;

/**
 * 
 * @author Laurent Cohen
 */
public enum Fields
{
  /**
   * Name for the total number of tasks executed.
   */
  TOTAL_TASKS_EXECUTED,
  /**
   * Name for the total execution time for all tasks.
   */
  TOTAL_EXECUTION_TIME,
  /**
   * Name for the execution time of the last executed task.
   */
  LATEST_EXECUTION_TIME,
  /**
   * Name for the minimum task execution time.
   */
  MIN_EXECUTION_TIME,
  /**
   * Name for the maximum task execution time.
   */
  MAX_EXECUTION_TIME,
  /**
   * Name for the average task execution time.
   */
  AVG_EXECUTION_TIME,
  /**
   * Name for the total transport time for all tasks.
   */
  TOTAL_TRANSPORT_TIME,
  /**
   * Name for the execution time of the last transported task.
   */
  LATEST_TRANSPORT_TIME,
  /**
   * Name for the minimum task transport time.
   */
  MIN_TRANSPORT_TIME,
  /**
   * Name for the maximum task transport time.
   */
  MAX_TRANSPORT_TIME,
  /**
   * Name for the average task transport time.
   */
  AVG_TRANSPORT_TIME,
  /**
   * Name for the total execution time for all tasks on the nodes.
   */
  TOTAL_NODE_EXECUTION_TIME,
  /**
   * Name for the execution time of the last executed task on a node.
   */
  LATEST_NODE_EXECUTION_TIME,
  /**
   * Name for the minimum task execution time on a node.
   */
  MIN_NODE_EXECUTION_TIME,
  /**
   * Name for the maximum task execution time on a node.
   */
  MAX_NODE_EXECUTION_TIME,
  /**
   * Name for the average task execution time on a node.
   */
  AVG_NODE_EXECUTION_TIME,
  /**
   * Name for the time the last queued task remained in the queue.
   */
  LATEST_QUEUE_TIME,
  /**
   * Name for the total time spent in the queue by all tasks.
   */
  TOTAL_QUEUE_TIME,
  /**
   * Name for the minimum time a task remained in the queue .
   */
  MIN_QUEUE_TIME,
  /**
   * Name for the maximum time a task remained in the queue .
   */
  MAX_QUEUE_TIME,
  /**
   * Name for the maximum time a task remained in the queue .
   */
  AVG_QUEUE_TIME,
  /**
   * Name for the total number of tasks that have been queued.
   */
  TOTAL_QUEUED,
  /**
   * Name for the current queue size.
   */
  QUEUE_SIZE,
  /**
   * Name for the maximum size the queue reached.
   */
  MAX_QUEUE_SIZE,
  /**
   * Name for the current number of nodes connected to the server.
   */
  NB_NODES,
  /**
   * Name for the maximum number of nodes ever connected to the server.
   */
  MAX_NODES,
  /**
   * Name for the current number of idle nodes connected to the server.
   */
  NB_IDLE_NODES,
  /**
   * Name for the current number of clients connected to the server.
   */
  NB_CLIENTS,
  /**
   * Name for the maximum number of clients ever connected to the server.
   */
  MAX_CLIENTS,
  /**
   * Total number of jobs submitted to the server.
   */
  JOBS_TOTAL,
  /**
   * Latest number of jobs present in the server.
   */
  JOBS_LATEST,
  /**
   * Maximum number of jobs present in the server.
   */
  JOBS_MAX,
  /**
   * Execution time of the latest job in the server.
   */
  JOBS_LATEST_TIME,
  /**
   * Minimum execution time of a job in the server.
   */
  JOBS_MIN_TIME,
  /**
   * Maximum execution time of a job in the server.
   */
  JOBS_MAX_TIME,
  /**
   * Average execution time of a job in the server.
   */
  JOBS_AVG_TIME,
  /**
   * Minimum number of tasks of a job in the server.
   */
  JOBS_MIN_TASKS,
  /**
   * Maximum number of tasks of a job in the server.
   */
  JOBS_MAX_TASKS,
  /**
   * Average number of tasks of a job in the server.
   */
  JOBS_AVG_TASKS,
  /**
   * Total number of class loading requests from the nodes.
   */
  NODE_TOTAL_CL_REQUEST_COUNT,
  /**
   * Average time of class loading requests from the nodes.
   */
  NODE_AVG_CL_REQUEST_TIME,
  /**
   * Minimum time of class loading requests from the nodes.
   */
  NODE_MIN_CL_REQUEST_TIME,
  /**
   * Maximum time of class loading requests from the nodes.
   */
  NODE_MAX_CL_REQUEST_TIME,
  /**
   * Latest time of class loading requests from the nodes.
   */
  NODE_LATEST_CL_REQUEST_TIME,
  /**
   * Total number of class loading requests to the clients.
   */
  CLIENT_TOTAL_CL_REQUEST_COUNT,
  /**
   * Average time of class loading requests to the clients.
   */
  CLIENT_AVG_CL_REQUEST_TIME,
  /**
   * Minimum time of class loading requests to the clients.
   */
  CLIENT_MIN_CL_REQUEST_TIME,
  /**
   * Maximum time of class loading requests to the clients.
   */
  CLIENT_MAX_CL_REQUEST_TIME,
  /**
   * Latest time of class loading requests to the clients.
   */
  CLIENT_LATEST_CL_REQUEST_TIME,
  /**
   * Inbound network traffic from clients.
   */
  CLIENT_INBOUND_MB,
  /**
   * Inbound network traffic to clients.
   */
  CLIENT_OUTBOUND_MB,
  /**
   * Outbound network traffic from nodes.
   */
  NODE_INBOUND_MB,
  /**
   * Inbound network traffic to nodes.
   */
  NODE_OUTBOUND_MB,
  /**
   * Inbound network traffic from peer servers.
   */
  PEER_INBOUND_MB,
  /**
   * Inbound network traffic to peer servers.
   */
  PEER_OUTBOUND_MB,
  /**
   * Unidentitified inbound network traffic.
   */
  UNIDENTIFIED_INBOUND_MB,
  /**
   * Unidentitified outbound network traffic.
   */
  UNIDENTIFIED_OUTBOUND_MB,
  /**
   * total inbound network traffic.
   */
  TOTAL_INBOUND_MB,
  /**
   * Total outbound network traffic.
   */
  TOTAL_OUTBOUND_MB;

  /**
   * The localized name of this enum item.
   */
  private String localName = null;
  /**
   * The base used for resource bundles lookups.
   */
  private static final String BASE = "org.jppf.ui.i18n.StatFields";

  /**
   * Initialize an enum item with a localized name.
   */
  private Fields()
  {
    localName = LocalizationUtils.getLocalized(BASE, name());
  }

  /**
   * Return a localized version of this item name.
   * @return the localized name as a string.
   * @see java.lang.Enum#toString()
   */
  @Override
  public String toString()
  {
    return localName;
  }
}
