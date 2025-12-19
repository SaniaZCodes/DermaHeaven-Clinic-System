import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.List;

/**
 * The main dashboard panel for the dermatology clinic, featuring a header, left-side menu, and dynamic main content area.
 */
public class ClinicDashboardPanel extends JPanel 
{
    // Dependencies injected from MainApplication
    private final JPanel mainPanelContainer;
    private final CardLayout cardLayout;
    private final PatientManager patientManager;
    private final DoctorAccessManager doctorAccessManager;
    private final FeedbackManager feedbackManager;
    private final ServiceManager serviceManager;
    private final SpecialPackageManager specialPackageManager;
    private final CustomPackageManager customPackageManager;
    private final AppointmentManager appointmentManager;
    private final DoctorManager doctorManager;

    // Main content CardLayout
    private JPanel mainContentPanel;
    private CardLayout mainContentCardLayout;

    // Components for login
    private JComboBox<String> doctorNameComboBox;
    private JPasswordField doctorPasswordField;
    private JTextField patientNameField;
    private JPasswordField patientPasswordField;

    // Store last logged-in patient name for convenience during registration
    private String lastPatientNameAttempt = "";

    // --- Constructor ---
    public ClinicDashboardPanel(JPanel mainPanelContainer, CardLayout cardLayout,
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
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(240, 248, 255)); // Very light blue background

        // 1. Header Panel
        JPanel headerPanel = createHeaderPanel();
        this.add(headerPanel, BorderLayout.NORTH);

        // 2. Left Menu Panel
        JPanel leftMenuPanel = createLeftMenuPanel();
        this.add(leftMenuPanel, BorderLayout.WEST);

        // 3. Main Content Panel with custom layout to center without expansion
        mainContentPanel = new JPanel();
        mainContentCardLayout = new CenteringLayout();
        mainContentPanel.setLayout(mainContentCardLayout);
        mainContentPanel.setBackground(new Color(240, 248, 255)); // Light blue background

        // Add cards
        mainContentPanel.add(createWelcomePanel(), "WELCOME");

