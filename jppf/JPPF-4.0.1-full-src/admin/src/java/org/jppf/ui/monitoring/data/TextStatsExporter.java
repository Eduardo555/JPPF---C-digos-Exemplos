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

package org.jppf.ui.monitoring.data;

import static org.jppf.ui.monitoring.data.StatsConstants.*;

import java.util.*;

import org.jppf.utils.*;

/**
 * 
 * @author Laurent Cohen
 */
public class TextStatsExporter implements StatsExporter
{
  /**
   * Base name for localization bundle lookups.
   */
  private static final String BASE = "org.jppf.ui.i18n.StatsPage";
  /**
   * The object from which to get the values.
   */
  private final StatsHandler statsHandler;
  /**
   * 
   */
  private int maxNameLength = 0;
  /**
   * 
   */
  private int maxValueLength = 0;

  /**
   * 
   * @param statsHandler the object from which to get the values.
   */
  public TextStatsExporter(final StatsHandler statsHandler)
  {
    this.statsHandler = statsHandler;
  }

  @Override
  public String formatAll()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("JPPF driver statistics\n\n");
    Map<Fields, String> map = new HashMap<>(statsHandler.getLatestStringValues());
    maxNameLength = 0;
    maxValueLength = 0;
    updateMaxLengths(map, StatsConstants.ALL_FIELDS);
    sb.append(format(map, EXECUTION_PROPS, "ExecutionTable.label"));
    sb.append(format(map, NODE_EXECUTION_PROPS, "NodeExecutionTable.label"));
    sb.append(format(map, TRANSPORT_PROPS, "NetworkOverheadTable.label"));
    sb.append(format(map, JOB_PROPS, "JobTable.label"));
    sb.append(format(map, QUEUE_PROPS, "QueueTable.label"));
    sb.append(format(map, CONNECTION_PROPS, "ConnectionsTable.label"));
    sb.append(format(map, NODE_CL_REQUEST_TIME_PROPS, "NodeClassLoadingRequestTable.label"));
    sb.append(format(map, CLIENT_CL_REQUEST_TIME_PROPS, "ClientClassLoadingRequestTable.label"));
    sb.append(format(map, INBOUND_NETWORK_TRAFFIC_PROPS, "InboundTrafficTable.label"));
    sb.append(format(map, OUTBOUND_NETWORK_TRAFFIC_PROPS, "OutboundTrafficTable.label"));
    return sb.toString();
  }

  /**
   * Format a set of values.
   * @param map the map of field names to values.
   * @param fields the labels for the values.
   * @param label the title given to the set of values.
   * @return the values formatted as plain text.
   */
  private String format(final Map<Fields, String> map, final Fields[] fields, final String label)
  {
    StringBuilder sb = new StringBuilder();
    String title = LocalizationUtils.getLocalized(BASE, label);
    sb.append(title).append('\n');
    sb.append(StringUtils.padRight("", '-', title.length())).append("\n\n");
    for (Fields field: fields)
    {
      String value = map.get(field);
      String name = field.toString();
      sb.append(StringUtils.padRight(name, ' ', maxNameLength));
      sb.append(" = ");
      sb.append(StringUtils.padLeft(value, ' ', maxValueLength));
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Format a set of values.
   * @param map the map of field names to values.
   * @param fieldsArrays the labels for the values.
   */
  private void updateMaxLengths(final Map<Fields, String> map, final Fields[]...fieldsArrays)
  {
    Map<String, String> nameValueMap = new HashMap<>();
    for (Fields[] fields: fieldsArrays)
    {
      for (Fields field: fields)
      {
        String value = map.get(field);
        String name = field.toString();
        maxNameLength = Math.max(maxNameLength, name.length());
        maxValueLength = Math.max(maxValueLength, value == null ? 0 : value.length());
      }
    }
  }
}
