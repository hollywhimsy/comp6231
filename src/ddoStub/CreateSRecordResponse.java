
package ddoStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="createSRecordReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "createSRecordReturn"
})
@XmlRootElement(name = "createSRecordResponse")
public class CreateSRecordResponse {

    @XmlElement(required = true)
    protected String createSRecordReturn;

    /**
     * Gets the value of the createSRecordReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateSRecordReturn() {
        return createSRecordReturn;
    }

    /**
     * Sets the value of the createSRecordReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateSRecordReturn(String value) {
        this.createSRecordReturn = value;
    }

}
