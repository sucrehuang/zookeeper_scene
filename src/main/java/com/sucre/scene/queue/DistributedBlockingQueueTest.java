package com.sucre.scene.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class DistributedBlockingQueueTest {

	public static void main(String[] rags) {
		ScheduledExecutorService delayExecutor = Executors.newScheduledThreadPool(1);
		int delayTime = 5;
		ZkClient client = new ZkClient("127.0.0.1:2181", 5000, 5000, new SerializableSerializer());
		final DistributedBlockingQueue<User> queue = new DistributedBlockingQueue<User>(client, "/Queue");
		final User user1 = new User();
		user1.setId("1");
		user1.setName("张三");

		final User user2 = new User();
		user2.setId("2");
		user2.setName("李四");
		try {

			delayExecutor.schedule(new Runnable() {
				public void run() {
					try {
						queue.offer(user1);
						queue.offer(user2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, delayTime, TimeUnit.SECONDS);

			System.out.println("ready poll");
			User u1 = queue.poll();
			User u2 = queue.poll();

			if (user1.getId().equals(u1.getId()) && user2.getId().equals(u2.getId())) {
				System.out.println("Success!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			delayExecutor.shutdown();
			try {
				delayExecutor.awaitTermination(2, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}

	}
}
