import java.util.*;
import java.io.Serializable;
import java.util.stream.Collectors; 

public class FeedbackManager implements Serializable
{
    private ArrayList<Feedback> feedbackList;
    private int feedbackCounter; 

    private final String FILE_NAME = "feedbacks.ser"; 

    @SuppressWarnings("unchecked")
    public FeedbackManager() 
    {
        feedbackList = Persistence.load(FILE_NAME);
        if (feedbackList == null) 
            {
                feedbackList = new ArrayList<>();
            }

        // Set feedbackCounter based on existing feedbacks
        feedbackCounter = 1;
        // Find the maximum existing ID number
        for (Feedback fb : feedbackList) 
        {
            try 
            {
                String idNumStr = fb.getFeedbackId().substring(1); 
                int idNum = Integer.parseInt(idNumStr);
                if (idNum >= feedbackCounter) 
                {
                    feedbackCounter = idNum + 1;
                }
            } 
            catch (Exception e) 
            {
                // If ID is invalid, skip it and continue the count.
            }
        }
    }

    public String submitFeedback(String patientName, String doctorName, String serviceName, String feedbackText, double rating) 
    {
        if (patientName == null || doctorName == null || serviceName == null || feedbackText == null || rating < 1.0 || rating > 5.0) 
            {
                return "Feedback submission failed: Invalid or missing input data.";
            }

        // Generate feedback ID
        String feedbackId = "F" + String.format("%03d", feedbackCounter++);
        
        Feedback fb = new Feedback(feedbackId, patientName, doctorName, serviceName, feedbackText, rating); 
        
        feedbackList.add(fb);

        // Save after adding
        Persistence.save(feedbackList, FILE_NAME);

        return "Thank you! Your feedback (" + feedbackId + ") has been recorded.";
    }
    
    public List<Feedback> getAllFeedbacks()
    {
        return feedbackList.stream()
            .sorted((fb1, fb2) -> fb2.getFeedbackId().compareTo(fb1.getFeedbackId())) // most recent will come first
            .collect(Collectors.toList());
    }
    
    public double getOverallAverageRating() 
    {
        return feedbackList.stream()
            .mapToDouble(Feedback::getRating)
            .average()
            .orElse(0.0);
    }

    public List<Feedback> getFeedbacksForDoctor(String doctorName) 
    {
        return feedbackList.stream()
            // KEY FILTER: Only show feedback addressed to the logged-in doctor
            .filter(fb -> fb.getDoctorName() != null && fb.getDoctorName().equalsIgnoreCase(doctorName))
            .sorted((fb1, fb2) -> fb2.getFeedbackId().compareTo(fb1.getFeedbackId()))
            .collect(Collectors.toList());
    }
    
    public double getDoctorAverageRating(String doctorName)
    {
        List<Feedback> filteredFeedback = getFeedbacksForDoctor(doctorName);
        
        return filteredFeedback.stream()
            .mapToDouble(Feedback::getRating)
            .average()
            .orElse(0.0);
    }
    
    // Getter for feedback list 
    public ArrayList<Feedback> getFeedbackList() 
    {
        return feedbackList;
    }
}