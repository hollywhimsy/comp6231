
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
 *         &lt;element name="transferRecordReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "transferRecordReturn"
})
@XmlRootElement(name = "transferRecordResponse")
public class TransferRecordResponse {

    @XmlElement(required = true)
    protected String transferRecordReturn;

    /**
     * Gets the value of the transferRecordReturn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferRecordReturn() {
        return transferRecordReturn;
    }

    /**
     * Sets the value of the transferRecordReturn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferRecordReturn(String value) {
        this.transferRecordReturn = value;
    }

}
