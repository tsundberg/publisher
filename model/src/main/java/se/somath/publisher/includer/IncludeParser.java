package se.somath.publisher.includer;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import se.somath.publisher.excpetion.PublishException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;


public class IncludeParser {
    private String root;
    private String fileName;

    public void parse(String include) {
        Document document;
        try {
            document = parseIncludeString(include);
        } catch (ParserConfigurationException e) {
            throw new PublishException(e);
        } catch (SAXException e) {
            throw new PublishException(e);
        } catch (IOException e) {
            throw new PublishException(e);
        }

        NamedNodeMap attributes = findIncludeTag(document);
        root = findRootName(attributes);
        fileName = findFileName(attributes);
    }

    public String getRoot() {
        return root;
    }

    public String getFileName() {
        return fileName;
    }

    private Document parseIncludeString(String include) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader reader = new StringReader(include);
        InputSource source = new InputSource(reader);
        return builder.parse(source);
    }

    private NamedNodeMap findIncludeTag(Document document) {
        String tagName = "include";
        NodeList elements = document.getElementsByTagName(tagName);
        int theOnlyExpectedElement = 0;
        Node item = elements.item(theOnlyExpectedElement);

        return item.getAttributes();
    }

    private String findRootName(NamedNodeMap attributes) {
        String rootAttributeName = "root";
        Node rootItem = attributes.getNamedItem(rootAttributeName);
        return rootItem.getNodeValue();
    }

    private String findFileName(NamedNodeMap attributes) {
        String fileAttributeName = "file";
        Node fileItem = attributes.getNamedItem(fileAttributeName);
        return fileItem.getNodeValue();
    }
}