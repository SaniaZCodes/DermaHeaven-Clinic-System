import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;

public class MainApplication 
{
    private static ServiceManager serviceManager;
    private static SpecialPackageManager specialPackageManager;
    private static CustomPackageManager customPackageManager;
    private static DoctorManager doctorManager;
    private static AppointmentManager appointmentManager;
    private static FeedbackManager feedbackManager;
    private static PatientManager patientManager;
    private static DoctorAccessManager doctorAccessManager;
    
    public static void main(String[] args) 
    {
        try 
        {
            // Core Data
            doctorManager = new DoctorManager(); 
            serviceManager = new ServiceManager();
            feedbackManager = new FeedbackManager();
            
            // Packages (Depend on ServiceManager)
            specialPackageManager = new SpecialPackageManager(serviceManager);
            customPackageManager = new CustomPackageManager(serviceManager);
            
            // Transactions (Depend on DoctorManager)
            appointmentManager = new AppointmentManager(doctorManager);
            
            // Access
            doctorAccessManager = new DoctorAccessManager(appointmentManager, feedbackManager, doctorManager);
            
            // Patient Manager (Depend on all necessary managers)
            patientManager = new PatientManager(appointmentManager, feedbackManager, doctorManager); 
            
        } 
        catch (Exception e) 
        {
            System.err.println("Fatal Error: Could not initialize core managers or load data.");
            e.printStackTrace();
            return;
        }
        
        // --- 2. Start the Swing application on the Event Dispatch Thread (EDT) ---
        SwingUtilities.invokeLater(() -> 
        {
            createAndShowGUI();
        });
    }
    
    private static void createAndShowGUI() 
    {
        // --- Setup the Main Frame ---
        JFrame frame = new JFrame("DermaHeaven Aesthetic - Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 800)); 
        frame.getContentPane().setBackground(Color.ORANGE);
        // Use CardLayout for switching between main screens
        JPanel mainPanelContainer = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanelContainer.setLayout(cardLayout);
        
        // --- Create the Screens (Panels) ---

        ClinicDashboardPanel clinicDashboard = new ClinicDashboardPanel(mainPanelContainer, cardLayout,
            patientManager, doctorAccessManager, feedbackManager,
            serviceManager, specialPackageManager, customPackageManager,
            appointmentManager, doctorManager);

        // --- Add Panel to the Main Container ---
        mainPanelContainer.add(clinicDashboard, "MAIN_DASHBOARD");
        
        frame.add(mainPanelContainer);
        
        // Finalize setup
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}