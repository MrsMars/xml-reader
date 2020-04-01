package com.aoher.util.sax;

import com.aoher.model.Student;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

import static com.aoher.util.Constants.*;

public class SAXHandler extends DefaultHandler {

    private List<Student> students = null;
    private Student student = null;
    private String elementValue;

    @Override
    public void startDocument() {
        students = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase(ELEMENT_STUDENT)) {
            student = new Student();

            if(attributes.getLength() > 0)
            {
                String graduated = attributes.getValue(ATTRIBUTE_GRADUATED);
                student.setGraduated(Boolean.parseBoolean(graduated));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
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

    @Override
    public void characters(char[] ch, int start, int length) {
        elementValue = new String(ch, start, length);
    }

    public List<Student> getStudents() {
        return students;
    }
}