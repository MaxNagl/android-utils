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
@file:JvmName("ThreadUtils")

package de.siebn.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*



// Ui Thread

val mainThreadHandler: Handler by lazy {
    Handler(Looper.getMainLooper())
}

fun runOnUiThread(runnable: Runnable) {
    mainThreadHandler.post(runnable)
}

@JvmSynthetic
fun runOnUiThread(runnable: () -> Unit) {
    mainThreadHandler.post(runnable)
}

fun runOnUiThreadDelayed(delayMillis: Long, runnable: Runnable) {
    mainThreadHandler.postDelayed(runnable, delayMillis)
}

@JvmSynthetic
fun runOnUiThreadDelayed(delayMillis: Long, runnable: () -> Unit) {
    mainThreadHandler.postDelayed(runnable, delayMillis)
}

fun runOnUiThreadSync(runnable: Runnable) {
    runOnHandlerSync(mainThreadHandler, runnable)
}

@JvmSynthetic
fun runOnUiThreadSync(runnable: () -> Unit) {
    runOnHandlerSync(mainThreadHandler, runnable)
}



// Bg Thread

val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

fun runOnBgThread(runnable: Runnable) {
    backgroundExecutor.execute(runnable)
}

@JvmSynthetic
fun runOnBgThread(runnable: () -> Unit) {
    backgroundExecutor.execute(runnable)
}

fun runOnBgThreadDelayed(delayMillis: Long, runnable: Runnable) {
    backgroundExecutor.schedule(runnable, delayMillis, TimeUnit.MILLISECONDS)
}

@JvmSynthetic
fun runOnBgThreadDelayed(delayMillis: Long, runnable: () -> Unit) {
    backgroundExecutor.schedule(runnable, delayMillis, TimeUnit.MILLISECONDS)
}

fun runOnBgThreadSync(runnable: Runnable) {
    runOnExecutorSync(backgroundExecutor, runnable)
}

@JvmSynthetic
fun runOnBgThreadSync(runnable: () -> Unit) {
    runOnExecutorSync(backgroundExecutor, runnable)
}



// Multi Bg Thread

val multiBackgroundExecutor = ThreadPoolExecutor(0, Runtime.getRuntime().availableProcessors(), 1, TimeUnit.MINUTES, LinkedBlockingQueue())

fun runOnMultiBgThread(runnable: Runnable) {
    multiBackgroundExecutor.execute(runnable)
}

@JvmSynthetic
fun runOnMultiBgThread(runnable: () -> Unit) {
    multiBackgroundExecutor.execute(runnable)
}

fun runOnMultiBgThreadSync(runnable: Runnable) {
    runOnExecutorSync(multiBackgroundExecutor, runnable)
}

@JvmSynthetic
fun runOnMultiBgThreadSync(runnable: () -> Unit) {
    runOnExecutorSync(multiBackgroundExecutor, runnable)
}



// Network Thread

val networkExecutor = Executors.newSingleThreadScheduledExecutor()

fun runOnNetworkThread(runnable: Runnable) {
    networkExecutor.execute(runnable)
}

@JvmSynthetic
fun runOnNetworkThread(runnable: () -> Unit) {
    networkExecutor.execute(runnable)
}

fun runOnNetworkThreadDelayed(delayMillis: Long, runnable: Runnable) {
    networkExecutor.schedule(runnable, delayMillis, TimeUnit.MILLISECONDS)
}

@JvmSynthetic
fun runOnNetworkThreadDelayed(delayMillis: Long, runnable: () -> Unit) {
    networkExecutor.schedule(runnable, delayMillis, TimeUnit.MILLISECONDS)
}

fun runOnNetworkThreadSync(runnable: Runnable) {
    runOnExecutorSync(networkExecutor, runnable)
}

@JvmSynthetic
fun runOnNetworkThreadSync(runnable: () -> Unit) {
    runOnExecutorSync(networkExecutor, runnable)
}



// Utils

fun runOnExecutorSync(executor: ExecutorService, runnable: Runnable) {
    executor.submit(runnable).get()
}

@JvmSynthetic
fun runOnExecutorSync(executor: ExecutorService, runnable: () -> Unit) {
    executor.submit(runnable).get()
}

fun runOnHandlerSync(handler: Handler, runnable: Runnable) {
    if (Thread.currentThread() == handler.looper.thread) {
        runnable.run()
    } else {
        val task = FutureTask<Any>(runnable, null)
        handler.post(task)
        task.get()
    }
}

@JvmSynthetic
fun runOnHandlerSync(handler: Handler, runnable: () -> Unit) {
    if (Thread.currentThread() == handler.looper.thread) {
        runnable()
    } else {
        val task = FutureTask<Any>(runnable, null)
        handler.post(task)
        task.get()
    }
}
