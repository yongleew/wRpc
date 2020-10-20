package cn.wu.wRpc.test;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        System.out.println("hello name");
    }

    @Override
    public String getName() {
        return "hello service";
    }
}
