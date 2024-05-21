package quackstagram.models;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class Notification extends AbstractModel<Notification> {
    private String username; // whose image was liked
    private String likedBy; // who liked the image
    private String pictureId;
    private String date;

    public Notification(String username, String likedBy, String pictureId, String date) {
        this.username = username;
        this.likedBy = likedBy;
        this.pictureId = pictureId;
        this.date = date;
    }

    public Notification(String username, String likedBy, String pictureId) {
        this.username = username;
        this.likedBy = likedBy;
        this.pictureId = pictureId;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = ZonedDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).format(formatter);
    }

    public static Notification createInstance(String[] args) throws RuntimeException {
        if (args.length != 4) {
            System.out.println(String.join(", ", args));
            throw new RuntimeException("Couldn't parse notifications line, expected 4 arguments!");
        }
        return new Notification(args[0], args[1], args[2], args[3]);
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String[] serialize() {
        return new String[] {username, likedBy, pictureId, date};
    }

    @Override
    public boolean isUpdatable() {
        return false;
    }

    @Override
    public boolean isIdEqualTo(Notification notification) {
        return this.username.equals(notification.getUsername());
    }

    public String getMessage() {
        return likedBy + " liked your picture - " + getElapsedTime(date) + " ago";
    }

    private String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        }
        return timeElapsed.toString();
    }
}
