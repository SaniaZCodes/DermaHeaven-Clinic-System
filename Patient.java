import java.io.Serializable;
class Patient implements Serializable
{
    private String patientId;
    private String patientName;
    private int patientAge;
    private String patientGender;
    private String patientPhoneNumber;
    private String patientEmail;  
    private String patientPassword; 

    public Patient()
    {

    }

    public Patient(String patientId, String patientName, int patientAge, String patientGender, 
    String patientPhoneNumber, String patientEmail, String patientPassword)
    {
        this.patientId=patientId;
        this.patientName=patientName;
        this.patientAge=patientAge;
        this.patientGender=patientGender;
        this.patientPhoneNumber=patientPhoneNumber;
        this.patientEmail=patientEmail; 
        this.patientPassword=patientPassword;
    }

    // Setters

    public void setPatientId(String patientId)
    {
        this.patientId=patientId;
    }

    public void setPatientName(String patientName)
    {
        this.patientName=patientName;
    }

    public void setPatientAge(int patientAge)
    {
        this.patientAge=patientAge;
    }

    public void setPatientGender(String patientGender)
    {
        this.patientGender=patientGender;
    }

    public void setPatientPhoneNumber(String patientPhoneNumber)
    {
        this.patientPhoneNumber=patientPhoneNumber;
    }

    public void setPatientEmail(String patientEmail) 
    {
        this.patientEmail = patientEmail;
    }

    public void setPatientPassword(String patientPassword) 
    {
        this.patientPassword = patientPassword;
    }

    // Getters 

    public String getPatientId()
    {
        return patientId;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public int getPatientAge()
    {
        return patientAge; 
    }

    public String getPatientGender()
    {
        return patientGender;
    }

    public String getPatientPhoneNumber()
    {
        return patientPhoneNumber;
    }
 
    public String getPatientEmail() 
    {
        return patientEmail;
    }

    public String getContact()
    {
        return patientPhoneNumber;
    }

    public String getEmail() 
    {
        return patientEmail;
    }

    public String getPassword() 
    {
        return patientPassword;
    }

    @Override
    public String toString()
    {
        return 
                "Patient Information: \nID: "+patientId+
                "\nName: "+patientName+
                "\nAge: "+patientAge+
                "\nGender: "+patientGender+
                "\nPhone Number: "+patientPhoneNumber+
                "\nEmail: "+patientEmail; 
    }
}