import java.io.Serializable;
public class Service implements Serializable, Bookable
{
    private String serviceId;
    private String serviceName;
    private String serviceCategory;
    private double servicePrice; 
    
    // default constructor
    public Service()
    {

    }

    // parameterized constructor
    public Service(String serviceId, String serviceName, String serviceCategory, double servicePrice)
    {
        this.serviceId=serviceId;
        this.serviceName=serviceName;
        this.serviceCategory=serviceCategory;
        this.servicePrice=servicePrice;
    }

    // setters of this class
    public void setServiceId(String serviceId)
    {
        this.serviceId=serviceId;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName=serviceName;
    }

    public void setServiceCategory(String serviceCategory)
    {
        this.serviceCategory=serviceCategory;
    }

    public void setServicePrice(double servicePrice)
    {
        this.servicePrice=servicePrice;
    }

    // getters of this class
    public String getServiceId()
    {
        return serviceId;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public String getServiceCategory()
    {
        return serviceCategory;
    }

    public double getServicePrice()
    {
        return servicePrice;
    }

    public double getPrice()
    {
        return servicePrice;
    }
    
    // IMPLEMENTATION OF BOOKABLE INTERFACE
    @Override
    public String getItemName()
    {
        return this.serviceName;
    }

    @Override
    public double getFinalPrice()
    {
        return this.servicePrice; 
    }

    // method to show details
    @Override
    public String toString()
    {
        return
        "Service Information:\nID: "+serviceId+
        "\nName: "+serviceName+
        "\nCategory: "+serviceCategory+
        "\nPrice: Rs. "+String.format("%.2f", servicePrice);
    }
}