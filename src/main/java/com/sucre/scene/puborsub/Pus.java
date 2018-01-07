package com.sucre.scene.puborsub;

import org.I0Itec.zkclient.ZkClient;

public class Pus {
	public static void main(String[] args)  {
		ZkClient client = new ZkClient("127.0.0.1:2181",5000);
		client.writeData("/app/database_config", "driverClass:com.mysql.jdbc.Driver|jdbcUrl:jdbc:mysql://localhost:3306/qh_db|user:root|password:123456");
	}
}
