import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * The initial screen, handling logins for Doctor and Patient, 
 * as well as registration and public access features.
 */
public class LoginScreenPanel extends JPanel 
{    
    // Dependencies injected from MainApplication
    private final JPanel mainPanelContainer;
    private final CardLayout cardLayout;
    private final PatientManager patientManager;
    private final DoctorAccessManager doctorAccessManager;
    private final FeedbackManager feedbackManager;
    
    // Dependencies needed to create the PatientDashboard Panel later
    private final ServiceManager serviceManager;
    private final SpecialPackageManager specialPackageManager;
    private final CustomPackageManager customPackageManager;
    private final AppointmentManager appointmentManager;
    private final DoctorManager doctorManager;

    // Components
    private JComboBox<String> doctorNameComboBox;
    private JPasswordField doctorPasswordField;
    private JTextField patientNameField;
    private JPasswordField patientPasswordField;
    
    // Store last logged-in patient name for convenience during registration
    private String lastPatientNameAttempt = "";
    
    // --- Constructor ---
    public LoginScreenPanel(JPanel mainPanelContainer, CardLayout cardLayout, 
                            PatientManager patientManager, DoctorAccessManager doctorAccessManager, 
                            FeedbackManager feedbackManager, 
                            ServiceManager serviceManager, SpecialPackageManager specialPackageManager, 
                            CustomPackageManager customPackageManager, 
                            AppointmentManager appointmentManager, DoctorManager doctorManager) 
                            {
                                this.mainPanelContainer = mainPanelContainer;
                                this.cardLayout = cardLayout;
                                this.patientManager = patientManager;
                                this.doctorAccessManager = doctorAccessManager;
                                this.feedbackManager = feedbackManager;
                                this.serviceManager = serviceManager;
                                this.specialPackageManager = specialPackageManager;
                                this.customPackageManager = customPackageManager;
                                this.appointmentManager = appointmentManager;
                                this.doctorManager = doctorManager;

                                setupLayout();
                            }

    // --- Layout Setup ---
    private void setupLayout() 
    {
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Title Panel (Center Top)
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel mainTitle = new JLabel("DERMAHEAVEN AESTHETIC");
        mainTitle.setFont(new Font("SansSerif", Font.BOLD, 36));
        mainTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subTitle = new JLabel("\"The Art of Timeless Glow\"");
        subTitle.setFont(new Font("Serif", Font.ITALIC, 18));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(mainTitle);
        titlePanel.add(subTitle);
        titlePanel.add(Box.createVerticalStrut(20)); // Spacer
        this.add(titlePanel, BorderLayout.NORTH);

        // 2. Main Center Panel (Using GridBagLayout for alignment)
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // --- Left Section: Doctor Login (Grid Row 0) ---
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        centerPanel.add(createDoctorLoginPanel(), gbc);

        // --- Separator (Grid Row 0) ---
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.05;
        centerPanel.add(new JSeparator(SwingConstants.VERTICAL), gbc); 

        // --- Right Section: Patient Login/Public Access (Grid Row 0) ---
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        centerPanel.add(createPatientAccessPanel(), gbc);
        
        this.add(centerPanel, BorderLayout.CENTER);
        
        // 3. Footer/Exit Button
        JButton exitButton = new JButton("Exit Application");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.addActionListener(e -> 
            {
                JOptionPane.showMessageDialog(this, "Thank you for using the DermaHeaven System!", "Exit", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            });
        this.add(exitButton, BorderLayout.SOUTH);
    }
    
    // --- Helper for Doctor Login Panel ---
    private JPanel createDoctorLoginPanel() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), 
                                                        "Doctor Login", 0, 0, new Font("SansSerif", Font.BOLD, 16)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 1. Doctor Name (ComboBox)
        JLabel nameLabel = new JLabel("Doctor Name:");
        gbc.gridx = 0; gbc.gridy = 0; panel.add(nameLabel, gbc);
        
