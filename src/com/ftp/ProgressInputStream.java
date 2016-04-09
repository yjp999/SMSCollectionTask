package com.ftp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.cloudyang.messageupload.SmsListActivity;
import com.ftp.FTP.UploadProgressListener;


public class ProgressInputStream extends InputStream {

    private static final int TEN_KILOBYTES = 1024 * 10;  //姣忎笂浼�0K杩斿洖涓�

    private InputStream inputStream;

    private long progress;
    private long lastUpdate;

    private boolean closed;
    
    private UploadProgressListener listener;
    private File localFile;

    public ProgressInputStream(InputStream inputStream,UploadProgressListener listener,File localFile) {
        this.inputStream = inputStream;
        this.progress = 0;
        this.lastUpdate = 0;
        this.listener = listener;
        this.localFile = localFile;
        
        this.closed = false;
    }

    @Override
    public int read() throws IOException {
        int count = inputStream.read();
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = inputStream.read(b, off, len);
        return incrementCounterAndUpdateDisplay(count);
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (closed)
            throw new IOException("already closed");
        closed = true;
    }

    private int incrementCounterAndUpdateDisplay(int count) {
        if (count > 0)
            progress += count;
        lastUpdate = maybeUpdateDisplay(progress, lastUpdate);
        return count;
    }

    private long maybeUpdateDisplay(long progress, long lastUpdate) {
        if (progress - lastUpdate > TEN_KILOBYTES) {
            lastUpdate = progress;
            this.listener.onUploadProgress(SmsListActivity.FTP_UPLOAD_LOADING, progress, this.localFile);
        }
        return lastUpdate;
    }
    
  
    
}
