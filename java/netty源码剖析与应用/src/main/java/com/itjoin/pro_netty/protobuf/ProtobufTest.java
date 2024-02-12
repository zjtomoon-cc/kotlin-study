package com.itjoin.pro_netty.protobuf;

import com.alibaba.fastjson.JSONObject;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProtobufTest {

    public static void main(String[] args) throws IOException {
        RuntimeSchema<User> schema = RuntimeSchema.createFrom(User.class);
        User user = new User();
        user.setAge(20);
        user.setUserName("zhangsan");
        user.setHobbyhobby("swimming");
        Product pp = new Product();
        pp.setId("1");
        user.setP(pp);
        Book b= new Book();
        b.setId(1);
        pp.setB(b);
        byte[] bytes = ProtostuffIOUtil.toByteArray(user, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        System.out.println(bytes.length);
        writeFile("D:\\2.bin",bytes);
        //反序列化
        User sc = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, sc, schema);
        System.out.println(JSONObject.toJSONString(sc));
    }

    public static void writeFile( String fileName, byte[] content)
            throws IOException {
        try {
            File f = new File(fileName);
//            if (!f.exists()) {
//                f.mkdirs();
//            }
            FileOutputStream fos = new FileOutputStream( fileName);
            fos.write(content);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
