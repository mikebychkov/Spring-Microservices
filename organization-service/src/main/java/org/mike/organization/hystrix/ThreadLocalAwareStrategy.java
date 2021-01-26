package org.mike.organization.hystrix;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import org.mike.organization.utils.UserContextHolder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadLocalAwareStrategy extends HystrixConcurrencyStrategy {

    private HystrixConcurrencyStrategy existingConcurrencyStrategy;

    public ThreadLocalAwareStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
        this.existingConcurrencyStrategy = existingConcurrencyStrategy;
    }

    @Override
    public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
                : super.getBlockingQueue(maxQueueSize);
    }

    @Override
    public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
        return existingConcurrencyStrategy != null
                ? existingConcurrencyStrategy.getRequestVariable(rv)
                : super.getRequestVariable(rv);
    }

    @Override
    public ThreadPoolExecutor getThreadPool(HystrixThreadPoolKey threadPoolKey,
                                            HystrixProperty<Integer> corePoolSize,
                                            HystrixProperty<Integer> maximumPoolSize,
                                            HystrixProperty<Integer> keepAliveTime, TimeUnit unit,
                                            BlockingQueue<Runnable> workQueue) {
        ThreadPoolExecutor rsl = null;
        if (existingConcurrencyStrategy != null) {
            rsl = existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize,
                    maximumPoolSize, keepAliveTime, unit, workQueue);
        } else {
            rsl = super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize,
                    keepAliveTime, unit, workQueue);
        }
        return rsl;
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        Callable<T> rsl = null;
        if (existingConcurrencyStrategy != null) {
            rsl = existingConcurrencyStrategy.wrapCallable(
                    new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext())
            );
        } else {
            rsl = super.wrapCallable(
                    new DelegatingUserContextCallable<T>(callable, UserContextHolder.getContext())
            );
        }
        return rsl;
    }
}
