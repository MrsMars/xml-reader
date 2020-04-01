package com.aoher.util.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static com.aoher.util.Constants.*;

public class ReadXMLWithDOM {

    private static final Logger log = LoggerFactory.getLogger(ReadXMLWithDOM.class);

    public static void main(String[] args) {
        try {
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(xmlFile);
            getStudentById(doc,ATTRIBUTE_ID, "2");
            getAllStudents(doc);
            getGraduatedStudents(doc, ATTRIBUTE_GRADUATED, "yes");
            parseWholeXML(doc.getDocumentElement());
        } catch (IOException e) {
            log.error("IOException => {}", e.getMessage());
        } catch (ParserConfigurationException e) {
            log.error("ParserConfigurationException => {}", e.getMessage());
        } catch (SAXException e) {
            log.error("SAXException => {}", e.getMessage());
        }
    }

    private static void getRootElement(Document doc) {
        log.info(doc.getDocumentElement().getNodeName());
    }

    private static void getStudentById(Document doc, String textNodeName, String textNodeValue) {
        NodeList studentNodes = doc.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, studentNodes.getLength()).mapToObj(studentNodes::item).filter(studentNode -> studentNode.getNodeType() == Node.ELEMENT_NODE).map(studentNode -> (Element) studentNode).forEach(studentElement -> {
            NodeList textNodes = studentElement.getElementsByTagName(textNodeName);
            if (textNodes.getLength() > 0 && textNodes.item(0).getTextContent().equalsIgnoreCase(textNodeValue)) {
                log.info(textNodes.item(0).getTextContent());
                log.info(studentElement.getElementsByTagName(ELEMENT_NAME).item(0).getTextContent());
            }
        });
    }

    private static void getAllStudents(Document doc) {
        NodeList studentNodes = doc.getElementsByTagName(ELEMENT_STUDENT);
        IntStream.range(0, studentNodes.getLength()).mapToObj(studentNodes::item).filter(studentNode -> studentNode.getNodeType() == Node.ELEMENT_NODE).map(studentNode -> (Element) studentNode).forEach(studentElement -> {
            String studentId = studentElement.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent();
            String studentName = studentElement.getElementsByTagName(ELEMENT_NAME).item(0).getTextContent();
            log.info("Student Id = {}", studentId);
            log.info("Student Name = {}", studentName);
        });
    }

    private static void parseWholeXML(Node startingNode) {
        NodeList childNodes = startingNode.getChildNodes();

        IntStream.range(0, childNodes.getLength()).mapToObj(childNodes::item).forEach(childNode -> {
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                parseWholeXML(childNode);
            } else {
                if (!childNode.getTextContent().trim().isEmpty()) {
                    log.info(childNode.getTextContent());
                }
            }
        });
    }

    private static void getGraduatedStudents(Document doc, String attributeName, String attributeValue) {
        NodeList studentNodes = doc.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, studentNodes.getLength()).mapToObj(studentNodes::item).filter(studentNode -> studentNode.getNodeType() == Node.ELEMENT_NODE).map(studentNode -> (Element) studentNode).filter(studentElement -> attributeValue.equalsIgnoreCase(studentElement.getAttribute(attributeName))).forEach(studentElement -> {
            String studentId = studentElement.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent();
            String studentName = studentElement.getElementsByTagName(ELEMENT_NAME).item(0).getTextContent();
            log.info("Student Id = " + studentId);
            log.info("Student Name = " + studentName);
        });
    }
}