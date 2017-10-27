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
package org.jppf.ui.monitoring;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.jppf.ui.layout.WrapLayout;
import org.jppf.ui.monitoring.data.*;
import org.jppf.ui.monitoring.event.*;
import org.jppf.utils.LocalizationUtils;
import org.slf4j.*;

/**
 * This class provides a graphical interface for monitoring the status and health
 * of the JPPF server.<br>
 * It also provides a few customization options, such as setting the interval between 2 server refreshes,
 * and switching the color scheme (skin) fot the whole UI.
 * @author Laurent Cohen
 */
public class MonitoringPanel extends JPanel implements StatsHandlerListener, StatsConstants
{
  /**
   * Logger for this class.
   */
  static Logger log = LoggerFactory.getLogger(MonitoringPanel.class);
  /**
   * Base name for localization bundle lookups.
   */
  private static final String BASE = "org.jppf.ui.i18n.StatsPage";
  /**
   * The stats formatter that provides the data.
   */
  private transient StatsHandler statsHandler = null;
  /**
   * Holds a list of table models to update when new stats are received.
   */
  private java.util.List<MonitorTableModel> tableModels = new ArrayList<>();

  /**
   * Default constructor.
   */
  public MonitoringPanel()
  {
    this.statsHandler = StatsHandler.getInstance();
    WrapLayout wl = new WrapLayout(FlowLayout.LEADING);
    wl.setAlignOnBaseline(true);
    setLayout(wl);
    addTablePanel(EXECUTION_PROPS, "ExecutionTable");
    addTablePanel(NODE_EXECUTION_PROPS, "NodeExecutionTable");
    addTablePanel(TRANSPORT_PROPS, "NetworkOverheadTable");
    addTablePanel(CONNECTION_PROPS, "ConnectionsTable");
    addTablePanel(QUEUE_PROPS, "QueueTable");
    addTablePanel(JOB_PROPS, "JobTable");
    addTablePanel(NODE_CL_REQUEST_TIME_PROPS, "NodeClassLoadingRequestTable");
    addTablePanel(CLIENT_CL_REQUEST_TIME_PROPS, "ClientClassLoadingRequestTable");
    addTablePanel(INBOUND_NETWORK_TRAFFIC_PROPS, "InboundTrafficTable");
    addTablePanel(OUTBOUND_NETWORK_TRAFFIC_PROPS, "OutboundTrafficTable");
    statsHandler.addStatsHandlerListener(this);
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(final ComponentEvent e) {
        MonitoringPanel.this.revalidate();
      }
    });
  }

  /**
   * Add a table panel to this panel.
   * @param fields the fields displayed in the table.
   * @param title the reference to the localized title of the table.
   */
  private void addTablePanel(final Fields[] fields, final String title)
  {
    JComponent comp = makeTablePanel(fields, LocalizationUtils.getLocalized(BASE, title + ".label"));
    comp.setToolTipText(LocalizationUtils.getLocalized(BASE, title + ".tooltip"));
    add(comp);
  }

  /**
   * Called when new stats have been received from the server.
   * @param event holds the new stats values.
   */
  @Override
  public void dataUpdated(final StatsHandlerEvent event)
  {
    for (final MonitorTableModel model: tableModels)
    {
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          model.fireTableDataChanged();
        }
      });
    }
  }

  /**
   * Create a chartPanel displaying a group of values.
   * @param props the names of the values to display.
   * @param title the title of the chartPanel.
   * @return a <code>JComponent</code> instance.
   */
  private JComponent makeTablePanel(final Fields[] props, final String title)
  {
    JPanel panel = new JPanel();
    panel.setAlignmentY(0f);
    panel.setLayout(new MigLayout("fill"));
    panel.setBorder(BorderFactory.createTitledBorder(title));
    JTable table = new JTable() {
      @Override
      public boolean isCellEditable(final int row, final int column) {
        return false;
      }
    };
    MonitorTableModel model = new MonitorTableModel(props);
    table.setModel(model);
    table.setOpaque(true);
    DefaultTableCellRenderer rend1 = new DefaultTableCellRenderer();
    rend1.setHorizontalAlignment(SwingConstants.RIGHT);
    rend1.setOpaque(true);
    table.getColumnModel().getColumn(1).setCellRenderer(rend1);
    DefaultTableCellRenderer rend0 = new DefaultTableCellRenderer();
    rend0.setHorizontalAlignment(SwingConstants.LEFT);
    rend0.setOpaque(true);
    table.getColumnModel().getColumn(0).setCellRenderer(rend0);
    table.getColumnModel().getColumn(0).setMinWidth(200);
    table.getColumnModel().getColumn(0).setMaxWidth(300);
    tableModels.add(model);
    panel.add(table, "growx, pushx");
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.doLayout();
    table.setShowGrid(false);
    return panel;
  }
}
