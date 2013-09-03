//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.07.23 at 08:13:35 PM MDT 
//

package org.smartreaction.boardgamegeek.xml.plays;

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
 *         &lt;element ref="{}subtypes"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="objectid" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="objecttype" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "subtypes"
})
@XmlRootElement(name = "item")
public class Item {

    @XmlElement(required = true)
    protected Subtypes subtypes;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String name;
    @XmlAttribute(required = true)
    protected String objectid;
    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String objecttype;

    /**
     * Gets the value of the subtypes property.
     * 
     * @return
     *     possible object is
     *     {@link Subtypes }
     *     
     */
    public Subtypes getSubtypes() {
        return subtypes;
    }

    /**
     * Sets the value of the subtypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link Subtypes }
     *     
     */
    public void setSubtypes(Subtypes value) {
        this.subtypes = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    public long getGameId()
    {
        if (objectid != null) {
            return Long.valueOf(objectid);
        }
        return 0;
    }

    /**
     * Gets the value of the objectid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectid() {
        return objectid;
    }

    /**
     * Sets the value of the objectid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectid(String value) {
        this.objectid = value;
    }

    /**
     * Gets the value of the objecttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjecttype() {
        return objecttype;
    }

    /**
     * Sets the value of the objecttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjecttype(String value) {
        this.objecttype = value;
    }

}