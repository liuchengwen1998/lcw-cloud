package com.lcw.cloud.deploy.configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * 配置文件加解密处理类
 * @author yzhang
 * @since 1.0
 */
public class JasyptEncryptor {
    /**
     * 加密秘钥
     */
    private static final String JASYPT_ENCRYPT_PASSWORD = "lcw-cloud";

    private static StringEncryptor encryptor = null;

    static {
        PooledPBEStringEncryptor pooledEncryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(JASYPT_ENCRYPT_PASSWORD);
        config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
        config.setStringOutputType("base64");
        pooledEncryptor.setConfig(config);
        encryptor = pooledEncryptor;
    }

    /**
     * 加密
     * @param command 明文加密
     * @return 密文
     */
    public  String encrypt(String command) {
        return encryptor.encrypt(command);
    }

    /**
     * 解密
     * @param command 密文解密
     * @return 明文
     */
    public  String decrypt(String command) {
        return encryptor.decrypt(command);
    }

}
