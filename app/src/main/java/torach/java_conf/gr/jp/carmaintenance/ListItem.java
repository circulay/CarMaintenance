package torach.java_conf.gr.jp.carmaintenance;

public class ListItem {

    private int id;

    private String category = null;
    private String date = null;
    private String price = null;
    private String notes = null;

    int getId() {return id;}

    String getCategory() {return category;}
    String getDate() {return date;}
    String getPrice() {return price;}
    String getNotes() {return notes;}

    void setId(int id) {this.id = id;}

    void setCategory(String category) {this.category = category;}
    void setDate(String date) {this.date = date;}
    void setPrice(String price) {this.price = price;}
    void setNotes(String notes) {this.notes = notes;}
}
