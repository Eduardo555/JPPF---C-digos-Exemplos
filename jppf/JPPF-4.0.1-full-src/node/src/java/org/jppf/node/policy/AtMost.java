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

package org.jppf.node.policy;

import org.jppf.utils.PropertiesCollection;

/**
 * An execution policy rule that encapsulates a test of type <i>property_value <= value</i>.
 * The test applies to numeric values only.
 * @author Laurent Cohen
 */
public class AtMost extends ExecutionPolicy
{
  /**
   * Explicit serialVersionUID.
   */
  private static final long serialVersionUID = 1L;
  /**
   * The name of the property to compare.
   */
  private String propertyName = null;
  /**
   * A numeric value to compare with.
   */
  private Number numberValue = null;

  /**
   * Define an equality comparison between the numeric value of a property and another numeric value.
   * @param propertyName the name of the property to compare.
   * @param a the value to compare with.
   */
  public AtMost(final String propertyName, final double a)
  {
    this.propertyName = propertyName;
    this.numberValue = a;
  }

  /**
   * Determines whether this policy accepts the specified node.
   * @param info system information for the node on which the tasks will run if accepted.
   * @return true if the node is accepted, false otherwise.
   */
  @Override
  public boolean accepts(final PropertiesCollection info)
  {
    try
    {
      String s = getProperty(info, propertyName);
      if (numberValue != null) return Double.valueOf(s).doubleValue() <= numberValue.doubleValue();
    }
    catch(Exception e)
    {
    }
    return false;
  }

  /**
   * Print this object to a string.
   * @return an XML string representation of this object
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    if (computedToString == null)
    {
      synchronized(ExecutionPolicy.class)
      {
        StringBuilder sb = new StringBuilder();
        sb.append(indent()).append("<AtMost>\n");
        toStringIndent++;
        sb.append(indent()).append("<Property>").append(propertyName).append("</Property>\n");
        sb.append(indent()).append("<Value>").append(numberValue).append("</Value>\n");
        toStringIndent--;
        sb.append(indent()).append("</AtMost>\n");
        computedToString = sb.toString();
      }
    }
    return computedToString;
  }
}
