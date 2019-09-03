package hello.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputMessage {

    private Date creation= new Date();
    private String from;
    private String text;

    public OutputMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return new SimpleDateFormat("HH:mm").format(creation);
    }
}
