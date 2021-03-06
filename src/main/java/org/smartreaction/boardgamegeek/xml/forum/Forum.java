//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.22 at 08:51:29 PM MST 
//


package org.smartreaction.boardgamegeek.xml.forum;

import java.math.BigInteger;
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
 *         &lt;element ref="{}threads"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="lastpostdate" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="noposting" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="numposts" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="numthreads" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="termsofuse" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "threads"
})
@XmlRootElement(name = "forum")
public class Forum {

    @XmlElement(required = true)
    protected Threads threads;
    @XmlAttribute(name = "id", required = true)
    protected BigInteger id;
    @XmlAttribute(name = "lastpostdate", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String lastpostdate;
    @XmlAttribute(name = "noposting", required = true)
    protected String noposting;
    @XmlAttribute(name = "numposts", required = true)
    protected String numposts;
    @XmlAttribute(name = "numthreads", required = true)
    protected String numthreads;
    @XmlAttribute(name = "termsofuse", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String termsofuse;
    @XmlAttribute(name = "title", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NCName")
    protected String title;

    /**
     * Gets the value of the threads property.
     * 
     * @return
     *     possible object is
     *     {@link Threads }
     *     
     */
    public Threads getThreads() {
        return threads;
    }

    /**
     * Sets the value of the threads property.
     * 
     * @param value
     *     allowed object is
     *     {@link Threads }
     *     
     */
    public void setThreads(Threads value) {
        this.threads = value;
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
     * Gets the value of the noposting property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoposting() {
        return noposting;
    }

    /**
     * Sets the value of the noposting property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoposting(String value) {
        this.noposting = value;
    }

    /**
     * Gets the value of the numposts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumposts() {
        return numposts;
    }

    /**
     * Sets the value of the numposts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumposts(String value) {
        this.numposts = value;
    }

    /**
     * Gets the value of the numthreads property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumthreads() {
        return numthreads;
    }

    /**
     * Sets the value of the numthreads property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumthreads(String value) {
        this.numthreads = value;
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

}
