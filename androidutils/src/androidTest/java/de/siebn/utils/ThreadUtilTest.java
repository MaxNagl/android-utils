package de.siebn.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.*;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ThreadUtilTest {
    @Test
    public void testRunOnHandlerSync() throws Exception {
        final HandlerThread ht = new HandlerThread("testHandler");
        ht.start();
        AtomicReference<Thread> calledThread = new AtomicReference<>();

        ThreadUtils.runOnHandlerSync(new Handler(ht.getLooper()), new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(50);
                calledThread.set(Thread.currentThread());
            }
        });

        assertEquals(ht, calledThread.get());
    }

    @Test
    public void testRunOnExecutorSync() throws Exception {
        AtomicReference<Thread> calledThread = new AtomicReference<>();

        ThreadUtils.runOnExecutorSync(ThreadUtils.getBackgroundExecutor(), new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(50);
                calledThread.set(Thread.currentThread());
            }
        });

        assertNotEquals(null, calledThread.get());
        assertNotEquals(Thread.currentThread(), calledThread.get());
    }
}
