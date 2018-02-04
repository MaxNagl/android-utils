/*
 * Copyright (C) 2018 Max Nagl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
