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

package org.jppf.jca.util;

import java.io.PrintWriter;

import javax.resource.ResourceException;

import org.jppf.client.AbstractGenericClient;

/**
 * Utility interface used for accessing common attributes of the j2ee connector objects.
 * @author Laurent Cohen
 */
public interface JPPFAccessor
{

  /**
   * Get the JPPF client used to submit tasks.
   * @return an <code>AbstractGenericClient</code> instance.
   */
  AbstractGenericClient retrieveJppfClient();

  /**
   * Set the JPPF client used to submit tasks.
   * @param jppfClient an <code>AbstractGenericClient</code> instance.
   */
  void assignJppfClient(AbstractGenericClient jppfClient);

  /**
   * Get the log writer for this object.
   * @return a <code>PrintWriter</code> instance.
   * @throws ResourceException if the log writer could not be obtained.
   * @see javax.resource.spi.ManagedConnectionFactory#getLogWriter()
   */
  PrintWriter getLogWriter() throws ResourceException;

  /**
   * Set the log writer for this object.
   * @param writer a <code>PrintWriter</code> instance.
   * @throws ResourceException if the log writer could not be set.
   * @see javax.resource.spi.ManagedConnectionFactory#setLogWriter(java.io.PrintWriter)
   */
  void setLogWriter(PrintWriter writer) throws ResourceException;

}
