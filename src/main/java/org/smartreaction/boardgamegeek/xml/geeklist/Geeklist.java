//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.27 at 06:56:44 AM MDT 
//


package org.smartreaction.boardgamegeek.xml.geeklist;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}postdate"/>
 *         &lt;element ref="{}postdate_timestamp"/>
 *         &lt;element ref="{}editdate"/>
 *         &lt;element ref="{}editdate_timestamp"/>
 *         &lt;element ref="{}thumbs"/>
 *         &lt;element ref="{}username"/>
 *         &lt;element ref="{}title"/>
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}comment" maxOccurs="unbounded"/>
 *         &lt;element ref="{}item" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="termsofuse" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "postdate",
    "postdateTimestamp",
    "editdate",
    "editdateTimestamp",
    "thumbs",
    "username",
    "title",
    "description",
    "comment",
    "item"
})
@XmlRootElement(name = "geeklist")
public class Geeklist {

    @XmlElement(required = true)
    protected String postdate;
    @XmlElement(name = "postdate_timestamp", required = true)
    protected BigInteger postdateTimestamp;
    @XmlElement(required = true)
    protected String editdate;
    @XmlElement(name = "editdate_timestamp", required = true)
    protected BigInteger editdateTimestamp;
    @XmlElement(required = true)
    protected BigInteger thumbs;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String username;
    @XmlElement(required = true)
    protected String title;
    @XmlElement(required = true)
    protected String description;
    @XmlElement(required = true)
    protected List<Comment> comment;
    @XmlElement(required = true)
    protected List<Item> item;
    @XmlAttribute(required = true)
    protected BigInteger id;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String termsofuse;

    /**
     * Gets the value of the postdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostdate() {
        return postdate;
    }

    /**
     * Sets the value of the postdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostdate(String value) {
        this.postdate = value;
    }

    /**
     * Gets the value of the postdateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPostdateTimestamp() {
        return postdateTimestamp;
    }

    /**
     * Sets the value of the postdateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPostdateTimestamp(BigInteger value) {
        this.postdateTimestamp = value;
    }

    /**
     * Gets the value of the editdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEditdate() {
        return editdate;
    }

    /**
     * Sets the value of the editdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEditdate(String value) {
        this.editdate = value;
    }

    /**
     * Gets the value of the editdateTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEditdateTimestamp() {
        return editdateTimestamp;
    }

    /**
     * Sets the value of the editdateTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEditdateTimestamp(BigInteger value) {
        this.editdateTimestamp = value;
    }

    /**
     * Gets the value of the thumbs property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getThumbs() {
        return thumbs;
    }

    /**
     * Sets the value of the thumbs property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setThumbs(BigInteger value) {
        this.thumbs = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Comment }
     * 
     * 
     */
    public List<Comment> getComment() {
        if (comment == null) {
            comment = new ArrayList<Comment>();
        }
        return this.comment;
    }

    /**
     * Gets the value of the item property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the item property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getItem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Item }
     * 
     * 
     */
    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<Item>();
        }
        return this.item;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setId(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the termsofuse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermsofuse() {
        return termsofuse;
    }

    /**
     * Sets the value of the termsofuse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermsofuse(String value) {
        this.termsofuse = value;
    }

}
