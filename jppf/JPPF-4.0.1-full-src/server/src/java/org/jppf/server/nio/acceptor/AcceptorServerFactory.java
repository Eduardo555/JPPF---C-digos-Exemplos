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

package org.jppf.server.nio.acceptor;

import static org.jppf.server.nio.acceptor.AcceptorState.IDENTIFYING_PEER;
import static org.jppf.server.nio.acceptor.AcceptorTransition.TO_IDENTIFYING_PEER;

import java.util.*;

import org.jppf.nio.*;

/**
 * Utility class used to specify the possible states of a node server connection, as well as the possible
 * transitions between those states.
 * @author Laurent Cohen
 */
final class AcceptorServerFactory extends NioServerFactory<AcceptorState, AcceptorTransition>
{
  /**
   * Initialize this factory with the specified server.
   * @param server the server for which to initialize.
   */
  public AcceptorServerFactory(final AcceptorNioServer server)
  {
    super(server);
  }

  /**
   * Create the map of all possible states.
   * @return a mapping of the states enumeration to the corresponding NioState instances.
   * @see org.jppf.nio.NioServerFactory#createStateMap()
   */
  @Override
  public Map<AcceptorState, NioState<AcceptorTransition>> createStateMap()
  {
    Map<AcceptorState, NioState<AcceptorTransition>> map = new EnumMap<>(AcceptorState.class);
    map.put(IDENTIFYING_PEER, new IdentifyingPeerState((AcceptorNioServer) server));
    //map.put(IDLE, new IdleState((AcceptorNioServer) server));
    return map;
  }

  /**
   * Create the map of all possible transitions.
   * @return a mapping of the transitions enumeration to the corresponding NioTransition instances.
   * @see org.jppf.nio.NioServerFactory#createTransitionMap()
   */
  @Override
  public Map<AcceptorTransition, NioTransition<AcceptorState>> createTransitionMap()
  {
    Map<AcceptorTransition, NioTransition<AcceptorState>> map = new EnumMap<>(AcceptorTransition.class);
    map.put(TO_IDENTIFYING_PEER, transition(IDENTIFYING_PEER, R));
    return map;
  }
}
