package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    //替换符
    private static final String REPLACEMENT = "***";

    private TireNode rootNode = new TireNode();

    //构造器之后调用，初始化方法,容器实例化这个bean后，调用构造器后，就会调用这个方法
    @PostConstruct
    public void init() {
        try (
                //类加载器找到target下的classes目录
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败" + e.getMessage());
        }
    }


    private void addKeyword(String keyword) {
        TireNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            //若没有这个子节点
            if(tempNode.getSubNode(c) == null) {
                TireNode subNode = new TireNode();
                tempNode.addSubNode(c,subNode);
            }

            tempNode = tempNode.getSubNode(c);


        }
        tempNode.setKeywordEnd(true);
    }

    /**
     *
     * @param text 待过滤文本
     * @return 过滤后文本
     */
    public String filter(String text) {
        if(StringUtils.isBlank(text)) return null;

        TireNode tempNode = rootNode;

        int begin = 0;

        int position = 0;

        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            //跳过符号
            if(isSymbol(c)) {
                if(tempNode == rootNode ) {
                    sb.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            //未触发敏感词
            if(tempNode == null) {
                //未触发时，begin只能一个一个字符推进
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            //触发敏感词，整段替换
            } else if (tempNode.isKeywordEnd()) {
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            //树没到底
            } else {
                position++;
            }

        }
        //position 到底了，把当前begin到position之间记录一下
        sb.append(text.substring(begin));

        return sb.toString();



    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }




    //前缀树
    private class TireNode {

        //关键词结束标记
        private boolean isKeywordEnd = false;

        private Map<Character,TireNode> subNodes = new HashMap<>();


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        //添加子节点
        public void addSubNode(Character c, TireNode node) {
            subNodes.put(c,node);
        }

        //获取子节点
        public TireNode getSubNode(Character c) {
            return subNodes.get(c);
        }


    }
}
