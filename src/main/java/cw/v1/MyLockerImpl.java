package cw.v1;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyLockerImpl implements Locker {

    private LoadingCache<Object, Lock> locks = CacheBuilder.newBuilder()
            .weakValues()
            .build(new CacheLoader<Object, Lock>() {
                @Override
                public Lock load(Object key) {
                    return new ReentrantLock();
                }
            });

    @Override
    public Mutex lock(Object obj) {

        try {
            Lock lock = locks.get(obj);
            lock.lock();

            return new Mutex() {
                @Override
                public void release() {
                    lock.unlock();
                }
            };

        } catch (ExecutionException e) {
            throw new RuntimeException("This should not happen! No checked exceptions within cache loading method.");
        }
    }
}
