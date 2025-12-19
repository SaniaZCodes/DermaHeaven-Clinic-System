import java.util.ArrayList;
import java.io.Serializable;

public class CustomPackage extends AestheticPackage implements Serializable 
{    
    // A fixed 15% discount for all custom packages, same as special packages
    private static final double DISCOUNT_RATE = 0.15;

    public CustomPackage()
    {
        // to call AestheticPackage parent class constructor
        super();
    }

    public CustomPackage(String packageName, ArrayList<Service> serviceList)
    {
        super(packageName, serviceList, new ArrayList<>());
        // Price calculated in super constructor via calculateGrandPrice()
    }

    // Overloaded constructor for custom packages that might involve sessions
    public CustomPackage(String packageName, ArrayList<Service> serviceList, ArrayList<Integer> sessionList)
    {
        super(packageName, serviceList, sessionList);
        // Price calculated in super constructor via calculateGrandPrice()
    }

    // Implementation of the abstract method from the base class
    @Override
    public void calculateGrandPrice()
    {
        double subTotal = 0;

        if (serviceList != null)
            {
                for (Service s : serviceList)
                    {
                        if (s != null)
                            {
                                subTotal += s.getServicePrice();
                            }
                    }
            }

        // No fixed discount; doctor's discount will be applied during booking
        this.grandPrice = subTotal;
    }

    // Getter for the discount rate
    public double getDiscountRate()
    {
        return DISCOUNT_RATE;
    }

    // Getter for raw price (subtotal before discount)
    public double getRawPrice()
    {
        double subTotal = 0;
        if (serviceList != null)
        {
            for (Service s : serviceList)
            {
                if (s != null)
                {
                    subTotal += s.getServicePrice();
                }
            }
        }
        return subTotal;
    }

    // Setter for package name
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    @Override
    public String toString()
    {
        // Ensure price is calculated before display
        if (this.grandPrice == 0.0 && !serviceList.isEmpty())
            {
                calculateGrandPrice();
            }

        String result = "\n========================================";
        result += "\n\tPackage Information: ";
        result += "\nPackage Name: " + packageName;
        result += "\nType: Custom Package (Doctor's Discount Applied at Booking)";
        result += "\n----------------------------------------";
        result += "\nServices Included:\n";

        double subTotal = 0;
        for (Service s : serviceList)
            {
                result += "- " + s.getServiceName() + " (Rs. " + String.format("%.2f", s.getServicePrice()) + ")\n";
                subTotal += s.getServicePrice();
            }

        result += "\nSubtotal: Rs. " + String.format("%.2f", subTotal);
        result += "\n(Discount will be applied by selected doctor)";
        result += "\n========================================";
        return result;
    }
}