package ntnu.idatt2106.group8.gidd.model.DTO;

public class FeedbackDTO {
    private String sender;
    private String text;
    private int rating;

    public FeedbackDTO(String sender, String text, int rating) {
        this.sender = sender;
        this.text = text;
        this.rating = rating;
    }

    public FeedbackDTO() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
