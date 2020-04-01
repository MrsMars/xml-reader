package com.aoher.util.stax;

import com.aoher.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.aoher.util.Constants.*;

public class ReadXMLWithSTAX {

    private static final Logger log = LoggerFactory.getLogger(ReadXMLWithSTAX.class);

    public static void main(String[] args) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader eventReader = factory.createXMLEventReader(new FileReader("students.xml"));

            List<Student> students = new ArrayList<>();
            Student student = null;
            String elementValue = null;

            while(eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                int eventType = event.getEventType();

                if (eventType == XMLStreamConstants.START_ELEMENT) {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (qName.equalsIgnoreCase(ELEMENT_STUDENT)) {
                        student = new Student();
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        if (attributes.hasNext()) {
                            Attribute attr = attributes.next();
                            String graduated = attr.getValue();
                            student.setGraduated(Boolean.parseBoolean(graduated));
                        }
                    }
                } else if (eventType == XMLStreamConstants.CHARACTERS) {
                    Characters characters = event.asCharacters();
                    elementValue = characters.getData();
                } else if (eventType == XMLStreamConstants.END_ELEMENT) {
                    EndElement endElement = event.asEndElement();
                    String qName = endElement.getName().getLocalPart();
                    if (qName.equalsIgnoreCase(ELEMENT_STUDENT)) {
                        students.add(student);
                    }
                    if (qName.equalsIgnoreCase(ATTRIBUTE_ID)) {
                        student.setId(Integer.parseInt(elementValue));
                    }

                    if (qName.equalsIgnoreCase(ELEMENT_NAME)) {
                        student.setName(elementValue);
                    }
                }
            }

            students.forEach(s -> {
               log.info("Student Id = {}", s.getId());
               log.info("Student Name = {}", s.getName());
               log.info("Is student graduated? {}", s.isGraduated());
            });
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException => {}", e.getMessage());
        } catch (XMLStreamException e) {
            log.error("XMLStreamException => {}", e.getMessage());
        }
    }
}
