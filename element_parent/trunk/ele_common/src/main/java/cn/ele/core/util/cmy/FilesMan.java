package cn.ele.core.util.cmy;

/**
 * Created:         2001.10
 * Last Modified:   2001.10.12/2001.11.3
 * Description:
 *
 *
 */

/**
 * <p>
 * Title: TRS ����Э��ƽ̨��TRS WCM��
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * class FilesMan �D�D WCM�ļ��������Ķ����ʵ��
 * <p>
 * �ļ���������
 * <p>
 * ��ʽ�� ff+yyyy,mm,dd+tt,ttt,ttt+rrrr.ext
 * <p>
 * index: 01 2345 67 89 01 234 567 8901
 * <p>
 * ���У�
 * <p>
 * <0>ff:�ļ�Ŀ¼�ı�ʶ��2λ
 * <p>
 * <1>yyyymmnn--��������, 8λ������
 * <p>
 * yyyy--4λ��ʾ����,mm--2λ��ʾ���£�dd--2λ��ʾ���գ�
 * <p>
 * �磺011009 ��ʾ 2001-10-09��
 * <p>
 * <2>tt,ttt,ttt Ϊ8λʱ��ֵ(��λ����)���磺1992849 ��ʾ 08:33:12
 * <p>
 * <3>rrrr -- 4λ�������
 * <p>
 * <4>.ext -- ��չ����
 * <p>
 * �ļ��洢Ŀ¼��֯����Dir1+Dir2+Dir3
 * <p>
 * �磺[UploadTempPath]\N200110\N20011015\
 * <p>
 * [1]һ��Ŀ¼(Dir1)
 * <p>
 * <1>�ϴ��ļ���ʱĿ¼ ����ʶ��U0��
 * <p>
 * <2>��ͨ�ļ�����Ŀ¼ ����ʶ��N0��
 * <p>
 * <3>�ܱ����ļ�����Ŀ¼ ����ʶ��P0��
 * <p>
 * <4>ϵͳ����ʱĿ¼ ����ʶ��ST��
 * <p>
 * <5>�û�����ʱĿ¼ ����ʶ��UT��
 * <p>
 * <6>�ܱ����ļ�����Ŀ¼ ����ʶ��P0��
 * <p>
 * <7>����·�� (��ʶ��B0)
 * <p>
 * <8>��ͨ��HTTPЭ����ʵ�·��(��ʶ��W0)
 * <p>
 * <9>�������û�����չ (��ʶ��2λ)
 * <p>
 * [2]����Ŀ¼(Dir2)
 * <p>
 * һ��Ŀ¼��ʶ+���£��ļ�����ǰ8���ַ����磺"N0200110\"
 * <p>
 * [3]����Ŀ¼(Dir3)
 * <p>
 * ����Ŀ¼+�ձ�ʶ���ļ�����ǰ10���ַ����磺"N020011015\"*
 * <p>
 * Copyright: Copyright (c) 2001-2002 TRS��Ϣ�������޹�˾
 * </p>
 * <p>
 * Company: TRS��Ϣ�������޹�˾(www.trs.com.cn)
 * </p>
 *
 * @author TRS��Ϣ�������޹�˾
 * @version 1.0
 */

import cn.ele.core.Exception.CMyException;
import cn.ele.core.Exception.ExceptionNumber;
import cn.ele.core.Exception.WCMException;
import cn.ele.core.message.I18NMessage;
//import com.trs.infra.support.config.PathConfig;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.TimeZone;

public class FilesMan extends Object {
    public static boolean DELETE_FILE_ON_MOVE = false;

    private static Logger s_logger = Logger.getLogger(FilesMan.class);

    private static FilesMan m_filesMan = new FilesMan();

    // �������壺�����ļ�����
    /** �ļ�������Сֵ */
    public final static int FILENAME_MIN_LENGTH = 22; //

    /** Ŀ¼��ʶ���� */
    public final static int FILENAME_FLAG_LENGTH = 2; //

    /** ���ڳ��� */
    public final static int FILENAME_DATE_LENGTH = 8; //

    /** ʱ��ֵ���� */
    public final static int FILENAME_TIME_LENGTH = 8; //

    /** ��������� */
    public final static int FILENAME_RANDOM_LENGTH = 4; //

    /** ��ǰʱ��ʱ��� */
    private final static int TIMEZONE_RAWOFFSET = TimeZone.getDefault()
            .getRawOffset();

    // �������壺�ļ���ʶ���ͣ���Ӧһ��Ŀ¼����
    /** �ļ���ʶ���ͣ���ͨ�ļ�Ŀ¼ */
    public final static String FLAG_NORMAL = "N0"; //

    /** �ļ���ʶ���ͣ��ܱ����ļ�Ŀ¼ */
    public final static String FLAG_PROTECTED = "P0"; //

    /** �ļ���ʶ���ͣ��ϴ�����ʱ�ļ�Ŀ¼ */
    public final static String FLAG_UPLOAD = "U0"; //

    /** �ļ���ʶ���ͣ�ϵͳ����ʱ�ļ�Ŀ¼ */
    public final static String FLAG_SYSTEMTEMP = "ST"; //

    /** �ļ���ʶ���ͣ��û�����ʱ�ļ�Ŀ¼ */
    public final static String FLAG_USERTEMP = "UT"; //

    /** �ļ���ʶ���ͣ�ģ���ļ�Ŀ¼ */
    public final static String FLAG_TEMPLATE = "TM"; //

    /** �ļ���ʶ���ͣ����ش�ŷ����ļ���·�� */
    public final static String FLAG_LOCALPUB = "LP"; //

