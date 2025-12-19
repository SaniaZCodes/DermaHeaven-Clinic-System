import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;

public class AppointmentManager implements Serializable
{
    private ArrayList<Appointment> appointmentList;
    private DoctorManager doctorManager;
    private final String FILE_NAME = "appointments.ser";
    private int appointmentCounter;

    // Constructor & Counter initialization 
    @SuppressWarnings("unchecked")
    public AppointmentManager(DoctorManager doctorManager)
    {
        this.doctorManager = doctorManager;
        appointmentList = Persistence.load(FILE_NAME);// because it is converted into list not object
        if (appointmentList == null || appointmentList.isEmpty())
            {
                appointmentList = new ArrayList<>();
            }
        initializeCounter();
    }

    // Counter initialization logic 
    private void initializeCounter()
    {
        appointmentCounter = 1;
        for (Appointment appt : appointmentList)
            {
                try
                {
                    String idNumStr = appt.getAppointmentId().substring(1);
                    int idNum = Integer.parseInt(idNumStr);
                    if (idNum >= appointmentCounter)
                        {
                            appointmentCounter = idNum + 1;
                        }
                }
                catch (Exception e)
                {
                }
            }
    }

    private Appointment findAppointmentById(String appointmentId) {
        return appointmentList.stream()
            .filter(a -> a.getAppointmentId().equalsIgnoreCase(appointmentId))
            .findFirst()
            .orElse(null);
    }

