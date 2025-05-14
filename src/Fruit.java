public class Fruit {
    private int id;
    private String name;
    private double price;
    private String unit;
    private String imagePath;

    // 构造方法 + getter 和 setter
    public Fruit(String name, double price, String unit, String imagePath) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getImagePath() { return imagePath; }

    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
