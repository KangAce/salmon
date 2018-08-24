package cn.ele.core.util;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;

/**
 * ���湤����
 * @author Administrator
 *
 */
public class CacheUtil {

	private RedisTemplate<String, Object> redisTemplate;

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}  
	
	//��Ӷ���
	public void addValue(String key,Object value){
		redisTemplate.boundValueOps(key).set(value);
	}
	
	//��ȡ����
	public Object getValue(String key){
		return redisTemplate.boundValueOps(key).get();
	}
	
	//ɾ������
	public void removeValue(String key){
		redisTemplate.delete(key);
	}
	
	//��Set�������ֵ
	public void addSetValue(String key,Object value){
		redisTemplate.boundSetOps(key).add(value);
	}
	
	//��ȡSet���ϵ�ֵ
	public Set getSetValue(String key){
		return redisTemplate.boundSetOps(key).members();
	}
	
	//ɾ��Set�����е�ĳ��ֵ
	public void removeSetValue(String key,Object value){
		redisTemplate.boundSetOps(key).remove(value);
	}
	
	
	//��List�������ֵ
	public void addListValue(String key,Object value){
		redisTemplate.boundListOps(key).leftPush(value);
	}
	
	//��ȡList����
	public List getListValues(String key){
		return redisTemplate.boundListOps(key).range(0, -1);//-1��������
	}
	
	//��ȡList����
	public Object getListValueByIndex(String key,long  index){
		return redisTemplate.boundListOps(key).index(index);
	}
	
	//ɾ��List�����е�ĳ��ֵ
	public void removeListValue(String key,long index){
		redisTemplate.boundListOps(key).remove(index,null);
	}
	
	
	
}
