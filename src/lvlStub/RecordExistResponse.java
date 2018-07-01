
package lvlStub;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="recordExistReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "recordExistReturn"
})
@XmlRootElement(name = "recordExistResponse")
public class RecordExistResponse {

    protected boolean recordExistReturn;

    /**
     * Gets the value of the recordExistReturn property.
     * 
     */
    public boolean isRecordExistReturn() {
        return recordExistReturn;
    }

    /**
     * Sets the value of the recordExistReturn property.
     * 
     */
    public void setRecordExistReturn(boolean value) {
        this.recordExistReturn = value;
    }

}