        List<String> doctorNames = doctorManager.getDoctorNamesList();
        doctorNameComboBox = new JComboBox<>(doctorNames.toArray(new String[0]));
        gbc.gridx = 1; gbc.gridy = 0; panel.add(doctorNameComboBox, gbc);

        // 2. Password Field
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1; panel.add(passwordLabel, gbc);
        
        doctorPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(doctorPasswordField, gbc);

        // 3. Login Button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setBackground(new Color(60, 179, 113)); // Medium Sea Green
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; 
        panel.add(loginButton, gbc);
        
        loginButton.addActionListener(this::handleDoctorLogin);

        return panel;
    }
    
    // --- Helper for Patient Access Panel ---
    private JPanel createPatientAccessPanel() 
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), 
                                                        "Patient Access", 0, 0, new Font("SansSerif", Font.BOLD, 16)));
        
        // --- North: Login/Signup ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // 1. Patient Name
        JLabel nameLabel = new JLabel("Your Name:");
        gbc.gridx = 0; gbc.gridy = 0; loginPanel.add(nameLabel, gbc);
        
        patientNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0; loginPanel.add(patientNameField, gbc);

        // 2. Patient Password
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1; loginPanel.add(passwordLabel, gbc);
        
        patientPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1; loginPanel.add(patientPasswordField, gbc);

        // 3. Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 2; loginPanel.add(loginButton, gbc);
        
        JButton registerButton = new JButton("New Patient? Sign Up");
        registerButton.setBackground(new Color(255, 165, 0)); // Orange
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        gbc.gridx = 1; gbc.gridy = 2; loginPanel.add(registerButton, gbc);
        
        loginButton.addActionListener(this::handlePatientLogin);
        registerButton.addActionListener(this::handlePatientSignup);

        panel.add(loginPanel, BorderLayout.NORTH);
        
        // --- Center: Public Access ---
        JPanel publicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        publicPanel.setBorder(BorderFactory.createTitledBorder("Public Info"));
        JButton feedbackButton = new JButton("Feedback");
        feedbackButton.addActionListener(this::handleFeedbackOptions);
        publicPanel.add(feedbackButton);
        
        panel.add(publicPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // --- Action Handlers ---
    
    private void handleDoctorLogin(ActionEvent e) 
    {
        String doctorName = (String) doctorNameComboBox.getSelectedItem();
        String password = new String(doctorPasswordField.getPassword());
        
        if (doctorName == null || doctorName.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please select a Doctor.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
        Doctor loggedInDoctor = doctorAccessManager.attemptLogin(doctorName, password);
        
        if (loggedInDoctor != null) 
            {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, Dr. " + doctorName + ".");
                // 1. Create the Doctor Dashboard Panel
                DoctorDashboardPanel doctorDashboard = new DoctorDashboardPanel(mainPanelContainer, cardLayout, 
                loggedInDoctor, appointmentManager, feedbackManager);
            
                // 2. Add it to the CardLayout container
                String cardName = "DOCTOR_DASHBOARD_" + loggedInDoctor.getDoctorId();
                mainPanelContainer.add(doctorDashboard, cardName);
            
                // 3. Switch view
                cardLayout.show(mainPanelContainer, cardName);
            } 
        else 
            {
                JOptionPane.showMessageDialog(this, "Invalid password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                doctorPasswordField.setText(""); // Clear password field
            }
    }

    private void handlePatientLogin(ActionEvent e) 
    {
        String name = patientNameField.getText().trim();
        String password = new String(patientPasswordField.getPassword());
        lastPatientNameAttempt = name;

        if (name.isEmpty() || password.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please enter both name and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        // Refactored PatientManager now returns Patient object on success
        Patient loggedInPatient = patientManager.attemptLogin(name, password);
        
        if (loggedInPatient != null) 
            {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + name + ".");
            
                // 1. Create the Patient Dashboard Panel
                PatientDashboardPanel patientDashboard = new PatientDashboardPanel(mainPanelContainer, cardLayout, 
                loggedInPatient, patientManager, serviceManager, specialPackageManager, 
                customPackageManager, appointmentManager, feedbackManager, doctorManager);
            
                // 2. Add it to the CardLayout container
                String cardName = "PATIENT_DASHBOARD_" + loggedInPatient.getPatientName().replace(" ", "_");
                mainPanelContainer.add(patientDashboard, cardName);
            
                // 3. Switch view
                cardLayout.show(mainPanelContainer, cardName);
            } 
        else 
            {
                JOptionPane.showMessageDialog(this, "Login Failed. Patient not found or incorrect password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                patientPasswordField.setText(""); // Clear password field
            }
    }
    
    private void handlePatientSignup(ActionEvent e)
    {
        // Open a new dialog or switch to a dedicated signup panel
        // For simplicity, we'll use a dialog here to capture necessary details.

        String name = patientNameField.getText().trim();
        String password = new String(patientPasswordField.getPassword());

        // Pre-fill name if available
        if (name.isEmpty() && !lastPatientNameAttempt.isEmpty())
            {
                name = lastPatientNameAttempt;
            }

        // Check if name is already taken
        if (patientManager.isPatientRegistered(name))
            {
                JOptionPane.showMessageDialog(this, name + " is already registered. Please log in.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        // Prompt for details using a custom dialog
        JPanel signupPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        JTextField signupNameField = new JTextField(name);
        JPasswordField signupPasswordField = new JPasswordField(password);
        JTextField signupContactField = new JTextField();
        JTextField signupEmailField = new JTextField();
        JTextField signupAgeField = new JTextField();
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JRadioButton maleBtn = new JRadioButton("Male");
        JRadioButton femaleBtn = new JRadioButton("Female");
        JRadioButton otherBtn = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn);
        genderGroup.add(femaleBtn);
        genderGroup.add(otherBtn);
        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);
        genderPanel.add(otherBtn);

        if (!name.isEmpty()) signupNameField.setEditable(false);

        signupPanel.add(new JLabel("Full Name:"));
        signupPanel.add(signupNameField);
        signupPanel.add(new JLabel("Set Password:"));
        signupPanel.add(signupPasswordField);
        signupPanel.add(new JLabel("Contact No:"));
        signupPanel.add(signupContactField);
        signupPanel.add(new JLabel("Email:"));
        signupPanel.add(signupEmailField);
        signupPanel.add(new JLabel("Age:"));
        signupPanel.add(signupAgeField);
        signupPanel.add(new JLabel("Gender:"));
        signupPanel.add(genderPanel);

        int result = JOptionPane.showConfirmDialog(this, signupPanel,
            "Patient Registration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION)
            {
                String newName = signupNameField.getText().trim();
                String newPassword = new String(signupPasswordField.getPassword());
                String contact = signupContactField.getText().trim();
                String email = signupEmailField.getText().trim();
                String ageStr = signupAgeField.getText().trim();
                String gender = "";
                java.util.Enumeration<AbstractButton> elements = genderGroup.getElements();
                while (elements.hasMoreElements()) {
                    AbstractButton button = elements.nextElement();
                    if (button.isSelected()) {
                        gender = button.getText();
                        break;
                    }
                }

                if (newName.isEmpty() || newPassword.isEmpty() || contact.isEmpty() || ageStr.isEmpty() || gender.isEmpty())
                    {
                        JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                try
                {
                    int age = Integer.parseInt(ageStr);
                    // Call the refactored method
                    Patient newPatient = patientManager.registerPatient(newName, newPassword, contact, email, age, gender);

                    if (newPatient != null)
                        {
                            JOptionPane.showMessageDialog(this, "Registration successful! Welcome, " + newName + ". Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            patientNameField.setText(newName);
                            patientPasswordField.setText(newPassword);
                        }
                    else
                        {
                            JOptionPane.showMessageDialog(this, "Registration failed. Name may be taken.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(this, "Invalid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    }
    
    private void handleFeedbackOptions(ActionEvent e)
    {
        // Create a dialog with two options: Enter Feedback and View All Feedbacks
        JPanel optionsPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton enterFeedbackButton = new JButton("Enter Feedback");
        JButton viewFeedbacksButton = new JButton("View All Feedbacks");

        enterFeedbackButton.addActionListener(ev -> handleEnterFeedback());
        viewFeedbacksButton.addActionListener(ev -> handleViewAllFeedbacks());

        optionsPanel.add(enterFeedbackButton);
        optionsPanel.add(viewFeedbacksButton);

        JOptionPane.showOptionDialog(this, optionsPanel, "Feedback Options",
            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
    }

    private void handleEnterFeedback()
    {
        // Create dialog for entering feedback
        JPanel feedbackPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField patientNameField = new JTextField(20);
        JComboBox<String> doctorComboBox = new JComboBox<>(doctorManager.getDoctorNamesList().toArray(new String[0]));
        JTextField serviceField = new JTextField(20);
        JSpinner ratingSpinner = new JSpinner(new SpinnerNumberModel(5.0, 1.0, 5.0, 0.5));
        JTextArea commentArea = new JTextArea(5, 20);
        JScrollPane commentScrollPane = new JScrollPane(commentArea);

        gbc.gridx = 0; gbc.gridy = 0; feedbackPanel.add(new JLabel("Your Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; feedbackPanel.add(patientNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; feedbackPanel.add(new JLabel("Select Doctor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; feedbackPanel.add(doctorComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 2; feedbackPanel.add(new JLabel("Service/Item Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; feedbackPanel.add(serviceField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; feedbackPanel.add(new JLabel("Rating (1.0-5.0):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; feedbackPanel.add(ratingSpinner, gbc);
        gbc.gridx = 0; gbc.gridy = 4; feedbackPanel.add(new JLabel("Comments:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; feedbackPanel.add(commentScrollPane, gbc);

        int result = JOptionPane.showConfirmDialog(this, feedbackPanel, "Submit Feedback", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION)
        {
            String patientName = patientNameField.getText().trim();
            String doctorName = (String) doctorComboBox.getSelectedItem();
            String serviceName = serviceField.getText().trim();
            double rating = (Double) ratingSpinner.getValue();
            String comment = commentArea.getText().trim();

            if (patientName.isEmpty() || doctorName == null || serviceName.isEmpty() || comment.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String message = feedbackManager.submitFeedback(patientName, doctorName, serviceName, comment, rating);
            JOptionPane.showMessageDialog(this, message);
        }
    }

    private void handleViewAllFeedbacks()
    {
        // 1. Get data from the refactored manager
        List<Feedback> allFeedbacks = feedbackManager.getAllFeedbacks();
        double avgRating = feedbackManager.getOverallAverageRating();

        // 2. Format output for a dialog box
        if (allFeedbacks.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "No feedbacks yet. Be the first to leave one!", "All Feedbacks", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<html><body><h2>Overall Average Rating: %.2f / 5.0 (from %d reviews)</h2>",
                                avgRating, allFeedbacks.size()));
        sb.append("<hr>");

        // Display all feedbacks
        for (Feedback fb : allFeedbacks)
            {
                sb.append(String.format("<b>Rating: %.1f / 5.0</b> | Doctor: %s | Patient: %s<br>", fb.getRating(), fb.getDoctorName(), fb.getPatientName()));
                sb.append(String.format("Treatment: %s<br>", fb.getServiceName()));
                sb.append(String.format("Comment: <i>\"%s\"</i><br>", fb.getFeedbackText()));
                sb.append("------------------------------------------<br>");
            }
        sb.append("</body></html>");

        // 3. Display in a scrollable panel within a dialog
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(this, scrollPane, "All Patient Feedbacks", JOptionPane.PLAIN_MESSAGE);
    }
}