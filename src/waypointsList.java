import java.util.ArrayList;

public class waypointsList<E> extends ArrayList<E> {
    //Custom arraylist that stores waypoints
    //there are two extra methods that help in creation of waypoint chunks.


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

    //Return specific number of waypoints
    public synchronized waypointsList<E> getNWaypoints(int value){
        waypointsList<E> temparray = new waypointsList<E>();
        for (int i=0; i<value;i++){
            temparray.add(super.get(i));
        }

        
        //Remove the selected elements
        for (int i=0; i<value;i++){
            super.remove(0);
        }
        
        return temparray;

    }
    
    //Return the whole list
    public synchronized waypointsList<E> getAll() {
        return this;
    }

    public synchronized void clearAll() {
        super.clear();
    }
	
}

