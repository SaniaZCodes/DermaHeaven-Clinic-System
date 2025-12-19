import java.io.Serializable;

public class Doctor implements Serializable 
{
    private String doctorId;
    private String doctorName;
    private String doctorSpecialization;
    private String doctorContact;
    private String workingDays;
    private String workingHours;
    private String password;
    private double discountRate;
    private int doctorAge;
    private String doctorGender;
    
    // Default constructor (Needed for deserialization)
    public Doctor() 
    {

    }

    // --- Parameterized constructor
    public Doctor(String doctorId, String doctorName, String doctorSpecialization, String workingDays, String workingHours, String doctorContact, String password, int doctorAge, String doctorGender)
    {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSpecialization = doctorSpecialization;
        this.workingDays = workingDays;
        this.workingHours = workingHours;
        this.doctorContact = doctorContact;
        this.password = password;
        this.doctorAge = doctorAge;
        this.doctorGender = doctorGender;
    }

    // --- Constructor for registration (without ID and contact)
    public Doctor(String doctorName, String password, String doctorSpecialization, String workingDays, String workingHours, double discountRate, int doctorAge, String doctorGender)
    {
        this.doctorName = doctorName;
        this.password = password;
        this.doctorSpecialization = doctorSpecialization;
        this.workingDays = workingDays;
        this.workingHours = workingHours;
        this.discountRate = discountRate;
        this.doctorAge = doctorAge;
        this.doctorGender = doctorGender;
    }

    // setters of this class
    public void setDoctorId(String doctorId) 
    {
        this.doctorId = doctorId;
    }

    public void setDoctorName(String doctorName) 
    {
        this.doctorName = doctorName;
    }

    public void setDoctorSpecialization(String doctorSpecialization) 
    {
        this.doctorSpecialization = doctorSpecialization;
    }

    public void setDoctorContact(String doctorContact) 
    {
        this.doctorContact = doctorContact;
    }

    public void setWorkingDays(String workingDays) 
    {
        this.workingDays = workingDays;
    }

    public void setWorkingHours(String workingHours) 
    {
        this.workingHours = workingHours;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    // Getters of this class  
    public String getDoctorId() 
    {
        return doctorId;
    }

    public String getDoctorName() 
    {
        return doctorName;
    }

    public String getDoctorSpecialization() 
    {
        return doctorSpecialization;
    }

    public String getDoctorContact() 
    {
        return doctorContact;
    }

    public String getWorkingDays() 
    {
        return workingDays;
    }

    public String getWorkingHours() 
    {
        return workingHours;
    }
    
    public String getPassword()
    {
        return password;
    }

    public double getDiscountRate()
    {
        return discountRate;
    }

    public void setDiscountRate(double discountRate)
    {
        this.discountRate = discountRate;
    }

    public int getDoctorAge()
    {
        return doctorAge;
    }

    public void setDoctorAge(int doctorAge)
    {
        this.doctorAge = doctorAge;
    }

    public String getDoctorGender()
    {
        return doctorGender;
    }

    public void setDoctorGender(String doctorGender)
    {
        this.doctorGender = doctorGender;
    }
    
    // method to show details
    @Override
    public String toString() 
    {
        return "\n--- Doctor Profile ---" +
                "\nID: " + doctorId +
                "\nName: Dr. " + doctorName +
                "\nSpecialization: " + doctorSpecialization +
                "\nAvailable: " + workingDays + " from " + workingHours +
                "\nContact: " + doctorContact;
    }
}