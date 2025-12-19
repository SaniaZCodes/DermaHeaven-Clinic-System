import java.util.*;

public class CustomPackageManager 
{
    private ArrayList<CustomPackage> customPackageList;
    private ServiceManager serviceManager;
    private final String FILE_NAME = "custom_packages.ser"; 

    @SuppressWarnings("unchecked") 
    public CustomPackageManager(ServiceManager serviceManager) 
    {
        this.serviceManager = serviceManager;
        customPackageList = Persistence.load(FILE_NAME); 
        if (customPackageList == null) 
            {
                customPackageList = new ArrayList<>();
            }
    }

    public String saveNewCustomPackage(String packageName, List<Service> selectedServices) 
    {
        // 1. INPUT VALIDATION
        if (packageName == null || packageName.trim().isEmpty()) 
            {
                return "Error: Package name cannot be empty.";
            }
        if (selectedServices == null || selectedServices.isEmpty()) 
            {
                return "Error: A custom package must include at least one service.";
            }
        
        // 2. CREATION LOGIC
        // Ensure services are of the correct type (though the GUI should handle this)
        ArrayList<Service> servicesToSave = new ArrayList<>(selectedServices); 
        
        // Assuming CustomPackage constructor handles discount calculation and price
        CustomPackage newPackage = new CustomPackage(packageName, servicesToSave); 
        
        // 3. PERSISTENCE LOGIC 
        customPackageList.add(newPackage);
        
        // Assuming Persistence.save handles the serialization correctly
        Persistence.save(customPackageList, FILE_NAME); 
        
        return "Success: Custom package '" + packageName + "' created and saved! Final Price: Rs. " + String.format("%.2f", newPackage.getPackageGrandPrice());
    }

    public List<CustomPackage> getAllCustomPackages() 
    {
        return customPackageList;
    }

    public CustomPackage getCustomPackageByName(String packageName) 
    {
        for (CustomPackage pkg : customPackageList) 
            {
                if (pkg.getPackageName().equalsIgnoreCase(packageName)) 
                    {
                        return pkg;
                    }
            }
        return null;
    }

    // Method to create a temporary custom package without saving
    public CustomPackage createTemporaryCustomPackage(ArrayList<Service> services) 
    {
        CustomPackage tempPackage = new CustomPackage("Temporary", services);
        return tempPackage;
    }

    // Method to save a custom package
    public CustomPackage saveCustomPackage(CustomPackage customPackage) 
    {
        customPackageList.add(customPackage);
        Persistence.save(customPackageList, FILE_NAME);
        return customPackage;
    }
}