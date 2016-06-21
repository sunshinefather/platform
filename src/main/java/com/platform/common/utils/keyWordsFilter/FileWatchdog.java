package com.platform.common.utils.keyWordsFilter;
import java.io.File;
/**
 * 文件变化监听
 * @ClassName:  FileWatchdog   
 * @Description:TODO   
 * @author: sunshine  
 * @date:   2016年3月24日 下午5:17:01
 */
public abstract class FileWatchdog extends Thread {  
  
	public static final  long DEFAULT_DELAY = 10*1000;
    private long delay = DEFAULT_DELAY;//每个多久检查一次
    private File file;
    private long lastModif = 0; 
    private boolean interrupted = false; //是否启动监听
  
    protected FileWatchdog(String filename) {  
        super("FileWatchdog");  
        this.file = new File(filename);
        setDaemon(true);
        checkAndConfigure();  
    }
    
    public void setDelay(long delay) {  
        this.delay = delay;  
    }  
  
    abstract protected void onChange(File  file);  
  
    protected void checkAndConfigure() {  
        boolean fileExists;  
        try {  
            fileExists = file.exists();
        } catch (SecurityException e) {  
            interrupted = true;
            return;  
        }  
  
        if (fileExists) {  
            long l = file.lastModified();
            if (l != lastModif) {
                lastModif = l;
                onChange(file);
            }  
        }
    }  
  
    public void run() { 
        while (!interrupted) {
            try {  
                Thread.sleep(delay);  
            } catch (InterruptedException e) {  
            }  
            checkAndConfigure();  
        }  
    }  
}