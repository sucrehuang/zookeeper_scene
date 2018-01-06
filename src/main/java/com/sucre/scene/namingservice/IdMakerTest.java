package com.sucre.scene.namingservice;

import com.sucre.scene.namingservice.IdMaker.RemoveMethod;

public class IdMakerTest {

	public static void main(String[] args) throws Exception {
		IdMaker idMaker = new IdMaker("127.0.0.1:2181", "/NameService/IdGen", "ID-");
		idMaker.start();
		try {
			for(int i=0; i<10;i++){
				String id = idMaker.generateId(RemoveMethod.DELAY);
				System.out.println(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			idMaker.stop();
		}
		
	}
}
