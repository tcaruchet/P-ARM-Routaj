import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Test {

    public static void main(String[] args) {
        List<String> liste = new ArrayList<>();
        List<Integer> l = new ArrayList<>();

        Consumer<String> c1 = s -> liste.add(s);
        Consumer<String> c2 = System.out::println;

        //Consumer<String> c3 =c1.andThen(c2);
        l.stream().forEach(liste::add);
    }
}
