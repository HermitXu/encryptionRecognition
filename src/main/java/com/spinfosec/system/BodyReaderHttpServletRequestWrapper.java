package com.spinfosec.system;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * @author ank
 * @version v 1.0
 * @title [自定义request包装类，用于克隆Request中的RequestBody数据]
 * @ClassName: com.spinfosec.system.BodyReaderHttpServletRequestWrapper
 * @description [自定义request包装类，用于克隆Request中的RequestBody数据]
 * @create 2018/11/26 19:49
 * @copyright Copyright(C) 2018 SHIPING INFO Corporation. All rights reserved.
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper
{
    private final byte[] body;

    public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException
    {
        super(request);
        String sessionStream = getBodyString(request);
        body = sessionStream.getBytes(Charset.forName("UTF-8"));
    }

    public String getBodyString()
    {
        return new String(body, Charset.forName("UTF-8"));
    }

    /**
     * 获取请求Body
     *
     * @param request
     * @return
     */
    private String getBodyString(final ServletRequest request)
    {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try
        {
            inputStream = cloneInputStream(request.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * Description: 复制输入流</br>
     *
     * @param inputStream
     * @return</br>
     */
    public InputStream cloneInputStream(ServletInputStream inputStream)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try
        {
            while ((len = inputStream.read(buffer)) > -1)
            {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException
    {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException
    {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream()
        {

            @Override
            public int read() throws IOException
            {
                return bais.read();
            }

            @Override
            public boolean isFinished()
            {
                return false;
            }

            @Override
            public boolean isReady()
            {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener)
            {

            }
        };
    }
}
