package com.platform.common.utils.keyWordsFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.platform.common.utils.StringUtils;

public class BanWordsUtil {
	    public static final int WORDS_MAX_LENGTH = 12;
	    public static final String BAN_WORDS_LIB_FILE_NAME = "banWords.txt";
	    public static Map<String,String>[] banWordsList = null;
	    public static Map<String, Integer> wordIndex = new HashMap<String, Integer>();
	  
	    /*
	     * 初始化敏感词库
	     */
	    @SuppressWarnings("unchecked")
		public static void initBanWordsList() throws IOException {
	        if (banWordsList == null) {
	            banWordsList = new Map[WORDS_MAX_LENGTH];
	            for (int i = 0; i < banWordsList.length; i++) {
	                banWordsList[i] = new HashMap<String, String>();
	            }
	        }
	        
	        String path = BanWordsUtil.class.getResource(BAN_WORDS_LIB_FILE_NAME).getPath();
	  
	        List<String> words = FileUtils.readLines(FileUtils.getFile(path));
	        
	        for (String w : words) {
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
	    }
	  
	    /**
	    * 检索敏感词
	    * @param content
	    * @return
	    */
	    public static List<String> searchBanWords(String content) {
	        if (banWordsList == null) {
	            try {
	                initBanWordsList();
	            } catch (IOException e) {
	                throw new RuntimeException(e);
	            }
	        }
	        List<String> result = new ArrayList<String>();
	        int i = 0;
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
	                   // index=0;//查找所有敏感词是去掉此行注释，同事注释下边第一条
	                    return result;//找到一个条就退出,否则全部找出
	                }
	            }
	        }
	        return result;
	    }
	}