
import java.io.*;
import java.util.ArrayList;

public class Persistence 
{
    private static final String DEFAULT_FILE_NAME = "data.ser";

    // Generic save method
    public static <T> void save(ArrayList<T> list, String fileName) 
    {
        try (FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) 
            {
                oos.writeObject(list);
            } 
        catch (IOException e) 
            {
                System.out.println("Error saving " + fileName + ": " + e.getMessage());
            }
    }

    // Generic load method
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> load(String fileName) 
    {
        try (FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis)) 
            {
                // Ensure the list is not null and is the correct type
                Object loadedObject = ois.readObject();
                if (loadedObject instanceof ArrayList) 
                    {
                        return (ArrayList<T>) loadedObject;
                    } 
                else 
                    {
                        System.out.println("Warning: Data in " + fileName + " is not an ArrayList. Starting fresh.");
                        return new ArrayList<>();
                    }
            } 
        catch (FileNotFoundException e) 
            {
                return new ArrayList<>(); 
            } 
        catch (IOException | ClassNotFoundException e) 
            {
                System.out.println("Error loading " + fileName + ". Data corrupted or incompatible. Starting fresh.");
                return new ArrayList<>();
            }
    }
}