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

package org.jppf.jca.spi;

import javax.resource.ResourceException;
import javax.resource.spi.ManagedConnectionMetaData;

/**
 * Implementation of the ManagedConnectionMetaData interface.
 * @author Laurent Cohen
 */
public class JPPFManagedConnectionMetaData implements ManagedConnectionMetaData
{
  /**
   * Name of the user of the connection.
   */
  private String userName;

  /**
   * Initialize this metadata with a specified user name.
   * @param userName the name of the user of the connection.
   */
  public JPPFManagedConnectionMetaData(final String userName)
  {
    this.userName = userName;
  }

  @Override
  public String getEISProductName() throws ResourceException
  {
    return "JPPF";
  }

  @Override
  public String getEISProductVersion() throws ResourceException
  {
    return "JPPF 4.0";
  }

  @Override
  public int getMaxConnections() throws ResourceException
  {
    return 10;
  }

  @Override
  public String getUserName() throws ResourceException
  {
    return userName;
  }

}
