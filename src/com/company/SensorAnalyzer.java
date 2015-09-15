/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Dimitrijs Fedotovs <www.bug.guru>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.company;

import java.util.Queue;

/**
 * @author Dimitrijs Fedotovs <a href="http://www.bug.guru">www.bug.guru</a>
 * @version 1.0
 * @since 1.0
 */
public class SensorAnalyzer implements Runnable {
    private final Queue<SensorData> queue;

    public SensorAnalyzer(Queue<SensorData> queue) {
        this.queue = queue;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        long sum = 0;
        int count = 0;
        try {
            int maxQueueSize = 0;
            int currentQueueSize;
            while (true) {
                SensorData data;
                synchronized (queue) {
                    while (queue.isEmpty()) {
                        queue.wait();
                    }
                    currentQueueSize = queue.size();
                    maxQueueSize = Math.max(maxQueueSize, currentQueueSize);
                    data = queue.remove();
                }
                Thread.sleep((long) (Math.random() * 200.0));
                sum += data.getTemp();
                count++;
                System.out.printf("AVG: %.3f; SUM: %d; COUNT: %d; queue size: %d; max queue size: %d\n", sum / (double) count, sum, count, currentQueueSize, maxQueueSize);
            }
        } catch (Throwable t) {
            System.out.println("Oh, my god!");
            t.printStackTrace();
        }
    }
}
