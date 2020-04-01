package com.aoher.util.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

import static com.aoher.util.Constants.*;

public class CreateXMLWithDOM {

    private static final Logger log = LoggerFactory.getLogger(CreateXMLWithDOM.class);

    public static void main(String[] args) {
        createPrettyXMLUsingDOM();
    }

    private static void createPrettyXMLUsingDOM() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement(ELEMENT_STUDENTS);
            document.appendChild(rootElement);

            for (int i = 1; i < 3; i++) {
                Element student = document.createElement(ELEMENT_STUDENT);

                Element studentName = document.createElement(ELEMENT_NAME);
                studentName.setTextContent("Hussein " + i);

                student.setAttribute(ATTRIBUTE_ID, String.valueOf(i));
                student.appendChild(studentName);

                rootElement.appendChild(student);
            }

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("students-new.xml"));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            log.error("ParserConfigurationException => {}", e.getMessage());
        } catch (TransformerConfigurationException e) {
            log.error("TransformerConfigurationException => {}", e.getMessage());
        } catch (TransformerException e) {
            log.error("TransformerException => {}", e.getMessage());
        }
    }
}
