package com.sucre.scene.queue;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.I0Itec.zkclient.ExceptionUtil;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

public class DistributedSimpleQueue<T> {
	protected ZkClient client;
	//queue节点
	protected String root;
	//顺序节点前缀
	protected static final String Node_Name = "n_";
	
	public DistributedSimpleQueue(ZkClient client,String root){
		this.client = client;
		this.root = root;
	}
	
	//获取队列大小
	public int size(){
		return client.getChildren(root).size();
	}
	
	//判断队列是否为空
	public boolean isEmpty(){
		return size()==0;
	}
	
	 // 向队列提供数据
    public boolean offer(T element) throws Exception{
        // 创建顺序节点
        String nodeFullPath = root .concat( "/" ).concat( Node_Name );
        try {
        	client.createPersistentSequential(nodeFullPath , element);
        }catch (ZkNoNodeException e) {
        	client.createPersistent(root);
            offer(element);
        } catch (Exception e) {
            throw ExceptionUtil.convertToRuntimeException(e);
        }
        return true;
    }
    
    //从队列取数据
    public T poll() throws Exception{
    	//获取所有顺序节点
    	List<String> list = client.getChildren(root);
    	if (list.size() == 0){
    		return null;
    	}
    	
    	//排序
    	Collections.sort(list, new Comparator<String>() {
			public int compare(String lhs, String rhs) {
				return getNodeNumber(lhs,Node_Name).compareTo(getNodeNumber(rhs, Node_Name));
			}
		});
    	//循环每个顺序节点名
    	for(String nodeName : list){
    		//构建完整的顺序节点路径
    		String nodeFullPath = root.concat("/").concat(nodeName);
    		try{
    		//读取顺序节点的内容
    		T node = (T)client.readData(nodeFullPath);
    		//删除顺序节点
    		client.delete(nodeFullPath);
    		return node;
    		}catch(ZkNoNodeException e){
    			//ignore 由其他客户端把这个顺序节点取消掉
    		}
    		
    	}
    	
    	return null;
    	
    }
    
    public String getNodeNumber(String str, String nodeName){
    	int index = str.lastIndexOf(nodeName);
    	if(index >= 0){
    		index += Node_Name.length();
    		return index <= str.length() ? str.substring(index):"";
    	}
		return str;
	}
	
}
