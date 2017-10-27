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

package org.jppf.client.balancer.queue;

import java.util.Comparator;

import org.jppf.client.balancer.*;

/**
 * A Comparator which compare ChannelWrapper objects in descending order of their priority.
 * @author Laurent Cohen
 */
class ChannelPriorityComparator implements Comparator<ChannelWrapper>
{
  @Override
  public int compare(final ChannelWrapper o1, final ChannelWrapper o2)
  {
    if (o1 == null) return (o2 == null) ? 0 : -1;
    if (o2 == null) return 1;
    int p1 = o1.getPriority();
    int p2 = o2.getPriority();
    return (p1 < p2) ? 1 : (p1 > p2 ? -1 : 0);
  }
}