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

package org.jppf.classloader;

import static org.jppf.utils.StringUtils.build;

import java.io.IOException;

import org.jppf.JPPFNodeReconnectionNotification;
import org.jppf.comm.socket.*;
import org.jppf.serialization.ObjectSerializer;
import org.jppf.ssl.SSLHelper;
import org.jppf.utils.*;
import org.slf4j.*;

/**
 * 
 * @author Laurent Cohen
 * @exclude
 */
public class RemoteClassLoaderConnection extends AbstractClassLoaderConnection<SocketWrapper>
{
  /**
   * Logger for this class.
   */
  private static Logger log = LoggerFactory.getLogger(AbstractClassLoaderConnection.class);
  /**
   * Determines whether the debug level is enabled in the log configuration, without the cost of a method call.
   */
  private static boolean debugEnabled = log.isDebugEnabled();
  /**
   * Used to synchronize access to the underlying socket from multiple threads.
   */
  private SocketInitializer socketInitializer = new SocketInitializerImpl();
  /**
   * Determines whether SSL is enabled.
   */
  private boolean sslEnabled = false;
  /**
   * The object used to serialize and deserialize resources.
   */
  private ObjectSerializer serializer = null;

  @Override
  public void init() throws Exception
  {
    lock.lock();
    try
    {
      if (initializing.compareAndSet(false, true))
      {
        try
        {
          if (debugEnabled) log.debug("initializing connection");
          initChannel();
          System.out.println("Attempting connection to the class server at " + channel.getHost() + ':' + channel.getPort());
          socketInitializer.initializeSocket(channel);
          if (!socketInitializer.isSuccessful())
          {
            channel = null;
            throw new JPPFNodeReconnectionNotification("Could not reconnect to the server");
          }
          performHandshake();
          System.out.println(build(getClass().getSimpleName(), ": Reconnected to the class server"));
        }
        finally
        {
          initializing.set(false);
        }
      }
    }
    finally
    {
      lock.unlock();
    }
  }

  /**
   * Create the ssl connection over an established plain connection.
   */
  private void createSSLConnection()
  {
    try
    {
      channel = SSLHelper.createSSLClientConnection(channel);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Perform the handshake with the server. The handshake consists in:
   * <ol>
   * <li>sending a channel identifier {@link JPPFIdentifiers#NODE_CLASSLOADER_CHANNEL} to the server</li>
   * <li>calling {@link #performCommonHandshake(ResourceRequestRunner) performCommonHandshake()} on the superclass</li>
   * </ol>
   */
  private void performHandshake()
  {
    try
    {
      if (debugEnabled) log.debug("sending channel identifier");
      channel.writeInt(JPPFIdentifiers.NODE_CLASSLOADER_CHANNEL);
      channel.flush();
      if (sslEnabled) createSSLConnection();
      ResourceRequestRunner rr = new RemoteResourceRequest(getSerializer(), channel);
      performCommonHandshake(rr);
    }
    catch (IOException e)
    {
      throw new JPPFNodeReconnectionNotification("Could not reconnect to the driver", e);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Initialize the underlying socket connection.
   */
  private void initChannel()
  {
    if (debugEnabled) log.debug("initializing socket connection");
    TypedProperties props = JPPFConfiguration.getProperties();
    sslEnabled = props.getBoolean("jppf.ssl.enabled", false);
    String host = props.getString("jppf.server.host", "localhost");
    int port = props.getInt("jppf.server.port", sslEnabled ? 11443 : 11111);
    channel = new BootstrapSocketClient();
    channel.setHost(host);
    channel.setPort(port);
  }

  @Override
  public void close()
  {
    lock.lock();
    try
    {
      if (requestHandler != null)
      {
        ResourceRequestRunner requestRunner = requestHandler.close();
        requestHandler = null;
        sendCloseChannelCommand(requestRunner);
      }
      if (socketInitializer != null) socketInitializer.close();
      if (channel != null)
      {
        try
        {
          channel.close();
        }
        catch(Exception e)
        {
          if (debugEnabled) log.debug(e.getMessage(), e);
        }
        channel = null;
      }
    }
    finally
    {
      lock.unlock();
    }
  }

  /**
   * Get the object used to serialize and deserialize resources.
   * @return an {@link ObjectSerializer} instance.
   * @throws Exception if any error occurs.
   * @exclude
   */
  private ObjectSerializer getSerializer() throws Exception
  {
    if (serializer == null) serializer = new BootstrapObjectSerializer();
    return serializer;
  }
}
