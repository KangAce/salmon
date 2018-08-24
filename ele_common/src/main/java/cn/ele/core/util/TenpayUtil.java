package cn.ele.core.util;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TenpayUtil {

	//private static Object Server;

	/**
	 * �Ѷ���ת�����ַ���
	 * 
	 * @param obj
	 * @return String ת�����ַ���,������Ϊnull,�򷵻ؿ��ַ���.
	 */
	
	/*
	public static String toString(Object obj) {
		if (obj == null)
			return "";

		return obj.toString();
	}*/

	/**
	 * �Ѷ���ת��Ϊint��ֵ.
	 * 
	 * @param obj
	 *            �������ֵĶ���.
	 * @return int ת�������ֵ,�Բ���ת���Ķ��󷵻�0��
	 */
	/*
	public static int toInt(Object obj) {
		int a = 0;
		try {
			if (obj != null)
				a = Integer.parseInt(obj.toString());
		} catch (Exception e) {

		}
		return a;
	}*/

	/**
	 * ��ȡ��ǰʱ�� yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}

	/**
	 * ��ȡ��ǰ���� yyyyMMdd
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String strDate = formatter.format(date);
		return strDate;
	}

	/**
	 * ȡ��һ��ָ�����ȴ�С�����������.
	 * 
	 * @param length
	 *            int �趨��ȡ��������ĳ��ȡ�lengthС��11
	 * @return int �������ɵ��������
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * ��ȡ�����ַ���
	 * 
	 * @param request
	 * @param response
	 * @return String
	 */
	/*
	public static String getCharacterEncoding(HttpServletRequest request,
			HttpServletResponse response) {

		if (null == request || null == response) {
			return "gbk";
		}
		String enc = request.getCharacterEncoding();
		if (null == enc || "".equals(enc)) {
			enc = response.getCharacterEncoding();
		}
		if (null == enc || "".equals(enc)) {
			enc = "gbk";
		}
		return enc;
	}

	public static String URLencode(String content) {

		String URLencode;

		URLencode = replace(Server.equals(content), "+", "%20");

		return URLencode;
	}*/

	/*
	private static String replace(boolean equals, String string, String string2) {
		return null;
	}
*/
	/**
	 * ��ȡunixʱ�䣬��1970-01-01 00:00:00��ʼ������
	 * 
	 * @param date
	 * @return long
	 */
	public static long getUnixTime(Date date) {
		if (null == date) {
			return 0;
		}

		return date.getTime() / 1000;
	}

	/**
	 * ʱ��ת�����ַ���
	 * 
	 * @param date
	 *            ʱ��
	 * @param formatType
	 *            ��ʽ������
	 * @return String
	 */
	/*
	public static String date2String(Date date, String formatType) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatType);
		return sdf.format(date);
	}*/

	/**
	 * ��ȡ����ַ���
	 * 
	 * @return
	 */
	public static String getRandomStr() {
		// �����
		String currTime = TenpayUtil.getCurrTime();
		// 8λ����
		String strTime = currTime.substring(8, currTime.length());
		// ��λ�����
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10λ���к�,�������е�����
		return strTime + strRandom;
	}

	/**
	 * Ԫת���ɷ�
	 * 
	 * @param amount
	 * @return
	 */
	public static String getMoney(String amount) {
		if (amount == null) {
			return "";
		}
		// ���ת��Ϊ��Ϊ��λ
		String currency = amount.replaceAll("\\$|\\��|\\,", ""); // �������, ��
																// ����$�Ľ��
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(
					".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(
					".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(
					".", "") + "00");
		}
		return amLong.toString();
	}

	//public static void main(String[] args) {
	//	System.out.println(getMoney("1"));
	//}

	/**
	 * description: ����΢��֪ͨxml
	 * 
	 * @param xml
	 * @return
	 * @see
	 */
	//@SuppressWarnings({"rawtypes", "unchecked" })
	/*
	public static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// �����µ�����ԴSAX ��������ʹ�� InputSource ������ȷ����ζ�ȡ XML ����
			InputSource source = new InputSource(read);
			// ����һ���µ�SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// ͨ������Դ����һ��Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// ָ����ڵ�
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
*/
	
	/*
	public static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}*/

	/**
	 * ����xml,���ص�һ��Ԫ�ؼ�ֵ�ԡ������һ��Ԫ�����ӽڵ㣬��˽ڵ��ֵ���ӽڵ��xml���ݡ�
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	/*
	public static Map<String,String> doXMLParse(String strxml) throws Exception {
		if (null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String,String> m = new HashMap<String,String>();
		InputStream in = String2Inputstream(strxml);
		Reader read = new InputStreamReader(in,"gbk");
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(read);
		Element root = doc.getRootElement();
		List<Element> list = root.getChildren();
		Iterator<Element> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List<Element> children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		// �ر���
		in.close();
		return m;
	}
    */
	
	
	/**
	 * ��ȡ�ӽ���xml
	 * 
	 * @param children
	 * @return String
	 */
	/*
	public static String getChildrenText(List<Element> children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List<Element> list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}
    */
	
	/*
	public static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}*/
}
