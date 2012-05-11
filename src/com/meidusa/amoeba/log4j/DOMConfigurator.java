package com.meidusa.amoeba.log4j;

import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.FileWatchdog;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.OptionConverter;

import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.URL;

import java.util.Properties;

/**
 * ��XML�ļ�����log4j�Ĺ����ࡣ��Log4jĬ�ϵ�<code>DOMConfigurator</code>��ȣ�����������ṩ�����properties�����������ļ��б����á�
 */
public class DOMConfigurator extends org.apache.log4j.xml.DOMConfigurator {
    private Properties props;

    /**
     * �����¶���
     */
    public DOMConfigurator() {
        this(null);
    }

    /**
     * �����¶���
     *
     * @param props ���������ļ��б����õ�����
     */
    public DOMConfigurator(Properties props) {
        this.props = props;
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param filename �����ļ���
     */
    public static void configure(String filename) {
        new DOMConfigurator().doConfigure(filename, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param stream �����ļ�������
     */
    public static void configure(InputStream stream) {
        new DOMConfigurator().doConfigure(stream, LogManager.getLoggerRepository());
    }
    
    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param filename �����ļ���
     * @param props ���������ļ��б����õ�����
     */
    public static void configure(String filename, Properties props) {
        new DOMConfigurator(props).doConfigure(filename, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param url �����ļ���URL
     */
    public static void configure(URL url) {
        new DOMConfigurator().doConfigure(url, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param url �����ļ���URL
     * @param props ���������ļ��б����õ�����
     */
    public static void configure(URL url, Properties props) {
        new DOMConfigurator(props).doConfigure(url, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param element �����ļ�����DOM element
     */
    public static void configure(Element element) {
        new DOMConfigurator().doConfigure(element, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��
     *
     * @param element �����ļ�����DOM element
     * @param props ���������ļ��б����õ�����
     */
    public static void configure(Element element, Properties props) {
        new DOMConfigurator(props).doConfigure(element, LogManager.getLoggerRepository());
    }

    /**
     * ʹ��XML�ļ�����log4j��ͬʱ����ļ��ĸ��ġ�
     *
     * @param filename �����ļ���
     */
    public static void configureAndWatch(String filename) {
        configureAndWatch(filename, null, FileWatchdog.DEFAULT_DELAY);
    }

    /**
     * ʹ��XML�ļ�����log4j��ͬʱ����ļ��ĸ��ġ�
     *
     * @param filename �����ļ���
     * @param props ���������ļ��б����õ�����
     */
    public static void configureAndWatch(String filename, Properties props) {
        configureAndWatch(filename, props, FileWatchdog.DEFAULT_DELAY);
    }

    /**
     * ʹ��XML�ļ�����log4j��ͬʱ����ļ��ĸ��ġ��˷���������һ������̣߳����̵߳ڸ�ָ��ʱ��ͻ����ļ��Ƿ񱻴�����ı䣬����ǣ�����ļ��ж�ȡlog4j���á�
     *
     * @param filename �����ļ���
     * @param interval ����̼߳������ms��
     */
    public static void configureAndWatch(String filename, long interval) {
        XMLWatchdog xdog = new XMLWatchdog(filename, null);

        xdog.setDelay(interval);
        xdog.start();
    }

    /**
     * ʹ��XML�ļ�����log4j��ͬʱ����ļ��ĸ��ġ��˷���������һ������̣߳����̵߳ڸ�ָ��ʱ��ͻ����ļ��Ƿ񱻴�����ı䣬����ǣ�����ļ��ж�ȡlog4j���á�
     *
     * @param filename �����ļ���
     * @param props ���������ļ��б����õ�����
     * @param interval ����̼߳������ms��
     */
    public static void configureAndWatch(String filename, Properties props, long interval) {
        XMLWatchdog xdog = new XMLWatchdog(filename, props);

        xdog.setDelay(interval);
        xdog.start();
    }

    /**
     * �������ԣ���Щ���Կ����������ļ��б����á�
     *
     * @param props ����
     */
    public void setProperties(Properties props) {
        this.props = props;
    }

    /**
     * �滻�ַ���ֵ�������е�${xxx}�滻�ɾ����ֵ��
     *
     * @param value Ҫ�滻��ֵ
     *
     * @return �滻���ֵ
     */
    protected String subst(String value) {
        try {
            return OptionConverter.substVars(value, props);
        } catch (IllegalArgumentException e) {
            LogLog.warn("Could not perform variable substitution.", e);
            return value;
        }
    }

    /**
     * ����̡߳�
     */
    private static class XMLWatchdog extends FileWatchdog {
        private Properties props;

        public XMLWatchdog(String filename, Properties props) {
            super(filename);
            this.props = props;
        }

        public void doOnChange() {
            new DOMConfigurator(props).doConfigure(filename, LogManager.getLoggerRepository());
            LogLog.warn("log4j config load completed from file:"+filename);
        }
    }
}
