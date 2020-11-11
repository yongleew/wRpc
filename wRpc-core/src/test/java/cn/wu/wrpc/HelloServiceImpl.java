package cn.wu.wrpc;

public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        System.out.println(name);
        return "Hello " + name + "!";
    }
}
