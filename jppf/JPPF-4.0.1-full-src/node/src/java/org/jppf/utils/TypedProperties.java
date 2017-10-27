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
package org.jppf.utils;


import java.io.*;
import java.net.*;
import java.util.*;

import org.jppf.utils.configuration.PropertiesLoader;
import org.jppf.utils.streams.StreamUtils;

/**
 * Extension of the <code>java.util.Properties</code> class to handle the conversion of
 * string values to other types.
 * @author Laurent Cohen
 */
public class TypedProperties extends Properties {
  /**
   * Explicit serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Default constructor.
   */
  public TypedProperties() {
  }

  /**
   * Initialize this object with a set of existing properties.
   * This will copy into the present object all map entries such that both key and value are strings.
   * @param map the properties to be copied. No reference to this parameter is kept in this TypedProperties object.
   */
  public TypedProperties(final Map<Object, Object> map) {
    if (map != null) {
      for (Map.Entry<Object, Object> entry: map.entrySet()) {
        if ((entry.getKey() instanceof String) && (entry.getValue() instanceof String)) {
          setProperty((String) entry.getKey(), (String) entry.getValue());
        }
      }
    }
  }

  /**
   * Get the string value of a property with a specified name.
   * @param key the name of the property to look for.
   * @return the value of the property as a string, or null if it is not found.
   */
  public String getString(final String key) {
    return getString(key, null);
  }

  /**
   * Get the string value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a string, or the default value if it is not found.
   */
  public String getString(final String key, final String defValue) {
    String val = getProperty(key);
    return (val == null) ? defValue : val;
  }

  /**
   * Set a property with the specified String value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setString(final String key, final String value) {
    setProperty(key, value);
  }

  /**
   * Get the integer value of a property with a specified name.
   * @param key the name of the property to look for.
   * @return the value of the property as an int, or zero if it is not found.
   */
  public int getInt(final String key) {
    return getInt(key, 0);
  }

  /**
   * Get the integer value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as an int, or the default value if it is not found.
   */
  public int getInt(final String key, final int defValue) {
    int intVal = defValue;
    String val = getProperty(key, null);
    if (val != null) {
      try {
        intVal = Integer.parseInt(val.trim());
      } catch(NumberFormatException e) {
      }
    }
    return intVal;
  }

  /**
   * Set a property with the specified int value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setInt(final String key, final int value) {
    setProperty(key, Integer.toString(value));
  }

  /**
   * Get the long integer value of a property with a specified name.
   * @param key the name of the property to look for.
   * @return the value of the property as a long, or zero if it is not found.
   */
  public long getLong(final String key) {
    return getLong(key, 0L);
  }

  /**
   * Get the long integer value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a long, or the default value if it is not found.
   */
  public long getLong(final String key, final long defValue) {
    long longVal = defValue;
    String val = getProperty(key, null);
    if (val != null) {
      try {
        longVal = Long.parseLong(val.trim());
      } catch(NumberFormatException e) {
      }
    }
    return longVal;
  }

  /**
   * Set a property with the specified long value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setLong(final String key, final long value) {
    setProperty(key, Long.toString(value));
  }

  /**
   * Get the single precision value of a property with a specified name.
   * @param key the name of the property to look for.
   * @return the value of the property as a float, or zero if it is not found.
   */
  public float getFloat(final String key) {
    return getFloat(key, 0.0f);
  }

  /**
   * Get the single precision value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a float, or the default value if it is not found.
   */
  public float getFloat(final String key, final float defValue) {
    float floatVal = defValue;
    String val = getProperty(key, null);
    if (val != null) {
      try {
        floatVal = Float.parseFloat(val.trim());
      } catch(NumberFormatException e) {
      }
    }
    return floatVal;
  }

  /**
   * Set a property with the specified float value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setFloat(final String key, final float value) {
    setProperty(key, Float.toString(value));
  }

  /**
   * Get the double precision value of a property with a specified name.
   * If the key is not found a default value of 0d is returned.
   * @param key the name of the property to look for.
   * @return the value of the property as a double, or zero if it is not found.
   */
  public double getDouble(final String key) {
    return getDouble(key, 0.0d);
  }

  /**
   * Get the double precision value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a double, or the default value if it is not found.
   */
  public double getDouble(final String key, final double defValue) {
    double doubleVal = defValue;
    String val = getProperty(key, null);
    if (val != null) {
      try {
        doubleVal = Double.parseDouble(val.trim());
      } catch(NumberFormatException e) {
      }
    }
    return doubleVal;
  }

  /**
   * Set a property with the specified double value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setDouble(final String key, final double value) {
    setProperty(key, Double.toString(value));
  }

  /**
   * Get the boolean value of a property with a specified name.
   * If the key is not found a default value of false is returned.
   * @param key the name of the property to look for.
   * @return the value of the property as a boolean, or <code>false</code> if it is not found.
   */
  public boolean getBoolean(final String key) {
    return getBoolean(key, false);
  }

