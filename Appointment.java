import java.io.Serializable;
public class Appointment implements Serializable 
{
    private String appointmentId;
    private String patientName; 
    private String doctorName;  
    private String itemName;    // Service or Package Name
    private double itemPrice;   // Final price of the Service or Package
    private String date;        // Format: YYYY-MM-DD
    private String time;        // Format: HH:MM
    private String status;      // e.g., "BOOKED", "COMPLETED", "CANCELLED"
    
    public Appointment(String appointmentId, String patientName, String doctorName, String itemName, double itemPrice, String date, String time) 
    {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.date = date;
        this.time = time;
        this.status = "BOOKED"; 
    }

    // Getters
    public String getAppointmentId() 
    {
        return appointmentId;
    }
    
    public String getPatientName() 
    {
        return patientName;
    }

    public String getDoctorName() 
    {
        return doctorName;
    }

    public String getItemName() 
    {
        return itemName;
    }

    public double getItemPrice()
    {
        return itemPrice;
    }

    public double getPrice()
    {
        return itemPrice;
    }

    public String getDate() 
    {
        return date;
    }

    public String getTime() 
    {
        return time;
    }

    public String getStatus() 
    {
        return status;
    }

    // Setters 
    public void setStatus(String status) 
    {
        this.status = status;
    }
    
    public void setDate(String date) 
    {
        this.date = date;
    }

    public void setTime(String time) 
    {
        this.time = time;
    }

    @Override
    public String toString() 
    {
        return "\n--- Appointment Details ---" +
               "\nID: " + appointmentId +
               "\nPatient: " + patientName +
               "\nDoctor: Dr. " + doctorName +
               "\nItem: " + itemName + 
               "\nPrice: Rs. " + String.format("%.2f", itemPrice) +
               "\nDate: " + date +
               "\nTime: " + time +
               "\nStatus: " + status;
    }
}