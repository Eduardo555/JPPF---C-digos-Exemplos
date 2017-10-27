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

package org.jppf.nio;

import java.nio.channels.*;

/**
 * Channel wrapper implementation for a {@link SelectionKey}.
 * @author Laurent Cohen
 */
public class SelectionKeyWrapper extends AbstractChannelWrapper<SelectionKey>
{
  /**
   * Initialize this channel wrapper with the specified channel.
   * @param channel the channel to wrap.
   */
  public SelectionKeyWrapper(final SelectionKey channel)
  {
    super(channel);
  }

  @Override
  public NioContext getContext()
  {
    return (NioContext) channel.attachment();
  }

  /**
   * Close the channel.
   * @throws Exception if any error occurs while closing the channel.
   */
  @Override
  public void close() throws Exception
  {
    SelectableChannel ch = (SelectableChannel) channel.channel();
    channel.cancel();
    ch.close();
  }

  /**
   * Determine whether the channel is opened.
   * @return true if the channel is opened, false otherwise.
   */
  @Override
  public boolean isOpen()
  {
    return channel.channel().isOpen();
  }

  /**
   * Generate a string that represents this channel wrapper.
   * @return a string that represents this channel wrapper.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    if ((channel == null) || !channel.isValid())
    {
      StringBuilder sb = new StringBuilder(1000);
      sb.append(getClass().getSimpleName());
      sb.append('[');
      sb.append("id=").append(getId());
      sb.append(", channel=").append(channel);
      sb.append(", context=").append(getContext());
      sb.append(']');
      return sb.toString();
    }
    return super.toString();
  }

  /**
   * Get the operations enabled for this channel.
   * @return the operations as an int value.
   * @see org.jppf.nio.AbstractChannelWrapper#getInterestOps()
   */
  @Override
  public int getInterestOps()
  {
    return channel.interestOps();
  }

  /**
   * Get the operations enabled for this channel.
   * @param keyOps the operations as an int value.
   * @see org.jppf.nio.AbstractChannelWrapper#setInterestOps(int)
   */
  @Override
  public void setInterestOps(final int keyOps)
  {
    channel.interestOps(keyOps);
  }

  /**
   * Get the operations available for this channel.
   * @return the operations as an int value.
   * @see org.jppf.nio.AbstractChannelWrapper#getReadyOps()
   */
  @Override
  public int getReadyOps()
  {
    return channel.readyOps();
  }

  /**
   * @return <code>false</code>.
   */
  @Override
  public boolean isLocal()
  {
    return false;
  }
}