    /** �ļ���ʶ���ͣ����ش��Ԥ���ļ���·�� */
    public final static String FLAG_LOCALPREVIEW = "LV"; //

    /** �ļ���ʶ���ͣ���ͨ��httpЭ����ʵ��ļ���·�� */
    public final static String FLAG_WEBFILE = "W0"; //

    /** �ļ���ʶ���ͣ��ĵ��������������Դ�ļ��Ĵ��·�� */
    public final static String FLAG_DOCUMENTSOURCE = "DS";

    /** �ļ���ʶ���ͣ����ܽ�վԤ��·�� */
    public final static String FLAG_SITEFROM = "SF";

    /** �ļ���ʶ���ͣ�BigTable��ϡ�����ݣ��Ĵ洢·�� */
    public final static String FLAG_BIGTABLE = "BT";

    /** �ļ���ʶ���ͣ�InfoView���Զ����������MS InfoPathʵ�֣��Ĵ洢·�� */
    public final static String FLAG_INFOVIEW = "IV";

    /** ϵͳ��XSL�ļ��Ĵ��·�� */
    public final static String FLAG_TRANSFORMER = "TF";

    /** �ļ���ʶ���ͣ�HelpSearchIndex�������ļ��������Ĵ洢·�� */
    public final static String FLAG_HELPSEARCH = "HS";

    // �������壺Ŀ¼���� 3��
    /** Ŀ¼���ͣ�����Ŀ¼������Server( for Server ) */
    public final static int PATH_LOCAL = 0; //

    /** Ŀ¼���ͣ�HttpĿ¼������Web( for Client ) */
    public final static int PATH_HTTP = 1; //

    /** Ŀ¼���ͣ�FtpĿ¼������Ftp( for Client ) */
    public final static int PATH_FTP = 2; //

    private final HashMap m_hPathConfig; // �ļ�Ŀ¼���ñ�

    // ���캯��
    /**
     * ���캯��
     *
//     * @param _app
     *            ������Application
     */
    private FilesMan() {
        m_hPathConfig = new HashMap(11);
        // wenyh@2005-7-1 10:02:26 add comment:Test for DB
        // DBManager.getDBManager();
        // wenyh@2005-7-1 10:02:26 add comment:Test for DB
    }

    public static FilesMan getFilesMan() {
        if (m_filesMan.m_hPathConfig.isEmpty()) {
            loadFilesMan();
        }
        return m_filesMan;
    }

    // =========================================================================
    // �ļ�Ŀ¼����

    /**
     * TODO װ��ϵͳ�й�Ŀ¼������Ϣ
     *
     * @param _bMakeDirIfNotExists
     *            Ŀ¼������ʱ�Ƿ��Զ�����
     * @throws WCMException
     */
/*    private synchronized void loadPathConfigs(boolean _bMakeDirIfNotExists)
            throws WCMException {
        if (!m_filesMan.m_hPathConfig.isEmpty()) {
            return;
        }
        Configs configs = null;
        Config aConfig = null;

        // modified by hxj.Ϊ�˰�ȫ�У��������ļ�����·�����ã����������ݿ���أ���ֹ�û����޸�
        ConfigServer server = ConfigServer.getServer();

        try {
            configs = new Configs();
            configs.open(new WCMFilter("", "CType=" + Config.TYPE_FILE_PATH, ""));
            for (int i = 0; i < configs.size(); i++) {
                aConfig = (Config) configs.getAt(i);
                if (aConfig == null)
                    continue;

                String sKey = aConfig.getConfigKey();
                String sPath = server.getInitProperty(sKey);
                if (CMyString.isEmpty(sPath)) {
                    sPath = aConfig.getValue();
                    server.setInitProperties(sKey, sPath);
                }

                try {
                    this.putPathConfig(sKey, sPath, _bMakeDirIfNotExists);
                    // this.putPathConfig(aConfig, _bMakeDirIfNotExists);
                    // //why@2002
                    // -04-24
                } catch (Exception ex) {
                    s_logger.error(
                            I18NMessage.get(FilesMan.class, "FilesMan.label1",
                                    "--->���ü�¼��WCMConfig Id=[")
                                    + aConfig.getId()
                                    + "] Key=["
                                    + aConfig.getConfigKey()
                                    + "]  Value=["
                                    + aConfig.getValue() + "] ��", ex);
                }// end try:������һ��Config����
            }// end for�����������е�Ŀ¼����
            configs.clear();
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label2",
                            "װ���ļ�Ŀ¼������Ϣʱʧ��(FilesMan.loadPathConfigs)"), ex);
        }
    }*/// END: loadPathConfigs( )

    /**
     * TODO װ��ָ��Ŀ¼������Ϣ
     *
     * @param _config
     *            ���ö���
     * @return ��װ�ڳɹ����򷵻�true�����򣬷���false
     * @throws WBEException
     */
/*
    public boolean putPathConfig(Config _config, boolean _bMakeDirIfNotExists)
            throws WCMException {
        if (_config == null || !_config.isValidInstance())
            return false;

        try {
            // ȡ�ؼ��֣��༴·����ʶ��
            String sKey = _config.getConfigKey().trim();
            if (sKey.length() != 2) { // �ؼ��ֱ�����2λ����Ϊ·����־
                throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                        I18NMessage.get(FilesMan.class, "FilesMan.label3",
                                "Ŀ¼�����йؼ��ֱ���Ϊ2λ(FilesMan.loadPathConfig)"));
            }

            // �ֽ�������Ϣ
            PathConfig pathConfig = new PathConfig(_config.getValue());
            return this.putPathConfig(sKey, pathConfig, _bMakeDirIfNotExists);
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label4",
                            "װ��������Ϣʧ��(FilesMan.putPathConfig)"), ex);
        }
    }

    public boolean putPathConfig(String sKey, String sPath,
                                 boolean _bMakeDirIfNotExists) throws WCMException {
        if (CMyString.isEmpty(sKey) || CMyString.isEmpty(sPath))
            return false;

        try {
            // ȡ�ؼ��֣��༴·����ʶ��
            sKey = sKey.trim();
            if (sKey.length() != 2) { // �ؼ��ֱ�����2λ����Ϊ·����־
                throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                        I18NMessage.get(FilesMan.class, "FilesMan.label3",
                                "Ŀ¼�����йؼ��ֱ���Ϊ2λ(FilesMan.loadPathConfig)"));
            }

            // �ֽ�������Ϣ
            PathConfig pathConfig = new PathConfig(sPath);
            return this.putPathConfig(sKey, pathConfig, _bMakeDirIfNotExists);
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label4",
                            "װ��������Ϣʧ��(FilesMan.putPathConfig)"), ex);
        }
    }
*/

