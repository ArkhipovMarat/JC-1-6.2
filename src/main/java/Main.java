import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ArrayList<Employee> employees = new ArrayList<>();

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String xmlFilePath = "data.xml";
        String jsonFilePath = "data.json";
        List<Employee> list = parseXML(xmlFilePath);
        saveXMLtoJson (list, jsonFilePath);
    }

    private static void saveXMLtoJson(List<Employee> list, String jsonFilePath) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter jsonWriter = new FileWriter(jsonFilePath);) {
            gson.toJson(list, jsonWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String xmlFilePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFilePath);

        NodeList employeeNodes = document.getDocumentElement().getElementsByTagName("employee");

        for (int i=0; i<employeeNodes.getLength(); i++){
            employees.add(getEmployeeAttributes(employeeNodes.item(i)));
        }
        return employees;
    }

    private static Employee getEmployeeAttributes(Node node) {
        Employee employee = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee.setId(getTagValue("id", element));
            employee.setFirstName(getTagValue("firstName", element));
            employee.setLastName(getTagValue("lastName", element));
            employee.setCountry(getTagValue("country", element));
            employee.setAge(getTagValue("age", element));
        }
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }
}

