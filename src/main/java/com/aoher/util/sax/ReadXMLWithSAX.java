package com.aoher.util.sax;

import com.aoher.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class ReadXMLWithSAX {

    private static final Logger log = LoggerFactory.getLogger(ReadXMLWithSAX.class);

    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SAXHandler saxHandler = new SAXHandler();
            saxParser.parse("students.xml", saxHandler);

            List<Student> students = saxHandler.getStudents();

            students.forEach(student -> {
                log.info("Student Id = {}", student.getId());
                log.info("Student Name = {}", student.getName());
                log.info("Is student graduated? {}", student.isGraduated());
            });
        } catch (ParserConfigurationException e) {
            log.error("ParserConfigurationException => {}", e.getMessage());
        } catch (IOException e) {
            log.error("IOException => {}", e.getMessage());
        } catch (SAXException e) {
            log.error("SAXException => {}", e.getMessage());
        }
    }
}
