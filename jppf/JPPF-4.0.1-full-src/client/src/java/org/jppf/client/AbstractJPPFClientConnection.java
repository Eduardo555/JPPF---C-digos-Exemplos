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

package org.jppf.client;

import static org.jppf.client.JPPFClientConnectionStatus.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jppf.client.event.*;
import org.jppf.comm.socket.SocketInitializer;
import org.jppf.management.*;
import org.jppf.node.protocol.TaskBundle;
import org.jppf.server.protocol.BundleParameter;
import org.slf4j.*;

/**
 * This class provides an API to submit execution requests and administration
 * commands, and request server information data.<br>
 * It has its own unique identifier, used by the nodes, to determine whether
 * classes from the submitting application should be dynamically reloaded or not
 * depending on whether the uuid has changed or not.
 * @author Laurent Cohen
 */
public abstract class AbstractJPPFClientConnection extends BaseJPPFClientConnection
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(AbstractJPPFClientConnection.class);
  /**
   * Determines whether the debug level is enabled in the logging configuration, without the cost of a method call.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * Priority given to the driver this client is connected to. The client is always connected to the available driver(s) with the highest
   * priority. If multiple drivers have the same priority, they will be used as a pool and tasks will be evenly distributed among them.
   */
  protected int priority = 0;
  /**
   * List of status listeners for this connection.
   */
  protected final List<ClientConnectionStatusListener> listeners = new CopyOnWriteArrayList<>();
  /**
   * The name displayed for this connection.
   */
  protected String displayName;
  /**
   * Determines whether the communication via the server is done via SSL.
   */
  protected boolean sslEnabled = false;
  /**
   * Represents the system information.
   */
  private JPPFSystemInformation systemInfo = null;
  /**
   * Provides access to the management functions of the driver.
   */
  protected JMXDriverConnectionWrapper jmxConnection = null;
  /**
   *
   */
  protected int jmxPort = -1;

  /**
   * Configure this client connection with the specified parameters.
   * @param uuid the remote driver's UUID.
   * @param name configuration name for this local client.
   * @param host the name or IP address of the host the JPPF driver is running on.
   * @param driverPort the TCP port the JPPF driver listening to for submitted tasks.
   * @param priority the assigned to this client connection.
   * @param sslEnabled determines whether the communication via the server is done via SSL.
   */
  protected void configure(final String uuid, final String name, final String host, final int driverPort, final int priority, final boolean sslEnabled)
  {
    this.driverUuid = uuid;
    this.host = host;
    this.port = driverPort;
    this.priority = priority;
    this.name = name;
    this.sslEnabled = sslEnabled;
    displayName = name;
    this.taskServerConnection = new TaskServerConnectionHandler(this, this.host, this.port);
  }

  /**
   * Initialize this client connection.
   * @see org.jppf.client.JPPFClientConnection#init()
   */
  @Override
  public abstract void init();

  /**
   * Get the priority assigned to this connection.
   * @return a priority as an int value.
   * @see org.jppf.client.JPPFClientConnection#getPriority()
   */
  @Override
  public int getPriority()
  {
    return priority;
  }

  /**
   * Set the priority assigned to this connection.
   * @param priority a priority as an int value.
   */
  public void setPriority(final int priority)
  {
    this.priority = priority;
  }

  @Override
  public JPPFClientConnectionStatus getStatus()
  {
    return status.get();
  }

  @Override
  public void setStatus(final JPPFClientConnectionStatus status)
  {
    JPPFClientConnectionStatus oldStatus = getStatus();
    if (status != oldStatus)
    {
      if (debugEnabled) log.debug("connection '" + name + "' status changing from " + oldStatus + " to " + status);
      this.status.set(status);
      fireStatusChanged(oldStatus);
    }
  }

  @Override
  public void addClientConnectionStatusListener(final ClientConnectionStatusListener listener)
  {
    listeners.add(listener);
  }

  @Override
  public void removeClientConnectionStatusListener(final ClientConnectionStatusListener listener)
  {
    listeners.remove(listener);
  }

  /**
   * Notify all listeners that the status of this connection has changed.
   * @param oldStatus the connection status before the change.
   */
  protected void fireStatusChanged(final JPPFClientConnectionStatus oldStatus)
  {
    ClientConnectionStatusEvent event = new ClientConnectionStatusEvent(this, oldStatus);
    for (ClientConnectionStatusListener listener : listeners) listener.statusChanged(event);
  }

  /**
   * Get a string representation of this client connection.
   * @return a string representing this connection.
   */
  @Override
  public String toString()
  {
    return displayName + " : " + status;
  }

  @Override
  protected abstract SocketInitializer createSocketInitializer();

  /**
   * Cancel the job with the specified id.
   * @param jobId the id of the job to cancel.
   * @throws Exception if any error occurs.
   * @see org.jppf.server.job.management.DriverJobManagementMBean#cancelJob(java.lang.String)
   * @return a <code>true</code> when cancel was successful <code>false</code> otherwise.
   */
  public boolean cancelJob(final String jobId) throws Exception
  {
    JMXDriverConnectionWrapper jmxConnection = this.getJmxConnection();
    if ( jmxConnection != null && jmxConnection.isConnected())
    {
      if (debugEnabled) log.debug("requesting cancel of jobUuid=" + jobId);
      jmxConnection.cancelJob(jobId);
      return true;
    }
    if (debugEnabled) log.debug("could not cancel jobUuid=" + jobId + " : jmx connection not ready");
    return false;
  }

  /**
   * Invoked to notify of a status change event on a client connection.
   * @param event the event to notify of.
   * @see org.jppf.client.event.ClientConnectionStatusListener#statusChanged(org.jppf.client.event.ClientConnectionStatusEvent)
   */
  public void delegateStatusChanged(final ClientConnectionStatusEvent event)
  {
    JPPFClientConnectionStatus s1 = event.getClientConnectionStatusHandler().getStatus();
    JPPFClientConnectionStatus s2 = taskServerConnection.getStatus();
    processStatusChanged(s1, s2);
  }

  /**
   * Invoked to notify of a status change event on a client connection.
   * @param event the event to notify of.
   * @see org.jppf.client.event.ClientConnectionStatusListener#statusChanged(org.jppf.client.event.ClientConnectionStatusEvent)
   */
  public void taskServerConnectionStatusChanged(final ClientConnectionStatusEvent event)
  {
    JPPFClientConnectionStatus s1 = event.getClientConnectionStatusHandler().getStatus();
    JPPFClientConnectionStatus s2 = delegate.getStatus();
    processStatusChanged(s2, s1);
  }

  /**
   * Handle a status change from either the class server delegate or the task server connection
   * and determine whether it triggers a status change for the client connection.
   * @param delegateStatus status of the class server delegate connection.
   * @param taskConnectionStatus status of the task server connection.
   */
  protected void processStatusChanged(final JPPFClientConnectionStatus delegateStatus, final JPPFClientConnectionStatus taskConnectionStatus)
  {
    if (delegateStatus == FAILED) setStatus(FAILED);
    else if (delegateStatus == ACTIVE)
    {
      if ((taskConnectionStatus == ACTIVE) && (this.getStatus() != ACTIVE)) setStatus(ACTIVE);
      else if (taskConnectionStatus != this.getStatus()) setStatus(taskConnectionStatus);
    }
    else
    {
      if (taskConnectionStatus == ACTIVE) setStatus(delegateStatus);
      else
      {
        int n = delegateStatus.compareTo(taskConnectionStatus);
        if ((n < 0) && (delegateStatus != this.getStatus())) setStatus(delegateStatus);
        else if (taskConnectionStatus != this.getStatus()) setStatus(taskConnectionStatus);
      }
    }
  }

  @Override
  public boolean isSSLEnabled()
  {
    return sslEnabled;
  }

  @Override
  public JPPFSystemInformation getSystemInfo()
  {
    return systemInfo;
  }

  @Override
  public TaskBundle sendHandshakeJob() throws Exception
  {
    TaskBundle bundle = super.sendHandshakeJob();
    this.systemInfo = bundle.getParameter(BundleParameter.SYSTEM_INFO_PARAM);
    this.driverUuid = bundle.getParameter(BundleParameter.DRIVER_UUID_PARAM);
    return bundle;
  }

  /**
   * Initialize the jmx connection using the specified jmx server information.
   */
  public void initializeJmxConnection()
  {
    jmxConnection = new JMXDriverConnectionWrapper(host, jmxPort, sslEnabled);
    jmxConnection.connect();
  }

  @Override
  public JMXDriverConnectionWrapper getJmxConnection()
  {
    return jmxConnection;
  }

  /**
   * Shutdown this client and retrieve all pending executions for resubmission.
   * @return a list of <code>JPPFJob</code> instances to resubmit; this list may be empty, but never null.
   * @see org.jppf.client.JPPFClientConnection#close()
   */
  @Override
  public List<JPPFJob> close() {
    if (debugEnabled) log.debug("closing connection " + toDebugString());
    List<JPPFJob> list = null;
    listeners.clear();
    try {
      sendCloseConnectionCommand();
    } catch (Exception e) {
      if (debugEnabled) log.debug('[' + name + "] " + e.getMessage(), e);
      else log.error('[' + name + "] " + e.getMessage());
    }
    try {
      if (debugEnabled) log.debug("closing task server connection " + this);
      if (taskServerConnection != null) taskServerConnection.close();
      if (debugEnabled) log.debug("closing class server connection " + this);
      if (delegate != null) delegate.close();
      if (debugEnabled) log.debug("closing jmx connection " + this);
      if (jmxConnection != null) jmxConnection.close();
    } catch (Exception e) {
      if (debugEnabled) log.debug('[' + name + "] " + e.getMessage(), e);
      else log.error('[' + name + "] " + e.getMessage());
    }
    if (list == null) list = Collections.emptyList();
    if (debugEnabled) log.debug("connection " + toDebugString() + " closed");
    return list;
  }

  @Override
  public boolean isClosed()
  {
    return client.isClosed();
  }
}
