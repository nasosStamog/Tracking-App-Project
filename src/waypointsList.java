import java.util.ArrayList;

public class waypointsList<E> extends ArrayList<E> {

    @Override
    public synchronized boolean add(E e) {
        return super.add(e);
    }

    @Override
    public synchronized E get(int index) {
        return super.get(index);
    }

    @Override
    public synchronized E remove(int index) {
        return super.remove(index);
    }

    @Override
    public synchronized boolean remove(Object o) {
        return super.remove(o);
    }

    @Override
    public synchronized E set(int index, E element) {
        return super.set(index, element);
    }
}

