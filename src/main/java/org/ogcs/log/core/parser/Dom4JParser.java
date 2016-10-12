package org.ogcs.log.core.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.ogcs.log.core.builder.Field;
import org.ogcs.log.core.builder.KeyIndex;
import org.ogcs.log.core.builder.Table;
import org.ogcs.log.util.XmlUtil;
import org.ogcs.utilities.StringUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Dom4J Parser.
 * 依赖Dom4J类库
 *
 * @author TinyZ
 * @date 2016-10-11.
 */
public class Dom4JParser implements StructParser<Table> {

    private static final Logger LOG = LogManager.getLogger(Dom4JParser.class);
    private String path;
    private boolean isInitialized = false;
    private Map<String, Table> tables;

    public Dom4JParser(String path) {
        this.path = path;
        this.tables = new HashMap<>();
    }

    @Override
    public Table getTable(String tableName) {
        checkAndInitialize();
        return tables.get(tableName.toLowerCase());
    }

    @Override
    public Map<String, Table> getAll() {
        checkAndInitialize();
        return tables;
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public Map<String, Table> load(String filePath) {
        if (!XmlUtil.validateXml(filePath)) {
            throw new IllegalStateException("XSD file is wrong");
        }
        final Map<String, Table> data = interpretXml(filePath);
        if (data == null || data.size() <= 0) {
            throw new IllegalStateException("The xml data struct file content is wrong.");
        }
        return data;
    }

    @Override
    public Map<String, Table> loadAndReplace(String filePath) {
        Map<String, Table> newTables = load(filePath);
        if (newTables != null) {
            this.replace(newTables, filePath);
            return newTables;
        }
        return null;
    }

    @Override
    public void load() {
        tables = load(path);
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

    public static void main(String[] args) throws DocumentException {
        String filePath = "./config/aolog.xml";
        interpretXml(filePath, Table.class, Field.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Table> interpretXml(String filePath) {
        this.path = filePath;
        return interpretXml(filePath, Table.class, Field.class);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Table<F>, F extends Field> Map<String, Table> interpretXml(String filePath, Class<T> clzOfTable, Class<F> clzOfField) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(filePath);
        } catch (DocumentException e) {
            LOG.error("Load XML file error.", e);
            return null;
        }
        Map<String, Table> tables = new HashMap<>();
        Element eleRoot = document.getRootElement();
        //  表
        eleRoot.elements(STRUCT_TABLE).forEach((c1) -> {
            Element nodeTable = (Element) c1;
            Table table = initObj(Table.class, nodeTable);
            if (table != null) {
                //  索引
                Element eleIndexes = nodeTable.element(STRUCT_INDEXES);
                if (eleIndexes != null) {
                    Map<String, String[]> tempIndexes = new HashMap<>();
                    eleIndexes.elements(STRUCT_INDEX).forEach((c2) -> {
                        Element nodeIndex = (Element) c2;
                        Attribute attrName = nodeIndex.attribute("name");
                        List<Element> columnList = nodeIndex.elements();
                        if (!columnList.isEmpty()) {
                            String[] columns = new String[columnList.size()];
                            for (int i = 0; i < columnList.size(); i++) {
                                columns[i] = columnList.get(i).attribute("name").getValue();
                            }
                            String kiName = attrName == null ? StringUtil.implode(columns, '_') : attrName.getValue();
                            tempIndexes.put(kiName, columns);
                        }
                    });
                    if (!tempIndexes.isEmpty()) {
                        List<KeyIndex> arrayKeyIndex = tempIndexes.entrySet().stream().map(entry -> new KeyIndex(entry.getKey(), entry.getValue())).collect(Collectors.toList());
                        final KeyIndex[] fAry = (KeyIndex[]) Array.newInstance(KeyIndex.class, arrayKeyIndex.size());
                        table.setIndexes(arrayKeyIndex.toArray(fAry));
                    }
                }
                //  字段
                List<F> arrayField = new ArrayList<>();
                nodeTable.elements(STRUCT_FIELD).forEach((c2) -> {
                    Element nodeField = (Element) c2;
                    final F f = initObj(clzOfField, nodeField);
                    if (f != null) {
                        arrayField.add(f);
                    }
                });
                if (!arrayField.isEmpty()) {
                    final F[] fAry = (F[]) Array.newInstance(clzOfField, arrayField.size());
                    table.setFields(arrayField.toArray(fAry));
                }
                tables.put(table.getName().toLowerCase(), table);
            }
        });
        return tables;
    }

    @SuppressWarnings("unchecked")
    private static <T> T initObj(Class<T> clazz, Element element) {
        T object;
        try {
            object = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        element.attributes().forEach((child) -> {
            Attribute attribute = (Attribute) child;
            java.lang.reflect.Field field;
            try {
                field = object.getClass().getDeclaredField(attribute.getName());
                setField(field, object, attribute.getValue());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return object;
    }

    public static void setField(java.lang.reflect.Field field, Object object, String value) throws IllegalAccessException {
        field.setAccessible(true);
        if (field.getType() == String.class) {
            field.set(object, value);
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.set(object, Integer.valueOf(value));
        } else if (field.getType() == double.class || field.getType() == Double.class) {
            field.set(object, Double.valueOf(value));
        } else if (field.getType() == float.class || field.getType() == Float.class) {
            field.set(object, Float.valueOf(value));
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            field.set(object, "true".equals(value.toLowerCase()));
        }
    }
}



















