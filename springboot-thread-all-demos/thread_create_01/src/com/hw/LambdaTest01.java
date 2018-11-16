package com.hw;

/**
 * Lambda推导
 */
public class LambdaTest01 {

    static class Like2 implements ILike {

        @Override
        public void lambda() {
            System.out.println("I like lambda2");
        }
    }

    public static void main(String[] args) {
        ILike like = new Like();
        like = new Like2();
        like = new ILike() {
            @Override
            public void lambda() {
                System.out.println("I like lambda3");
            }
        };
        like = () -> {
            System.out.println("I like lambda4");
        };
        like.lambda();
    }

    interface ILike {
        void lambda();
    }

    static class Like implements ILike {

        @Override
        public void lambda() {
            System.out.println("I like lambda");
        }
    }

}
