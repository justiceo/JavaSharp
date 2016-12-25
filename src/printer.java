import java.util.Iterator;

/**
 * Created by Justice on 12/24/2016.
 */
public final class printer {

    public static <T extends Iterable> void print(T iterable) {
        Iterator t = iterable.iterator();
        while(t.hasNext()) {
            print(t.next());
        }
    }

    public static void print(Object o) {
        System.out.println(o.toString());
    }

    public static void print(Object o, int index) {
        System.out.println(index + ". " + o.toString());
    }
}
