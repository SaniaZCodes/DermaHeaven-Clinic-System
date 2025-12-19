import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class PatientManager implements Serializable 
{
    private ArrayList<Patient> patientList; 
    private int patientCounter; 
    private final String FILE_NAME = "patients.ser";

    private final AppointmentManager appointmentManager;
    private final FeedbackManager feedbackManager;
    private final DoctorManager doctorManager; 
    
    @SuppressWarnings("unchecked")
    public PatientManager(AppointmentManager appointmentManager, FeedbackManager feedbackManager, DoctorManager doctorManager) 
    {
        this.appointmentManager = appointmentManager;
        this.feedbackManager = feedbackManager;
        this.doctorManager = doctorManager;

        // Load existing data (loads ArrayList<Patient>)
        ArrayList<?> loadedData = Persistence.load(FILE_NAME);
        if (loadedData != null && !loadedData.isEmpty() && loadedData.get(0) instanceof Patient) 
            {
                this.patientList = (ArrayList<Patient>) loadedData;
            } 
        else 
            {
                this.patientList = new ArrayList<>();
            }

        // 3. Set patientCounter for unique IDs
        patientCounter = 1;
        for (Patient p : patientList) 
            {
                try 
                {
                    // Assuming getPatientId() returns "Pxxx"
                    String idNumStr = p.getPatientId().substring(1); 
                    int idNum = Integer.parseInt(idNumStr);
                    if (idNum >= patientCounter) 
                        {
                            patientCounter = idNum + 1;
                        }
                } 
                catch (Exception e) 
                {
                // Ignore parsing errors
                }
            }
    }

    public Patient attemptLogin(String name, String password) 
    {
        return patientList.stream()
            .filter(p -> p.getPatientName().equalsIgnoreCase(name) && p.getPassword().equals(password))
            .findFirst()
            .orElse(null);
    }
    
    public Patient registerPatient(String name, String password, String contact, String email, int age, String gender)
    {
        if (isPatientRegistered(name))
            {
                return null; // Patient name already taken
            }

        String patientId = "P" + String.format("%03d", patientCounter++);
        Patient newPatient = new Patient(patientId, name, age, gender, contact, email, password);
        patientList.add(newPatient);

        Persistence.save(patientList, FILE_NAME);
        return newPatient;
    }

    // Checks if a patient name is already registered.
    public boolean isPatientRegistered(String name) 
    {
        return patientList.stream().anyMatch(p -> p.getPatientName().equalsIgnoreCase(name));
    }

    public String submitFeedback(String patientName, String doctorName, String serviceName, String comment, double rating) 
    {
        return feedbackManager.submitFeedback(patientName, doctorName, serviceName, comment, rating);
    }
    
    public ArrayList<Patient> getPatientList() 
    {
        return patientList;
    }
}