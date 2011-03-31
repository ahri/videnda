package com.kaizen.videnda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RemoteDeck
{
        public String md5;
        public String name;
        public URL    remote;
        public File   local;

        public RemoteDeck(URL remote, File local, String md5)
        {
                this.remote = remote;
                this.local = local;
                this.md5 = md5;
                this.name = remote.getFile().replaceFirst("^.*/", "").replaceFirst("\\.zip$", "").replaceAll("%20", " ");
        }

        public boolean alreadyDownloaded()
        {
                if (!this.local.exists()) {
                        return false;
                }

                MessageDigest md;
                InputStream is = null;
                try {
                        md = MessageDigest.getInstance("MD5");
                        try {
                                is = new FileInputStream(this.local);
                                is = new DigestInputStream(is, md);
                                byte[] b = new byte[(int) this.local.length()];
                                is.read(b);
                        } finally {
                                is.close();
                        }
                        byte[] digest = md.digest();
                        BigInteger bi = new BigInteger(1, digest);
                        return String.format("%0" + (digest.length << 1) + "x", bi).equals(this.md5);
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                
                return false;
        }
        
        public boolean download()
        {
                int readBufLen = 1024;
                HttpURLConnection http_conn = null;
                InputStream in = null;
                FileOutputStream fos = null;

                try {
                        http_conn = (HttpURLConnection) this.remote.openConnection();

                        if (http_conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                                return false;
                        }
                        
                        in = http_conn.getInputStream();
                        fos = new FileOutputStream(this.local);

                        byte[] b = new byte[readBufLen];
                        int len = 0;
                        while((len = in.read(b, 0, readBufLen)) > 0) {
                                fos.write(b, 0, len);
                        }

                        return true;
                } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } finally {
                        try {
                                http_conn.disconnect();
                                in.close();
                                fos.close();
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                }

                return false;
        }
}
