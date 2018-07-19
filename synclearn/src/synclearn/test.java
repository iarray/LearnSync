import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalVar<T> {

    public T initVaribale() {

        return null;
    }

    volatile ConcurrentHashMap<Thread, T> map = new ConcurrentHashMap<Thread, T>(); 

    public void put(T t) {

        map.put(Thread.currentThread(), t);
    }

    public T get() {

        T value = map.get(Thread.currentThread());
        if(null == value) {

            T t = initVaribale();
            if(null == t) {
                return null;
            }
            map.put(Thread.currentThread(), t);
            return t;
        } else {

            return value;
        }
    }
}