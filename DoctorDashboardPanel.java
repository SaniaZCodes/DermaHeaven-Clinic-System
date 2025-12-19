import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This panel provides the main interface for a logged-in doctor.
 * It focuses on viewing their scheduled appointments and patient feedback.
 */
public class DoctorDashboardPanel extends JPanel 
{    
    // --- Managers & Core Data ---
    private final JPanel mainPanelContainer;
    private final CardLayout cardLayout;
    private final Doctor loggedInDoctor; // The doctor object currently logged in
    
    // Injected managers
    private final AppointmentManager appointmentManager;
    private final FeedbackManager feedbackManager;
    
    // --- Components ---
    private DefaultTableModel appointmentsTableModel;
    private DefaultTableModel feedbackTableModel;
    private JTabbedPane tabbedPane;

    // --- Constructor ---
    public DoctorDashboardPanel(JPanel mainPanelContainer, CardLayout cardLayout, 
                                Doctor loggedInDoctor, AppointmentManager appointmentManager, 
                                FeedbackManager feedbackManager) 
                                {
                                    this.mainPanelContainer = mainPanelContainer;
                                    this.cardLayout = cardLayout;
                                    this.loggedInDoctor = loggedInDoctor;
                                    this.appointmentManager = appointmentManager;
                                    this.feedbackManager = feedbackManager;

                                    setupLayout();
                                    refreshAppointmentData();
                                    refreshFeedbackData();
                                }

    // --- Layout Setup ---
    private void setupLayout() 
    {
        this.setLayout(new BorderLayout());
        
        // 1. Header (Welcome and Logout)
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, Dr. " + loggedInDoctor.getDoctorName() + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.addActionListener(e -> handleLogout());
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.add(headerPanel, BorderLayout.NORTH);
        
        // 2. Tabbed Pane (Main Content)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        tabbedPane.addTab("📅 Today's Schedule", createScheduleTab());
        tabbedPane.addTab("⭐ Patient Feedbacks", createFeedbackTab());
        
        this.add(tabbedPane, BorderLayout.CENTER);
    }
    
    // --- Tab 1: Schedule (Appointments) ---
    private JPanel createScheduleTab() 
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table Model Setup
        String[] columnNames = {"ID", "Date", "Time", "Patient Name", "Item/Package", "Price", "Status"};
        appointmentsTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        JTable appointmentsTable = new JTable(appointmentsTableModel);
        appointmentsTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Button Panel (Doctor only marks complete, patient handles reschedule/cancel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton completeButton = new JButton("Mark Completed");
        completeButton.addActionListener(e -> handleMarkCompleted(appointmentsTable));

        buttonPanel.add(completeButton);
        
        // Layout
        panel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // --- Tab 2: Feedback Review ---
    private JPanel createFeedbackTab() 
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Display Average Rating
        double avgRating = feedbackManager.getDoctorAverageRating(loggedInDoctor.getDoctorName());
        JLabel ratingLabel = new JLabel(String.format("<html><h2>Your Average Rating: <span style='color: #4CAF50;'>%.2f / 5.0</span></h2></html>", avgRating));
        ratingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(ratingLabel, BorderLayout.NORTH);

        // Table Model Setup
        String[] columnNames = {"Rating", "Patient", "Service", "Comments"};
        feedbackTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };
        JTable feedbackTable = new JTable(feedbackTableModel);
        feedbackTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        feedbackTable.getColumnModel().getColumn(3).setPreferredWidth(400); // Give comments more space
        
        // Layout
        panel.add(new JScrollPane(feedbackTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    // ===============================================
    // --- Data Loaders ---
    // ===============================================

    /**
     * Refreshes the Appointments table with the latest data for the logged-in doctor.
     */
    private void refreshAppointmentData() 
    {
        appointmentsTableModel.setRowCount(0); 
        
        // Get only BOOKED appointments for the doctor's schedule view (or all, depending on design)
        // We get all to show history/full calendar, then filter by date if needed.
        List<Appointment> appts = appointmentManager.getAppointmentsByDoctor(loggedInDoctor.getDoctorName(), false); // Only Booked/Completed
        
        if (appts != null) 
            {
                for (Appointment appt : appts) 
                    {
                        appointmentsTableModel.addRow(new Object[]
                            {
                                appt.getAppointmentId(), 
                                appt.getDate(), 
                                appt.getTime(), 
                                appt.getPatientName(), 
                                appt.getItemName(), 
                                String.format("%.2f", appt.getPrice()),
                                appt.getStatus()
                            });
                    }
            }
    }

    /**
     * Refreshes the Feedback table with data relevant to the logged-in doctor.
     */
    private void refreshFeedbackData() 
    {
        feedbackTableModel.setRowCount(0);
        
        // Use the refactored method to get feedback filtered by doctor name
        List<Feedback> feedbacks = feedbackManager.getFeedbacksForDoctor(loggedInDoctor.getDoctorName());
        
        for (Feedback fb : feedbacks) 
            {
                feedbackTableModel.addRow(new Object[]
                    {
                        String.format("%.1f / 5.0", fb.getRating()), 
                        fb.getPatientName(), 
                        fb.getServiceName(), 
                        fb.getFeedbackText()
                    });
            }
    }

    // ===============================================
    // --- Action Handlers ---
    // ===============================================
    
    private void handleMarkCompleted(JTable appointmentsTable) 
    {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) 
            {
                JOptionPane.showMessageDialog(this, "Please select an appointment to mark as completed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        String apptId = (String) appointmentsTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) appointmentsTable.getValueAt(selectedRow, 6);

        if (!currentStatus.equalsIgnoreCase("BOOKED")) 
            {
                JOptionPane.showMessageDialog(this, "Only BOOKED appointments can be marked completed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Mark Appointment ID: " + apptId + " as COMPLETED?", "Confirm Completion", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) 
            {
                // Call refactored Manager method
                String message = appointmentManager.markAppointmentCompleted(apptId);
            
                // Display result and refresh data
                JOptionPane.showMessageDialog(this, message);
                refreshAppointmentData();
            }
    }

    private void handleLogout() 
    {
        // Find the Login Screen card name and switch back
        cardLayout.show(mainPanelContainer, "LOGIN_SCREEN");
        
        // Clean up the dashboard instance (as done in Patient Dashboard)
        Component[] components = mainPanelContainer.getComponents();
        for (Component comp : components) 
            {
                if (comp == this) 
                    {
                        mainPanelContainer.remove(comp);
                        break;
                    }
            }
        mainPanelContainer.revalidate();
        mainPanelContainer.repaint();
    }
}