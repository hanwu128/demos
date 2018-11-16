package com.hw;

/**
 * 取钱
 */
public class UnSafeTest01 {
    public static void main(String[] args) {
        Account account = new Account(100, "总额");
        Drawing d1 = new Drawing(account, 80, "张三");
        Drawing d2 = new Drawing(account, 90, "李四");

        d1.start();
        d2.start();
    }
}

//账户
class Account {
    int money;//存款
    String name;//账户名

    public Account(int money, String name) {
        this.money = money;
        this.name = name;
    }
}

//模拟取款
class Drawing extends Thread {
    Account account;//取钱的账户
    int drawingMoney;//取得钱数
    int packetTotal;//取的总数

    public Drawing(Account account, int drawingMoney, String name) {
        super(name);
        this.account = account;
        this.drawingMoney = drawingMoney;
    }

    @Override
    public void run() {
        if (account.money - drawingMoney < 0) return;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.money -= drawingMoney;
        packetTotal += drawingMoney;
        System.out.println(this.getName() + "-->账户余额：" + account.money);
        System.out.println(this.getName() + "-->取钱总数：" + packetTotal);
    }
}