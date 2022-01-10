package com.company;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {

    // 1-300위 파싱
    public void novelpia(){
        try {

            // 노벨피아 URL
            String url = "https://novelpia.com/top100/all/today/view/all/plus/";

            //JSOUP으로 연결
            Connection conn = Jsoup.connect(url);

            // 해당 웹페이지 파싱
            Document html = conn.get();

            // HTML 코드를 String으로 바꾼 후 웹소설 PK에 해당하는 경로만 가져오기
            String saveStr = html.toString();
            Pattern pattern = Pattern.compile("<*/novel/[\\\"']?([^>\\\"']+)[\\\"']?[^>]*>");
            Matcher matcher = pattern.matcher(saveStr);

            // PK와 순위를 담을 맵 선언
            Map<Integer,String> setUrl2 = new HashMap<Integer, String>();

            // key선언
            Integer key = 1;

            // 중복된 걸 저장할 비어있는 변수 선언
            String backWord = "";

            //while문을 돌리며 Pattern에 해당하는 모든 경로 탐색
            while(matcher.find()){

                // 만약 url이 중복이라면 맵에 저장하지 않는다
                if(matcher.group().equals(backWord)){
                    continue;
                }else if(key < 11){

                    // 10위까지만 가져오고, 소설 PK에 해당하는 숫자만 남기고 전부 제거한다
                    setUrl2.put(key,matcher.group().replaceAll("[^0-9]",""));

                    // 중복 제거를 위해 해당 url을 저장한다.
                    backWord = matcher.group();
                    key++;
                }
            }

            // 향상된 for문으로 PK와 순위를 출력한다.
            Set<Integer> keySet = setUrl2.keySet();
            for(Integer key1 : keySet){
                System.out.println(key1 + "위 - " + setUrl2.get(key1));

                // 제목, 조회수, 회차, 좋아요를 출력한다.
                detailParse(setUrl2.get(key1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 소설의 세부사항 출력
    public void detailParse(String no){

        try {
            // 세부사항 조회 URL
            String url = "https://novelpia.com/novel/"+ no;

            Connection conn = Jsoup.connect(url);
            Document html = conn.get();

            // 좋아요, 회차, 조회수를 가져오기
            Elements body = html.body().select("span[style='font-size:14px;font-weight:600;color:#333;']");

            // 제목 가가져오기
            Elements nameBody = html.body().select("span[style='font-weight:700;font-size:29px;margin-bottom:10px;']");

            // 세부내역 출력
            String name = nameBody.toString();
            System.out.println("==============================");
            System.out.println("제목:"+name.replaceAll("<[^>]*>", " "));
            for(Element info : body){
                String [] newStr = info.text().split(" ");
                for(int i = 0; i < newStr.length; i++){

                   switch (i){
                       case 0 :
                           System.out.println("조회수 : "+newStr[i]);
                           break;

                       case 1:
                           System.out.println(newStr[i]);
                           break;

                       case 2:
                           System.out.println("좋아요 : " + newStr[i]);
                   }
                }
            }
            System.out.println("==============================");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
