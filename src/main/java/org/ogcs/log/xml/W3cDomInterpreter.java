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

import org.ogcs.log.mysql.Field;
import org.ogcs.log.mysql.Table;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML Document Object Model(XML DOM).
 * <p>Use JDK DOM API to interpreter xml log struct defined file</p>
 *
 * @author TinyZ
 * @date 2016-06-24.
 * @since 1.0
 */
public final class W3cDomInterpreter implements XmlInterpreter<Table> {

    private boolean isInitialized = false;
    private Map<String, Table> tables;
    private String path;

    public W3cDomInterpreter(String path) {
        this.path = path;
    }

    //    public Map<String, Table> interpretXml() {
//        return interpretXml(EosProperties.SERVER_EOS_FILE_PATH);
//    }

    public Map<String, Table> interpretXml(String filePath) {
        this.path = filePath;
        this.tables = new HashMap<>();
        return interpretXml(filePath, Table.class, Field.class);
    }

    private <T extends Table<F>, F extends Field> Map<String, Table> interpretXml(String filePath, Class<T> clzOfTable, Class<F> clzOfField) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document;
        NodeList childNodes = null;
        try {
            if (builder != null) {
                document = builder.parse(new File(filePath));
                if (document != null) {
                    childNodes = document.getChildNodes();
                }
            }
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
        if (childNodes == null) {
            return null;
        }
        // The xml root field is okra-log file
        Node rootNode = childNodes.item(0);

        Map<String, Table> tables = new HashMap<>();
        NodeList nodeListTable = rootNode.getChildNodes();
        for (int i = 0; i < nodeListTable.getLength(); i++) {
            Node nodeTable = nodeListTable.item(i);
            if (XML_ELEMENT_TABLE.equals(nodeTable.getNodeName())) {
                final T table = initObj(clzOfTable, nodeTable.getAttributes());
                if (table != null) {
                    NodeList nodeListField = nodeTable.getChildNodes();
                    List<F> arrayField = new ArrayList<>();
                    for (int j = 0; j < nodeListField.getLength(); j++) {
                        final Node fieldNode = nodeListField.item(j);
                        if (null != fieldNode && XML_ELEMENT_FIELD.equals(fieldNode.getNodeName())) {
                            final F f = initObj(clzOfField, fieldNode.getAttributes());
                            if (f != null) {
                                arrayField.add(f);
                            }
                        }
                    }
                    if (arrayField.size() > 0) {
                        @SuppressWarnings("unchecked")
                        final F[] fAry = (F[]) Array.newInstance(clzOfField, arrayField.size());
                        table.setFields(arrayField.toArray(fAry));
                    }
                    tables.put(table.getName().toLowerCase(), table);
                }
            }
        }
        return tables;
    }

    /**
     * Initialize T instance
     *
     * @param clazz          The class of T
     * @param fileAttributes The xml defined the object 's fields.
     * @param <T>            Any java object class
     * @return Return the T instance
     */
    private <T> T initObj(Class<T> clazz, NamedNodeMap fileAttributes) {
        T object;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        for (int k = 0; k < fileAttributes.getLength(); k++) {
            String nodeName = fileAttributes.item(k).getNodeName();
            String nodeValue = fileAttributes.item(k).getNodeValue();

            java.lang.reflect.Field declaredField;
            try {
                declaredField = object.getClass().getDeclaredField(nodeName);
                declaredField.setAccessible(true);
                if (declaredField.getType() == String.class) {
                    declaredField.set(object, nodeValue);
                } else if (declaredField.getType() == int.class || declaredField.getType() == Integer.class) {
                    declaredField.set(object, Integer.valueOf(nodeValue));
                } else if (declaredField.getType() == double.class || declaredField.getType() == Double.class) {
                    declaredField.set(object, Double.valueOf(nodeValue));
                } else if (declaredField.getType() == float.class || declaredField.getType() == Float.class) {
                    declaredField.set(object, Float.valueOf(nodeValue));
                } else if (declaredField.getType() == boolean.class || declaredField.getType() == Boolean.class) {
                    declaredField.set(object, "true".equals(nodeValue.toLowerCase()));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    @Override
    public Table getTable(String tableName) {
        checkAndInitialize();
        return tables.get(tableName.toLowerCase());
    }

    @Override
    public Map<String, Table> getAllTables() {
        checkAndInitialize();
        return tables;
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public Map<String, Table> loadXML(String filePath) {
        if (XmlUtil.validateXml(filePath)) {
            throw new IllegalStateException("XSD file is wrong");
        }
        final Map<String, Table> data = interpretXml(filePath);
        if (data == null || data.size() <= 0) {
            throw new IllegalStateException("The xml data struct file content is wrong.");
        }
        return data;
    }

    @Override
    public Map<String, Table> loadXmlAndReplace(String filePath) {
        Map<String, Table> newTables = loadXML(filePath);
        if (newTables != null) {
            this.replace(newTables, filePath);
            return newTables;
        }
        return null;
    }

    @Override
    public void load() {
        tables = loadXML(path);
        isInitialized = true;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized;
    }

    @Override
    public void replace(Map<String, Table> tables) {
        this.tables = tables;
        isInitialized = true;
    }

    @Override
    public void replace(Map<String, Table> tables, String filePath) {
        this.tables = tables;
        this.path = filePath;
        isInitialized = true;
    }

    private void checkAndInitialize() {
        if (!isInitialized && tables.size() <= 0) {
            load();
            isInitialized = true;
        }
    }
}
