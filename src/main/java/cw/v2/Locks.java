package cw.v2;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class Locks {
    private static final Supplier<Locker> locker = Suppliers.memoize(MyLockerImpl::new);

    public static Mutex lock(Object obj) {
        return locker.get().lock(obj);
    }
}
