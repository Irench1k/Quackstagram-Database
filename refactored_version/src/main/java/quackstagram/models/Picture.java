package quackstagram.models;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

// Represents a picture on Quackstagram
public class Picture extends AbstractModel<Picture> implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private String pictureID;
    private String owner;
    private String caption;
    private String date;
    private int likesCount;

    public Picture(String pictureId, String owner, String caption, String date, int likesCount) {
        this.pictureID = pictureId;
        this.owner = owner;
        this.caption = caption;
        this.date = date;
        this.likesCount = likesCount;
    }

    public static Picture createInstance(String[] args) throws RuntimeException {
        if (args.length != 5) {
            System.out.println(String.join(", ", args));
            throw new RuntimeException("Could parse picture line, expected 5 arguments!");
        }
        int likes = Integer.parseInt(args[4]);
        return new Picture(args[0], args[1], args[2], args[3], likes);
    }

    public static Picture createNewForUser(String owner, String caption) {
        // UNIX timestamp
        String pictureId = String.valueOf(Instant.now().getEpochSecond());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCurrentDate = ZonedDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).format(formatter);

        return new Picture(
                pictureId,
                owner,
                caption,
                formattedCurrentDate,
                0);
    }

    @Override
    public String[] serialize() {
        return new String[] { pictureID, owner, caption, date, String.valueOf(likesCount) };
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public boolean isIdEqualTo(Picture picture) {
        return this.pictureID.equals(picture.pictureID);
    }

    public String getOwner() {
        return this.owner;
    }

    /**
     * Increases the number of likes for this picture by one and notifies the
     * observers.
     * 
     * Observer Design Pattern
     */
    public void addLike() {
        likesCount++;
        notifyObservers();
    }

    /**
     * Adds an observer to the list of observers for this Picture.
     *
     * @param observer the observer to be added
     * 
     *                 Observer Design Pattern
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Notifies all the observers by calling their update method.
     * 
     * Observer Design Pattern
     */
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Removes an observer from the list of observers for this Picture.
     *
     * @param observer the observer to be removed
     * 
     *                 Observer Design Pattern
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public String getCaption() {
        return caption;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getPictureID() {
        return pictureID;
    }

    public String getPath() {
        return "img/uploaded/" + pictureID + ".png";
    }

    public String getDate() {
        return date;
    }
}
