package com.diewland.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Oryor {

    public static final Map<String, String> patts;
    static
    {
        patts = new HashMap<String, String>();
        patts.put("ContentPlaceHolder1_lbl_fdpdtno", "Product No");
        patts.put("ContentPlaceHolder1_lbl_typecd", "Type #1");
        patts.put("ContentPlaceHolder1_lbl_type", "Type #2");
        patts.put("ContentPlaceHolder1_lbl_thai", "Name (TH)");
        patts.put("ContentPlaceHolder1_lbl_eng", "Name (EN)");
        patts.put("ContentPlaceHolder1_lbl_lcnstatus0", "License Status #1");
        patts.put("ContentPlaceHolder1_lbl_name", "License Name");
        patts.put("ContentPlaceHolder1_lbl_lctname", "Location Name");
        patts.put("ContentPlaceHolder1_lbl_lctaddr", "Location Address");
        patts.put("ContentPlaceHolder1_lbl_lcnstatus", "License Status #2");
    }

    public static String parse(String html){
        String result = "";
        for(Map.Entry<String, String> entry : patts.entrySet()){
            String id = entry.getKey();
            String label = entry.getValue();

            Pattern p = Pattern.compile("[\\s\\S]+<span id=\""+ id +"\">(.+?)</span>[\\s\\S]+");
            Matcher m = p.matcher(html);
            if(m.matches()){
                result += String.format("<%s>\n%s\n\n", label, m.group(1));
            }
        }
        return result;
    }

     public static HashMap<String, String> parseHash(String html){
        HashMap<String, String> result = new HashMap<>();
        for(Map.Entry<String, String> entry : patts.entrySet()){
            String id = entry.getKey();
            String label = entry.getValue();

            Pattern p = Pattern.compile("[\\s\\S]+<span id=\""+ id +"\">(.+?)</span>[\\s\\S]+");
            Matcher m = p.matcher(html);
            if(m.matches()){
                result.put(label, m.group(1));
            }
        }
        return result;
    }

    public static void main(String[] args) {

        String s = "       <div class=\"col-md-2\">\n" +
                "                \n" +
                "                 <span id=\"ContentPlaceHolder1_Label1\">เลขสารบบ</span>\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "        <span id=\"ContentPlaceHolder1_lbl_fdpdtno\">14-2-00459-2-0001</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "              <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "            ประเภท\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "                     <span id=\"ContentPlaceHolder1_lbl_typecd\">ผลิต</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                             อาหาร                              \n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "       <span id=\"ContentPlaceHolder1_lbl_type\">น้ำบริโภคในภาชนะบรรจุที่ปิดสนิท</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                         <span id=\"ContentPlaceHolder1_Label3\">ชื่อผลิตภัณฑ์(TH)</span>\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "         <span id=\"ContentPlaceHolder1_lbl_thai\">น้ำดื่มสิงห์</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "       <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "             <span id=\"ContentPlaceHolder1_Label4\">ชื่อผลิตภัณฑ์(EN)</span>\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "     <span id=\"ContentPlaceHolder1_lbl_eng\"></span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                            สถานะผลิตภัณฑ์\n" +
                "                \n" +
                "\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "           <span id=\"ContentPlaceHolder1_lbl_lcnstatus0\">คงอยู่</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "       <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                \n" +
                "                    <p >ชื่อผู้รับอนุญาต</p>\n" +
                "                  \n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "         <span id=\"ContentPlaceHolder1_lbl_name\">บริษัทวังน้อย เบเวอเรช  จำกัด</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "       <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                \n" +
                "                        ชื่อสถานที่\n" +
                "                    \n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "       <span id=\"ContentPlaceHolder1_lbl_lctname\">บริษัท วังน้อย เบเวอเรช จำกัด (สาขา 1)</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "              <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                            <p >   \n" +
                "                      ที่ตั้ง</p>\n" +
                "                     \n" +
                "\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "      <span id=\"ContentPlaceHolder1_lbl_lctaddr\">บ้านเลขที่9/99      หมู่1   ตำบลลำไทร อำเภอวังน้อย จังหวัดพระนครศรีอยุธยา 13170</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "              <div class=\"row\" style=\"padding:10px;\">\n" +
                "            <div class=\"col-md-2\">\n" +
                "                         สถานะใบอนุญาตสถานที่\n" +
                "                     \n" +
                "\n" +
                "            </div>\n" +
                "            <div class=\"col-md-10\">\n" +
                "      <span id=\"ContentPlaceHolder1_lbl_lcnstatus\">คงอยู่</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n";

        // System.out.println(Oryor.parse(s));
        System.out.println(Oryor.parseHash(s));

    }
}
