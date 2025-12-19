import java.util.List;
import java.util.stream.Collectors;

public class DoctorAccessManager 
{
    private AppointmentManager appointmentManager;
    private FeedbackManager feedbackManager;
    private DoctorManager doctorManager;
        
    // Store the logged-in doctor's name 
    private String loggedInDoctorName = null; 

    public DoctorAccessManager(AppointmentManager appointmentManager, FeedbackManager feedbackManager, DoctorManager doctorManager) 
    {
        this.appointmentManager = appointmentManager;
        this.feedbackManager = feedbackManager;
        this.doctorManager = doctorManager;
    }

    public Doctor attemptLogin(String doctorName, String password) 
    {
        // 1. Check if a Doctor object exists for the given name
        Doctor selectedDoctor = doctorManager.getDoctorByName(doctorName);
        
        if (selectedDoctor == null) 
        {
            return null;
        }
        
        // 2. AUTHENTICATION CHECK: Compare entered password with stored password 
        if (password != null && password.equals(selectedDoctor.getPassword())) 
        {
            this.loggedInDoctorName = doctorName;
            return selectedDoctor; 
        } 
        else 
        {
            // Invalid password
            this.loggedInDoctorName = null;
            return null;
        }
    }

    public List<String> getAllDoctorNames() 
    {
        return doctorManager.getDoctorNamesList();
    }
}