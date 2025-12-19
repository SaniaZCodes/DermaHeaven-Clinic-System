import java.io.Serializable;
import java.util.ArrayList;

public abstract class AestheticPackage implements Serializable, Bookable 
{    
    protected String packageName;
    protected ArrayList<Service> serviceList;
    protected ArrayList<Integer> sessionList; 
    protected double grandPrice; 

    public AestheticPackage() 
    {

    }

    public AestheticPackage(String packageName, ArrayList<Service> serviceList, ArrayList<Integer> sessionList) 
    {
        this.packageName = packageName;
        this.serviceList = serviceList;
        this.sessionList = sessionList;
        calculateGrandPrice(); // Calculate price upon creation
    }

    // Abstract method: Must be implemented by subclasses to define price logic
    public abstract void calculateGrandPrice();

    // Getters 
    public String getPackageName() 
    {
        return packageName;
    }

    public ArrayList<Service> getServiceList() 
    {
        return serviceList;
    }

    public ArrayList<Integer> getSessionList() 
    {
        return sessionList;
    }
    
    // Implementation of Bookable interface methods
    @Override
    public String getItemName() 
    {
        return packageName;
    }

    @Override
    public double getFinalPrice() 
    {
        return grandPrice;
    }
    
    public double getPackageGrandPrice() 
    {
        return grandPrice;
    }
    
    @Override
    public abstract String toString();
}
