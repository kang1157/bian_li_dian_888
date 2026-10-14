import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Shop implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    HashMap<String, Product> pros    =new HashMap<>();//商品集合
    ArrayList<Record>        records =new ArrayList<>();//记录集合
    HashMap<String, User>    users   =new HashMap<>();//用户集合

    //用户注册
    public int register(User user) {
        if(!users.containsKey(user.userName)) {
            users.put(user.userName, user);
            System.out.println("注册成功！");
            return 1;
        } else {
            System.out.println("用户名已存在！");
            return 0;
        }
    }
    //用户登录
    public int login(String userName, String password) {
        if(users.containsKey(userName) && users.get(userName).password.equals(password)) {
            return 1;
        } else {
            return 0;
        }
    }

    //采购商品
    public void addPro(Product pro, double amount, int count) {//采购
        if(!pros.containsKey(pro.name)){//如果商品不存在
            pros.put(pro.name,pro);//添加商品信息
        }
        pros.get(pro.name).count+=count;//更新商品库存

        records.add(new Record(pro.name, LocalDateTime.now(), count, amount*count, 0,0));
    }

    //销售商品
    public int sell(String procode,double price,int count) {//销售
        if(pros.containsKey(procode) && pros.get(procode).count>=count){
            pros.get(procode).count-=count;
            records.add(new Record(procode, LocalDateTime.now(), count, 0, price*count, 1));
            return 1;
        }
        return 0;
    }
    //库存预警
    public void warn() {
        for(Product pro:pros.values()) {
            if(pro.count<=10) {
                System.out.println("商品"+pro.name+"库存预警!  商品剩余数量："+pro.count);
            }
        }
    }
    //查看库存
    public void query(){
        for(Product pro:pros.values()){
            System.out.println(pro.name+"     数量："+pro.count);
        }
    }
    //查看账本
    public void queryRecord(){
        for(Record record:records){
            System.out.println("时间： "+record.date.toLocalDate()+", 商品代码： "+record.procode+", 数量： "+record.count+", 金额： "+(record.amount+record.price)+", 类型： "+record.type);
        }
    }

    //每日利润统计  &&record.date.isEqual(LocalDateTime.now().minusDays(1))
    public void profit() {
        double ru=0;//收入
        double chu=0;//支出
        for(Record record:records) {
            if(record.type==0 && record.date.toLocalDate().isEqual(LocalDate.now())) {
                chu+=(record.amount);
                System.out.println("支出："+chu);
            }else if(record.type==1 && record.date.toLocalDate().isEqual(LocalDate.now())) {
                ru+=(record.price);
                System.out.println("收入："+ru);
            }
        }
        System.out.println("今日利润为："+(ru-chu));
    }
    //个人信息

    //修改密码

    //行为日志

    //会员

    //面板

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        Shop shop = new Shop();
        boolean isLogin = false;

        //创建序列化对象文件
//        File file=new File("C:/JAVA2/xiang_mu/bian_li_dian_888/bian_li_dian_888/src/shop.ser");
//        if(!file.exists()){
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                System.out.println("未找到序列化对象文件");
//            }
//        }
        // 反序列化：文件 -> 对象
//        try (ObjectInputStream ois = new ObjectInputStream(
//                new FileInputStream("C:/JAVA2/xiang_mu/bian_li_dian_888/bian_li_dian_888/src/shop.ser"))) {
//            shop  = (Shop) ois.readObject();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        Object obj = ObjectUtile.readObjectFromFile("C:/JAVA2/xiang_mu/bian_li_dian_888/bian_li_dian_888/src/shop.ser");
        if (obj instanceof Shop) {
            shop = (Shop) obj;
            System.out.println("已从文件加载历史数据！");
        } else {
            System.out.println("未发现数据文件，使用全新数据！");
        }
        //登录
        while (!isLogin) {
            System.out.println("用户登录，请输入用户名：");
            String userName = scanner.next();
            System.out.println("请输入密码：");
            String password = scanner.next();
            int result = shop.login(userName, password);
            if (result == 1) {
                System.out.println("登录成功！");
                isLogin = true;
            } else {
                System.out.println("登录失败！请注册后登录");
                System.out.println("用户注册，请输入用户名：");
                String regUserName = scanner.next();
                System.out.println("请输入密码：");
                String regPassword = scanner.next();
                int regResult = shop.register(new User(regUserName, regPassword));
                if (regResult == 1) System.out.println("注册成功！");
                else System.out.println("注册失败！!!");
            }
        }

        while (true) {
            System.out.println("=========================================");
            System.out.println("===============摊位管理系统================");
            System.out.println("=========================================");
            System.out.println("1.采购商品 2.销售商品 3.查看库存 4.查看账本 5.退出系统");
            int mark = scanner.nextInt();
            shop.warn();//库存预警
            switch (mark) {
                case 1: {
                    System.out.println("请输入商品名称：");
                    String name = scanner.next();
                    System.out.println("请输入商品价格：");
                    double price = scanner.nextDouble();
                    System.out.println("请输入商品数量：");
                    int count = scanner.nextInt();
                    Product product = new Product(0, name);
                    shop.addPro(product, price, count);
                    System.out.println("采购成功！");
                    break;
                }
                case 2: {
                    System.out.println("请输入商品名称：");
                    String name = scanner.next();
                    System.out.println("请输入商品价格：");
                    double price = scanner.nextDouble();
                    System.out.println("请输入商品数量：");
                    int count = scanner.nextInt();
                    int result = shop.sell(name, price, count);
                    if (result == 1) {
                        System.out.println("销售成功！");
                        shop.warn();
                    }
                    else System.out.println("销售失败！");
                    break;
                }
                case 3: {
                    shop.query();
                    break;
                }
                case 4: {
                    shop.queryRecord();
                    shop.profit();
                    break;
                }
                case 5: {
                    shop.profit();
                    //序列化
//                    try (ObjectOutputStream oos = new ObjectOutputStream(
//                            new FileOutputStream("C:/JAVA2/xiang_mu/bian_li_dian_888/bian_li_dian_888/src/shop.ser"))) {
//                        oos.writeObject(shop);
//                        System.out.println("对象序列化成功");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    ObjectUtile.saveObjectToFile(shop, "C:/JAVA2/xiang_mu/bian_li_dian_888/bian_li_dian_888/src/shop.ser");
                    System.exit(0);
                    break;
                }
                default:{
                    System.out.println("输入错误，请重新输入！");
                }
            }
        }
    }

}

