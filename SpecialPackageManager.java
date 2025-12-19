import java.util.*;

public class SpecialPackageManager
{
    private ServiceManager serviceManager;

    public SpecialPackageManager(ServiceManager serviceManager)
    {
        this.serviceManager = serviceManager;
    }

    // Generate packages for a specific category using predefined service IDs
    public List<SpecialPackage> getPackagesByCategory(String category)
    {
        List<String> silverIds = getSilverServiceIds(category);
        List<String> goldenIds = getGoldenServiceIds(category);
        List<String> platinumIds = getPlatinumServiceIds(category);

        List<Service> silverServices = getServicesByIds(silverIds);
        List<Service> goldenServices = getServicesByIds(goldenIds);
        List<Service> platinumServices = getServicesByIds(platinumIds);

        SpecialPackage silver = new SpecialPackage(category + " Silver Package", silverServices);
        SpecialPackage golden = new SpecialPackage(category + " Golden Package", goldenServices);
        SpecialPackage platinum = new SpecialPackage(category + " Platinum Package", platinumServices);

        return Arrays.asList(silver, golden, platinum);
    }

    // Get all categories
    public List<String> getCategories()
    {
        return serviceManager.getServiceCategories();
    }

    // For backward compatibility, return empty list
    public List<SpecialPackage> getAllPackages()
    {
        return new ArrayList<>();
    }

    public SpecialPackage getSpecialPackageByName(String packageName)
    {
        // Since packages are now predefined, this method is less useful
        // But kept for compatibility
        return null;
    }

    private List<Service> getServicesByIds(List<String> ids)
    {
        List<Service> services = new ArrayList<>();
        for (String id : ids)
        {
            Service service = serviceManager.searchServiceById(id);
            if (service != null)
            {
                services.add(service);
            }
        }
        return services;
    }

    private List<String> getSilverServiceIds(String category)
    {
        switch (category.toLowerCase())
        {
            case "skin":
                return Arrays.asList("S01", "S02", "S10");
            case "hair":
                return Arrays.asList("H01", "H02", "H05");
            case "laser":
                return Arrays.asList("L01", "L04", "L11");
            case "prp":
                return Arrays.asList("P03", "P04", "P09");
            case "body contouring":
                return Arrays.asList("BC05", "BC09", "BC14");
            case "other":
                return Arrays.asList("O01", "O11", "O15");
            default:
                return new ArrayList<>();
        }
    }

    private List<String> getGoldenServiceIds(String category)
    {
        switch (category.toLowerCase())
        {
            case "skin":
                return Arrays.asList("S01", "S02", "S03", "S06", "S11");
            case "hair":
                return Arrays.asList("H01", "H03", "H06", "H10", "H15");
            case "laser":
                return Arrays.asList("L01", "L02", "L05", "L08", "L15");
            case "prp":
                return Arrays.asList("P01", "P02", "P03", "P05", "P10");
            case "body contouring":
                return Arrays.asList("BC01", "BC03", "BC06", "BC07", "BC15");
            case "other":
                return Arrays.asList("O01", "O03", "O04", "O08", "O09");
            default:
                return new ArrayList<>();
        }
    }

    private List<String> getPlatinumServiceIds(String category)
    {
        switch (category.toLowerCase())
        {
            case "skin":
                return Arrays.asList("S03", "S04", "S06", "S11", "S14", "S15", "S12");
            case "hair":
                return Arrays.asList("H01", "H03", "H04", "H07", "H09", "H11", "H13");
            case "laser":
                return Arrays.asList("L01", "L03", "L05", "L06", "L07", "L12", "L14");
            case "prp":
                return Arrays.asList("P01", "P02", "P11", "P12", "P13", "P14", "P15");
            case "body contouring":
                return Arrays.asList("BC01", "BC02", "BC04", "BC08", "BC11", "BC12", "BC13");
            case "other":
                return Arrays.asList("O01", "O02", "O07", "O10", "O12", "O13", "O14");
            default:
                return new ArrayList<>();
        }
    }
}
