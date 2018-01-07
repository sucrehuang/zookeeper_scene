package com.sucre.scene.puborsub;

import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;


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
		
		public void handleDataDeleted(String dataPath) throws Exception {
			System.out.println("服务端数据已删除-----------");
			System.out.println(dataPath);
		}
		
		public void handleDataChange(String dataPath, Object data) throws Exception {
			System.out.println("服务端数据已发生修改-----------");
			System.out.println(dataPath+"======"+data);
			
		}
	};
	
	public static void main(String[] args) {
		ZkClient zk = new ZkClient(ADDRESS, 50000);
		System.out.println("zookeeper服务端连接状态："+zk);
		zk.subscribeDataChanges(PATH, listener);
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
