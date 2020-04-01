package com.aoher.util.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import static com.aoher.util.Constants.*;

public class ModifyXMLWithDOM {

    private static final Logger log = LoggerFactory.getLogger(ModifyXMLWithDOM.class);

    public static void main(String[] args) {
        try {

            File xmlFile = new File("students.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            setGraduatedStudent(doc, 2);
            modifyStudentFirstName(doc, 2, "Alexa");
            setStudentLastName(doc, 1, "Terek");
            removeStudentLastName(doc, 1);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("students.xml"));
            transformer.transform(source, result);
        } catch (IOException e) {
            log.error("IOException => {}", e.getMessage());
        } catch (TransformerConfigurationException e) {
            log.error("TransformerConfigurationException => {}", e.getMessage());
        } catch (ParserConfigurationException e) {
            log.error("ParserConfigurationException => {}", e.getMessage());
        } catch (SAXException e) {
            log.error("SAXException => {}", e.getMessage());
        } catch (TransformerException e) {
            log.error("TransformerException => {}", e.getMessage());
        }

    }

    private static void setGraduatedStudent(Document document, int id) {
        NodeList students = document.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, students.getLength()).mapToObj(i -> (Element) students.item(i)).forEach(studentNode -> {
            int studentId = Integer.parseInt(studentNode.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent());
            if (studentId == id) {
                studentNode.setAttribute(ATTRIBUTE_GRADUATED, Boolean.TRUE.toString());
            }
        });
    }

    private static void modifyStudentFirstName(Document document, int id, String updatedFirstName) {
        NodeList students = document.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, students.getLength()).mapToObj(i -> (Element) students.item(i)).forEach(studentNode -> {
            int studentId = Integer.parseInt(studentNode.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent());
            if (studentId == id) {
                Element studentName = (Element) studentNode.getElementsByTagName(ELEMENT_NAME).item(0);
                studentName.setTextContent(updatedFirstName);
            }
        });
    }

    private static void setStudentLastName(Document doc, int id, String lastName) {

        NodeList students = doc.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, students.getLength()).mapToObj(i -> (Element) students.item(i)).forEach(studentNode -> {
            int studentId = Integer.parseInt(studentNode.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent());
            if (studentId == id) {
                Element lastNameElement = doc.createElement(ELEMENT_LAST_NAME);
                lastNameElement.setTextContent(lastName);
                studentNode.appendChild(lastNameElement);
            }
        });
    }

    private static void removeStudentLastName(Document doc, int id) {

        NodeList students = doc.getElementsByTagName(ELEMENT_STUDENT);

        IntStream.range(0, students.getLength()).mapToObj(i -> (Element) students.item(i)).forEach(studentNode -> {
            int studentId = Integer.parseInt(studentNode.getElementsByTagName(ATTRIBUTE_ID).item(0).getTextContent());
            if (studentId == id) {
                Element studentLastName = (Element) studentNode.getElementsByTagName(ELEMENT_LAST_NAME).item(0);
                studentNode.removeChild(studentLastName);
            }
        });
    }
}
