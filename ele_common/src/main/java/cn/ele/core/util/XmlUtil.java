package cn.ele.core.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlUtil {
	
	 /**
	 * @description ��xml�ַ���ת����map
	 * @param xml
	 * @return Map
	 */
	 public static Map convertXmlToMap(String xml) {
	         Map map = new HashMap();
	         Document doc = null;
	          
             try {
				// ���ַ���תΪXML
				 doc = DocumentHelper.parseText(xml); 
				 // ��ȡ���ڵ�
				 Element rootElt = doc.getRootElement(); 				 
				 // ��ȡ���ڵ��µ��ӽڵ�head
				 List<Element> elements = rootElt.elements();
				 for(Element element:elements){
					 map.put(element.getName(), element.getStringValue());	            	 
				 }
			} catch (DocumentException e) {
				
				e.printStackTrace();
			}	            
	        return map;
	 }
	 
	 /**
	  * ��Map����ת��Ϊxml�ַ���
	  * @param map
	  * @return
	  */
	 public static String convertMapToXml(Map<String,String> map){
		 StringBuilder xml=new StringBuilder("<xml>");
		 for(String key:map.keySet()){
			 xml.append("<"+key + ">"+map.get(key)+"</"+ key + ">"   );
		 }
		 xml.append("</xml>");
		 return xml.toString();
	 }
	 

}
