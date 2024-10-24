package p12.exercise;

import java.util.*;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private final Map<Q, List<T>> map;

    public MultiQueueImpl(Map<Q, List<T>> map) {
        this.map = map;
    }

    public MultiQueueImpl(){
        this(new HashMap<Q, List<T>>());
    }

    private List<T> getQueue(Q queue){
        if ( this.map.containsKey(queue) ) {
            return this.map.get(queue);
        } else {
            throw new IllegalArgumentException();
        }
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
        if ( this.map.containsKey(queue) ) {
            return this.getQueue(queue).isEmpty();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void enqueue(T elem, Q queue) {
        if ( this.map.containsKey(queue) ) {
            this.getQueue(queue).add(elem);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public T dequeue(Q queue) {
        if ( this.map.containsKey(queue) ) {
            try {
                return this.getQueue(queue).removeFirst();
            } catch (NoSuchElementException e) {
                return null;
            }
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
            enqueued.addAll(this.getQueue(i));
        }
        return enqueued;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if ( this.map.containsKey(queue) ) {
            List<T> allDequeued = new ArrayList<>();
            while( !this.getQueue(queue).isEmpty() ) {
                allDequeued.add(this.dequeue(queue));
            }
            return allDequeued;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        if(this.map.containsKey(queue)){
            List<T> queueToBeClosed = this.dequeueAllFromQueue(queue);
            for ( Q i : this.availableQueues()) {
                if(!queueToBeClosed.isEmpty()){
                    this.enqueue(queueToBeClosed.removeLast(), queue);
                }
            }
            if (!queueToBeClosed.isEmpty()) {
                throw new IllegalStateException();
            }
            this.map.remove(queue);
        } else {
            throw new IllegalArgumentException();
        }
        
    }

}
