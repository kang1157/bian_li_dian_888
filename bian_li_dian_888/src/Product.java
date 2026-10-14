import java.io.Serializable;

public class Product implements Serializable {


    String name;//商品名称
    int count;//商品库存

    public Product(int count, String name) {
        this.count = count;
        this.name = name;
    }
}
