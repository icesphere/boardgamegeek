//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.22 at 08:51:29 PM MST 
//


package org.smartreaction.boardgamegeek.xml.forum;

import org.apache.commons.lang.StringUtils;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="author" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="lastpostdate" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="numarticles" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="postdate" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="subject" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "thread")
public class Thread {

    @XmlAttribute(name = "author", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String author;
    @XmlAttribute(name = "id", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "lastpostdate", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String lastpostdate;
    @XmlAttribute(name = "numarticles", required = true)
    protected String numarticles;
    @XmlAttribute(name = "postdate", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String postdate;
    @XmlAttribute(name = "subject", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String subject;

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
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
     * Gets the value of the lastpostdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastpostdate() {
        return lastpostdate;
    }

    public Date getLastPostDate() {
        if (StringUtils.isEmpty(lastpostdate)) {
            return null;
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                return sdf.parse(lastpostdate);
            } catch (ParseException e) {
                return null;
            }
        }
    }

    /**
     * Sets the value of the lastpostdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastpostdate(String value) {
        this.lastpostdate = value;
    }

    /**
     * Gets the value of the numarticles property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumarticles() {
        return numarticles;
    }

    /**
     * Sets the value of the numarticles property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumarticles(String value) {
        this.numarticles = value;
    }

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
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

}