    /**
     * TODO ����ָ��Ŀ¼������Ϣ
     *
     * @param _sPathFlag
     *            Ŀ¼��־�����ļ���־Ϊͬһ��־��FLAG_NORMAL�ȳ�����
     * @param _pathConfig
     *            Ŀ¼���ö���
     * @param _bMakeDirIfNotExists
     *            Ŀ¼�������Ƿ��Զ�����
     * @return
     * @throws Exception
     */
/*    public boolean putPathConfig(String _sPathFlag, PathConfig _pathConfig,
                                 boolean _bMakeDirIfNotExists) throws Exception {
        if (_pathConfig == null)
            return false;

        // ��鱾��·���Ƿ����
        String sLocalPath = _pathConfig.getLocalPath();
        if (!CMyFile.fileExists(sLocalPath)) { // Ŀ¼������
            if (_bMakeDirIfNotExists) { // ����Ŀ¼
                CMyFile.makeDir(sLocalPath, true);
            } else {
                throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                        I18NMessage.get(FilesMan.class, "FilesMan.label5",
                                "����·��")
                                + sLocalPath
                                + I18NMessage.get(FilesMan.class,
                                "FilesMan.label6",
                                "������(FilesMan.loadPathConfig)"));
            }
        }// end if

        // װ�����ù�ϣ��
        synchronized (m_hPathConfig) {
            this.m_hPathConfig.put(_sPathFlag.toUpperCase(), _pathConfig);
        }

        return true;
    }*/

    /**
     *  TODO ��Ŀ¼��������ɾ��ָ��������
     *
     * @param _sPathFlag
     *            Ŀ¼��־�����ļ���־Ϊͬһ��־��FLAG_NORMAL�ȳ�����
     * @return
     */
/*    public PathConfig removePathConfig(String _sPathFlag) {
        if (true) {// �������ݿ�������ã����Ǵ��ļ�����
            return null;
        }
        synchronized (m_hPathConfig) {
            return (PathConfig) this.m_hPathConfig.remove(_sPathFlag
                    .toUpperCase());
        }
    }*/

    /**
     * TODO ȡָ��Ŀ¼��ʶ��������Ϣ
     *
     * @param _sPathFlag
     *            Ŀ¼��־�����ļ���־Ϊͬһ��־��FLAG_NORMAL�ȳ�����
     * @return ָ��Ŀ¼��ʶ��������Ϣ
     */
//    public PathConfig getPathConfig(String _sPathFlag) {
//        return (PathConfig) m_hPathConfig.get(_sPathFlag);
//    }

    /**
     * ��ȡָ��Ŀ¼��ʶ��������Ϣֵ
     *
     * @param _sPathFlag
     *            Ŀ¼��־�����ļ���־Ϊͬһ��־��FLAG_NORMAL�ȳ�����
     * @param _nPathType
     *            Ŀ¼���ͣ�������FilesMan�У�PATH_LOCAL�ȳ�����
     * @return ָ��Ŀ¼��ʶ��������Ϣֵ
     */
    public String getPathConfigValue(String _sPathFlag, int _nPathType) {
        String sPath=null;
        //TODO ��������
       /* PathConfig pathConfig = this.getPathConfig(_sPathFlag); // Ŀ¼������Ϣ
        if (pathConfig == null) {
            return null;
        }

        switch (_nPathType) {
            case PATH_LOCAL: {
                sPath = pathConfig.getLocalPath();
                // ��·�����д�������ж���ָ���������Ҫ�ữ��һ����Windowsϵͳ�½�4���滻��2����
                sPath = getLocalFormatPath(sPath);
                break;
            }
            case PATH_HTTP: {
                sPath = pathConfig.getHttpPath();
                break;
            }
            case PATH_FTP: {
                sPath = pathConfig.getFtpPath();
                break;
            }
            default:
                return null;
        }*/// end case
        return sPath;
    }

    /**
     * ��·�����д��������·���ָ����滻Ϊһ��·���ָ���<BR>
     * ���·���ָ���Ϊ��/���������еġ�//���滻Ϊ��/��; ���·���ָ���Ϊ��\���������еġ�\\\\���滻Ϊ��\\��.
     *
     * @param sPath
     * @return
     */
    private static String getLocalFormatPath(String sPath) {
        // add by caohui@2012-11-26 ����3:01:42
        // �����ݴ���д������������֧������·��
        if (true)
            return sPath;

        if (CMyString.isEmpty(sPath))
            return "";
        String source;
        String dest;
        if (File.separatorChar == '/') {
            source = "//";
            dest = "/";
        } else {
            source = "\\\\\\\\";
            dest = "\\\\";
        }
        return sPath.replaceAll(source, dest);
    }

