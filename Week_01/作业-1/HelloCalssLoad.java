package com.bootdo.jike;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

public class HelloCalssLoad extends  ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object hello = new HelloCalssLoad().findClass("com.bootdo.jike.Hello").newInstance();

    }
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //这个classLoader的主要方法
        System.out.println("findClassfindClassfindClassfindClass");
        Class clazz = null;
        try {
            File file = new File("D:\\F盘\\github\\bootdo\\bootdo\\src\\test\\java\\com\\bootdo\\jike\\Hello.class");
            byte[] data = getClassFileBytes(file);
            clazz = defineClass(name, data, 0, data.length);//这个方法非常重要
            if (null == clazz) {//如果在这个类加载器中都不能找到这个类的话，就真的找不到了

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }


    private byte[] getClassFileBytes(File file) throws Exception {
        //采用NIO读取
        FileInputStream fis = new FileInputStream(file);
        FileChannel fileC = fis.getChannel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        WritableByteChannel outC = Channels.newChannel(baos);
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        while (true) {
            int i = fileC.read(buffer);
            if (i == 0 || i == -1) {
                break;
            }
            buffer.flip();
            outC.write(buffer);
            buffer.clear();
        }
        fis.close();
        return baos.toByteArray();
    }

    }
