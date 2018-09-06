package com.tecsun.card.common.clarencezeroutils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;

public class DomUtil {
    private static final Logger logger = LoggerFactory.getLogger(DomUtil.class);

    public DomUtil () {
    }

    public static Document createDocument (String classPathXmlFile) {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        Document document = null;

        try {
            DocumentBuilder dom = domFactory.newDocumentBuilder();
            String skr = ResourceUtils.getFile("classpath:threadPool.xml").getPath();

            document = dom.parse(new FileInputStream(new File(skr)));
        } catch (Exception e) {
            logger.error(String.format("create Document of xml file[%s] occurs error", classPathXmlFile), e);
        }
        return document;
    }
}
