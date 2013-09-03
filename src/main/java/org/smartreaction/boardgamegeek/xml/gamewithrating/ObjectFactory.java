//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.23 at 08:04:42 PM MDT 
//


package org.smartreaction.boardgamegeek.xml.gamewithrating;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.smartreaction.boardgamegeek.xml.gamewithrating package. 
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

    private final static QName _Thumbnail_QNAME = new QName("", "thumbnail");
    private final static QName _Description_QNAME = new QName("", "description");
    private final static QName _Image_QNAME = new QName("", "image");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.smartreaction.boardgamegeek.xml.gamewithrating
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Name }
     * 
     */
    public Name createName() {
        return new Name();
    }

    /**
     * Create an instance of {@link Minplayers }
     * 
     */
    public Minplayers createMinplayers() {
        return new Minplayers();
    }

    /**
     * Create an instance of {@link Minage }
     * 
     */
    public Minage createMinage() {
        return new Minage();
    }

    /**
     * Create an instance of {@link Yearpublished }
     * 
     */
    public Yearpublished createYearpublished() {
        return new Yearpublished();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link Poll }
     * 
     */
    public Poll createPoll() {
        return new Poll();
    }

    /**
     * Create an instance of {@link Playingtime }
     * 
     */
    public Playingtime createPlayingtime() {
        return new Playingtime();
    }

    /**
     * Create an instance of {@link Items }
     * 
     */
    public Items createItems() {
        return new Items();
    }

    /**
     * Create an instance of {@link Results }
     * 
     */
    public Results createResults() {
        return new Results();
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
     * Create an instance of {@link Maxplayers }
     * 
     */
    public Maxplayers createMaxplayers() {
        return new Maxplayers();
    }

    /**
     * Create an instance of {@link Link }
     * 
     */
    public Link createLink() {
        return new Link();
    }

    /**
     * Create an instance of {@link Comments }
     * 
     */
    public Comments createComments() {
        return new Comments();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "thumbnail")
    public JAXBElement<String> createThumbnail(String value) {
        return new JAXBElement<String>(_Thumbnail_QNAME, String.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "image")
    public JAXBElement<String> createImage(String value) {
        return new JAXBElement<String>(_Image_QNAME, String.class, null, value);
    }

}
