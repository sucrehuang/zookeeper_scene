package com.sucre.scene.queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class DistributedSimpleQueueTest {

	public static void main(String []args) {
		ZkClient client = new ZkClient("127.0.0.1:2181",5000,5000,new SerializableSerializer());
		DistributedSimpleQueue<User> queue = new DistributedSimpleQueue<User>(client, "/Queue");
		User user1 = new User();
		user1.setId("1");
		user1.setName("张三");
		
		User user2 = new User();
		user2.setId("2");
		user2.setName("李四");
		
		 try {
			queue.offer(user1);
			queue.offer(user2);
			
			User u1 = queue.poll();
			User u2 = queue.poll();

			if(user1.getId().equals(u1.getId())){
				System.out.println("Success!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
