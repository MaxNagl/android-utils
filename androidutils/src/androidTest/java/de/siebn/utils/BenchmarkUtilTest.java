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

import android.os.SystemClock;
import android.support.test.runner.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.*;

@RunWith(AndroidJUnit4.class)
public class BenchmarkUtilTest {
    @BeforeClass
    public static void init() {
        BenchmarkUtil.setEnabled(true);
    }

    @Test
    public void testAutomatic() throws Exception {
        BenchmarkUtil.start("automatic");
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("automatic");
        SystemClock.sleep(20);
        BenchmarkUtil.benchmark("automatic");
        SystemClock.sleep(30);
        BenchmarkUtil.start("automatic");
        SystemClock.sleep(40);
        BenchmarkUtil.benchmark("automatic");
    }

    @Test
    public void testCascaded() throws Exception {
        BenchmarkUtil.start("cascaded");
        SystemClock.sleep(10);
        BenchmarkUtil.start("cascaded", "a");
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "a", "b"); // 10
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "a"); // 20
        SystemClock.sleep(10);
        BenchmarkUtil.start("cascaded", "c");
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "c", "d"); // 10
        SystemClock.sleep(15);
        BenchmarkUtil.benchmark("cascaded", "c", "e");  // 15
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "c", "e");  // 10 total: 25
        SystemClock.sleep(10);
        BenchmarkUtil.start("cascaded", "c", "e");
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "c", "e");  // 10 total: 35
        SystemClock.sleep(10);
        BenchmarkUtil.start("cascaded", "c", "f");
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "c", "f");  // 10
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded", "c");  // 85
        SystemClock.sleep(10);
        BenchmarkUtil.benchmark("cascaded");  // 135
        BenchmarkUtil.stats("cascaded");
    }

    @Test
    public void testManual() throws Exception {
        long benchmark = System.nanoTime();
        SystemClock.sleep(10);
        benchmark = BenchmarkUtil.benchmark("manual", benchmark);
        SystemClock.sleep(20);
        benchmark = BenchmarkUtil.benchmark("manual", benchmark);
        SystemClock.sleep(30);
        benchmark = System.nanoTime();
        SystemClock.sleep(40);
        BenchmarkUtil.benchmark("manual", benchmark);
    }
}
