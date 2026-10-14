import java.io.*;

public class ObjectUtile {
    /**
     * 对象工具类
     */

    /**
     * 保存对象到文件 序列化
     * @param obj 要保存的对象
     * @param filePath 文件路径
     */
    public static void saveObjectToFile(Object obj,String filePath){
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(obj);
            System.out.println("对象序列化成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取对象 反序列化
     * @param filePath 文件路径
     * @return 读取的对象
     */
    public static Object readObjectFromFile(String filePath){
        Object obj = null;
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("数据文件不存在：" + filePath);
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
             obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
