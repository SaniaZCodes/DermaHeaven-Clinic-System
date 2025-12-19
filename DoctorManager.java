import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class DoctorManager implements Serializable 
{ 
     private ArrayList<Doctor> doctorList;
     private final String FILE_NAME = "doctors.ser";

     private void loadDefaultDoctors()
     {
          // as  Doctor constructor is: new Doctor(ID, Name, Specialization, Working Days, Working Hours, Contact, Password, Age, Gender)

          // Doctor 1 (Rehan Khan's password is "rhpass")
          doctorList.add(new Doctor("D001",
                                   "Rehan Khan",
                                   "Dermatology (Skin/Hair)",
                                   "Monday, Tuesday",
                                   "9:00am to 1:00pm",
                                   "0301-1234567",
                                   "rhpass",
                                   0,
                                   "Not Specified"));

          // Doctor 2 (Sarah Ahmed's password is "sahpass")
          doctorList.add(new Doctor("D002",
                                   "Sarah Ahmed",
                                   "Laser & PRP Specialist",
                                   "Wednesday to Friday",
                                   "2:00pm to 5:00pm",
                                   "0302-7654321",
                                   "sahpass",
                                   0,
                                   "Not Specified"));

          // Doctor 3 (Ayesha Malik's password is "aypass")
          doctorList.add(new Doctor("D003",
                                   "Ayesha Malik",
                                   "Aesthetic Surgery Consult",
                                   "Saturday, Sunday",
                                   "4:00pm to 8:00pm",
                                   "0303-9876543",
                                   "aypass",
                                   0,
                                   "Not Specified"));
     }
 
     @SuppressWarnings("unchecked")
     public DoctorManager()
     {
          doctorList = Persistence.load(FILE_NAME);
          if (doctorList == null)
               {
                    doctorList = new ArrayList<>();
                    Persistence.save(doctorList, FILE_NAME);
               }
     }

     // doctor names list
     public List<String> getDoctorNamesList() 
     {
          return doctorList.stream()
          .map(Doctor::getDoctorName)
          .collect(Collectors.toList());
     }
 
     // Retrieves the Doctor object by name for password verification.
     public Doctor getDoctorByName(String name) 
     {
          return doctorList.stream()
          .filter(d -> d.getDoctorName().equalsIgnoreCase(name))
          .findFirst()
          .orElse(null);
     }
 
     public Doctor addDoctor(String name, String specialization, String workingDays, String workingHours, String contact, String password)
     {
          if (name == null || name.trim().isEmpty() || password == null || password.isEmpty())
               {
                    return null;
               }

          String newId = "D" + String.format("%03d", doctorList.size() + 1);
          Doctor newDoctor = new Doctor(newId, name, specialization, workingDays, workingHours, contact, password, 0, "Not Specified");
          doctorList.add(newDoctor);
          Persistence.save(doctorList, FILE_NAME);

          return newDoctor;
     }
 
     public List<Doctor> getAllDoctors()
          {
               return doctorList;
          }

     public Doctor registerDoctor(String name, String password, String specialization, String workingDays, String startTime, String endTime, double discountRate, int age, String gender)
     {
          if (name == null || name.trim().isEmpty() || password == null || password.isEmpty())
          {
               return null;
          }

          String workingHours = startTime + " to " + endTime;
          String newId = "D" + String.format("%03d", doctorList.size() + 1);
          Doctor newDoctor = new Doctor(name, password, specialization, workingDays, workingHours, discountRate, age, gender);
          newDoctor.setDoctorId(newId);
          doctorList.add(newDoctor);
          Persistence.save(doctorList, FILE_NAME);

          return newDoctor;
     }
}