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

package org.jppf.server.debug;

import java.io.Serializable;

/**
 * 
 * @author Laurent Cohen
 */
public interface ServerDebugMBean extends Serializable
{
  /**
   * The name of this mbean.
   */
  String MBEAN_NAME = "org.jppf:name=debug,type=driver";
  /**
   * Get the states of the class loader channels.
   * @return the states as as an array of strings.
   */
  String clientClassLoaderChannels();
  /**
   * Get the states of the class loader channels.
   * @return the states as as an array of strings.
   */
  String nodeClassLoaderChannels();
  /**
   * Get the states of the node data channels.
   * @return the states as as an array of strings.
   */
  String nodeDataChannels();
  /**
   * Get the states of the client data channels.
   * @return the states as as an array of strings.
   */
  String clientDataChannels();
  /**
   * Get a view of the nio messages for the node data channels.
   * @return the nio messages as an array of string in format channelId = NioMessage.toString().
   */
  String nodeMessages();
  /**
   * Get the states of all channels.
   * @return the states as as an array of strings.
   */
  String allChannels();
  /**
   * Dump the job queue.
   * @return a string representing the job queue.
   */
  String dumpQueue();
  /**
   * Dump the job queue with fine details of the jobs.
   * @return a string representing the job queue.
   */
  String dumpQueueDetails();
  /**
   * View all debuig info in a formatted string.
   * @return a string rpesenting all the debug information.
   */
  String all();
}