    /**
     * Ŀ¼ӳ�䣺�����ļ���ӳ���ļ����ڵ�·��
     * <p>
     * �ļ�����·��
     * <p>
     * ˵����������ļ�·���Ƿ���������
     *
     * @param _sFileName
     *            �ļ���
     * @param _nPathType
     *            Ŀ¼����
     * @return �����ļ���ӳ���ļ����ڵ�·��
     * @throws WCMException
     */
    public String mapFilePath(String _sFileName, int _nPathType)
            throws WCMException {
        if (_sFileName == null) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label7",
                            "�ļ���Ϊ��(FilesMan.mapFilePath)"));
        }

        // ����ļ��������Ƿ������С�ļ�����
        _sFileName = _sFileName.trim();

        // add by liuhm@20140114 ��ȫ�����⴦����Ҫ���ж��ٻ�ȡ�ļ���
        if (_sFileName.indexOf("..") >= 0) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID, "["
                    + _sFileName
                    + I18NMessage.get(FilesMan.class, "FilesMan.label8",
                    "]��Ч���ļ���ʽ(FilesMan.mapFilePath)"));
        }

        _sFileName = CMyFile.extractFileName(_sFileName);
        if (_sFileName.length() < FILENAME_MIN_LENGTH) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID, "["
                    + _sFileName
                    + I18NMessage.get(FilesMan.class, "FilesMan.label8",
                    "]��Ч���ļ���ʽ(FilesMan.mapFilePath)"));
        }

        // TODO ȡ�ļ��洢Ŀ¼������Ϣ����������ͱ�ʶ��ǰ2λ���Ƿ���ȷ
        /*PathConfig pathConfig = null; // Ŀ¼������Ϣ
        pathConfig = this.getPathConfig(_sFileName.substring(0, 2));
        if (pathConfig == null) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label9",
                            "_sFileName:[" + _sFileName
                                    + "]���ļ���ʽ��ƥ�䣺���ͱ�ʶ��Ч(FilesMan.mapFilePath)"));
        }*/

        // TODO �����ļ�·��
        String sPath=null; // �ļ�·��
