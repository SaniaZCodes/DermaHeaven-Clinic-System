import java.io.Serializable;
class Feedback implements Serializable
{
    private String feedbackId;
    private String patientName;
    private String serviceName;
    private String feedbackText; 
    private double rating;
    private String doctorName; 

    // default constructor
    public Feedback()
    {

    }

    // PARAMETERIZED CONSTRUCTOR ---
    public Feedback(String feedbackId, String patientName, String doctorName, String serviceName, String feedbackText, double rating)
    {
        this.feedbackId=feedbackId;
        this.patientName=patientName;
        this.doctorName=doctorName; 
        this.serviceName=serviceName;
        this.feedbackText=feedbackText;
        this.rating=rating;
    }
    
    // --- Getters ---

    public String getFeedbackId() 
    {
        return feedbackId;
    }

    public double getRating() 
    {
        return rating;
    }
    
    public String getPatientName() 
    {
        return patientName;
    }

    public String getDoctorName() 
    {
        return doctorName;
    }
    
    public String getServiceName() 
    {
        return serviceName;
    }

    public String getFeedbackText() 
    {
        return feedbackText;
    }


    // Setters 

    public void setFeedbackId(String feedbackId) 
    {
        this.feedbackId=feedbackId;
    }

    public void setPatientName(String patientName) 
    {
        this.patientName=patientName;
    }

    public void setServiceName(String serviceName) 
    {
        this.serviceName=serviceName;
    }

    public void setFeedbackText(String feedbackText) 
    {
        this.feedbackText=feedbackText;
    }

    public void setRating(double rating) 
    {
        this.rating=rating;
    }

    // method to show details
    @Override
    public String toString()
    {
        return "Feedback ID: "+feedbackId+
                "\nPatient Name: "+patientName+
                "\nDoctor Name: "+doctorName+ 
                "\nService Name: "+serviceName+
                "\nRating: "+String.format("%.1f", rating)+"/5"+
                "\nFeedback: "+feedbackText;
    }
}