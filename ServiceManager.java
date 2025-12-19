import java.util.*; 
public class ServiceManager 
{
    private ArrayList<Service> allServices; 
    private final List<String> CATEGORIES = Arrays.asList("Skin", "Laser", "Hair", "PRP", "Body Contouring", "Other");

    public ServiceManager() 
    {
        allServices = new ArrayList<>();
        loadServices(); 
    }

    private void loadServices() 
    {
        // --- SKIN SERVICES (15) ---
        allServices.add(new Service("S01","Acne Treatment","Skin",2500));
        allServices.add(new Service("S02","Pigmentation Treatment","Skin",3000));
        allServices.add(new Service("S03","Anti-Aging Therapy","Skin",3500));
        allServices.add(new Service("S04","Chemical Peel","Skin",4000));
        allServices.add(new Service("S05","Mole & Wart Removal","Skin",2500));
        allServices.add(new Service("S06","Skin Brightening","Skin",3500));
        allServices.add(new Service("S07","Rosacea Treatment","Skin",3000));
        allServices.add(new Service("S08","Dark Circle Treatment","Skin",2500));
        allServices.add(new Service("S09","Skin Allergy Test","Skin",2500));
        allServices.add(new Service("S10","Hydration Therapy","Skin",2800));
        allServices.add(new Service("S11","Microdermabrasion","Skin",4500));
        allServices.add(new Service("S12","Deep Pore Cleansing","Skin",2200));
        allServices.add(new Service("S13","Sun Damage Repair","Skin",3200));
        allServices.add(new Service("S14","Skin Tightening (HIFU)","Skin",12000));
        allServices.add(new Service("S15","Eczema Relief Facial","Skin",2000));

        // --- HAIR SERVICES (15) ---
        allServices.add(new Service("H01","Hair Loss Therapy","Hair",3000));
        allServices.add(new Service("H02","Dandruff Treatment","Hair",2500));
        allServices.add(new Service("H03","PRP Hair Therapy","Hair",7000));
        allServices.add(new Service("H04","Hair Transplant Consultation","Hair",1500));
        allServices.add(new Service("H05","Scalp Treatment","Hair",2800));
        allServices.add(new Service("H06","Hair Strengthening Therapy","Hair",3000));
        allServices.add(new Service("H07","Follicle Stimulation Therapy","Hair",3500));
        allServices.add(new Service("H08","Hair Coloring Treatment","Hair",3500));
        allServices.add(new Service("H09","Keratin Treatment","Hair",4000));
        allServices.add(new Service("H10","Anti-Hair Fall Therapy","Hair",3200));
        allServices.add(new Service("H11","Scalp Micropigmentation","Hair",15000));
        allServices.add(new Service("H12","Biotin Hair Injections","Hair",5000));
        allServices.add(new Service("H13","Laser Hair Regrowth","Hair",8500));
        allServices.add(new Service("H14","Split End Repair Mask","Hair",2200));
        allServices.add(new Service("H15","Ozone Hair Therapy","Hair",4500));

        // --- LASER SERVICES (15) ---
        allServices.add(new Service("L01","Face Laser","Laser",5000));
        allServices.add(new Service("L02","Half Body Laser","Laser",12000));
        allServices.add(new Service("L03","Full Body Laser","Laser",20000));
        allServices.add(new Service("L04","Laser Scar Removal","Laser",6000));
        allServices.add(new Service("L05","Laser Skin Rejuvenation","Laser",6500));
        allServices.add(new Service("L06","Laser Pigmentation Removal","Laser",7000));
        allServices.add(new Service("L07","Laser Tattoo Removal","Laser",8000));
        allServices.add(new Service("L08","Laser Stretch Mark Treatment","Laser",7500));
        allServices.add(new Service("L09","Laser Vein Treatment","Laser",6500));
        allServices.add(new Service("L10","Laser Acne Treatment","Laser",6000));
        allServices.add(new Service("L11","Carbon Laser Peel","Laser",5500));
        allServices.add(new Service("L12","Fractional CO2 Laser","Laser",18000));
        allServices.add(new Service("L13","Laser Birthmark Removal","Laser",9500));
        allServices.add(new Service("L14","Q-Switched Laser","Laser",11000));
        allServices.add(new Service("L15","Laser Whitening (Underarms)","Laser",4500));

        // --- PRP & AESTHETIC SERVICES (15) ---
        allServices.add(new Service("P01","Botox Injection","PRP",8000));
        allServices.add(new Service("P02","Dermal Fillers","PRP",10000));
        allServices.add(new Service("P03","PRP Facial","PRP",7000));
        allServices.add(new Service("P04","Advanced Chemical Peel","PRP",5500));
        allServices.add(new Service("P05","Lip Augmentation","PRP",8000));
        allServices.add(new Service("P06","Nose Reshaping (Non-Surgical)","PRP",9000));
        allServices.add(new Service("P07","Chin/ Jawline Contouring","PRP",8500));
        allServices.add(new Service("P08","Eyebrow Lift (Non-Surgical)","PRP",7500));
        allServices.add(new Service("P09","Mesotherapy","PRP",6500));
        allServices.add(new Service("P10","Scar Reduction Injection","PRP",6000));
        allServices.add(new Service("P11","Vampire Facial","PRP",12000));
        allServices.add(new Service("P12","Tear Trough Fillers","PRP",9500));
        allServices.add(new Service("P13","Liquid Facelift","PRP",25000));
        allServices.add(new Service("P14","Hyperhidrosis Treatment","PRP",15000));
        allServices.add(new Service("P15","Masseter Botox (Jaw Slimming)","PRP",13000));

        // --- BODY CONTOURING SERVICES (15) ---
        allServices.add(new Service("BC01", "Cryolipolysis (Fat Freezing)", "Body Contouring", 15000));
        allServices.add(new Service("BC02", "Liposuction (Surgical)", "Body Contouring", 55000));
        allServices.add(new Service("BC03", "Non-Surgical Skin Tightening", "Body Contouring", 12000));
        allServices.add(new Service("BC04", "Laser Lipolysis", "Body Contouring", 18000));
        allServices.add(new Service("BC05", "Cellulite Treatment", "Body Contouring", 8000));
        allServices.add(new Service("BC06", "Muscle Toning (EMS)", "Body Contouring", 7500));
        allServices.add(new Service("BC07", "Radiofrequency Body Sculpting", "Body Contouring", 10000));
        allServices.add(new Service("BC08", "Ultrasound Fat Reduction", "Body Contouring", 11000));
        allServices.add(new Service("BC09", "Post-Surgery Massage", "Body Contouring", 4500));
        allServices.add(new Service("BC10", "Lymphatic Drainage Therapy", "Body Contouring", 5000));
        allServices.add(new Service("BC11", "Non-Surgical Buttock Lift", "Body Contouring", 35000));
        allServices.add(new Service("BC12", "Arm Toning Treatment", "Body Contouring", 9500));
        allServices.add(new Service("BC13", "Double Chin Fat Reduction", "Body Contouring", 12000));
        allServices.add(new Service("BC14", "Thermal Body Wrap", "Body Contouring", 6500));
        allServices.add(new Service("BC15", "Cavitation Body Sculpting", "Body Contouring", 8500));

        // --- OTHER SERVICES (15) ---
        allServices.add(new Service("O01","General Consultation","Other",1500));
        allServices.add(new Service("O02","Skin Biopsy","Other",4000));
        allServices.add(new Service("O03","Wart / Skin Tag Removal","Other",2000));
        allServices.add(new Service("O04","Allergy Patch Test","Other",2500));
        allServices.add(new Service("O05","Nail Fungus Treatment","Other",2800));
        allServices.add(new Service("O06","Eczema Treatment","Other",3000));
        allServices.add(new Service("O07","Psoriasis Treatment","Other",3500));
        allServices.add(new Service("O08","Sun Damage Treatment","Other",3000));
        allServices.add(new Service("O09","Cryotherapy","Other",2500));
        allServices.add(new Service("O10","Minor Surgical Procedures","Other",4000));
        allServices.add(new Service("O11","Skin Analysis Report","Other",1000));
        allServices.add(new Service("O12","Steroid Injection (Keloids)","Other",4500));
        allServices.add(new Service("O13","Burn Scar Management","Other",6000));
        allServices.add(new Service("O14","Vitiligo Repigmentation","Other",9000));
        allServices.add(new Service("O15","Fungal Skin Treatment","Other",3200));            
    }

    
    public List<String> getServiceCategories() 
    {
        return Collections.unmodifiableList(CATEGORIES);
    }
    
    public List<Service> getServicesByCategory(String category) 
    {
        ArrayList<Service> categoryServices = new ArrayList<>();
        
        // Basic validation for the category
        if (category == null || !CATEGORIES.contains(category)) 
            {
                return categoryServices; 
            }

        for(Service s : allServices) 
        {
            if(s.getServiceCategory().equalsIgnoreCase(category)) 
            {
                categoryServices.add(s);
            }
        }

        return categoryServices;
    }

    // Getter for all services (for other managers) 
    public ArrayList<Service> getAllServices()
    {
        return allServices;
    }

    // Getter for service list (for GUI)
    public List<Service> getServiceList()
    {
        return allServices;
    }

    // Search by ID 
    public Service searchServiceById(String serviceId) 
    {
        for (Service s : allServices) 
        {
            if (s.getServiceId().equalsIgnoreCase(serviceId)) 
            {
                return s;
            }
        }
        return null;
    }
}