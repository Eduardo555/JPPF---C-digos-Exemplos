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

package org.jppf.server.nio.client;

import org.jppf.io.IOHelper;
import org.jppf.nio.ChannelWrapper;
import org.jppf.server.nio.AbstractTaskBundleMessage;
import org.jppf.server.protocol.JPPFTaskBundle;

/**
 * Representation of a message sent or received by a remote node.
 * @author Laurent Cohen
 */
public class ClientMessage extends AbstractTaskBundleMessage
{
  /**
   * Initialize this nio message with the specified ssl flag.
   * @param channel the channel to read from or write to.
   */
  public ClientMessage(final ChannelWrapper<?> channel)
  {
    super(channel);
  }

  /**
   * Actions to take after the first object in the message has been fully read.
   * @throws Exception if an IO error occurs.
   */
  @Override
  protected void afterFirstRead() throws Exception
  {
    bundle = (JPPFTaskBundle) IOHelper.unwrappedData(locations.get(0));
    nbObjects = bundle.getTaskCount() + 2;
  }

  /**
   * Actions to take before the first object in the message is written.
   * @throws Exception if an IO error occurs.
   */
  @Override
  protected void beforeFirstWrite() throws Exception
  {
    nbObjects = bundle.getTaskCount() + 1;
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append('[');
    sb.append("nb locations=").append(locations == null ? -1 : locations.size());
    sb.append(", position=").append(position);
    sb.append(", nbObjects=").append(nbObjects);
    sb.append(", length=").append(length);
    sb.append(", count=").append(count);
    sb.append(", currentLength=").append(currentLength);
    sb.append(']');
    return sb.toString();
  }
}
