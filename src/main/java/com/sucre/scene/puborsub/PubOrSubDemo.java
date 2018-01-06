package com.sucre.scene.puborsub;

import java.util.concurrent.CountDownLatch;

import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;

/**
 * zookeeper 数据订阅/发布Demo
 * @author sucre
 *
 */
public class PubOrSubDemo {
	private static CountDownLatch latch =  new CountDownLatch(1);
	public static final String ADDRESS = "127.0.0.1:2181";
	public static final String PATH = "/app/database_config";
	
	public static IZkDataListener listener = new IZkDataListener() {
		
		public void handleDataDeleted(String arg0) throws Exception {
			System.out.println("服务端数据已删除-----------");
			System.out.println(arg0);
		}
		
		public void handleDataChange(String arg0, byte[] arg1) throws Exception {
			System.out.println("服务端数据已发生修改-----------");
			System.out.println(arg0+"======"+new String(arg1));
		}
		
	};
	
	public static void main(String[] args) {
		ZkClient zk = new ZkClient(ADDRESS, 50000);
		System.out.println("zookeeper服务端连接状态："+zk.isConnected());
		zk.subscribeDataChanges(PATH, listener);
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
