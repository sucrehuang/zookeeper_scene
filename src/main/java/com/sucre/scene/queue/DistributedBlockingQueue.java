package com.sucre.scene.queue;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * 阻塞分布式队列
 * 扩展自简单分布式队列，在拿不到队列数据时，进行阻塞直到拿到数据
 */
public class DistributedBlockingQueue<T> extends DistributedSimpleQueue<T> {

	public DistributedBlockingQueue(ZkClient client, String root) {
		super(client, root);
	}

	@Override
	public T poll() throws Exception {
		while (true) {// 结束在latch上的等待后，再来一次
			final CountDownLatch latch = new CountDownLatch(1);
			final IZkChildListener childListener = new IZkChildListener() {
				public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
					latch.countDown();// 队列有变化，结束latch上的等待
				}
			};
			client.subscribeChildChanges(root, childListener);
			try {
				T node = super.poll();// 获取队列数据
				if (node != null) {
					return node;
				} else {
					latch.await();// 拿不到队列数据，则在latch上await
				}
			} finally {
				client.unsubscribeChildChanges(root, childListener);
			}

		}
	}
}
