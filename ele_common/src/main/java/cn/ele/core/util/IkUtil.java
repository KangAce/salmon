package cn.ele.core.util;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class IkUtil {
	public static Set<String> analyzer(String analyzerString) {
		Set<String> set = new HashSet<String>();
		TokenStream stream;
		try {
			// �����ִʵ�io��,��title���зִ�
			stream = new IKAnalyzer(true).tokenStream("myfield", analyzerString);
			stream.reset();
			// �����������װ��
			CharTermAttribute offsetAtt = stream.getAttribute(CharTermAttribute.class);
			// �ж��Ƿ�����һ���ִ�

			while (stream.incrementToken()) {
				set.add(offsetAtt.toString());
			}
			// �ر���
			stream.end();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return set;
	}
}
