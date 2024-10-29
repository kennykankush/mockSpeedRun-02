public class Item {

    private String id;
    private String title;
    private Double rating;
    private Double price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Item(String id, String title, Double price, Double rating){
        this.id = id;
        this.title = title;
        this.rating = rating;
        this.price = price;
    }

    @Override
    public String toString(){
        return id + ", " + title + ", " + rating + ", " + price;
    }

    public String[] toArray(){
        
        return new String[]{id, title, Double.toString(rating), Double.toString(price)};
    }
    
}
