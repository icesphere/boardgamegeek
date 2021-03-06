//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.27 at 06:56:44 AM MDT 
//


package org.smartreaction.boardgamegeek.xml.geeklist;

import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.smartreaction.boardgamegeek.xml.geeklist package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Thumbs_QNAME = new QName("", "thumbs");
    private final static QName _Body_QNAME = new QName("", "body");
    private final static QName _Postdate_QNAME = new QName("", "postdate");
    private final static QName _Title_QNAME = new QName("", "title");
    private final static QName _Username_QNAME = new QName("", "username");
    private final static QName _EditdateTimestamp_QNAME = new QName("", "editdate_timestamp");
    private final static QName _Description_QNAME = new QName("", "description");
    private final static QName _PostdateTimestamp_QNAME = new QName("", "postdate_timestamp");
    private final static QName _Editdate_QNAME = new QName("", "editdate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.smartreaction.boardgamegeek.xml.geeklist
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link Comment }
     * 
     */
    public Comment createComment() {
        return new Comment();
    }

    /**
     * Create an instance of {@link Geeklist }
     * 
     */
    public Geeklist createGeeklist() {
        return new Geeklist();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "thumbs")
    public JAXBElement<BigInteger> createThumbs(BigInteger value) {
        return new JAXBElement<BigInteger>(_Thumbs_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "body")
    public JAXBElement<String> createBody(String value) {
        return new JAXBElement<String>(_Body_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "postdate")
    public JAXBElement<String> createPostdate(String value) {
        return new JAXBElement<String>(_Postdate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "title")
    public JAXBElement<String> createTitle(String value) {
        return new JAXBElement<String>(_Title_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "username")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createUsername(String value) {
        return new JAXBElement<String>(_Username_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "editdate_timestamp")
    public JAXBElement<BigInteger> createEditdateTimestamp(BigInteger value) {
        return new JAXBElement<BigInteger>(_EditdateTimestamp_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "postdate_timestamp")
    public JAXBElement<BigInteger> createPostdateTimestamp(BigInteger value) {
        return new JAXBElement<BigInteger>(_PostdateTimestamp_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "editdate")
    public JAXBElement<String> createEditdate(String value) {
        return new JAXBElement<String>(_Editdate_QNAME, String.class, null, value);
    }

}
