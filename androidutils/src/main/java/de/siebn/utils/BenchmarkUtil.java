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

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@SuppressLint("DefaultLocale")
public class BenchmarkUtil {
    private final static HashMap<String, Benchmark> benchmarks = new LinkedHashMap<>();
    private static boolean enabled;

    public static void setEnabled(boolean enabled) {
        BenchmarkUtil.enabled = enabled;
    }

    public static long benchmark(String name, long start) {
        long now = System.nanoTime();
        if (!enabled) return now;
        long time = (now - start) / 1000;
        Benchmark benchmark = benchmarks.get(name);
        if (benchmark == null) benchmarks.put(name, benchmark = new Benchmark(name));
        benchmark.inc(time);
        benchmark.print(time);
        return benchmark.start = System.nanoTime();
    }

    public static void start(String name) {
        if (!enabled) return;
        Benchmark benchmark = benchmarks.get(name);
        if (benchmark == null) benchmarks.put(name, benchmark = new Benchmark(name));
        benchmark.start = benchmark.startCascaded = System.nanoTime();
    }

    public static void benchmark(String name) {
        long now = System.nanoTime();
        if (!enabled) return;
        Benchmark benchmark = benchmarks.get(name);
        long time = (now - benchmark.start) / 1000;
        benchmark.inc(time);
        benchmark.print(time);
        benchmark.start = benchmark.startCascaded = System.nanoTime();
    }

    public static void start(String... names) {
        if (!enabled) return;
        Benchmark[] benchmark = new Benchmark[names.length];
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            name.append(names[i]);
            String n = name.toString();
            benchmark[i] = benchmarks.get(n);
            if (benchmark[i] == null) {
                benchmarks.put(n, benchmark[i] = new Benchmark(n));
                if (i != 0) benchmark[i].start = benchmark[i - 1].startCascaded;
                benchmark[i].startCascaded = benchmark[i].start;
            }
            name.append(".");
        }
        Benchmark b = benchmark[benchmark.length - 1];
        Benchmark b2 = benchmark[benchmark.length - 2];
        b2.startCascaded = b.startCascaded = b.start = System.nanoTime();
    }

    public static void benchmark(String... names) {
        long now = System.nanoTime();
        if (!enabled) return;
        Benchmark[] benchmark = new Benchmark[names.length];
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            name.append(names[i]);
            String n = name.toString();
            benchmark[i] = benchmarks.get(n);
            if (benchmark[i] == null) {
                benchmarks.put(n, benchmark[i] = new Benchmark(n));
                if (i != 0) benchmark[i].start = benchmark[i - 1].startCascaded;
                benchmark[i].startCascaded = benchmark[i].start;
            }
            name.append(".");
        }
        Benchmark b = benchmark[benchmark.length - 1];
        Benchmark b2 = benchmark[benchmark.length - 2];
        long time = (now - b.start) / 1000;
        b.inc(time);
        b.print(time);
        b2.startCascaded = b.start = System.nanoTime();
    }

    public static void stats(String name) {
        stats(new ArrayList<>(), benchmarks.get(name));
    }

    private static void stats(List<Benchmark> parents, Benchmark benchmark) {
        StringBuilder sb = new StringBuilder(String.format("%-40s:", benchmark.name));
        for (Benchmark p : parents) {
            float percent = benchmark.total * 100f / p.total;
            if (p == parents.get(parents.size() - 1)) {
                sb.append(String.format("     %5.1f%%    ", percent));
            } else {
                sb.append(String.format("    (%5.1f%%)   ", percent));
            }
        }
        sb.append(String.format(" %9dus    ", benchmark.total));
        Log.d("Benchmark", sb.toString());
        parents.add(benchmark);
        for (Benchmark b : benchmarks.values()) {
            if (b != benchmark && b.name.startsWith(benchmark.name) && b.name.indexOf(".", benchmark.name.length() + 1) < 0) {
                stats(parents, b);
            }
        }
        parents.remove(benchmark);
    }

    private final static class Benchmark {
        private final String name;
        private long start;
        private long startCascaded;
        private long invocations;
        private long total;

        private Benchmark(String name) {
            this.name = name;
        }

        private void inc(long time) {
            invocations++;
            total += time;
        }

        private void print(long time) {
            Log.d("Benchmark", String.format("%-40s: %9dus   total: %9dus   avg: %8dus   calls: %5d", name, time, total, (total / invocations), invocations));
        }
    }
}