  /**
   * Get the boolean value of a property with a specified name.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a boolean, or the default value if it is not found.
   */
  public boolean getBoolean(final String key, final boolean defValue) {
    boolean booleanVal = defValue;
    String val = getProperty(key, null);
    if (val != null) booleanVal = Boolean.valueOf(val.trim()).booleanValue();
    return booleanVal;
  }

  /**
   * Set a property with the specified boolean value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setBoolean(final String key, final boolean value) {
    setProperty(key, Boolean.toString(value));
  }

  /**
   * Get the char value of a property with a specified name.
   * If the key is not found a default value of ' ' is returned.
   * @param key the name of the property to look for.
   * @return the value of the property as a char, or the default value ' ' (space character) if it is not found.
   */
  public char getChar(final String key) {
    return getChar(key, ' ');
  }

  /**
   * Get the char value of a property with a specified name.
   * If the value has more than one character, the first one will be used.
   * @param key the name of the property to look for.
   * @param defValue a default value to return if the property is not found.
   * @return the value of the property as a char, or the default value if it is not found.
   */
  public char getChar(final String key, final char defValue) {
    char charVal = defValue;
    String val = getProperty(key, null);
    if ((val != null) && (val.length() > 0)) charVal = val.charAt(0);
    return charVal;
  }

  /**
   * Set a property with the specified char value.
   * @param key the name of the property to set.
   * @param value the value to set on the property.
   */
  public void setChar(final String key, final char value) {
    setProperty(key, Character.toString(value));
  }

  /**
   * Get the value of a property with the specified name as a set of a properties.
   * @param key the name of the property to look for.
   * Its value is the path to another properties file. Relative paths are evaluated against the current application directory.
   * @return the value of the property as another set of properties, or null if it is not found.
   */
  public TypedProperties getProperties(final String key) {
    return getProperties(key, null);
  }

  /**
   * Get the value of a property with the specified name as a set of properties.
   * @param key the name of the property to look for.
   * Its value is the path to another properties file. Relative paths are evaluated against the current application directory.
   * @param def a default value to return if the property is not found.
   * @return the value of the property as another set of properties, or the default value if it is not found.
   */
  public TypedProperties getProperties(final String key, final TypedProperties def) {
    String path = getString(key);
    TypedProperties res = new TypedProperties();
    InputStream is = null;
    try {
      is = FileUtils.getFileInputStream(path);
      res.load(is);
    } catch(IOException e) {
      return def;
    } finally {
      StreamUtils.closeSilent(is);
    }
    return res;
  }

  /**
   * Get the value of a property with the specified name as an {@link InetAddress}.
   * @param key the name of the property to retrieve.
   * @return the property as an {@link InetAddress} instance, or null if the property is not defined or the host doesn't exist.
   */
  public InetAddress getInetAddress(final String key) {
    return getInetAddress(key, null);
  }

  /**
   * Get the value of a property with the specified name as an {@link InetAddress}.
   * @param key the name of the property to retrieve.
   * @param def the default value to use if the property is not defined.
   * @return the property as an {@link InetAddress} instance, or the specified default value if the property is not defined.
   */
  public InetAddress getInetAddress(final String key, final InetAddress def) {
    String val = getString(key);
    if (val == null) return def;
    try {
      return InetAddress.getByName(val);
    } catch(UnknownHostException e) {
      return def;
    }
  }

  /**
   * Convert this set of properties into a string.
   * @return a representation of this object as a string.
   */
  public String asString() {
    StringBuilder sb = new StringBuilder();
    Set<Map.Entry<Object, Object>> entries = entrySet();
    for (Map.Entry<Object, Object> entry: entries) {
      if ((entry.getKey() instanceof String) && (entry.getValue() instanceof String)) {
        sb.append(entry.getKey()).append(" = ").append(entry.getValue()).append('\n');
      }
    }
    return sb.toString();
  }

  /**
   * Extract the properties that pass the specfied filter.
   * @param filter the filter to use, if <code>null</code> then all properties are retruned.
   * @return a new <code>TypedProperties</code> object containing only the properties matching the filter.
   */
  public TypedProperties filter(final Filter filter) {
    TypedProperties result = new TypedProperties();
    for (Map.Entry<Object, Object> entry: entrySet()) {
      if ((entry.getKey() instanceof String) && (entry.getKey() instanceof String)) {
        if ((filter == null) || filter.accepts((String) entry.getKey(), (String) entry.getValue()))
          result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  /**
   * Load this properties objects from the specified reader.
   * This load operation will recursively process all <code>!#include</code> statements before parsing the properties.
   * @param reader the reader to load from.
   * @throws IOException if any I/O error occurs.
   */
  public synchronized void loadWithIncludes(final Reader reader) throws IOException {
    new PropertiesLoader().load(this, reader);
  }

  /**
   * A filter for <code>TypedProperties</code> objects.
   */
  public interface Filter {
    /**
     * Determine whether this filter accepts a property with the specirfied name and value.
     * @param name the name of the property.
     * @param value the value of the property.
     * @return <code>true</code> if the property is accepted, <code>false</code> otherwise.
     */
    boolean accepts(String name, String value);
  }
}
