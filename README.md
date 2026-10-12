# 便利店管理系统 (bian_li_dian_888)

一个基于 **Java SE** 的命令行便利店（摊位）管理系统，用于演示面向对象程序设计的基本实践：
商品采购入库、商品销售出库、库存查询与预警、账本流水与每日利润统计，以及用户注册登录。

项目刻意保持轻量：**无第三方依赖、无构建工具、无数据库**，仅用 `HashMap` 与 `ArrayList`
在内存中维护运行状态，适合作为 Java 课程设计、实验报告或初学 OOP 的参考实现。

---

## 目录

- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [快速开始](#快速开始)
- [使用说明](#使用说明)
- [业务规则](#业务规则)
- [核心类设计](#核心类设计)
- [数据结构](#数据结构)
- [已知限制](#已知限制)
- [后续改进方向](#后续改进方向)

---

## 功能特性

| 编号 | 功能 | 说明 |
| :---: | :--- | :--- |
| 1 | 用户注册 | 用户名唯一，重复注册会被拒绝并提示"用户名已存在" |
| 2 | 用户登录 | 校验用户名与密码，成功返回 `1`，失败返回 `0` |
| 3 | 采购商品 | 商品不存在则新建，存在则累加库存；同时写入一条采购流水 |
| 4 | 销售商品 | 库存充足才允许销售，扣减库存并写入一条销售流水 |
| 5 | 查看库存 | 遍历打印全部商品的名称与剩余数量 |
| 6 | 库存预警 | 每次进入主菜单时自动检查，剩余数量 ≤ 10 即打印预警 |
| 7 | 查看账本 | 打印全部流水（时间、商品代码、数量、金额、类型） |
| 8 | 每日利润统计 | 汇总当日支出与收入，输出"今日利润" |
| 9 | 退出系统 | 退出前打印一次当日利润后结束程序 |

设计要点：

- **商品以名称为唯一键**：`HashMap<String, Product>` 的 key 是商品名称，同名商品被视为同一件商品。
- **采购与销售共用流水表**：`Record.type` 区分 `0`（采购）与 `1`（销售），一张表记录全部进出。
- **库存不足拦截销售**：`sell()` 只有在商品存在且库存足够时才返回 `1`。
- **利润按自然日统计**：只统计 `date` 当天（`LocalDate.now()`）的流水，历史数据仍保留在账本中。

---

## 技术栈

- **语言**：Java（使用 `java.time.LocalDate` / `LocalDateTime`，需 JDK 8 及以上）
- **运行形态**：控制台（Console）交互程序
- **依赖**：无
- **开发环境**：IntelliJ IDEA（仓库内含 `.iml` 与 `.idea/` 配置）

---

## 项目结构

```text
lession1/
├── src/
│   ├── bian_li_dian_888/
│   │   ├── Shop.java      # 主类：菜单循环 + 注册登录 + 采购销售 + 查询统计
│   │   ├── Product.java   # 商品：名称 + 库存数量
│   │   ├── User.java      # 用户：用户名 + 密码
│   │   └── Record.java    # 流水：日期 + 商品编号 + 数量 + 金额 + 单价 + 类型
│   └── L1.java ~ L4.java  # 课程其他练习
├── out/                   # 编译输出（已在 .gitignore 中忽略）
├── .gitignore
├── lession1.iml
└── README.md
```

四个类职责分明：`商店` 负责业务流程与菜单交互，`Product` 负责商品与库存，
`User` 负责账号信息，`Record` 负责一笔进出的账目明细。

---

## 快速开始

### 环境要求

- JDK 8 或更高版本

```bash
java -version
javac -version
```

### 编译

在项目根目录执行：

```bash
# Windows
mkdir out
javac -encoding UTF-8 -d out src/bian_li_dian_888/*.java

# Linux / macOS
mkdir -p out
javac -encoding UTF-8 -d out src/bian_li_dian_888/*.java
```

> 源码与输出包含中文，Windows 终端如出现乱码，请先执行 `chcp 65001`，
> 或使用 `javac -encoding UTF-8` 并确保控制台输出编码为 UTF-8。

### 运行

```bash
java -cp out bian_li_dian_888.Shop
```

### 在 IDEA 中运行

1. `File → Open` 选择本项目（`lession1`）目录；
2. 确认模块 SDK 已配置为 JDK 8+；
3. 打开 `src/bian_li_dian_888/Shop.java`，点击 `main` 方法旁的运行按钮。

---

## 使用说明

启动后进入主菜单循环：

```text
=========================================
===============摊位管理系统================
=========================================
1.采购商品 2.销售商品 3.查看库存 4.查看账本 5.退出系统
```

### 示例会话

采购 100 件矿泉水（单价 1.5）：

```text
1
请输入商品名称：
矿泉水
请输入商品价格：
1.5
请输入商品数量：
100
采购成功！
```

销售 5 件矿泉水（售价 3）：

```text
2
请输入商品名称：
矿泉水
请输入商品价格：
3
请输入商品数量：
5
销售成功！
```

查看库存：

```text
3
矿泉水     数量：95
```

查看账本（账本后紧跟当日利润）：

```text
4
时间： 2025-01-01, 商品代码： 矿泉水, 数量： 100, 金额： 150.0, 类型： 0
时间： 2025-01-01, 商品代码： 矿泉水, 数量： 5, 金额： 15.0, 类型： 1
今日利润为：-135.0
```

> 提示：**每次进入主菜单都会先执行一次库存预警**，剩余数量 ≤ 10 的商品会直接打印提醒。

---

## 业务规则

### 采购（`addPro`）

```java
public void addPro(Product pro, double amount, int count) {
    if (!pros.containsKey(pro.name)) {
        pros.put(pro.name, pro);          // 新商品入库
    }
    pros.get(pro.name).count += count;    // 已存在则累加库存

    records.add(new Record(pro.name, LocalDateTime.now(), count, amount * count, 0, 0));
}
```

- 参数 `amount` 为**进货单价**，流水中的 `amount` 字段记录的是 `进货单价 × 数量` 的总额。
- 主菜单中输入的"商品价格"在采购时被当作进货单价使用。

### 销售（`sell`）

```java
public int sell(String procode, double price, int count) {
    if (pros.containsKey(procode) && pros.get(procode).count >= count) {
        pros.get(procode).count -= count;
        records.add(new Record(procode, LocalDateTime.now(), count, 0, price * count, 1));
        return 1;
    }
    return 0;
}
```

- 参数 `price` 为**销售单价**，流水中的 `price` 字段记录的是 `销售单价 × 数量` 的总额。
- 返回 `1` 表示销售成功，`0` 表示商品不存在或库存不足。

### 每日利润（`profit`）

```text
今日利润 = Σ 当日销售流水的 price  −  Σ 当日采购流水的 amount
```

| 类型值 | 含义 | 计入利润的字段 |
| :---: | :--- | :--- |
| `0` | 采购 | `amount`（支出） |
| `1` | 销售 | `price`（收入） |

### 库存预警阈值

```java
if (pro.count <= 10) {
    System.out.println("商品" + pro.name + "库存预警!  商品剩余数量：" + pro.count);
}
```

当前阈值 `10` 是硬编码的，修改该判断即可整体调整预警线。

---

## 核心类设计

### `商店` — 业务主类

| 成员 | 类型 | 说明 |
| :--- | :--- | :--- |
| `pros` | `HashMap<String, Product>` | 商品集合，键为商品名称 |
| `records` | `ArrayList<Record>` | 账本流水，按操作顺序追加 |
| `users` | `HashMap<String, User>` | 用户集合，键为用户名 |

| 方法 | 输入 | 输出 | 说明 |
| :--- | :--- | :--- | :--- |
| `register(User user)` | 用户对象 | `1` 成功 / `0` 重复 | 用户名已存在时拒绝注册 |
| `login(String userName, String password)` | 用户名、密码 | `1` 成功 / `0` 失败 | 校验用户名存在且密码相等 |
| `addPro(Product pro, double amount, int count)` | 商品、进货单价、数量 | 无 | 入库 + 写采购流水 |
| `sell(String procode, double price, int count)` | 商品名、销售单价、数量 | `1` 成功 / `0` 失败 | 校验库存后出库 + 写销售流水 |
| `warn()` | 无 | 打印预警 | 遍历商品检查低库存 |
| `query()` | 无 | 打印库存 | 输出每个商品的名称与数量 |
| `queryRecord()` | 无 | 打印账本 | 输出每条流水的明细 |
| `profit()` | 无 | 打印利润 | 汇总当日收入与支出 |
| `main(String[] args)` | 无 | 无 | 菜单循环与输入分发 |

### `Product` — 商品与库存

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `name` | `String` | 商品名称（同时作为集合键） |
| `count` | `int` | 商品库存数量 |

构造方法：`Product(int count, String name)`，注意参数顺序为**数量在前、名称在后**。

### `User` — 用户

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `userName` | `String` | 用户名 |
| `password` | `String` | 密码（当前为明文保存） |

构造方法：`User(String userName, String password)`。

### `Record` — 账目流水

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `date` | `LocalDateTime` | 发生时间 |
| `procode` | `String` | 商品编号（当前实际存的是商品名称） |
| `count` | `int` | 数量 |
| `amount` | `double` | 采购总额（销售流水为 `0`） |
| `price` | `double` | 销售总额（采购流水为 `0`） |
| `type` | `int` | `0` 采购 / `1` 销售 |

构造方法：`Record(String procode, LocalDateTime date, int count, double amount, double price, int type)`。

---

## 数据结构

```text
商店
 ├── pros : HashMap<商品名, Product>     ← 以名称查商品与库存，O(1) 平均
 │      "矿泉水" → Product(name="矿泉水", count=95)
 ├── records : ArrayList<Record>          ← 按操作顺序追加的账本
 │      [ Record(type=0, count=100, amount=150.0), Record(type=1, count=5, price=15.0) ]
 └── users : HashMap<用户名, User>        ← 注册去重与登录校验，O(1) 平均
        "admin" → User("admin", "123456")
```

- `pros` 与 `users` 使用 `HashMap`，使商品查找与用户校验均为 O(1) 平均复杂度。
- `records` 使用 `ArrayList` 顺序保存流水，`queryRecord()` 与 `profit()` 均为 O(n) 线性遍历。

---

## 已知限制

本项目定位为教学演示，以下限制是刻意保留的简化，也提醒使用者注意其影响：

1. **数据不持久化**：商品、用户与账本全部保存在内存中，程序退出即丢失。
2. **采购会覆盖同名商品信息**：`addPro()` 中 `pros.put(pro.name, pro)` 只在商品不存在时执行，
   但若传入的是已存在名称的新对象，库存会累加到旧对象上，商品的其他属性不会更新。
3. **账本金额打印为两者之和**：`queryRecord()` 输出 `record.amount + record.price`，
   对采购流水该值为采购总额，对销售流水该值为销售总额，单条流水含义正确但字段含义不够自洽。
4. **利润统计会重复累计打印**：`profit()` 在循环内打印累加中的收入/支出，输出较为啰嗦。
5. **只统计当天利润**：跨天查询历史利润需要扩展 `profit()` 的日期条件。
6. **登录流程被注释**：`main()` 中的注册/登录循环当前处于注释状态，直接进入业务菜单，
   `register()` 与 `login()` 已实现但尚未接入主流程。
7. **输入未做异常防护**：`scanner.nextInt()` / `nextDouble()` 遇到非数字输入会抛出异常。
8. **密码明文保存**：`User.password` 为明文，未做任何哈希处理。
9. **销售按名称匹配**：`sell()` 的参数名为 `procode`，实际传入的是商品名称，缺少独立的商品编码体系。
10. **单机单线程**：未考虑并发访问与多人同时操作同一份库存。
11. **未实现的功能**：源码中预留了"个人信息""修改密码""行为日志""会员"等空注释，
    属于规划中的待开发模块。

---

## 后续改进方向

- [ ] 接入注册/登录流程，并让业务菜单区分权限（如店长 / 店员）
- [ ] 将 `password` 改为加盐哈希存储（如 `MessageDigest` 或 BCrypt）
- [ ] 为 `Product` 增加独立的商品编码字段，`Record.procode` 改存编码而非名称
- [ ] 将 `amount` / `price` 语义统一为"单价 + 数量"，金额由二者计算得出
- [ ] 用 `LocalDate.now().equals(record.date.toLocalDate())` 抽取公共判断，支持按日期区间统计
- [ ] 增加按商品维度的进销存汇总（累计进货量、累计销量、当前库存、累计毛利）
- [ ] 使用 `try/catch` 或 `hasNextInt()` 处理非法输入
- [ ] 支持商品删除、改价与库存盘点
- [ ] 将预警阈值抽为常量或可配置项
- [ ] 将 `pros`、`users`、`records` 持久化到文件（如 CSV / JSON）或数据库（如 SQLite / MySQL）
- [ ] 抽出 `ShopService` 服务层，界面与业务解耦，便于扩展为 Swing / Web 版本
- [ ] 为 `sell()`、`addPro()`、`profit()` 编写 JUnit 单元测试

---

## 作者

[kang1157](https://github.com/kang1157)
