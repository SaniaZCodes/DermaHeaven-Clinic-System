import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * The main interface for a logged-in patient, organized by JTabbedPane.
 * Handles all patient-facing interactions using the refactored managers.
 */
public class PatientDashboardPanel extends JPanel 
{
    
    // --- Managers & Core Data ---
    private final JPanel mainPanelContainer;
    private final CardLayout cardLayout;
    private final Patient loggedInPatient; // The patient object currently logged in
    
    // All injected managers
    private final PatientManager patientManager;
    private final ServiceManager serviceManager;
    private final SpecialPackageManager specialPackageManager;
    private final CustomPackageManager customPackageManager;
    private final AppointmentManager appointmentManager;
    private final FeedbackManager feedbackManager;
    private final DoctorManager doctorManager;
    
    // --- Components ---
    private JTabbedPane tabbedPane;
    private DefaultTableModel appointmentsTableModel;
    private JTable appointmentsTable;
    private JSpinner ratingSpinner;

    // --- Constructor ---
    public PatientDashboardPanel(JPanel mainPanelContainer, CardLayout cardLayout, 
                                 Patient loggedInPatient, PatientManager patientManager, 
                                 ServiceManager serviceManager, SpecialPackageManager specialPackageManager, 
                                 CustomPackageManager customPackageManager, AppointmentManager appointmentManager, 
                                 FeedbackManager feedbackManager, DoctorManager doctorManager) 
                                {
                                    this.mainPanelContainer = mainPanelContainer;
                                    this.cardLayout = cardLayout;
                                    this.loggedInPatient = loggedInPatient;
                                    this.patientManager = patientManager;
                                    this.serviceManager = serviceManager;
                                    this.specialPackageManager = specialPackageManager;
                                    this.customPackageManager = customPackageManager;
                                    this.appointmentManager = appointmentManager;
                                    this.feedbackManager = feedbackManager;
                                    this.doctorManager = doctorManager;

                                    setupLayout();
                                    refreshAppointmentData(); // Initial data load
                                }

