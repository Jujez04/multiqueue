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
        if ( !this.map.containsKey(queue) ) {
            throw new IllegalArgumentException();  // From L21 --> L19 : for better readability
        } 
        return this.map.get(queue);
    }

    @Override
    public Set<Q> availableQueues() {
       return this.map.keySet();
    }

    @Override
    public void openNewQueue(Q queue) throws IllegalArgumentException{
        if ( this.map.containsKey(queue) ) {
            throw new IllegalArgumentException();  // Modified for readability
        }
        this.map.put(queue, new LinkedList<>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) throws IllegalArgumentException {
        if ( !this.map.containsKey(queue) ) {
            throw new IllegalArgumentException();  //Modified
        }
        return this.getQueue(queue).isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) throws IllegalArgumentException{
        if ( !this.map.containsKey(queue) ) {
            throw new IllegalArgumentException(); //Modified  
        }
        this.getQueue(queue).add(elem);   
    }

    @Override
    public T dequeue(Q queue) throws IllegalArgumentException {
        if ( !this.map.containsKey(queue) ) {
            throw new IllegalArgumentException();
        }
        if (this.getQueue(queue).isEmpty()) {
            return null;
        }
        return this.getQueue(queue).remove(0);
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() throws IllegalArgumentException{
        Map<Q, T> dequeued = new HashMap<>();
        for ( Q i : this.availableQueues()) {
            dequeued.put(i, this.dequeue(i));
        }
        return dequeued;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> enqueued = new HashSet<>();
        for ( Q i : this.availableQueues()) {
            enqueued.addAll(new ArrayList<>(this.getQueue(i)));
        }
        return enqueued;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) throws IllegalArgumentException{
        if ( !this.map.containsKey(queue) ) {
            throw new IllegalArgumentException();
        } 
        List<T> allDequeued = new ArrayList<>(this.getQueue(queue));
        this.getQueue(queue).clear();    //Modified : now it creates a defensive copy and calls queue.clear 
        return allDequeued;    
    }

    @Override
    public void closeQueueAndReallocate(Q queue) throws IllegalArgumentException, IllegalStateException{
        if(!this.map.containsKey(queue)){
            throw new IllegalArgumentException();
        }
        List<T> queueToBeClosed = this.dequeueAllFromQueue(queue);
        this.map.remove(queue);
        //Use iterator for reallocating elements of the removed queue
        Iterator<Q> queueIterator = this.availableQueues().iterator();
        for (T elem : queueToBeClosed) {
            if (!queueIterator.hasNext()) {
                queueIterator = this.availableQueues().iterator(); 
            }
            this.enqueue(elem, queueIterator.next());
        }
        if (!queueToBeClosed.isEmpty()) {
            throw new IllegalStateException();
        }
    }
}