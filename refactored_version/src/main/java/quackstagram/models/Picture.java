package quackstagram.models;

import java.io.File;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.TimeZone;

// Represents a picture on Quackstagram
public class Picture extends AbstractModel<Picture> {
    private String pictureID;
    private String owner;
    private String caption;
    private String date;
    private int likesCount;

    public String ducky = "img\\defaultImages\\ducky.png";
    public String marxy = "img\\defaultImages\\marxy.png";
    public String monety = "img\\defaultImages\\monety.png";
    public String programmy = "img\\defaultImages\\programmy.png";
    public String sailormoony = "img\\defaultImages\\sailormoony.png";

    

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            throw new RuntimeException("Could not parse picture line, expected 5 arguments!");
        }
        int likes = Integer.parseInt(args[4]);
        return new Picture(args[0], args[1], args[2], args[3], likes);
    }
    
    public static Picture createNewForUser(String owner, String caption) {
        String pictureId = String.valueOf(Instant.now().getEpochSecond());
        String formattedCurrentDate = ZonedDateTime.now(TimeZone.getTimeZone("UTC").toZoneId()).format(formatter);
        return new Picture(pictureId, owner, caption, formattedCurrentDate, 0);
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

    // Increment likes count
    public void addLike() {
        likesCount++;
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
    String[] defaultImagePaths = {ducky, marxy, monety, programmy, sailormoony};
    Random random = new Random();
    int index = random.nextInt(defaultImagePaths.length);
    String selectedImagePath = defaultImagePaths[index];

    File file = new File(selectedImagePath);
    if (!file.exists()) {
        // If the file doesn't exist, return a default path
        return "default_image_not_found.png";
    }
    return selectedImagePath;
}

    public String getDate() {
        return date;
    }
}
