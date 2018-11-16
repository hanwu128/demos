package com.hw;

/**
 * 静态代理
 * 1、真实角色
 * 2、代理角色
 */
public class StaticProxy {

    public static void main(String[] args) {
        new WeddingCompany(new You()).happyMarry();
    }

    interface Marry {
        void happyMarry();
    }

    //真是角色
    static class You implements Marry {

        @Override
        public void happyMarry() {
            System.out.println("you and ......");
        }
    }

    //代理角色
    static class WeddingCompany implements Marry {

        //真实角色
        private Marry target;

        public WeddingCompany(Marry target) {
            this.target = target;
        }

        @Override
        public void happyMarry() {
            ready();
            this.target.happyMarry();
            after();
        }

        private void ready() {
            System.out.println("布置现场......");
        }

        private void after() {
            System.out.println("完成......");
        }
    }

}
