import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class SpecialPackage extends AestheticPackage implements Serializable
{
    // A fixed 15% discount for all special packages
    private static final double DISCOUNT_RATE = 0.15; 

    public SpecialPackage() 
    {
        super();
    }

    // Parameterized Constructor: takes name and service list
    public SpecialPackage(String packageName, List<Service> serviceList)
    {
        // Special packages do not use sessions, so we pass an empty list.
        super(packageName, new ArrayList<>(serviceList), new ArrayList<>());
    }

    // Implementation of the abstract method from the base class
    @Override
    public void calculateGrandPrice() 
    {
        double subTotal = 0;
        
        // Calculate the sum of prices
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
        
        // Apply the discount (15% off)
        double discountAmount = subTotal * DISCOUNT_RATE;
        double finalPrice = subTotal - discountAmount;
        
        // Set the final price field inherited from AestheticPackage
        this.grandPrice = finalPrice;
    }
    
    // Override the abstract toString method for detailed output
    @Override
    public String toString() 
    {
        String result = "\n========================================";
        result += "\n\tPackage Information: ";
        result += "\nPackage Name: " + packageName;
        result += "\nType: Special Package (Guaranteed " + (DISCOUNT_RATE * 100) + "% Discount)";
        result += "\n----------------------------------------";
        result += "\nServices Included:\n";

        double subTotal = 0;
        for (Service s : serviceList) 
        {
            result += "- " + s.getServiceName() + " (" + s.getServiceCategory() + ") - Rs. " + String.format("%.2f", s.getServicePrice()) + "\n";
            subTotal += s.getServicePrice();
        }
        
        double discountAmount = subTotal * DISCOUNT_RATE;
        
        result += "\nOriginal Total: Rs. " + String.format("%.2f", subTotal);
        result += "\nDiscount Applied: Rs. " + String.format("%.2f", discountAmount);
        result += "\nGrand Total: **Rs. " + String.format("%.2f", grandPrice) + "**";
        result += "\n========================================";
        return result;
    }
}