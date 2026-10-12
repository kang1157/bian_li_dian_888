import java.time.LocalDateTime;

public class Record {
    LocalDateTime date;//日期
    String        procode;//商品编号
    int           count;//数量
    double        amount;//成本金额
    //double cost;//商品成本价
    double        price;//商品销售价
    int           type;//1:销售 0:采购

    public Record(String procode, LocalDateTime date, int count, double amount, double price, int type) {
        this.procode = procode;
        this.date = date;
        this.count = count;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }
}