/*
        char chrPathSeparator = (_nPathType == PATH_LOCAL ? File.separatorChar
                : '/'); // ·���ָ��
        switch (_nPathType) {
            case PATH_LOCAL: {
                sPath = pathConfig.getLocalPath();
                break;
            }
            case PATH_HTTP: {
                sPath = pathConfig.getHttpPath();
                break;
            }
            case PATH_FTP: {
                sPath = pathConfig.getFtpPath();
                break;
            }
            default:
                throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                        I18NMessage.get(FilesMan.class, "FilesMan.label10",
                                "��Ч��·�����(FilesMan.mapFilePath)"));
        }// end case
        sPath += _sFileName.substring(0, 8) + chrPathSeparator
                + _sFileName.substring(0, 10) + chrPathSeparator;
*/

        return sPath; // ����·��
    }

    // =========================================================================
    // TODO ������Ч�ļ���
    /**
     * ��ȡ��һ�����õ��ļ�����
     * <p>
     * ����˵����
     * <p>
     * [1]�����ļ���ʱ���Զ�����ļ����Ƿ��ظ���
     * <p>
     * [2]���ظ��������ظ����ļ�������׷��2λ�������
     * <p>
     * ˵����Ϊ����ϵͳ������һ�����λ�ȡ�ļ���ʧ�ܣ�����null.
     *
     * @param _sPathFlag
     *            �ļ����ͱ�ʶ��ֵ��FLAG_NORMAL�ȳ������壩
     * @param _sFileExt
     *            �ļ���չ����".ext"��"ext"��ʽ��
     * @param _crTime
     *            ����ʱ�䣨��ʡ��Ĭ��ֵnull����ʾ��ǰʱ�䣩
     * @param _bIncludePath
     *            ����ֵ���Ƿ����·������ʡ��Ĭ��ֵ������
     * @return ���ɹ������صõ����ļ��������򣬷���null.
     * @throws WCMException
     */
    public synchronized String getNextFileName(String _sPathFlag,
                                               String _sFileExt, CMyDateTime _crTime, boolean _bIncludePath)
            throws WCMException {
        // ��ȫ�Լ�飺_sFileExt���ܺ���..��Ϣ
        // http://192.9.200.87:8989/browse/WCMVS-420
        /*if (_sFileExt.indexOf("..") >= 0) {
            throw new WCMException("�Ƿ��ļ���׺��Ϣ�����ܻ�ȡ��Ӧ���ļ���");
        }
        _sFileExt = _sFileExt.replace("?", "");
        if (_sFileExt.length() > 8) {
            throw new WCMException("���ǳ����׺��Ϣ�����ܻ�ȡ��Ӧ���ļ���");
        }

        PathConfig pathConfig = null; // Ŀ¼������Ϣ

        String sDate, sTime, sRandom;
        long lTime;
        String sFilePath, sFileName, sFileExt; // ������ļ���������չ����
        int i;

        // ȡĿ¼������Ϣ����������_sPathFlag�Ƿ���Ч
        if (_sPathFlag == null) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label11",
                            "·����ʶΪ�գ�FilesMan.getNextFileName��"));
        }
        _sPathFlag = _sPathFlag.trim().toUpperCase();
        pathConfig = (PathConfig) m_hPathConfig.get(_sPathFlag);
        if (pathConfig == null) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label12",
                            "��Ч��·����ʶ����(FilesMan.getNextFileName)"));
        }

        // �����ļ��Ĵ���ʱ�����
        if ((_crTime == null) || _crTime.isNull()) {
            _crTime = CMyDateTime.now(); // ȡ��ǰʱ��
        }
        // ��ȡ�������ڣ�8λ:yyyMMdd��
        sDate = _crTime.toString("yyyyMMdd");

        // ��ȡ�ļ����·��������·����
        sFilePath = pathConfig.getLocalPath() + _sPathFlag
                + sDate.substring(0, 6) + File.separatorChar + _sPathFlag
                + sDate.substring(0, 8) + File.separatorChar;
        // ���·���Ƿ����
        if (!CMyFile.fileExists(sFilePath)) { // ·�������ڣ��򴴽�֮
            try {
                CMyFile.makeDir(sFilePath, true); // ����Ŀ¼
            } catch (Exception ex) {
                throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                        I18NMessage.get(FilesMan.class, "FilesMan.label13",
                                "ָ��·��")
                                + sFilePath
                                + I18NMessage.get(FilesMan.class,
                                "FilesMan.label14",
                                "�޷�����(FilesMan.getNextFileName)"));
            }// end try
        }// end if

        // ȡ����ʱ�䣨7λ��
        // ע�⣺����ʱ��ֵʱ�����뽫mydtNow.getTimeInMillis()ֵ����ʱ���
        lTime = (_crTime.getTimeInMillis() + TIMEZONE_RAWOFFSET)
                % CMyDateTime.ADAY_MILLIS;
        sTime = CMyString.numberToStr(lTime, 8, '0');

        // ȡ4λ�����
        sRandom = CMyString.numberToStr(Math.round(Math.random() * 10000), 4,
                '0');

        // ������չ��
        sFileExt = _sFileExt.trim();
        // ����һ���ַ��Ƿ�Ϊ'.'
        if ((sFileExt.length() > 0) && (sFileExt.charAt(0) != '.')) {
            sFileExt = "." + sFileExt;
        }

        // �����ļ�����
        // �ڼ���ļ����ظ�ʱ��������������Ρ�
        sFileName = _sPathFlag + sDate + sTime + sRandom;
        for (i = 0; i < 2; i++) {
            if (i > 0) { // ��չ������Խ���ظ����⣬ÿ�������չ��λ
                sFileName += CMyString.numberToStr(
                        Math.round(Math.random() * 100), 2, '0');
            }
            // ����ļ����Ƿ��ظ�
            if (!CMyFile.fileExists(sFilePath + sFileName + sFileExt))
                return (_bIncludePath ? sFilePath : "") + sFileName + sFileExt; // �ҵ����ظ����ļ���
        }*/// end for
        return null; // û���ҵ����ظ����ļ���
    }// END:getNextFileName

    /**
     * ��ȡ��һ�����õ��ļ�����
     *String _sPathFlag,String _sFileExt, CMyDateTime _crTime, boolean _bIncludePath
//     * @see getNextFileName( String _sPathFlag, String _sFileExt, CMyDateTime
//     *      _crTime, boolean _bIncludePath )
     */
    public synchronized String getNextFileName(String _sPathFlag,
                                               String _sFileExt) throws WCMException {
        return getNextFileName(_sPathFlag, _sFileExt, null, false);
    }

    /**
     * ��ȡ��һ�����õ��ļ�����
     *
//     * @see getNextFileName( String _sPathFlag, String _sFileExt, CMyDateTime
//     *      _crTime, boolean _bIncludePath )
     */
    public synchronized String getNextFileName(String _sPathFlag,
                                               String _sFileExt, CMyDateTime _crTime) throws WCMException {
        return getNextFileName(_sPathFlag, _sFileExt, _crTime, false);
    }

    /**
     * ��ȡ��һ�����õ��ļ�����
     *
//     * @see getNextFileName( String _sPathFlag, String _sFileExt, CMyDateTime
//     *      _crTime, boolean _bIncludePath )
     */
    public synchronized String getNextFilePathName(String _sPathFlag,
                                                   String _sFileExt) throws WCMException {
        return getNextFileName(_sPathFlag, _sFileExt, null, true);
    }

    /**
     * ��ȡ��һ�����õ��ļ�����
     *
//     * @see getNextFileName( String _sPathFlag, String _sFileExt, CMyDateTime
//     *      _crTime, boolean _bIncludePath )
     */
    public synchronized String getNextFilePathName(String _sPathFlag,
                                                   String _sFileExt, CMyDateTime _crTime) throws WCMException {
        return getNextFileName(_sPathFlag, _sFileExt, _crTime, true);
    }

    // ==========================================================================
    // ���ļ�������ȡ�ļ�����ʱ��
    // �ļ���ʽ��ff+yyyy,mm,dd+tt,ttt,ttt+rrrr.ext
    // index: 01 2345 67 89 01 234 567 8901

    /**
     * ��ȡ����ֵ
     *
     * @param _sFileName
     *            �ļ���
     * @return �ַ�������ʽ��yyyy-MM-dd
     * @throws WCMException
     *             ���ļ�����ʽ�������׳��쳣
     */
    public static String extractFileCrDateValue(String _sFileName)
            throws WCMException {
        if (_sFileName.length() < FILENAME_MIN_LENGTH) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label15",
                            "��Ч���ļ���ʽ(FilesMan.extractFileCrDateValue)"));
        }

        String sDate;
        sDate = _sFileName.substring(2, 6) + "-" + _sFileName.substring(6, 8)
                + "-" + _sFileName.substring(8, 10);
        return sDate;
    }

    /**
     * ���ļ�������ȡ����ʱ��ֵ
     *
     * @param _sFileName
     *            �ļ���
     * @return long��
     * @throws WCMException
     *             ���ļ�����ʽ�������׳��쳣
     */
    public static long extractFileCrTimeValue(String _sFileName)
            throws WCMException {
        if (_sFileName.length() < FILENAME_MIN_LENGTH) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label16",
                            "��Ч���ļ���ʽ(FilesMan.extractFileCrTimeValue)"));
        }

        long lTime;
        try {
            lTime = Integer.parseInt(_sFileName.substring(10, 18));
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label16",
                            "��Ч���ļ���ʽ(FilesMan.extractFileCrTimeValue)"), ex);
        }// end try
        return lTime;
    }

    /**
     * ��ȡ��������ֵ
     *
     * @param _sFileName
     * @return ����ֵ��CMyDateTime
     * @throws WCMException
     *             �쳣�����ļ�����ʽ�������׳��쳣
     */
    public static CMyDateTime extractFileCrDate(String _sFileName)
            throws WCMException {
        CMyDateTime mydtCrDate;
        String sDate;

        // ȡ�����ַ���ֵ
        sDate = extractFileCrDateValue(_sFileName);

        // ת��Ϊ����ֵ
        try {
            mydtCrDate = new CMyDateTime();
            mydtCrDate.setDateWithString(sDate, CMyDateTime.FORMAT_DEFAULT);
        } catch (CMyException ex) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label17",
                            "��Ч���ļ���ʽ(FilesMan.extractFileCrDate)"), ex);
        }// end try
        return mydtCrDate;
    }

    /**
     * ��ȡ�ļ�����������ʱ��ֵ
     *
     * @param _sFileName
     *            �ļ���
     * @return CMyDateTime ����ʱ��ֵ
     * @throws WCMException
     *             �쳣�����ļ�����ʽ�������׳��쳣
     */
    public static CMyDateTime extractFileCrDateTime(String _sFileName)
            throws WCMException {
        CMyDateTime mydtCrDate = null; // ��������
        long lTime; // ʱ��ֵ����������

        mydtCrDate = extractFileCrDate(_sFileName); // ȡ����ֵ
        lTime = extractFileCrTimeValue(_sFileName); // ȡʱ��ֵ
        return new CMyDateTime(mydtCrDate.getTimeInMillis() + lTime);
    }// END:extractFileCrTime

    // =======================================================================
    // ��չ�߼��ӿ�

    /**
     * ��ָ���ļ����Ƶ�ָ����Ŀ¼
     *
     * @param _srcFilePathName
     *            ԭ�ļ���������·����
     * @param _dstPathFlag
     *            Ŀ��Ŀ¼��ʶ����������Ч��Ŀ¼��ʶ��
     * @param _bReturnPath
     *            �Ƿ񷵻ر������ļ���
     * @return
     * @throws WCMException
     */
    public String copyFile(String _srcFilePathName, String _dstPathFlag,
                           boolean _bReturnPath) throws WCMException {
        try {
            String sFileExt = CMyFile.extractFileExt(_srcFilePathName); // ȡԭ�ļ���չ��
            String sSaveFilePathName = getNextFilePathName(_dstPathFlag,
                    sFileExt);
            // �����һ�����õ��ļ���
            CMyFile.copyFile(_srcFilePathName, sSaveFilePathName); // �����ļ�
            return (_bReturnPath ? sSaveFilePathName : CMyFile
                    .extractFileName(sSaveFilePathName));
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label18",
                            "�����ļ���ָ��Ŀ¼ʧ�ܣ�FilesMan.copyFile��"), ex);
        }
    }// END: copyFile()

    /**
     * ��ָ���ļ��ƶ���ָ����Ŀ¼
     *
     * @param _srcFilePathName
     *            ԭ�ļ���������·����
     * @param _dstPathFlag
     *            Ŀ��Ŀ¼��ʶ����������Ч��Ŀ¼��ʶ��
     * @param _bReturnPath
     *            �Ƿ񷵻ر������ļ���
     * @return
     * @throws WCMException
     */
    public String moveFile(String _srcFilePathName, String _dstPathFlag,
                           boolean _bReturnPath) throws WCMException {
        try {
            String sSaveFile = copyFile(_srcFilePathName, _dstPathFlag,
                    _bReturnPath);
            // add by caohui@2016��3��20�� ����1:20:20
            // ���ǰ�˶�����󣬵����ļ���ɾ��
            if (DELETE_FILE_ON_MOVE)
                CMyFile.deleteFile(_srcFilePathName);
            return sSaveFile;
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label19",
                            "�ƶ��ļ���ָ��Ŀ¼ʧ�ܣ�FilesMan.moveFile��"), ex);
        }
    }// END: moveFile()

    /**
     * ɾ��ָ�����ļ�
     *
     * @param _sFileName
     *            ����WCM�ļ�������������ļ���
     * @return ɾ���ɹ�ʱ����true�����򷵻�false
     * @throws WCMException
     */
    public boolean deleteFile(String _sFileName) throws WCMException {
        try {
            String sFilePathName;

            _sFileName = CMyFile.extractFileName(_sFileName.trim());
            sFilePathName = this.mapFilePath(_sFileName, PATH_LOCAL)
                    + _sFileName;

            return CMyFile.deleteFile(sFilePathName);

        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label20",
                            "ɾ��ָ�����ļ�ʧ�ܣ�FilesMan.deleteFile��"), ex);
        }
    }// END: removeFile()

    // =======================================================================
    // Ӧ�����

    /**
     * <p>
     * ģ�帽���ļ�����Ŀ¼ӳ��
     * <p>
     * Ŀ¼��֯���ļ�������TemplateĿ¼�£�����վ�㻮����Ŀ¼
     * <p>
     * �������� <Template Path>\site[վ��ID]\
     *
     * @param _nSiteId
     *            վ��ID
     * @param _nPathType
     *            ·�����ͣ�������FilesMan�У�
     * @param _bCreateWhenNotExists
     *            ·��������ʱ�Ƿ��Զ�����
     * @return ģ�帽���ļ�����Ŀ¼
     * @throws WCMException
     *             ·�����Ͳ������������µ�Ŀ¼ʱ
     * @deprecated to use getTemplateAppendixPath(WebSite.OBJ_TYPE, _nSiteId,
     *             _nPathType, _bCreateWhenNotExists) instead please.
     */
    public String getTempAppendixFilePath(int _nSiteId, int _nPathType,
                                          boolean _bCreateWhenNotExists) throws WCMException {
        return getTemplateAppendixPath(103, _nSiteId, _nPathType,
                _bCreateWhenNotExists);
    }

    /**
     * Returns the template appendix file path of the sepcified root. <BR>
     * Path format rule: <BR>
     * [1]to keep the compatibility with WCM5.1 and before versions, if the root
     * is a website (Type=103), returns "site[Root Id]\" or "site[Root Id]/"
     * like "site2\" or "site2/"; <BR>
     * [2]otherwise, returns "root[Root Type in Hex mode]\[Root Id]\" or
     * "root[Root Type in Hex mode]\[Root Id]\", like "root10A\12\" or
     * "root10A/12/". <BR>
     *
     * @param _nRootType
     *            root type, like WebSite.OBJ_TYPE
     * @param _nRootId
     *            root id.
     * @param _nPathType
     *            path type, like PATH_LOCAL, PATH_HTTP, etc.
     * @param _bCreateWhenNotExists
     *            <code>true</code>, to create the path if it is a local
     *            directory.
     * @return the template appendix file path of the sepcified root.
     * @throws WCMException
     *             if failed to find the config or create path.
     */
    public String getTemplateAppendixPath(int _nRootType, int _nRootId,
                                          int _nPathType, boolean _bCreateWhenNotExists) throws WCMException {
        String sPath = this.getPathConfigValue(FLAG_TEMPLATE, _nPathType);
        if (sPath == null) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    "Path config for " + FLAG_TEMPLATE + " missing!");
        }

        char chrSpearator = (_nPathType == PATH_LOCAL ? File.separatorChar
                : '/');
        if (_nRootType == 103) {// The magic num. for not to import the WebSite.
            sPath += "site";
        } else {
            sPath += "root" + Integer.toHexString(_nRootType) + chrSpearator;
        }

        sPath += _nRootId + "" + chrSpearator;
        if (_nPathType == PATH_LOCAL && _bCreateWhenNotExists) {
            try {
                CMyFile.makeDir(sPath, true);
            } catch (Exception ex) {
                throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                        "Failed to create template appendixes path:" + sPath,
                        ex);
            }
        }

        return sPath;
    }

    /**
     * �����ļ����������ļ�����·����Flag��ʶ����FilesMan.Flag_Protected
     *
     * @param sFileName
     * @return
     * @throws WCMException
     */
    public String getFileFlag(String sFileName) {

        String sFlag = "";
        String sFileHeader = "";

        // У�����
        if (sFileName == null)
            sFlag = "";

        // ����ͷ��������
        sFileHeader = sFileName.substring(0, 2);

        if (sFileHeader.equals(FLAG_PROTECTED))
            sFlag = FLAG_PROTECTED;

        if (sFileHeader.equals(FLAG_NORMAL))
            sFlag = FLAG_NORMAL;

        if (sFileHeader.equals(FLAG_UPLOAD))
            sFlag = FLAG_UPLOAD;

        if (sFileHeader.equals(FLAG_SYSTEMTEMP))
            sFlag = FLAG_SYSTEMTEMP;

        if (sFileHeader.equals(FLAG_USERTEMP))
            sFlag = FLAG_USERTEMP;

        if (sFileHeader.equals(FLAG_TEMPLATE))
            sFlag = FLAG_TEMPLATE;

        if (sFileHeader.equals(FLAG_LOCALPUB))
            sFlag = FLAG_LOCALPUB;

        if (sFileHeader.equals(FLAG_WEBFILE))
            sFlag = FLAG_WEBFILE;

        return sFlag;
    }

    // =======================================================================
    // ����ӿڲ���

    /**
     * ��ָ���ļ��ƶ���ָ����Ŀ¼
     *
     * @param _srcFileName
     *            ԭ�ļ�����������·������WCM��ʶ���е�
     * @param _dstPathFlag
     *            Ŀ��Ŀ¼��ʶ����������Ч��Ŀ¼��ʶ��
     * @param _bReturnPath
     *            �Ƿ񷵻ر������ļ���
     * @return
     * @throws WCMException
     */
    public String moveWCMFile(String _srcFileName, String _dstPathFlag,
                              boolean _bReturnPath) throws WCMException {

        // У�����
        if (_srcFileName == null) {
            throw new WCMException(ExceptionNumber.ERR_PARAM_INVALID,
                    I18NMessage.get(FilesMan.class, "FilesMan.label21",
                            "Դ�ļ���Ϊ�գ�"));
        }

        // ����·�������Ա���ýӿ��ƶ�
        String sPathName = mapFilePath(_srcFileName, PATH_LOCAL);

        try {
            String sMoveFile = moveFile(sPathName + _srcFileName, _dstPathFlag,
                    _bReturnPath);
            return sMoveFile;
        } // enf of try
        catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label19",
                            "�ƶ��ļ���ָ��Ŀ¼ʧ�ܣ�FilesMan.moveFile��"), ex);
        }

    }// END: moveWCMFile()

    /**
     * ��ָ��WCM�ļ����Ƶ�Ŀ��Ŀ¼
     *
     * @param _srcFileName
     *            ��WCMԭʼ�ļ�������·��
     * @param _dstPathFlag
     * @return�����ƺ��WCM�ļ���
     * @throws WCMException
     */
    public String copyWCMFile(String _srcFileName, String _dstPathFlag)
            throws WCMException {

        try {
            String sFileName = mapFilePath(_srcFileName, FilesMan.PATH_LOCAL)
                    + _srcFileName;
            // �����ļ�
            return copyFile(sFileName, _dstPathFlag, false);
        } catch (Exception ex) {
            throw new WCMException(ExceptionNumber.ERR_WCMEXCEPTION,
                    I18NMessage.get(FilesMan.class, "FilesMan.label22",
                            "����WCM�ļ���ָ��Ŀ¼ʧ��"), ex);
        }
    }// END: copyFile()

    private static void loadFilesMan() {
        try {
//            m_filesMan.loadPathConfigs(true); // TODO װ��Ŀ¼������Ϣ
            s_logger.info(I18NMessage.get(FilesMan.class, "FilesMan.label23",
                    "װ��Ŀ¼������Ϣ��ɣ�"));
        } catch (Exception ex) {
            s_logger.error(I18NMessage.get(FilesMan.class, "FilesMan.label24",
                    "װ��Ŀ¼������Ϣʧ��"), ex);
        }
    }

    public static boolean isValidFile(String _sFileName, String _sPathFlag) {
        // HTTP������ͼƬ
        int nLastPos = _sFileName.lastIndexOf('/');
        if (nLastPos >= 0) {
            // 1.Extract File Name
            String sFileName = _sFileName.substring(nLastPos + 1);

            // �����WEB��ַ���ļ���ҪУ���ļ��Ƿ����
            if (_sFileName.trim().toUpperCase().indexOf("HTTP") == 0) {
                try {
                    if (!CMyFile.fileExists(FilesMan.getFilesMan().mapFilePath(
                            sFileName, FilesMan.PATH_LOCAL)
                            + sFileName)) {
                        s_logger.error(I18NMessage.get(FilesMan.class,
                                "FilesMan.label25", "�ļ�[")
                                + sFileName
                                + I18NMessage.get(FilesMan.class,
                                "FilesMan.label26", "]�����ڣ��Զ���[")
                                + _sFileName
                                + I18NMessage.get(FilesMan.class,
                                "FilesMan.label27", "]���أ�"));
                        return false;
                    }
                    // ge modify by gfc @2008-2-20 ����10:24:52
                    // ���β���Ҫ���ļ���ʽУ���error��Ϣ
                    // }catch (Exception e) {
                    // s_logger.error("�ļ�["+sFileName+I18NMessage.get(FilesMan.
                    // class, "FilesMan.label28", "]������Ч���ļ�!"));
                    // return false;
                    // //e.printStackTrace();
                    // }
                } catch (WCMException e) {
                    // ��֪�ġ���FilesMan.mapFilePath�׳����쳣��ֱ�ӷ��ز������Ϣ
                    return false;
                } catch (Exception e) {
                    s_logger.warn(I18NMessage.get(FilesMan.class,
                            "FilesMan.label29", "У���ļ�[")
                            + sFileName
                            + I18NMessage.get(FilesMan.class,
                            "FilesMan.label30", "]��ʽʱ����δ������쳣��")
                            + e.getMessage());
                    return false;
                }
            }

            // 2.Validate File Name
            boolean bValidFile = isValidFile(sFileName, _sPathFlag);
            if (!bValidFile)
                return false;

            // 3.Validate HttpPath
            try {
                String sPath = FilesMan.getFilesMan().mapFilePath(sFileName,
                        FilesMan.PATH_HTTP);
                return _sFileName.indexOf(sPath) >= 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (_sFileName == null
                || _sFileName.length() < FilesMan.FILENAME_MIN_LENGTH)
            return false;
        if (_sFileName.indexOf(_sPathFlag) != 0)
            return false;
        return true;
    }

    /**
     * ����FilesMan�Ĺ����ж��ļ���Ӧ��ʵ���ļ������Ƿ���� <BR>
     * ���׳��쳣��ֻ����true/false <BR>
     *
     * @param _sFileName
     *            ����·����������FilesMan������ļ���
     * @return true��ʾ�ļ����ڣ�false��ʾ������
     */
    public boolean fileExists(String _sFileName) {
        // 01. У�����
        if (_sFileName == null) {
            return false;
        }

        // 02. �����ļ���ȫ�ļ�����·��+�ļ�����
        try {
            String sFullName = mapFilePath(_sFileName, FilesMan.PATH_LOCAL)
                    + _sFileName;
            if (CMyFile.fileExists(sFullName))
                return true;
        } catch (WCMException ex) {
            s_logger.warn(I18NMessage.get(FilesMan.class, "FilesMan.label31",
                    "�޷�ӳ���ļ��ı���·���������������߼�������������С�"));
            return false;
        }

        return false;
    } // END of fileExists

    /**
     * TODO ˢ��Ŀ¼���û���
     *
     * @param _config
     *            ָ����Ŀ¼����
     * @return
     * @throws Exception
     */
/*    public boolean refreshPathConfig(Config _config) throws Exception {
        if (true) {// �������ݿ�������ã����Ǵ��ļ�����
            return true;
        }
        String value = _config.getNewPropertyAsString("CVALUE");
        if (value == null) {
            return this.putPathConfig(_config, true);
        }// �½�

        // else config value changed
        return this.putPathConfig(_config.getConfigKey(),
                new PathConfig(value), true);
    }*/
}