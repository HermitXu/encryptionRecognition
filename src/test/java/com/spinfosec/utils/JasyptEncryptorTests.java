package com.spinfosec.utils;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JasyptEncryptorTests
{
    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void contextLoads()
    {
        String spinfoEnc = stringEncryptor.encrypt("Spinfo0123");
        String rootEnc = stringEncryptor.encrypt("root");
        System.out.println("spinfoEnc enc = " + spinfoEnc);
        System.out.println("rootEnc enc = " + rootEnc);
    }

}
