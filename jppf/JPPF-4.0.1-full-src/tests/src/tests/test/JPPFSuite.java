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

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import test.org.jppf.serialization.*;






/**
 * A suite of JUnit tests.
 * @author Laurent Cohen
 */
@RunWith(Suite.class)
//@Suite.SuiteClasses({ TestSSL.class, TestFileDataLocation.class, TestMultiServerWithSSL.class })
//@Suite.SuiteClasses({ TestSSL.class, TestMultiServer.class, TestFileDataLocation.class, TestSSL2.class })
@Suite.SuiteClasses({ TestKryo.class, TestJPPFSerialization.class })
public class JPPFSuite
{
}
