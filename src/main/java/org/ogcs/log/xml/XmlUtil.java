/*
 *     Copyright 2016-2026 TinyZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ogcs.log.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.net.URISyntaxException;

/**
 * Xml file utility.
 * <p>
 * Verify xml by XSD file.
 *
 * @author TinyZ.
 * @date 2016-06-24
 */
public final class XmlUtil {

    private static final Logger LOG = LogManager.getLogger(XmlUtil.class);

    private static final String filePath = "/okra-log.xsd";

    private XmlUtil() {
        // no-op
    }

    /**
     * Check an Extensible Markup Language(XML) document against Schema.
     *
     * @param xmlPath The XML file path
     * @return Return true if the xml is right.
     */
    public static boolean validateXml(String xmlPath) {
        String property = System.getProperty("log.xsd.path", filePath);
        try {
            String path = XmlUtil.class.getResource(property).toURI().toString();
            return validateXml(path, xmlPath);
        } catch (URISyntaxException e) {
            LOG.warn("Load *.xsd file failure : " + property, e);
        }
        return false;
    }

    /**
     * Check an Extensible Markup Language(XML) document against Schema.
     *
     * @param xsdPath The XML schema definition(XSD) file path
     * @param xmlPath The XML file path
     * @return Return true if XML is valid. false otherwise
     */
    public static boolean validateXml(String xsdPath, String xmlPath) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        File schemaFile = new File(xsdPath);
        Schema schema;
        try {
            schema = schemaFactory.newSchema(schemaFile);
        } catch (SAXException e) {
            LOG.warn("The XSD file not found in path : " + xsdPath, e);
            return false;
        }
        Validator validator = schema.newValidator();
        Source source = new StreamSource(xmlPath);
        try {
            validator.validate(source);
        } catch (Exception ex) {
            LOG.warn("The XML file is invalid in path : " + xmlPath, ex);
            return false;
        }
        return true;
    }
}
