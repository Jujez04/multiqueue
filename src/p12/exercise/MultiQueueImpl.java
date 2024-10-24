package p12.exercise;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private final Map<Q, List<T>> map;

    public MultiQueueImpl(Map<Q, List<T>> map) {
        this.map = map;
    }

    public MultiQueueImpl(){
        this(new HashMap<Q, List<T>>());
    }

    @Override
    public Set<Q> availableQueues() {
       return this.map.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
        if ( !this.map.containsKey(queue) ) {
            this.map.put(queue, new LinkedList<>());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        if ( !this.map.containsKey(queue) ) {
            return this.map.get(queue).isEmpty();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if ( !this.map.containsKey(queue) ) {
            this.map.get(queue).add(elem);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public T dequeue(Q queue) {
        if ( !this.map.containsKey(queue) ) {
            return this.map.get(queue).removeFirst();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequeued = new HashMap<>();
        for ( Q i : this.availableQueues()) {
            T removed = this.dequeue(i);
            dequeued.put(i, removed);
        }
        return dequeued;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> enqueued = new HashSet<>();
        for ( Q i : this.availableQueues()) {
            enqueued.addAll(this.map.get(i));
        }
        return enqueued;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if ( !this.map.containsKey(queue) ) {
            List<T> allDequeued = this.map.get(queue);
            this.map.get(queue).clear();
            return allDequeued;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeQueueAndReallocate'");
    }

}
