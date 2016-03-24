package com.platform.common.utils.keyWordsFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class BanWordsUtil {
	    public static final int WORDS_MAX_LENGTH = 12;
	    public static final String BAN_WORDS_LIB_FILE_NAME = "banWords.txt";
	    public static Map<String,String>[] banWordsList = null;
	    public static Map<String, Integer> wordIndex = new Hashtable<String, Integer>();
	    private static volatile boolean loadTag=false;//加载成功标记,加载成功才能重新写入数据到文件
	    static{
				initBanWordsList();
	    }
	    
	    private BanWordsUtil(){
	    	
	    }
	    /*
	     * 初始化加载文件中的敏感词库
	     */
	    @SuppressWarnings("unchecked")
		public static void initBanWordsList(){
	    	
	        if (banWordsList == null) {
	            banWordsList = new Map[WORDS_MAX_LENGTH];
	            for (int i = 0; i < banWordsList.length; i++) {
	                banWordsList[i] = new Hashtable<String, String>();
	            }
	        }
	        
	        String path = BanWordsUtil.class.getResource(BAN_WORDS_LIB_FILE_NAME).getPath();
	  
	        List<String> words=null;
			try {
				words = FileUtils.readLines(FileUtils.getFile(path));
			} catch (IOException e) {
				loadTag=false;
				e.printStackTrace();
			}
	        
	        for (String w : words) {
	        	addBanWords(w);
	        }
	        loadTag=true;
	    }
	    
	    /**
	     * 动态添加关键字到内存中
	     * @Title: addBanWords
	     * @Description: TODO  
	     * @param: @param w      
	     * @return: void
	     * @author: sunshine  
	     * @throws
	     */
	    public static void addBanWords(String w){
	    	if (StringUtils.isNotBlank(w)) {
            	if(w.length()<WORDS_MAX_LENGTH){
            		banWordsList[w.length()].put(w.toLowerCase(), "");
	                Integer index = wordIndex.get(w.substring(0,1));
	                if (index == null) {
	                    index = 0;
	                }
	                int x = (int) Math.pow(2, w.length());
	                index = index > x ? index : x;
	                wordIndex.put(w.substring(0, 1), index);
            	}
            }
	    }
	    /**
	     * 刷入动态添加的关键字
	     * @Title: writeBanWordsToFile
	     * @Description: TODO  
	     * @param: @return      
	     * @return: boolean
	     * @author: sunshine  
	     * @throws
	     */
	    public static boolean writeBanWordsToFile() {
	    	boolean rt=false;
	    	if (loadTag) {
		    	String path = BanWordsUtil.class.getResource(BAN_WORDS_LIB_FILE_NAME).getPath();
		    	Set<String> set=Collections.emptySet();
		    	for(Map<String,String> map:banWordsList){
		    		for(String str:map.keySet()){
		    			set.add(str);
		    		}
		    	}
		    	try {
					FileUtils.writeLines(FileUtils.getFile(path),set,false);
					rt=true;
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    	return rt;
	    }
	  
	    /**
	    * 检索敏感词
	    * @param content
	    * @return
	    */
	    public static List<String> searchBanWords(String content) {
	        List<String> result = new ArrayList<String>();
	        if (banWordsList == null || banWordsList.length==0) {
	        	return result;
	        }
	        int i = 0;
	        content = com.platform.common.utils.StringUtils.extractNLCC(content);
	        for (;i < content.length(); i++) {
	            Integer index = wordIndex.get(content.substring(i, i + 1));
	            int p = 0;
	  
	            while ((index != null) && (index > 0)) {
	                p++;
	                index = index >> 1;
	                String sub = "";
	                if ((i + p) < (content.length())) {
	                    sub = content.substring(i, i+p);
	                } else {
	                    sub = content.substring(i);
	                }

	                if (p < WORDS_MAX_LENGTH && banWordsList[p].containsKey(sub)) {
	                    result.add(content.substring(i, i + p));
	                   // index=0;//查找所有敏感词时去掉此行注释，同时注释下边第一条
	                    return result;//找到一个条就退出,否则全部找出
	                }
	            }
	        }
	        return result;
	    }
	}