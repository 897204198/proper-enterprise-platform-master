package com.proper.enterprise.platform.oopsearch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class EntityRelationUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRelationUtil.class);

    /**
     * scan the path.
     */
    public static List<Class> getEntityClasses() {
        List<Class> list = new ArrayList();
        ClassPathScanningCandidateComponentProvider provider =
            new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Table.class));
        Set<BeanDefinition> entitys = provider.findCandidateComponents("com.proper.**.entity");
        for (BeanDefinition entity : entitys) {
            try {
                Class cl = Class.forName(entity.getBeanClassName());
                list.add(cl);
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return list;
    }

    /**
     * reflect table relations.
     */
    public static List<Map<String, Object>> getRelationFromEntitys() {
        List<Class> list = getEntityClasses();
        List<Map<String, Object>> configInfo = new ArrayList<>();
        for (Class cl : list) {
            boolean hasAnno = cl.isAnnotationPresent(Table.class);
            if (hasAnno) {
                Table annotation = (Table) cl.getAnnotation(Table.class);
                String nodeA = annotation.name();
                Field[] fields = cl.getDeclaredFields();
                for (Field f : fields) {
                    JoinTable joinTable = f.getAnnotation(JoinTable.class);
                    if (joinTable != null) {
                        String nodeB = joinTable.name();
                        JoinColumn[] joinColumns = joinTable.joinColumns();
                        JoinColumn[] inverseJoinColumns = joinTable.inverseJoinColumns();
                        if (joinColumns.length > 0 && inverseJoinColumns.length > 0) {
                            JoinColumn joinColumn = joinColumns[0];
                            JoinColumn inverseJoinColumn = joinTable.inverseJoinColumns()[0];
                            String colAB = joinColumn.name();
                            String colCB = inverseJoinColumn.name();
                            Type type = f.getGenericType();
                            if (type != null && type instanceof ParameterizedType) {
                                Map<String, Object> nodeMap = new HashMap<>();
                                String[] datas = new String[]{"id", colAB.toLowerCase()};
                                nodeMap.put("nodeA", nodeA.toLowerCase());
                                nodeMap.put("nodeB", nodeB.toLowerCase());
                                nodeMap.put("datas", datas);
                                configInfo.add(nodeMap);

                                ParameterizedType pt = (ParameterizedType) type;
                                Class genericClazz = (Class) pt.getActualTypeArguments()[0];
                                annotation = (Table) genericClazz.getAnnotation(Table.class);
                                String nodeC = annotation.name();
                                nodeMap = new HashMap<>();
                                datas = new String[]{"id", colCB.toLowerCase()};
                                nodeMap.put("nodeA", nodeC.toLowerCase());
                                nodeMap.put("nodeB", nodeB.toLowerCase());
                                nodeMap.put("datas", datas);
                                configInfo.add(nodeMap);
                                //LOGGER.debug("nodeA:" + nodeA + "," + "nodeB:" + nodeB + ",data:" + "id-" + colAB);
                                //LOGGER.debug("nodeA:" + nodeC + "," + "nodeB:" + nodeB + ",data:" + "id-" + colCB);
                            }
                        }
                    }
                }
            }
        }
        return configInfo;
    }

}
