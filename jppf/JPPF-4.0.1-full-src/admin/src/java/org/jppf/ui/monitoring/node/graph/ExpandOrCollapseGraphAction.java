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

package org.jppf.ui.monitoring.node.graph;

import java.awt.event.ActionEvent;
import java.util.Set;

import org.jppf.ui.monitoring.node.TopologyData;

import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * Action performed to select all drivers in the topology view.
 * @author Laurent Cohen
 */
public class ExpandOrCollapseGraphAction extends AbstractGraphSelectionAction
{
  /**
   * Determines whether this action is for collapsing or expanding graph vertices.
   */
  private final boolean collapse;

  /**
   * Initialize this action with the specified tree table panel.
   * @param panel the tree table panel to which this action applies.
   * @param collapse determines whether this action is for collapsing or expanding graph vertices.
   */
  public ExpandOrCollapseGraphAction(final GraphOption panel, final boolean collapse)
  {
    super(panel);
    this.collapse = collapse;
    //String s = collapse ? "collapse" : "expand";
    if (collapse)
    {
      setupIcon("/org/jppf/ui/resources/collapse.gif");
      setupNameAndTooltip("graph.button.collapse");
    }
    else
    {
      setupIcon("/org/jppf/ui/resources/expand.gif");
      setupNameAndTooltip("graph.button.expand");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(final ActionEvent e)
  {
    synchronized(panel)
    {
      VisualizationViewer<TopologyData, Number> viewer = panel.getViewer();
      Set<TopologyData> sel = viewer.getPickedVertexState().getPicked();
      int count = 0;
      if (collapse)
      {
        for (TopologyData data: sel)
        {
          if (!data.isNode() && !data.isCollapsed())
          {
            panel.getGraphHandler().collapse(data);
            count++;
          }
        }
      }
      else
      {
        for (TopologyData data: sel)
        {
          if (!data.isNode() && data.isCollapsed())
          {
            panel.getGraphHandler().expand(data);
            count++;
          }
        }
      }
      if (count > 0) panel.repaintGraph(panel.isAutoLayout());
    }
  }
}