    // --- Layout Setup ---
    private void setupLayout() 
    {
        this.setLayout(new BorderLayout());
        
        // 1. Header (Welcome and Logout)
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInPatient.getPatientName() + "!", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.addActionListener(this::handleLogout);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.add(headerPanel, BorderLayout.NORTH);
        
        // 2. Tabbed Pane (Main Content)
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 16));
        
        tabbedPane.addTab("🗓️ Appointments", createAppointmentsTab());
        tabbedPane.addTab("✨ Services & Packages", createServicesTab());
        tabbedPane.addTab("📝 Submit Feedback", createFeedbackTab());
        tabbedPane.addTab("👤 Profile", createProfileTab());

        this.add(tabbedPane, BorderLayout.CENTER);
    }
    
    // --- Tab 1: Appointments ---
    private JPanel createAppointmentsTab() 
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Table Model Setup
        String[] columnNames = {"ID", "Date", "Time", "Doctor", "Item", "Price", "Status"};
        appointmentsTableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; // Users can't edit cells directly
            }
        };
        appointmentsTable = new JTable(appointmentsTableModel);
        appointmentsTable.setFont(new Font("Monospaced", Font.PLAIN, 12));
        appointmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton rescheduleButton = new JButton("Reschedule Appointment");
        JButton cancelButton = new JButton("Cancel Appointment");
        
        rescheduleButton.addActionListener(this::handleReschedule);
        cancelButton.addActionListener(this::handleCancel);

        buttonPanel.add(rescheduleButton);
        buttonPanel.add(cancelButton);
        
        // Layout
        panel.add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // --- Tab 2: Services & Packages ---
    private JTabbedPane createServicesTab() 
    {
        JTabbedPane servicesTab = new JTabbedPane();
        servicesTab.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        servicesTab.addTab("Available Services", createServiceViewerPanel());
        servicesTab.addTab("Special Packages", createSpecialPackagePanel());
        servicesTab.addTab("Custom Package Builder", createCustomPackageCreationPanel());
        
        return servicesTab;
    }
    
    // --- Sub-Panel for Services & Packages (Service Viewer) ---
    private JPanel createServiceViewerPanel() 
    {
        JPanel panel = new JPanel(new CardLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Category Selection Panel
        JPanel categoryPanel = createCategorySelectionPanel(panel);
        panel.add(categoryPanel, "CATEGORY_SELECTION");

        // Service Selection Panel (initially empty)
        JPanel servicePanel = createServiceSelectionPanel(panel);
        panel.add(servicePanel, "SERVICE_SELECTION");

        return panel;
    }

    private JPanel createCategorySelectionPanel(JPanel parentPanel) 
    {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Select Service Category"));

        List<String> categories = serviceManager.getServiceCategories();
        for (String category : categories) 
            {
                JButton categoryButton = new JButton(category);
                categoryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                categoryButton.setPreferredSize(new Dimension(200, 50));
                categoryButton.addActionListener(e -> {
                showServicesForCategory(parentPanel, category);
            });
            panel.add(categoryButton);
        }

        return panel;
    }

    private JPanel createServiceSelectionPanel(JPanel parentPanel) 
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Select a Service"));

        String[] columnNames = {"Service Name", "Price (Rs.)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton backButton = new JButton("Back to Categories");
        backButton.addActionListener(e -> 
            {
                CardLayout cl = (CardLayout) parentPanel.getLayout();
                cl.show(parentPanel, "CATEGORY_SELECTION");
            });

        JButton bookButton = new JButton("Book Selected Service");
        bookButton.addActionListener(e -> 
            {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) 
                    {
                        String serviceName = (String) table.getValueAt(selectedRow, 0);
                        Service selectedService = serviceManager.getServiceList().stream()
                                                .filter(s -> s.getServiceName().equals(serviceName))
                                                .findFirst().orElse(null);
                        if (selectedService != null) 
                            {
                                new BookableItemDialog(
                                                        SwingUtilities.getWindowAncestor(this),
                                                        selectedService,
                                                        doctorManager,
                                                        (date, time, doctorName, price) ->
                                                        {
                                                            String message = appointmentManager.bookAppointment(
                                                            loggedInPatient.getPatientName(),
                                                            doctorName,
                                                            selectedService,
                                                            date,
                                                            time);
                                                            JOptionPane.showMessageDialog(this, message);
                                                            refreshAppointmentData();
                                                            tabbedPane.setSelectedIndex(0);
                                                        }
                                                        );
                            }
                    } 
                else 
                    {
                        JOptionPane.showMessageDialog(this, "Please select a service to book.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Store table model for later updates
        panel.putClientProperty("tableModel", model);

        return panel;
    }

    private void showServicesForCategory(JPanel parentPanel, String category) 
    {
        List<Service> services = serviceManager.getServicesByCategory(category);
        DefaultTableModel model = (DefaultTableModel) ((JPanel) parentPanel.getComponent(1)).getClientProperty("tableModel");

        model.setRowCount(0);
        for (Service svc : services) 
            {
                model.addRow(new Object[]
                    {
                        svc.getServiceName(), String.format("%.2f", svc.getPrice())
                    }
                            );
            }

        CardLayout cl = (CardLayout) parentPanel.getLayout();
        cl.show(parentPanel, "SERVICE_SELECTION");
    }
    
    // --- Sub-Panel for Services & Packages (Special Packages) ---
    private JPanel createSpecialPackagePanel()
    {
        JPanel panel = new JPanel(new CardLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Category Selection Panel
        JPanel categoryPanel = createSpecialPackageCategoryPanel(panel);
        panel.add(categoryPanel, "CATEGORY_SELECTION");

        // Package Selection Panel (initially empty)
        JPanel packagePanel = createSpecialPackageSelectionPanel(panel);
        panel.add(packagePanel, "PACKAGE_SELECTION");

        return panel;
    }

    private JPanel createSpecialPackageCategoryPanel(JPanel parentPanel)
    {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Select Category for Special Packages"));

        List<String> categories = specialPackageManager.getCategories();
        for (String category : categories)
            {
                JButton categoryButton = new JButton(category);
                categoryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                categoryButton.setPreferredSize(new Dimension(200, 50));
                categoryButton.addActionListener(e -> showPackagesForCategory(parentPanel, category));
                panel.add(categoryButton);
            }

        return panel;
    }

    private JPanel createSpecialPackageSelectionPanel(JPanel parentPanel)
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Select a Special Package"));

        String[] columnNames = {"Package Name", "Final Price (Rs.)", "Discount (%)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTextArea detailsArea = new JTextArea(5, 50);
        detailsArea.setEditable(false);
        detailsArea.setText("Select a package from the table above to view details.");

        JButton backButton = new JButton("Back to Categories");
        backButton.addActionListener(e ->
            {
                CardLayout cl = (CardLayout) parentPanel.getLayout();
                cl.show(parentPanel, "CATEGORY_SELECTION");
            });

        JButton bookButton = new JButton("Book Selected Special Package");
        bookButton.addActionListener(e ->
            {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1)
                    {
                        List<SpecialPackage> packages = (List<SpecialPackage>) ((JPanel) parentPanel.getComponent(1)).getClientProperty("currentPackages");
                        String category = (String) ((JPanel) parentPanel.getComponent(1)).getClientProperty("currentCategory");
                        if (packages != null && selectedRow < packages.size())
                            {
                                SpecialPackage selectedPackage = packages.get(selectedRow);
                                new SpecialPackageBookingDialog(
                                    SwingUtilities.getWindowAncestor(this),
                                    selectedPackage,
                                    category,
                                    doctorManager,
                                    (String date, String time, String doctorName, double price) ->
                                    {
                                        String message = appointmentManager.bookAppointment(
                                            loggedInPatient.getPatientName(),
                                            doctorName,
                                            selectedPackage,
                                            date,
                                            time);
                                        JOptionPane.showMessageDialog(this, message);
                                        refreshAppointmentData();
                                        tabbedPane.setSelectedIndex(0);
                                    }
                                );
                            }
                    }
                else
                    {
                        JOptionPane.showMessageDialog(this, "Please select a package to book.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                    }
            });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(backButton);
        buttonPanel.add(bookButton);

        // Listener to display package details
        table.getSelectionModel().addListSelectionListener(e ->
            {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1)
                    {
                        List<SpecialPackage> packages = (List<SpecialPackage>) ((JPanel) parentPanel.getComponent(1)).getClientProperty("currentPackages");
                        if (packages != null && table.getSelectedRow() < packages.size())
                            {
                                SpecialPackage selected = packages.get(table.getSelectedRow());
                                detailsArea.setText(selected.toString());
                            }
                    }
            });

        panel.add(new JScrollPane(table), BorderLayout.NORTH);
        panel.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Store table model for later updates
        panel.putClientProperty("tableModel", model);

        return panel;
    }

        private void showPackagesForCategory(JPanel parentPanel, String category)
    {
        List<SpecialPackage> packages = specialPackageManager.getPackagesByCategory(category);
        DefaultTableModel model = (DefaultTableModel) ((JPanel) parentPanel.getComponent(1)).getClientProperty("tableModel");

        model.setRowCount(0);
        for (SpecialPackage pkg : packages)
            {
                model.addRow(new Object[]{pkg.getPackageName(), String.format("%.2f", pkg.getFinalPrice()), "15.0"});
            }

        // Store current packages and category for booking
        ((JPanel) parentPanel.getComponent(1)).putClientProperty("currentPackages", packages);
        ((JPanel) parentPanel.getComponent(1)).putClientProperty("currentCategory", category);

        CardLayout cl = (CardLayout) parentPanel.getLayout();
        cl.show(parentPanel, "PACKAGE_SELECTION");
    }
    
    // --- Sub-Panel for Services & Packages (Custom Package Builder) ---
    private JPanel createCustomPackageCreationPanel() 
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Create a dedicated helper panel for complexity
        CustomPackageCreationPanel builderPanel = new CustomPackageCreationPanel(
                                                                                    serviceManager, 
                                                                                    customPackageManager, 
                                                                                    appointmentManager, 
                                                                                    doctorManager, 
                                                                                    loggedInPatient, 
                                                                                    this
                                                                                );

        panel.add(builderPanel, BorderLayout.CENTER);

        return panel;
    }

    // --- Tab 3: Submit Feedback ---
    private JPanel createFeedbackTab() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Submit Patient Feedback"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Components
        JComboBox<String> doctorComboBox = new JComboBox<>(doctorManager.getDoctorNamesList().toArray(new String[0]));
        JTextField serviceField = new JTextField(20);
        JTextArea commentArea = new JTextArea(5, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5.0, 1.0, 5.0, 0.5));
        
        // Labels
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Doctor:"), gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Service/Item Name:"), gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Rating (1.0-5.0):"), gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Comments:"), gbc);

        // Inputs
        gbc.gridx = 1; gbc.gridy = 0; panel.add(doctorComboBox, gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(serviceField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(ratingSpinner, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weighty = 1.0; panel.add(commentScrollPane, gbc); // Take up more space

        // Button
        JButton submitButton = new JButton("Submit Feedback");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0;
        panel.add(submitButton, gbc);
        
        submitButton.addActionListener(e -> handleSubmitFeedback(doctorComboBox, serviceField, (Double) ratingSpinner.getValue(), commentArea));

        return panel;
    }
    
    // --- Tab 4: Profile ---
    private JPanel createProfileTab() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.5;

        // Display Patient Details (assuming Patient class has these getters)
        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("<html><b>Patient ID:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(new JLabel(loggedInPatient.getPatientId()), gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("<html><b>Name:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(new JLabel(loggedInPatient.getPatientName()), gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("<html><b>Contact:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(new JLabel(loggedInPatient.getContact()), gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("<html><b>Email:</b></html>"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(new JLabel(loggedInPatient.getEmail()), gbc);

        // Add a filler component to push content to the top
        gbc.gridx = 0; gbc.gridy = 4; gbc.weighty = 1.0; panel.add(new JPanel(), gbc);

        return panel;
    }
    
    // --- Data and Action Handlers ---

    /**
     * Refreshes the Appointments table with the latest data from the AppointmentManager.
     */
    public void refreshAppointmentData() 
    {
        appointmentsTableModel.setRowCount(0); // Clear existing data
        
        // Get all appointments for the logged-in patient (true = include all statuses)
        List<Appointment> appts = appointmentManager.getAppointmentsByPatient(loggedInPatient.getPatientName(), true);
        
        if (appts != null) 
            {
                for (Appointment appt : appts) 
                    {
                        appointmentsTableModel.addRow(new Object[]
                            {
                                appt.getAppointmentId(), 
                                appt.getDate(), 
                                appt.getTime(), 
                                appt.getDoctorName(), 
                                appt.getItemName(), 
                                String.format("%.2f", appt.getPrice()),
                                appt.getStatus()
                            });
                    }
            }
    }

    private void handleLogout(ActionEvent e) 
    {
        // Find the Login Screen card name and switch back
        cardLayout.show(mainPanelContainer, "LOGIN_SCREEN");
        
        // Clean up the dashboard instance to free resources (optional but recommended)
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
    
    private void handleReschedule(ActionEvent e) 
    {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) 
            {
                JOptionPane.showMessageDialog(this, "Please select an appointment to reschedule.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        String apptId = (String) appointmentsTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) appointmentsTable.getValueAt(selectedRow, 6);

        if (!currentStatus.equalsIgnoreCase("BOOKED")) 
            {
                JOptionPane.showMessageDialog(this, "Only BOOKED appointments can be rescheduled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        // Custom Dialog for new Date/Time
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this));
        dialog.setTitle("Reschedule Appointment " + apptId);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField dateField = new JTextField("DD-MM-YYYY");
        JTextField timeField = new JTextField("HH:MM");
        
        panel.add(new JLabel("New Date (DD-MM-YYYY):"));
        panel.add(dateField);
        panel.add(new JLabel("New Time (HH:MM):"));
        panel.add(timeField);

        int result = JOptionPane.showConfirmDialog(dialog, panel, "Enter New Details", JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) 
            {
                String newDate = dateField.getText().trim();
                String newTime = timeField.getText().trim();
            
                // Validation (minimal, relying on Manager's internal check)
                if (!newDate.matches("\\d{2}-\\d{2}-\\d{4}") || !newTime.matches("\\d{2}:\\d{2}")) 
                    {
                        JOptionPane.showMessageDialog(this, "Invalid date or time format. Use DD-MM-YYYY and HH:MM.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

            // Call refactored Manager method
            String message = appointmentManager.rescheduleAppointment(apptId, newDate, newTime);
            
            // Display result and refresh data
            JOptionPane.showMessageDialog(this, message);
            refreshAppointmentData();
        }
    }
    
    private void handleCancel(ActionEvent e) 
    {
        int selectedRow = appointmentsTable.getSelectedRow();
        if (selectedRow == -1) 
            {
                JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        String apptId = (String) appointmentsTable.getValueAt(selectedRow, 0);
        String currentStatus = (String) appointmentsTable.getValueAt(selectedRow, 6);

        if (!currentStatus.equalsIgnoreCase("BOOKED")) 
            {
                JOptionPane.showMessageDialog(this, "Only BOOKED appointments can be cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to CANCEL Appointment ID: " + apptId + "?", 
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) 
            {
                // Call refactored Manager method
                String message = appointmentManager.cancelAppointment(apptId);
            
                // Display result and refresh data
                JOptionPane.showMessageDialog(this, message);
                refreshAppointmentData();
            }
    }
    
    private void handleBookPackage(JTable table, List<SpecialPackage> packages) 
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) 
            {
                JOptionPane.showMessageDialog(this, "Please select a package to book.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        SpecialPackage selectedPackage = packages.get(selectedRow);
        
        // Use a helper method (BookableItemDialog) to capture date/time and doctor
        new BookableItemDialog(
            SwingUtilities.getWindowAncestor(this),
            selectedPackage,
            doctorManager,
            (date, time, doctorName, price) ->
            {
                // Lambda function executed upon successful booking details entry
                String message = appointmentManager.bookAppointment(
                    loggedInPatient.getPatientName(),
                    doctorName,
                    selectedPackage,
                    date,
                    time);

                JOptionPane.showMessageDialog(this, message);
                refreshAppointmentData(); // Update appointments tab
                tabbedPane.setSelectedIndex(0); // Switch to appointments tab
            }
        );
    }
    
    private void handleSubmitFeedback(JComboBox<String> doctorComboBox, JTextField serviceField, Double rating, JTextArea commentArea) 
    {
        String doctorName = (String) doctorComboBox.getSelectedItem();
        String serviceName = serviceField.getText().trim();
        String comment = commentArea.getText().trim();
        
        if (serviceName.isEmpty() || comment.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please enter the Service Name and your Comments.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        // Call the refactored Manager method
        String message = feedbackManager.submitFeedback(
                                                            loggedInPatient.getPatientName(), 
                                                            doctorName, 
                                                            serviceName, 
                                                            comment, 
                                                            rating
                                                        );
        
        // Display result and clear fields
        JOptionPane.showMessageDialog(this, message);
        serviceField.setText("");
        commentArea.setText("");
        ratingSpinner.setValue(5.0); // Reset rating
    }
    
    // ===============================================
    // --- INNER CLASS: Custom Package Creation Panel ---
    // ===============================================

    /**
     * Handles the complex workflow of selecting services, building, and booking a Custom Package.
     */
    private class CustomPackageCreationPanel extends JPanel
    {
        private final ServiceManager serviceManager;
        private final CustomPackageManager customPackageManager;
        private final AppointmentManager appointmentManager;
        private final DoctorManager doctorManager;
        private final Patient loggedInPatient;
        private final PatientDashboardPanel parentPanel;

        private DefaultListModel<Service> selectedServicesModel;
        private JList<Service> selectedServicesList;
        private JTextArea priceDisplayArea;
        private CustomPackage currentCustomPackage;
        private JPanel serviceSelectionPanel;
        private CardLayout serviceCardLayout;
        private String selectedCategory;
        private boolean categoryLocked = false;
        private JTable doctorTable;
        private DefaultTableModel doctorTableModel;
        private JPanel doctorSelectionPanel;

        public CustomPackageCreationPanel(ServiceManager sm, CustomPackageManager cpm, AppointmentManager am, DoctorManager dm, Patient p, PatientDashboardPanel pp) 
        {
            this.serviceManager = sm;
            this.customPackageManager = cpm;
            this.appointmentManager = am;
            this.doctorManager = dm;
            this.loggedInPatient = p;
            this.parentPanel = pp;

            setLayout(new BorderLayout(15, 15));
            setBorder(new EmptyBorder(15, 15, 15, 15));

            // --- 1. Service Selection (West) ---
            serviceSelectionPanel = createServiceSelectionPanel();
            add(serviceSelectionPanel, BorderLayout.WEST);

            // --- 2. Builder/Price Display (Center) ---
            JPanel builderPanel = createBuilderPanel();
            add(builderPanel, BorderLayout.CENTER);

            // --- Initial Calculation ---
            calculatePrice();
        }

        private JPanel createServiceSelectionPanel() 
        {
            JPanel panel = new JPanel();
            serviceCardLayout = new CardLayout();
            panel.setLayout(serviceCardLayout);
            panel.setBorder(BorderFactory.createTitledBorder("Select Services"));
            panel.setPreferredSize(new Dimension(300, 400));

            // Category Selection Panel
            JPanel categoryPanel = createCategorySelectionPanel();
            panel.add(categoryPanel, "CATEGORY_SELECTION");

            // Service Selection Panel (initially empty)
            JPanel servicePanel = createServiceTablePanel();
            panel.add(servicePanel, "SERVICE_SELECTION");

            // Default to CATEGORY_SELECTION
            serviceCardLayout.show(panel, "CATEGORY_SELECTION");

            return panel;
        }

        private JPanel createCategorySelectionPanel() 
        {
            JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));

            List<String> categories = serviceManager.getServiceCategories();
            for (String category : categories) 
                {
                    JButton categoryButton = new JButton(category);
                    categoryButton.setFont(new Font("SansSerif", Font.BOLD, 16));
                    categoryButton.setPreferredSize(new Dimension(200, 50));
                    categoryButton.addActionListener(e -> 
                        {
                            showServicesForCategory(category);
                        });
                    panel.add(categoryButton);
                }

            return panel;
        }

        private JPanel createServiceTablePanel() 
        {
            JPanel panel = new JPanel(new BorderLayout());

            String[] columnNames = {"Service Name", "Price (Rs.)"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0) 
            {
                @Override
                public boolean isCellEditable(int row, int column) 
                {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(new Font("Monospaced", Font.PLAIN, 12));
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JButton backButton = new JButton("Back to Categories");
            backButton.addActionListener(e -> 
                {
                    serviceCardLayout.show(serviceSelectionPanel, "CATEGORY_SELECTION");
                });

            JButton addButton = new JButton("Add Service");
            addButton.addActionListener(e -> 
                {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) 
                        {
                            String serviceName = (String) table.getValueAt(selectedRow, 0);
                            Service selectedService = serviceManager.getServiceList().stream()
                                                    .filter(s -> s.getServiceName().equals(serviceName))
                                                    .findFirst().orElse(null);
                            if (selectedService != null)
                                {
                                    if (!selectedServicesModel.contains(selectedService))
                                        {
                                            selectedServicesModel.addElement(selectedService);
                                            // Lock category after first service
                                            if (selectedServicesModel.size() == 1) {
                                                categoryLocked = true;
                                            }
                                            calculatePrice();
                                            // Ask if user wants to add more services
                                            int option = JOptionPane.showConfirmDialog(this, "Service added. Add more services?", "Add More Services", JOptionPane.YES_NO_OPTION);
                                            if (option == JOptionPane.YES_OPTION)
                                                {
                                                    serviceCardLayout.show(serviceSelectionPanel, "CATEGORY_SELECTION");
                                                }
                                            else
                                                {
                                                    // Stay on service panel or proceed to book
                                                    JOptionPane.showMessageDialog(this, "You can now proceed to book your custom package.");
                                                }
                                        }
                                    else
                                        {
                                            JOptionPane.showMessageDialog(this, "Service already added.", "Error", JOptionPane.WARNING_MESSAGE);
                                        }
                                }
                        } 
                    else 
                        {
                            JOptionPane.showMessageDialog(this, "Please select a service to add.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                        }
                });

            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(backButton);
            buttonPanel.add(addButton);

            panel.add(new JScrollPane(table), BorderLayout.CENTER);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Store table model for later updates
            panel.putClientProperty("tableModel", model);

            return panel;
        }

        private void showServicesForCategory(String category)
        {
            if (categoryLocked && !category.equals(selectedCategory))
                {
                    JOptionPane.showMessageDialog(this, "Category is locked. You can only select services from '" + selectedCategory + "'.", "Category Locked", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            selectedCategory = category;
            List<Service> services = serviceManager.getServicesByCategory(category);
            DefaultTableModel model = (DefaultTableModel) ((JPanel) serviceSelectionPanel.getComponent(1)).getClientProperty("tableModel");

            model.setRowCount(0);
            for (Service svc : services)
                {
                    model.addRow(new Object[]{svc.getServiceName(), String.format("%.2f", svc.getPrice())});
                }

            serviceCardLayout.show(serviceSelectionPanel, "SERVICE_SELECTION");
        }

        private JPanel createBuilderPanel() 
        {
            JPanel panel = new JPanel(new BorderLayout(10, 10));

            // Selected Services List (North)
            selectedServicesModel = new DefaultListModel<>();
            selectedServicesList = new JList<>(selectedServicesModel);
            selectedServicesList.setFont(new Font("Monospaced", Font.PLAIN, 12));
            selectedServicesList.setBorder(BorderFactory.createTitledBorder("Selected Services (30% Discount)"));

            // Price and Action Panel (Center)
            JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
            priceDisplayArea = new JTextArea(5, 30);
            priceDisplayArea.setEditable(false);
            priceDisplayArea.setFont(new Font("Monospaced", Font.BOLD, 14));
            priceDisplayArea.setText("Select services to calculate price...");
            actionPanel.add(new JScrollPane(priceDisplayArea), BorderLayout.NORTH);

            // Buttons
            JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
            JButton removeButton = new JButton("Remove Selected");
            JButton clearButton = new JButton("Clear All");
            JButton bookButton = new JButton("Book Custom Package");

            removeButton.addActionListener(e -> 
                {
                    if (selectedServicesList.getSelectedIndex() != -1) 
                        {
                            selectedServicesModel.remove(selectedServicesList.getSelectedIndex());
                            calculatePrice();
                        }
                });
            clearButton.addActionListener(e -> 
                {
                    selectedServicesModel.clear();
                    calculatePrice();
                });
            bookButton.addActionListener(this::handleBookCustomPackage);

            buttonPanel.add(removeButton);
            buttonPanel.add(clearButton);
            buttonPanel.add(bookButton);

            actionPanel.add(buttonPanel, BorderLayout.CENTER);

            panel.add(new JScrollPane(selectedServicesList), BorderLayout.CENTER);
            panel.add(actionPanel, BorderLayout.SOUTH);

            return panel;
        }

        private void calculatePrice() 
        {
            ArrayList<Service> services = new ArrayList<>();
            for (int i = 0; i < selectedServicesModel.size(); i++) 
                {
                    services.add(selectedServicesModel.getElementAt(i));
                }

            if (services.isEmpty()) 
                {
                    priceDisplayArea.setText("Select services to calculate price...");
                    currentCustomPackage = null;
                    return;
                }
            
            // Use the CustomPackageManager to create/re-calculate the package price
            // Assuming CustomPackageManager has a method to calculate price without persisting it yet.
            currentCustomPackage = customPackageManager.createTemporaryCustomPackage(services);

            // Display breakdown
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Custom Package Price ---\n");
            sb.append(String.format("Subtotal (Raw Price): Rs. %.2f\n", currentCustomPackage.getRawPrice()));
            sb.append(String.format("Discount (30.0%%):     Rs. %.2f\n", currentCustomPackage.getRawPrice() - currentCustomPackage.getFinalPrice()));
            sb.append("---------------------------------\n");
            sb.append(String.format("FINAL PRICE:          Rs. %.2f\n", currentCustomPackage.getFinalPrice()));
            sb.append("---------------------------------");
            
            priceDisplayArea.setText(sb.toString());
        }
        
        private void handleBookCustomPackage(ActionEvent e) 
        {
            if (currentCustomPackage == null || currentCustomPackage.getServiceList().isEmpty()) 
                {
                    JOptionPane.showMessageDialog(this, "Please select at least one service to create a custom package.", "Booking Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
            // 1. Persist the Custom Package first using the manager
            String packageName = JOptionPane.showInputDialog(this, "Enter a name for your custom package (e.g., 'My Glow Up'):");
            if (packageName == null || packageName.trim().isEmpty()) return;
            
            // Update the package name for persistence
            currentCustomPackage.setPackageName(packageName.trim()); 
            
            // Assuming a save method on CustomPackageManager returns the saved package
            CustomPackage finalPackage = customPackageManager.saveCustomPackage(currentCustomPackage); 

            if (finalPackage == null) 
                {
                    JOptionPane.showMessageDialog(this, "Failed to save the custom package.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            
            // 2. Book the Appointment using a helper dialog
             new BookableItemDialog
             (
                SwingUtilities.getWindowAncestor(this),
                finalPackage, // The newly saved package
                doctorManager,
                (date, time, doctorName, price) ->
                {
                    // Lambda function executed upon successful booking details entry
                    String message = appointmentManager.bookAppointment(
                                                                            loggedInPatient.getPatientName(),
                                                                            doctorName,
                                                                            finalPackage,
                                                                            date,
                                                                            time);

                    JOptionPane.showMessageDialog(this, message);
                    parentPanel.refreshAppointmentData(); // Refresh appointments tab on main panel
                    parentPanel.tabbedPane.setSelectedIndex(0); // Switch to appointments tab

                    // Clear the builder state after booking
                    selectedServicesModel.clear();
                    calculatePrice();
                }
            );
        }
    }
    
    // ===============================================
    // --- INNER CLASS: Booking Details Dialog ---
    // ===============================================

    /**
     * A reusable dialog to capture appointment details (Date, Time, Doctor) 
     * for any Bookable item (Service, Special Package, Custom Package).
     */
    private class BookableItemDialog extends JDialog
    {

        private final Window owner;
        private final Bookable item;
        private final DoctorManager doctorManager;
        private final BookingDetailsCallback callback;
        private double currentPrice;

        public BookableItemDialog(Window owner, Bookable item, DoctorManager dm, BookingDetailsCallback cb)
        {
            this.owner = owner;
            this.item = item;
            this.doctorManager = dm;
            this.callback = cb;
            this.currentPrice = item.getFinalPrice();

            setTitle("Book Appointment for " + item.getItemName() + " (Rs. " + String.format("%.2f", currentPrice) + ")");
            setModalityType(ModalityType.APPLICATION_MODAL);

            setupDialog();
        }
        
        private void setupDialog()
        {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Filter doctors based on service category if it's a single service or custom package
            String[] doctorNames;
            if (item instanceof Service) {
                String category = ((Service) item).getServiceCategory();
                String specialization = mapCategoryToSpecialization(category);
                doctorNames = doctorManager.getAllDoctors().stream()
                    .filter(d -> d.getDoctorSpecialization().equals(specialization))
                    .map(Doctor::getDoctorName)
                    .toArray(String[]::new);
            } else if (item instanceof CustomPackage) {
                String category = ((CustomPackage) item).getServiceList().get(0).getServiceCategory();
                String specialization = mapCategoryToSpecialization(category);
                doctorNames = doctorManager.getAllDoctors().stream()
                    .filter(d -> d.getDoctorSpecialization().equals(specialization))
                    .map(Doctor::getDoctorName)
                    .toArray(String[]::new);
            } else {
                doctorNames = doctorManager.getDoctorNamesList().toArray(new String[0]);
            }

            // Components
            JComboBox<String> doctorComboBox = new JComboBox<>(doctorNames);
            JTextField dateField = new JTextField("DD-MM-YYYY", 10);
            JTextField timeField = new JTextField("HH:MM", 10);
            JTextArea availabilityArea = new JTextArea(5, 20);
            availabilityArea.setEditable(false);
            availabilityArea.setText("Select a doctor to view their available days and times.\nThen enter date/time to check availability.");

            JButton checkButton = new JButton("Check Availability");
            JButton bookButton = new JButton("Confirm Booking");
            bookButton.setEnabled(false); // Initially disabled

            // Labels
            gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Select Doctor:"), gbc);
            gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Date (DD-MM-YYYY):"), gbc);
            gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Time (HH:MM):"), gbc);

            // Inputs
            gbc.gridx = 1; gbc.gridy = 0; panel.add(doctorComboBox, gbc);
            gbc.gridx = 1; gbc.gridy = 1; panel.add(dateField, gbc);
            gbc.gridx = 1; gbc.gridy = 2; panel.add(timeField, gbc);

            // Availability Check
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(checkButton, gbc);
            gbc.gridx = 0; gbc.gridy = 4; panel.add(new JScrollPane(availabilityArea), gbc);

            // Listener to show doctor's schedule when selected
            doctorComboBox.addActionListener(e ->
                {
                    String selectedDoctorName = (String) doctorComboBox.getSelectedItem();
                    if (selectedDoctorName != null)
                        {
                            Doctor doctor = doctorManager.getDoctorByName(selectedDoctorName);
                            if (doctor != null)
                                {
                                    // Update price for custom packages
                                    if (item instanceof CustomPackage) {
                                        double doctorDiscount = doctor.getDiscountRate();
                                        double discountAmount = item.getFinalPrice() * doctorDiscount;
                                        currentPrice = item.getFinalPrice() - discountAmount;
                                        setTitle("Book Appointment for " + item.getItemName() + " (Rs. " + String.format("%.2f", currentPrice) + ")");
                                    }

                                    availabilityArea.setText("Doctor's Available Days: " + doctor.getWorkingDays() + "\n" +
                                                 "Working Hours: " + doctor.getWorkingHours() + "\n\n" +
                                                 "Enter date/time above and click 'Check Availability' to confirm.");
                                }
                        }
                });
            
            // Actions
            checkButton.addActionListener(e -> 
                {
                    String date = dateField.getText().trim();
                    String time = timeField.getText().trim();
                    String doctorName = (String) doctorComboBox.getSelectedItem();
                
                    if (doctorName == null || date.isEmpty() || time.isEmpty()) 
                        {
                            availabilityArea.setText("Error: Select doctor and enter date/time.");
                            bookButton.setEnabled(false);
                            return;
                        }
                
                    Doctor doctor = doctorManager.getDoctorByName(doctorName);
                    if (doctor == null) 
                        {
                            availabilityArea.setText("Error: Doctor not found.");
                            bookButton.setEnabled(false);
                            return;
                        }
                
                    // Use the refactored checkAvailability method
                    if (appointmentManager.checkAvailability(doctor, date, time)) 
                        {
                            availabilityArea.setText("✅ Dr. " + doctorName + " IS available on " + date + " at " + time + ".");
                            bookButton.setEnabled(true);
                        } 
                    else 
                        {
                            availabilityArea.setText("❌ Dr. " + doctorName + " is NOT available at this time.\nDoctor's Working Days: " + doctor.getWorkingDays());
                            bookButton.setEnabled(false);
                        }
                });
            
            bookButton.addActionListener(e ->
                {
                    // Final check before calling callback
                    if (bookButton.isEnabled())
                        {
                            callback.onBook(dateField.getText().trim(), timeField.getText().trim(), (String) doctorComboBox.getSelectedItem(), currentPrice);
                            dispose(); // Close dialog on success
                        }
                });

            // Layout finalize
            gbc.gridx = 0; gbc.gridy = 5; panel.add(bookButton, gbc);

            add(panel);
            pack();
            setLocationRelativeTo(owner);
            setVisible(true);
        }



        private String mapCategoryToSpecialization(String category) {
            switch (category.toLowerCase()) {
                case "skin":
                    return "Dermatologist";
                case "laser":
                    return "Laser Specialist";
                case "prp":
                    return "Cosmetologist";
                case "body contouring":
                    return "Plastic Surgeon";
                case "other":
                    return "General Practitioner";
                default:
                    return "General Practitioner";
            }
        }
    }
    
    // ===============================================
    // --- INNER CLASS: Special Package Booking Dialog ---
    // ===============================================

    /**
     * A specialized dialog for booking special packages, showing category-specific doctors with their discount rates.
     */
    private class SpecialPackageBookingDialog extends JDialog
    {

        private final Window owner;
        private final Bookable item;
        private final DoctorManager doctorManager;
        private final BookingDetailsCallback callback;
        private String selectedCategory;
        private double basePrice;
        private JTextField dateField;
        private JTextField timeField;
        private JTextArea availabilityArea;
        private JButton checkButton;
        private JButton bookButton;

        public SpecialPackageBookingDialog(Window owner, Bookable item, String category, DoctorManager dm, BookingDetailsCallback cb)
        {
            this.owner = owner;
            this.item = item;
            this.doctorManager = dm;
            this.callback = cb;
            this.basePrice = item.getFinalPrice();
            this.selectedCategory = category;
            setTitle("Book " + item.getItemName());
            setModalityType(ModalityType.APPLICATION_MODAL);
            setupDialog();
        }

        private void setupDialog()
        {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));

            // Package Details (North)
            JTextArea packageDetailsArea = new JTextArea(8, 50);
            packageDetailsArea.setEditable(false);
            packageDetailsArea.setText(item.toString());
            packageDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            panel.add(new JScrollPane(packageDetailsArea), BorderLayout.NORTH);

            // Buttons (South) - Create first to assign bookButton
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton cancelButton = new JButton("Cancel");
            bookButton = new JButton("Book Appointment");
            bookButton.setEnabled(false); // Initially disabled

            cancelButton.addActionListener(e -> dispose());

            buttonPanel.add(cancelButton);
            buttonPanel.add(bookButton);

            // Booking Panel (Center)
            JPanel bookingPanel = createBookingPanel();
            panel.add(bookingPanel, BorderLayout.CENTER);

            // Add listener to bookButton after bookingPanel is created
            bookButton.addActionListener(e ->
                {
                    if (bookButton.isEnabled())
                        {
                            String date = dateField.getText().trim();
                            String time = timeField.getText().trim();
                            String doctorName = (String) ((JComboBox<String>) bookingPanel.getClientProperty("doctorComboBox")).getSelectedItem();
                            Doctor doctor = doctorManager.getDoctorByName(doctorName);
                            double finalPrice = basePrice * (1 - doctor.getDiscountRate());
                            callback.onBook(date, time, doctorName, finalPrice);
                            dispose();
                        }
                });

            panel.add(buttonPanel, BorderLayout.SOUTH);

            add(panel);
            pack();
            setLocationRelativeTo(owner);
            setVisible(true);
        }

        private JPanel createBookingPanel()
        {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;

            // Get doctors for the category
            String specialization = mapCategoryToSpecialization(selectedCategory);
            List<Doctor> categoryDoctors = doctorManager.getAllDoctors().stream()
                .filter(d -> d.getDoctorSpecialization().equals(specialization))
                .collect(java.util.stream.Collectors.toList());

            String[] doctorNames;
            if (categoryDoctors.isEmpty()) {
                doctorNames = new String[0];
            } else {
                doctorNames = categoryDoctors.stream()
                    .map(Doctor::getDoctorName)
                    .toArray(String[]::new);
            }

            // Components
            JComboBox<String> doctorComboBox = new JComboBox<>(doctorNames);
            dateField = new JTextField("DD-MM-YYYY", 10);
            timeField = new JTextField("HH:MM", 10);
            availabilityArea = new JTextArea(5, 20);
            availabilityArea.setEditable(false);
            availabilityArea.setText("Select a doctor to view their available days and times.\nThen enter date/time to check availability.");

            checkButton = new JButton("Check Availability");
            bookButton.setEnabled(false); // Initially disabled

            // Labels
            gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Select Doctor:"), gbc);
            gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Date (DD-MM-YYYY):"), gbc);
            gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Time (HH:MM):"), gbc);

            // Inputs
            gbc.gridx = 1; gbc.gridy = 0; panel.add(doctorComboBox, gbc);
            gbc.gridx = 1; gbc.gridy = 1; panel.add(dateField, gbc);
            gbc.gridx = 1; gbc.gridy = 2; panel.add(timeField, gbc);

            // Availability Check
            gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; panel.add(checkButton, gbc);
            gbc.gridx = 0; gbc.gridy = 4; panel.add(new JScrollPane(availabilityArea), gbc);

            // Store doctorComboBox for later use
            panel.putClientProperty("doctorComboBox", doctorComboBox);

            // Listener to show doctor's schedule when selected
            doctorComboBox.addActionListener(e ->
                {
                    String selectedDoctorName = (String) doctorComboBox.getSelectedItem();
                    if (selectedDoctorName != null)
                        {
                            Doctor doctor = doctorManager.getDoctorByName(selectedDoctorName);
                            if (doctor != null)
                                {
                                    double discountRate = doctor.getDiscountRate() * 100;
                                    double discountedPrice = basePrice * (1 - doctor.getDiscountRate());
                                    availabilityArea.setText("Doctor: " + doctor.getDoctorName() + "\n" +
                                                             "Specialization: " + doctor.getDoctorSpecialization() + "\n" +
                                                             "Working Days: " + doctor.getWorkingDays() + "\n" +
                                                             "Working Hours: " + doctor.getWorkingHours() + "\n" +
                                                             "Discount Rate: " + String.format("%.1f%%", discountRate) + "\n" +
                                                             "Final Package Price: Rs. " + String.format("%.2f", discountedPrice) + "\n\n" +
                                                             "Enter date/time above and click 'Check Availability' to confirm.");
                                }
                        }
                });

            // Actions
            checkButton.addActionListener(e ->
                {
                    String date = dateField.getText().trim();
                    String time = timeField.getText().trim();
                    String doctorName = (String) doctorComboBox.getSelectedItem();

                    if (doctorName == null || date.isEmpty() || time.isEmpty())
                        {
                            availabilityArea.setText("Error: Select doctor and enter date/time.");
                            bookButton.setEnabled(false);
                            return;
                        }

                    Doctor doctor = doctorManager.getDoctorByName(doctorName);
                    if (doctor == null)
                        {
                            availabilityArea.setText("Error: Doctor not found.");
                            bookButton.setEnabled(false);
                            return;
                        }

                    // Use the refactored checkAvailability method
                    if (appointmentManager.checkAvailability(doctor, date, time))
                        {
                            availabilityArea.setText("✅ Dr. " + doctorName + " IS available on " + date + " at " + time + ".");
                            bookButton.setEnabled(true);
                        }
                    else
                        {
                            availabilityArea.setText("❌ Dr. " + doctorName + " is NOT available at this time.\nDoctor's Working Days: " + doctor.getWorkingDays());
                            bookButton.setEnabled(false);
                        }
                });

            return panel;
        }

        private String mapCategoryToSpecialization(String category) {
            switch (category.toLowerCase()) {
                case "skin":
                    return "Dermatologist";
                case "laser":
                    return "Laser Specialist";
                case "prp":
                    return "Cosmetologist";
                case "body contouring":
                    return "Plastic Surgeon";
                case "other":
                    return "General Practitioner";
                default:
                    return "General Practitioner";
            }
        }
    }

    // ===============================================
    // --- FUNCTIONAL INTERFACE for Callback ---
    // ===============================================

    /**
     * Functional interface for handling the result of the Booking Dialog.
     */
    @FunctionalInterface
    private interface BookingDetailsCallback
    {
        void onBook(String date, String time, String doctorName, double price);
    }
}
