package com.spinfosec.utils.sm2;

/**
 * 公钥和私钥对
 *
 * @author liuqianfei
 * @since 2018/6/24 14:47
 */
public class SMKeyPair
{
    private String publicKey;

    private String privateKey;

    public SMKeyPair()
    {
    }

    public SMKeyPair(String publicKey, String privateKey)
    {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String getPublicKey()
    {
        return publicKey;
    }

    public void setPublicKey(String publicKey)
    {
        this.publicKey = publicKey;
    }

    public String getPrivateKey()
    {
        return privateKey;
    }

    public void setPrivateKey(String privateKey)
    {
        this.privateKey = privateKey;
    }

    @Override
    public String toString()
    {
        return "SMKeyPair{" +
                "publicKey='" + publicKey + '\'' +
                ", privateKey='" + privateKey + '\'' +
                '}';
    }
}