        // Wrap small panels to prevent expansion
        JPanel doctorPanelWrapper = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                Dimension pref = getPreferredSize();
                int newX = x + (width - pref.width) / 2;
                int newY = y + (height - pref.height) / 2;
                super.setBounds(newX, newY, pref.width, pref.height);
            }
        };
        doctorPanelWrapper.setPreferredSize(new Dimension(400, 200));
        doctorPanelWrapper.add(createDoctorPanel(), BorderLayout.CENTER);
        mainContentPanel.add(doctorPanelWrapper, "DOCTOR_PANEL");

        mainContentPanel.add(createDoctorLoginPanel(), "DOCTOR_LOGIN");
        mainContentPanel.add(createDoctorSignupPanel(), "DOCTOR_SIGNUP");

        JPanel patientPanelWrapper = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                Dimension pref = getPreferredSize();
                int newX = x + (width - pref.width) / 2;
                int newY = y + (height - pref.height) / 2;
                super.setBounds(newX, newY, pref.width, pref.height);
            }
        };
        patientPanelWrapper.setPreferredSize(new Dimension(400, 200));
        patientPanelWrapper.add(createPatientPanel(), BorderLayout.CENTER);
        mainContentPanel.add(patientPanelWrapper, "PATIENT_PANEL");

        mainContentPanel.add(createPatientLoginPanel(), "PATIENT_LOGIN");
        mainContentPanel.add(createPatientSignupPanel(), "PATIENT_SIGNUP");

        JPanel feedbackPanelWrapper = new JPanel(new BorderLayout()) {
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize();
            }

            @Override
            public void setBounds(int x, int y, int width, int height) {
                Dimension pref = getPreferredSize();
                int newX = x + (width - pref.width) / 2;
                int newY = y + (height - pref.height) / 2;
                super.setBounds(newX, newY, pref.width, pref.height);
            }
        };
        feedbackPanelWrapper.setPreferredSize(new Dimension(600, 400));
        feedbackPanelWrapper.add(createFeedbackPanel(), BorderLayout.CENTER);
        mainContentPanel.add(feedbackPanelWrapper, "FEEDBACK");

        // Default to WELCOME
        mainContentCardLayout.show(mainContentPanel, "WELCOME");

        this.add(mainContentPanel, BorderLayout.CENTER);
    }

    // --- Header Panel ---
    private JPanel createHeaderPanel() 
    {
        JPanel header = new JPanel() 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(173, 216, 230), getWidth(), 0, new Color(255, 255, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo (using text for simplicity, can be replaced with image)
        JLabel logoLabel = new JLabel("DERMAHEAVEN");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        logoLabel.setForeground(new Color(70, 130, 180));

        // Tagline
        JLabel taglineLabel = new JLabel("\"The Art of Timeless Glow\"");
        taglineLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        taglineLabel.setForeground(new Color(105, 105, 105));

        header.add(logoLabel, BorderLayout.WEST);
        header.add(taglineLabel, BorderLayout.EAST);

        return header;
    }

    // --- Left Menu Panel ---
    private JPanel createLeftMenuPanel() 
    {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setPreferredSize(new Dimension(250, 0));
        menu.setBackground(new Color(173, 216, 230)); // Light blue background
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, Color.GRAY), // Right border line
            BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));

        // Buttons with icons/emojis
        RoundedButton doctorBtn = new RoundedButton("👨‍⚕️ Doctor");
        RoundedButton patientBtn = new RoundedButton("👤 Patient");
        RoundedButton viewFeedbackBtn = new RoundedButton("⭐ View Patient Feedback");
        RoundedButton exitBtn = new RoundedButton("🚪 Exit");

        doctorBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "DOCTOR_PANEL"));
        patientBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "PATIENT_PANEL"));
        viewFeedbackBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "FEEDBACK"));
        exitBtn.addActionListener(e ->
            {
                JOptionPane.showMessageDialog(this, "Thank you for using the DermaHeaven System!", "Exit", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            });

        menu.add(doctorBtn);
        menu.add(Box.createVerticalStrut(15));
        menu.add(patientBtn);
        menu.add(Box.createVerticalStrut(15));
        menu.add(viewFeedbackBtn);
        menu.add(Box.createVerticalStrut(15));
        menu.add(exitBtn);

        return menu;
    }

    // --- Welcome Panel ---
    private JPanel createWelcomePanel()
    {
        JPanel panel = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 149, 237), getWidth(), 0, new Color(186, 85, 211)); // Lighter blue to lighter purple gradient
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.putClientProperty("center", false); // Do not center this panel, let it fill the space

        JLabel welcomeLabel = new JLabel("<html><center><font color='#FFFFFF' size='6'><b>✨ Welcome to DermaHeaven Aesthetic ✨</b></font><br><br><font color='#FFD700' size='4'>Select an option from the menu to get started.</font></center></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        panel.add(welcomeLabel, BorderLayout.CENTER);

        return panel;
    }

    // --- Doctor Login Panel ---
    private JPanel createDoctorLoginPanel() 
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2), "Doctor Login", 0, 0, new Font("SansSerif", Font.BOLD, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Doctor Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Doctor Name:"), gbc);
        doctorNameComboBox = new JComboBox<>(new String[0]);
        refreshDoctorComboBox();
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(doctorNameComboBox, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        doctorPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(doctorPasswordField, gbc);

        // Login Button
        RoundedButton loginBtn = new RoundedButton("Login");
        loginBtn.addActionListener(this::handleDoctorLogin);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        return panel;
    }

    // --- Patient Login Panel ---
    private JPanel createPatientLoginPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2), "Patient Access", 0, 0, new Font("SansSerif", Font.BOLD, 16)));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Login Panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(new JLabel("Your Name:"), gbc);
        patientNameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        loginPanel.add(patientNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        patientPasswordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(patientPasswordField, gbc);

        RoundedButton loginBtn = new RoundedButton("Login");
        loginBtn.addActionListener(this::handlePatientLogin);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        loginPanel.add(loginBtn, gbc);

        panel.add(loginPanel, BorderLayout.CENTER);

        return panel;
    }

    // --- Feedback Panel ---
    private JPanel createFeedbackPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Get data
        List<Feedback> allFeedbacks = feedbackManager.getAllFeedbacks();
        double avgRating = feedbackManager.getOverallAverageRating();

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(new Color(255, 255, 255));

        if (allFeedbacks.isEmpty())
            {
                textArea.setText("No feedbacks yet. Be the first to leave one!");
            }
        else
            {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Overall Average Rating: %.2f / 5.0 (from %d reviews)\n", avgRating, allFeedbacks.size()));
                sb.append("----------------------------------------\n");

                for (int i = 0; i < Math.min(10, allFeedbacks.size()); i++)
                    { // Show top 10
                        Feedback fb = allFeedbacks.get(i);
                        sb.append(String.format("Rating: %.1f / 5.0 | Doctor: %s\n", fb.getRating(), fb.getDoctorName()));
                        sb.append(String.format("Treatment: %s\n", fb.getServiceName()));
                        sb.append(String.format("Comment: \"%s\"\n", fb.getFeedbackText()));
                        sb.append("----------------------------------------\n");
                    }
                if (allFeedbacks.size() > 10)
                    {
                        sb.append("... and more.\n");
                    }
                textArea.setText(sb.toString());
            }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // --- Doctor Panel (Sub-menu for Login/Signup) ---
    private JPanel createDoctorPanel()
    {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(new Color(240, 248, 255));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setMaximumSize(new Dimension(300, 150));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        RoundedButton loginBtn = new RoundedButton("👨‍⚕️ Doctor Login");
        loginBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "DOCTOR_LOGIN"));

        RoundedButton signupBtn = new RoundedButton("📝 Doctor Sign Up");
        signupBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "DOCTOR_SIGNUP"));

        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(signupBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        wrapper.add(panel);

        return wrapper;
    }

    // --- Patient Panel (Sub-menu for Login/Signup) ---
    private JPanel createPatientPanel()
    {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setBackground(new Color(240, 248, 255));

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setPreferredSize(new Dimension(300, 150));
        panel.setMaximumSize(new Dimension(300, 150));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        RoundedButton loginBtn = new RoundedButton("👤 Patient Login");
        loginBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "PATIENT_LOGIN"));

        RoundedButton signupBtn = new RoundedButton("📝 Patient Sign Up");
        signupBtn.addActionListener(e -> mainContentCardLayout.show(mainContentPanel, "PATIENT_SIGNUP"));

        buttonPanel.add(loginBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(signupBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        wrapper.add(panel);

        return wrapper;
    }

    // --- Doctor Signup Panel ---
    private JPanel createDoctorSignupPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2), "Doctor Registration", 0, 0, new Font("SansSerif", Font.BOLD, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        JTextField nameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(nameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Specialization
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Specialization:"), gbc);
        String[] specializations = {"Dermatologist", "Laser Specialist", "Cosmetologist", "Plastic Surgeon", "General Practitioner"};
        JComboBox<String> specializationCombo = new JComboBox<>(specializations);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(specializationCombo, gbc);

        // Working Days (Checkboxes)
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Working Days:"), gbc);
        JPanel daysPanel = new JPanel(new GridLayout(1, 7));
        JCheckBox mondayCheck = new JCheckBox("Mon");
        JCheckBox tuesdayCheck = new JCheckBox("Tue");
        JCheckBox wednesdayCheck = new JCheckBox("Wed");
        JCheckBox thursdayCheck = new JCheckBox("Thu");
        JCheckBox fridayCheck = new JCheckBox("Fri");
        JCheckBox saturdayCheck = new JCheckBox("Sat");
        JCheckBox sundayCheck = new JCheckBox("Sun");
        daysPanel.add(mondayCheck);
        daysPanel.add(tuesdayCheck);
        daysPanel.add(wednesdayCheck);
        daysPanel.add(thursdayCheck);
        daysPanel.add(fridayCheck);
        daysPanel.add(saturdayCheck);
        daysPanel.add(sundayCheck);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(daysPanel, gbc);

        // Working Hours Start
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Start Time (HH:MM):"), gbc);
        JTextField startTimeField = new JTextField("09:00", 10);
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(startTimeField, gbc);

        // Working Hours End
        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("End Time (HH:MM):"), gbc);
        JTextField endTimeField = new JTextField("17:00", 10);
        gbc.gridx = 1; gbc.gridy = 6;
        panel.add(endTimeField, gbc);

        // Discount Rate
        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Discount Rate (%):"), gbc);
        JTextField discountField = new JTextField("10.0", 10);
        gbc.gridx = 1; gbc.gridy = 7;
        panel.add(discountField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 8;
        panel.add(new JLabel("Age:"), gbc);
        JTextField ageField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 8;
        panel.add(ageField, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 9;
        panel.add(new JLabel("Gender:"), gbc);
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
        gbc.gridx = 1; gbc.gridy = 9;
        panel.add(genderPanel, gbc);

        // Register Button
        RoundedButton registerBtn = new RoundedButton("Register");
        registerBtn.addActionListener(e -> handleDoctorSignup(nameField, passwordField, specializationCombo, mondayCheck, tuesdayCheck, wednesdayCheck, thursdayCheck, fridayCheck, saturdayCheck, sundayCheck, startTimeField, endTimeField, discountField, ageField, genderGroup));
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        return panel;
    }

    // --- Patient Signup Panel ---
    private JPanel createPatientSignupPanel()
    {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2), "Patient Registration", 0, 0, new Font("SansSerif", Font.BOLD, 16)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Full Name:"), gbc);
        JTextField nameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(nameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(passwordField, gbc);

        // Contact
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Contact No:"), gbc);
        JTextField contactField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(contactField, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        JTextField emailField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(emailField, gbc);

        // Age
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Age:"), gbc);
        JTextField ageField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(ageField, gbc);

        // Gender
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Gender:"), gbc);
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
        gbc.gridx = 1; gbc.gridy = 5;
        panel.add(genderPanel, gbc);

        // Register Button
        RoundedButton registerBtn = new RoundedButton("Register");
        registerBtn.addActionListener(e -> handlePatientSignup(nameField, passwordField, contactField, emailField, ageField, genderGroup));
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        return panel;
    }

    // --- Password Validation ---
    private boolean validatePassword(String password)
    {
        if (password.length() < 8)
            return false;

        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray())
        {
            if (Character.isDigit(c))
                hasDigit = true;
            else if (!Character.isLetterOrDigit(c))
                hasSpecial = true;
        }

        return hasDigit && hasSpecial;
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
                // Create Doctor Dashboard
                DoctorDashboardPanel doctorDashboard = new DoctorDashboardPanel(mainPanelContainer, cardLayout,
                loggedInDoctor, appointmentManager, feedbackManager);

                String cardName = "DOCTOR_DASHBOARD_" + loggedInDoctor.getDoctorId();
                mainPanelContainer.add(doctorDashboard, cardName);
                cardLayout.show(mainPanelContainer, cardName);
            } 
        else 
            {
                JOptionPane.showMessageDialog(this, "Invalid password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                doctorPasswordField.setText("");
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

        Patient loggedInPatient = patientManager.attemptLogin(name, password);

        if (loggedInPatient != null) 
            {
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + name + ".");
                // Create Patient Dashboard
                PatientDashboardPanel patientDashboard = new PatientDashboardPanel(mainPanelContainer, cardLayout,
                loggedInPatient, patientManager, serviceManager, specialPackageManager,
                customPackageManager, appointmentManager, feedbackManager, doctorManager);

                String cardName = "PATIENT_DASHBOARD_" + loggedInPatient.getPatientName().replace(" ", "_");
                mainPanelContainer.add(patientDashboard, cardName);
                cardLayout.show(mainPanelContainer, cardName);
            } 
        else 
            {
                JOptionPane.showMessageDialog(this, "Login Failed. Patient not found or incorrect password.", "Login Error", JOptionPane.ERROR_MESSAGE);
                patientPasswordField.setText("");
            }
    }

    private void handleDoctorSignup(JTextField nameField, JPasswordField passwordField, JComboBox<String> specializationCombo, JCheckBox mondayCheck, JCheckBox tuesdayCheck, JCheckBox wednesdayCheck, JCheckBox thursdayCheck, JCheckBox fridayCheck, JCheckBox saturdayCheck, JCheckBox sundayCheck, JTextField startTimeField, JTextField endTimeField, JTextField discountField, JTextField ageField, ButtonGroup genderGroup)
    {
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String specialization = (String) specializationCombo.getSelectedItem();
        StringBuilder workingDays = new StringBuilder();
        if (mondayCheck.isSelected()) workingDays.append("Monday, ");
        if (tuesdayCheck.isSelected()) workingDays.append("Tuesday, ");
        if (wednesdayCheck.isSelected()) workingDays.append("Wednesday, ");
        if (thursdayCheck.isSelected()) workingDays.append("Thursday, ");
        if (fridayCheck.isSelected()) workingDays.append("Friday, ");
        if (saturdayCheck.isSelected()) workingDays.append("Saturday, ");
        if (sundayCheck.isSelected()) workingDays.append("Sunday, ");
        String workingDaysStr = workingDays.length() > 0 ? workingDays.substring(0, workingDays.length() - 2) : "";
        String startTime = startTimeField.getText().trim();
        String endTime = endTimeField.getText().trim();
        String discountStr = discountField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = "";
        Enumeration<AbstractButton> elements = genderGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.isSelected()) {
                gender = button.getText();
                break;
            }
        }

        if (name.isEmpty() || password.isEmpty() || specialization == null || workingDaysStr.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || discountStr.isEmpty() || ageStr.isEmpty() || gender.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validatePassword(password))
        {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and include at least 1 digit and 1 special character.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try
        {
            double discountRate = Double.parseDouble(discountStr);
            int age = Integer.parseInt(ageStr);
            Doctor newDoctor = doctorManager.registerDoctor(name, password, specialization, workingDaysStr, startTime, endTime, discountRate, age, gender);

            if (newDoctor != null)
            {
                JOptionPane.showMessageDialog(this, "Doctor registration successful! Welcome, Dr. " + name + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshDoctorComboBox();
                mainContentCardLayout.show(mainContentPanel, "DOCTOR_LOGIN");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Registration failed. Name may be taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Invalid discount rate or age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePatientSignup(JTextField nameField, JPasswordField passwordField, JTextField contactField, JTextField emailField, JTextField ageField, ButtonGroup genderGroup)
    {
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String contact = contactField.getText().trim();
        String email = emailField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = "";
        Enumeration<AbstractButton> elements = genderGroup.getElements();
        while (elements.hasMoreElements()) {
            AbstractButton button = elements.nextElement();
            if (button.isSelected()) {
                gender = button.getText();
                break;
            }
        }

        if (name.isEmpty() || password.isEmpty() || contact.isEmpty() || ageStr.isEmpty() || gender.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!validatePassword(password))
        {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and include at least 1 digit and 1 special character.", "Password Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (patientManager.isPatientRegistered(name))
        {
            JOptionPane.showMessageDialog(this, name + " is already registered. Please log in.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try
        {
            int age = Integer.parseInt(ageStr);
            Patient newPatient = patientManager.registerPatient(name, password, contact, email, age, gender);

            if (newPatient != null)
            {
                JOptionPane.showMessageDialog(this, "Registration successful! Welcome, " + name + ". Please log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                mainContentCardLayout.show(mainContentPanel, "PATIENT_LOGIN");
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Registration failed. Name may be taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(this, "Invalid age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handlePatientSignup(ActionEvent e)
    {
        String name = patientNameField.getText().trim();
        String password = new String(patientPasswordField.getPassword());

        if (name.isEmpty() && !lastPatientNameAttempt.isEmpty())
        {
            name = lastPatientNameAttempt;
        }

        if (patientManager.isPatientRegistered(name))
        {
            JOptionPane.showMessageDialog(this, name + " is already registered. Please log in.", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
            Enumeration<AbstractButton> elements = genderGroup.getElements();
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

            if (!validatePassword(newPassword))
            {
                JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and include at least 1 digit and 1 special character.", "Password Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try
            {
                int age = Integer.parseInt(ageStr);
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

    private void refreshDoctorComboBox()
    {
        doctorNameComboBox.removeAllItems();
        List<String> doctorNames = doctorManager.getDoctorNamesList();
        for (String name : doctorNames)
        {
            doctorNameComboBox.addItem(name);
        }
    }

    private void handleViewPublicFeedback(ActionEvent e)
    {
        // Switch to feedback panel
        mainContentCardLayout.show(mainContentPanel, "FEEDBACK");
    }

    // --- Custom Rounded Button ---
    private static class RoundedButton extends JButton
    {
        public RoundedButton(String text)
        {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("SansSerif", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(220, 50));
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gradient background
            GradientPaint gp = new GradientPaint(0, 0, new Color(70, 130, 180), 0, getHeight(), new Color(100, 149, 237));
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            // Shadow effect
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(2, 2, getWidth(), getHeight(), 20, 20);

            super.paintComponent(g);
            g2.dispose();
        }
    }

    // --- Custom Centering Layout ---
    private static class CenteringLayout extends CardLayout
    {
        @Override
        public void layoutContainer(Container parent)
        {
            super.layoutContainer(parent);
            Component[] components = parent.getComponents();
            for (Component comp : components)
            {
                if (comp.isVisible())
                {
                    Boolean center = (Boolean) ((JComponent) comp).getClientProperty("center");
                    if (center != null && !center)
                    {
                        // Do not center, let it fill the space
                        comp.setBounds(0, 0, parent.getWidth(), parent.getHeight());
                    }
                    else
                    {
                        // Center the component
                        Dimension pref = comp.getPreferredSize();
                        int x = (parent.getWidth() - pref.width) / 2;
                        int y = (parent.getHeight() - pref.height) / 2;
                        comp.setBounds(x, y, pref.width, pref.height);
                    }
                }
            }
        }
    }
}