    private List<Appointment> getPatientAppointments(String patientName, boolean includeAllStatus)
    {
        return appointmentList.stream()
            .filter(a -> a.getPatientName().equalsIgnoreCase(patientName))
            .filter(a -> includeAllStatus || a.getStatus().equalsIgnoreCase("BOOKED"))
            .sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime))
            .collect(Collectors.toList());
    }

    public boolean checkAvailability(Doctor doctor, String date, String time)
    {
        if (doctor == null) return false;

        try
        {
            // 1. Parse Date and Determine Day of Week
            LocalDate apptDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d-M-yyyy"));
            DayOfWeek dayOfWeek = apptDate.getDayOfWeek();
            String apptDay = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH);

            // 2. Check Day of the Week against Doctor's Working Days
            String workingDaysStr = doctor.getWorkingDays();
            boolean dayFound = false;

            String normalizedWorkingDays = workingDaysStr.toLowerCase().replace(",", "").trim();

            if (workingDaysStr.contains(apptDay))
                {
                    dayFound = true;
                }
            else if (normalizedWorkingDays.contains(" to "))
                {
                    String[] parts = normalizedWorkingDays.split(" to ");
                    if (parts.length == 2)
                        {
                            // Assuming days are correctly capitalized in Doctor's data (e.g., Monday, Tuesday)
                            DayOfWeek startDay = DayOfWeek.valueOf(parts[0].trim().toUpperCase());
                            DayOfWeek endDay = DayOfWeek.valueOf(parts[1].trim().toUpperCase());

                            // Standard case (Mon to Fri)
                            if (startDay.getValue() <= endDay.getValue())
                                {
                                    if (dayOfWeek.getValue() >= startDay.getValue() && dayOfWeek.getValue() <= endDay.getValue())
                                        {
                                            dayFound = true;
                                        }
                                }
                            else 
                                {
                                    if (dayOfWeek.getValue() >= startDay.getValue() || dayOfWeek.getValue() <= endDay.getValue())
                                        {
                                            dayFound = true;
                                        }
                                }
                        }
                }
            else 
                {
                    String[] workingDays = workingDaysStr.split("[,\\s\\-&]+");
                    for (String day : workingDays)
                        {
                            if (day.trim().equalsIgnoreCase(apptDay))
                                {
                                    dayFound = true;
                                    break;
                                }
                        }
                }

            // 3. Check for existing appointments at the exact date/time
            boolean timeSlotTaken = appointmentList.stream()
                .filter(a -> a.getDoctorName().equalsIgnoreCase(doctor.getDoctorName()))
                .filter(a -> a.getDate().equals(date))
                .filter(a -> a.getTime().equals(time))
                .filter(a -> a.getStatus().equalsIgnoreCase("BOOKED") || a.getStatus().equalsIgnoreCase("RESCHEDULED"))
                .findAny().isPresent();

            return dayFound && !timeSlotTaken;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    // NEW GUI-FOCUSED PUBLIC METHODS

    public String bookAppointment(String patientName, String doctorName, Bookable item, String date, String time)
    {
        Doctor doctor = doctorManager.getDoctorByName(doctorName);

        if (doctor == null) {
            return "Booking Failed: Doctor not found.";
        }

        if (!checkAvailability(doctor, date, time))
            {
                return "Booking Failed: Dr. " + doctorName + " is not available on " + date + " at " + time + ".";
            }

        double finalPrice = item.getFinalPrice();

        // Apply doctor's discount rate for custom packages
        if (item instanceof CustomPackage) {
            double doctorDiscount = doctor.getDiscountRate();
            double discountAmount = finalPrice * doctorDiscount;
            finalPrice = finalPrice - discountAmount;
        }

        String appointmentId = "A" + String.format("%04d", appointmentCounter++);

        Appointment newAppt = new Appointment(appointmentId, patientName, doctorName, item.getItemName(), finalPrice, date, time);

        appointmentList.add(newAppt);
        Persistence.save(appointmentList, FILE_NAME);
        return "Appointment " + appointmentId + " successfully booked! Item: " + item.getItemName();
    }

    // --- Viewing Methods ---

    public List<Appointment> getAppointmentsByPatient(String patientName, boolean includeAllStatus)
    {
        return getPatientAppointments(patientName, includeAllStatus);
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorName, boolean includeAllStatus)
    {
        return appointmentList.stream()
            .filter(a -> a.getDoctorName().equalsIgnoreCase(doctorName))
            .filter(a -> includeAllStatus || a.getStatus().equalsIgnoreCase("BOOKED") || a.getStatus().equalsIgnoreCase("COMPLETED"))
            .sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime))
            .collect(Collectors.toList());
    }

    // --- Reschedule Method ---

    public String rescheduleAppointment(String appointmentId, String newDate, String newTime)
    {
        Appointment selectedAppt = findAppointmentById(appointmentId);

        if (selectedAppt == null || !selectedAppt.getStatus().equalsIgnoreCase("BOOKED"))
            {
                return "Reschedule Failed: Appointment ID " + appointmentId + " not found or is not currently BOOKED.";
            }

        Doctor doctor = doctorManager.getDoctorByName(selectedAppt.getDoctorName());

        if (!checkAvailability(doctor, newDate, newTime))
            {
                return "Reschedule Failed: Dr. " + selectedAppt.getDoctorName() + " is not available at the new time/date.";
            }

        selectedAppt.setDate(newDate);
        selectedAppt.setTime(newTime);

        Persistence.save(appointmentList, FILE_NAME);
        return "Appointment " + appointmentId + " successfully RESCHEDULED to " + newDate + " at " + newTime + ".";
    }

    // --- Cancel Method ---

    public String cancelAppointment(String appointmentId)
    {
        Appointment selectedAppt = findAppointmentById(appointmentId);

        if (selectedAppt == null || !selectedAppt.getStatus().equalsIgnoreCase("BOOKED"))
            {
                return "Cancellation Failed: Appointment ID " + appointmentId + " not found or is not currently BOOKED.";
            }

        // Update status
        selectedAppt.setStatus("CANCELLED");

        Persistence.save(appointmentList, FILE_NAME);
        return "Appointment " + appointmentId + " successfully CANCELLED.";
    }

    // --- Doctor-Facing Update Method ---
    public String markAppointmentCompleted(String appointmentId)
    {
        String newStatus = "COMPLETED"; // 

        Appointment selectedAppt = findAppointmentById(appointmentId);

        if (selectedAppt == null || !selectedAppt.getStatus().equalsIgnoreCase("BOOKED"))
            {
                return "Update Failed: Appointment ID " + appointmentId + " not found or is not currently BOOKED.";
            }

        selectedAppt.setStatus(newStatus.toUpperCase());
        Persistence.save(appointmentList, FILE_NAME);
        return "Appointment " + appointmentId + " status updated to **" + newStatus.toUpperCase() + "**.";
    }

    // Getter for all appointments 
    public List<Appointment> getAllAppointments()
    {
        return appointmentList;
    }
}