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

package org.jppf.jca.cci;

import java.util.*;

import javax.resource.*;
import javax.resource.cci.*;
import javax.resource.spi.ConnectionEvent;

import org.jppf.client.*;
import org.jppf.client.event.SubmissionStatusListener;
import org.jppf.client.submission.*;
import org.jppf.jca.spi.JPPFManagedConnection;
import org.jppf.jca.work.JcaSubmissionManager;
import org.jppf.node.protocol.Task;
import org.jppf.server.protocol.JPPFTask;
import org.slf4j.*;

/**
 * Implementation of a JCA connection. This class provides an API to send tasks to a JPPF driver.
 * @author Laurent Cohen
 * @exclude
 */
public class JPPFConnectionImpl implements JPPFConnection
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(JPPFConnectionImpl.class);
  /**
   * Determines whether the debug level is enabled in the logging configuration, without the cost of a method call.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * The associated managed connection.
   */
  private JPPFManagedConnection managedConnection;

  /**
   * Initialize this connection from a managed connection.
   * @param conn a <code>ManagedConnection</code> instance.
   */
  public JPPFConnectionImpl(final JPPFManagedConnection conn)
  {
    this.managedConnection = conn;
  }

  @Override
  public void close()
  {
    if (managedConnection != null) managedConnection.fireConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED, null);
  }

  @Override
  public Interaction createInteraction()
  {
    return new JPPFInteraction(this);
  }

  /**
   * Transaction management is not supported in this version.
   * @return nothing.
   * @throws ResourceException this method always throws a NotSupportedException.
   */
  @Override
  public LocalTransaction getLocalTransaction() throws ResourceException
  {
    throw new NotSupportedException("Method not supported");
  }

  @Override
  public ConnectionMetaData getMetaData()
  {
    return new JPPFConnectionMetaData(null);
  }

  /**
   * This method is not supported in this version.
   * @return nothing.
   * @throws ResourceException this method always throws a NotSupportedException.
   */
  @Override
  public ResultSetInfo getResultSetInfo() throws ResourceException
  {
    throw new NotSupportedException("Method not supported");
  }

  @Override
  public String submit(final JPPFJob job) throws Exception
  {
    return submit(job, null);
  }

  @Override
  public String submit(final JPPFJob job, final SubmissionStatusListener listener) throws Exception
  {
    if (job == null) throw new IllegalArgumentException("job cannot be null");
    if (job.getJobTasks().isEmpty()) throw new IllegalArgumentException("job cannot be empty");
    job.setBlocking(false);
    return managedConnection.retrieveJppfClient().getSubmissionManager().submitJob(job, listener);
  }

  @Override
  public void addSubmissionStatusListener(final String submissionId, final SubmissionStatusListener listener)
  {
    JPPFResultCollector res = getResultCollector(submissionId);
    if (res != null) res.addSubmissionStatusListener(listener);
  }

  @Override
  public void removeSubmissionStatusListener(final String submissionId, final SubmissionStatusListener listener)
  {
    JPPFResultCollector res = getResultCollector(submissionId);
    if (res != null) res.removeSubmissionStatusListener(listener);
  }

  @Override
  public SubmissionStatus getSubmissionStatus(final String submissionId) throws Exception
  {
    SubmissionStatusHandler res = getResultCollector(submissionId);
    if (res == null) return null;
    return res.getStatus();
  }

  /**
   * Get the results of an execution request.<br>
   * This method should be called only once a call to
   * {@link #getSubmissionStatus(java.lang.String submissionId) getSubmissionStatus(submissionId)} has returned
   * either {@link org.jppf.client.submission.SubmissionStatus#COMPLETE COMPLETE} or
   * {@link org.jppf.client.submission.SubmissionStatus#FAILED FAILED}
   * @param submissionId the id of the submission for which to get the execution results.
   * @return the list of resulting JPPF tasks, or null if the execution failed.
   * @throws Exception if an error occurs while submitting the request.
   * @deprecated use {@link #getResults(String)} instead.
   */
  @Deprecated
  @Override
  public List<JPPFTask> getSubmissionResults(final String submissionId) throws Exception
  {
    JcaSubmissionManager mgr = (JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager();
    JPPFResultCollector res = mgr.peekSubmission(submissionId);
    if (res == null) return null;
    res = mgr.pollSubmission(submissionId);
    return res.getResults();
  }

  /**
   * Get the results of an execution request.<br>
   * This method should be called only once a call to
   * {@link #getSubmissionStatus(java.lang.String submissionId) getSubmissionStatus(submissionId)} has returned
   * either {@link org.jppf.client.submission.SubmissionStatus#COMPLETE COMPLETE} or
   * {@link org.jppf.client.submission.SubmissionStatus#FAILED FAILED}
   * @param submissionId the id of the submission for which to get the execution results.
   * @return the list of resulting JPPF tasks, or null if the execution failed.
   * @throws Exception if an error occurs while submitting the request.
   */
  @Override
  public List<Task<?>> getResults(final String submissionId) throws Exception
  {
    JcaSubmissionManager mgr = (JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager();
    JPPFResultCollector res = mgr.peekSubmission(submissionId);
    if (res == null) return null;
    res = mgr.pollSubmission(submissionId);
    return res.getAllResults();
  }

  /**
   * Get the submission result with the specified id.
   * @param submissionId the id of the submission to find.
   * @return a <code>JPPFSubmissionResult</code> instance, or null if no submission can be found for the specified id.
   */
  private JPPFResultCollector getResultCollector(final String submissionId)
  {
    return ((JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager()).peekSubmission(submissionId);
  }

  @Override
  public Collection<String> getAllSubmissionIds()
  {
    return ((JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager()).getAllSubmissionIds();
  }

  @Override
  public boolean cancelJob(final String submissionId) throws Exception
  {
    return managedConnection.retrieveJppfClient().cancelJob(submissionId);
  }

  /**
   * Set the associated managed connection.
   * @param conn a <code>JPPFManagedConnection</code> instance.
   * @exclude
   */
  public void setManagedConnection(final JPPFManagedConnection conn)
  {
    this.managedConnection = conn;
  }

  /**
   * {@inheritDoc}
   * @deprecated use {@link #awaitResults(String)} instead.
   */
  @Deprecated
  @Override
  public List<JPPFTask> waitForResults(final String submissionId) throws Exception
  {
    JPPFResultCollector result = getResultCollector(submissionId);
    if (debugEnabled) log.debug("result collector = " + result);
    if (result == null) return null;
    result.waitForResults();
    List<JPPFTask> tasks = result.getResults();
    ((JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager()).pollSubmission(submissionId);
    return tasks;
  }

  @Override
  public List<Task<?>> awaitResults(final String submissionId) throws Exception
  {
    JPPFResultCollector result = getResultCollector(submissionId);
    if (debugEnabled) log.debug("result collector = " + result);
    if (result == null) return null;
    result.awaitResults();
    List<Task<?>> tasks = result.getAllResults();
    ((JcaSubmissionManager) managedConnection.retrieveJppfClient().getSubmissionManager()).pollSubmission(submissionId);
    return tasks;
  }

  @Override
  public void resetClient()
  {
    if (managedConnection != null) managedConnection.resetClient();
  }
}
