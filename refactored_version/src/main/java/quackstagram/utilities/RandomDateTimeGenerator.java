package quackstagram.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

// Thank you, ChatGPT
public class RandomDateTimeGenerator {
    public static void main(String[] args) {
        int numberOfDates = 12000;
        List<String> randomDates = generateRandomDates(numberOfDates);

        // Write the random dates to a file

        String filePath = "C:\\Users\\V\\Desktop\\[Databases]\\project\\final-project\\refactored_version\\src\\main\\java\\quackstagram\\utilities\\random_dates.txt";
        
        // Write the random dates to a file
        writeDatesToFile(randomDates, filePath);
    }

    public static List<String> generateRandomDates(int numberOfDates) {
        List<String> randomDates = new ArrayList<>();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Define the range: past two decades
        LocalDateTime start = LocalDateTime.now().minusYears(20);
        LocalDateTime end = LocalDateTime.now();

        for (int i = 0; i < numberOfDates; i++) {
            // Generate random datetime
            LocalDateTime randomDateTime = generateRandomDateTime(start, end);
            randomDates.add(randomDateTime.format(formatter));
        }

        return randomDates;
    }

    private static LocalDateTime generateRandomDateTime(LocalDateTime start, LocalDateTime end) {
        long startSeconds = start.toEpochSecond(java.time.ZoneOffset.UTC);
        long endSeconds = end.toEpochSecond(java.time.ZoneOffset.UTC);

        long randomSeconds = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);

        return LocalDateTime.ofEpochSecond(randomSeconds, 0, java.time.ZoneOffset.UTC);
    }

    private static void writeDatesToFile(List<String> dates, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String date : dates) {
                writer.write(date);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
