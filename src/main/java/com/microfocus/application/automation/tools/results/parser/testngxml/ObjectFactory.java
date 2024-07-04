/*
 * Certain versions of software accessible here may contain branding from Hewlett-Packard Company (now HP Inc.) and Hewlett Packard Enterprise Company.
 * This software was acquired by Micro Focus on September 1, 2017, and is now offered by OpenText.
 * Any reference to the HP and Hewlett Packard Enterprise/HPE marks is historical in nature, and the HP and Hewlett Packard Enterprise/HPE marks are the property of their respective owners.
 * __________________________________________________________________
 * MIT License
 *
 * Copyright 2012-2024 Open Text
 *
 * The only warranties for products and services of Open Text and
 * its affiliates and licensors ("Open Text") are as may be set forth
 * in the express warranty statements accompanying such products and services.
 * Nothing herein should be construed as constituting an additional warranty.
 * Open Text shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Except as specifically indicated otherwise, this document contains
 * confidential information and a valid license is required for possession,
 * use or copying. If this work is provided to the U.S. Government,
 * consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are
 * licensed to the U.S. Government under vendor's standard commercial license.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ___________________________________________________________________
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.13 at 09:25:36 AM CST 
//


package com.microfocus.application.automation.tools.results.parser.testngxml;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestngResults }
     * 
     */
    public TestngResults createTestngResults() {
        return new TestngResults();
    }

    /**
     * Create an instance of {@link TestngResults.Suite }
     * 
     */
    public TestngResults.Suite createTestngResultsSuite() {
        return new TestngResults.Suite();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Test }
     * 
     */
    public TestngResults.Suite.Test createTestngResultsSuiteTest() {
        return new TestngResults.Suite.Test();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Test.Class }
     * 
     */
    public TestngResults.Suite.Test.Class createTestngResultsSuiteTestClass() {
        return new TestngResults.Suite.Test.Class();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Test.Class.TestMethod }
     * 
     */
    public TestngResults.Suite.Test.Class.TestMethod createTestngResultsSuiteTestClassTestMethod() {
        return new TestngResults.Suite.Test.Class.TestMethod();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Groups }
     * 
     */
    public TestngResults.Suite.Groups createTestngResultsSuiteGroups() {
        return new TestngResults.Suite.Groups();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Groups.Group }
     * 
     */
    public TestngResults.Suite.Groups.Group createTestngResultsSuiteGroupsGroup() {
        return new TestngResults.Suite.Groups.Group();
    }

    /**
     * Create an instance of {@link NewDataSet }
     * 
     */
    public NewDataSet createNewDataSet() {
        return new NewDataSet();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Test.Class.TestMethod.Exception }
     * 
     */
    public TestngResults.Suite.Test.Class.TestMethod.Exception createTestngResultsSuiteTestClassTestMethodException() {
        return new TestngResults.Suite.Test.Class.TestMethod.Exception();
    }

    /**
     * Create an instance of {@link TestngResults.Suite.Groups.Group.Method }
     * 
     */
    public TestngResults.Suite.Groups.Group.Method createTestngResultsSuiteGroupsGroupMethod() {
        return new TestngResults.Suite.Groups.Group.Method();
    }

}
