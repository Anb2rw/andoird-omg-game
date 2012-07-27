/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anb2rw.omg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 *
 * @author АндрюшкА
 */
public class Settings {
    public static int TeamCount=2;
    public static int PlayerCount=2;
    public static int PlayerTeam=0;
    public static int Level=100,PlayerEXP=0;
    public static int selAA=-1;
    public static int[] Ability={
    	10, //Air Jump//0
        10, //Rocket Boots//1
        10, //Invisibility//2
        10, //Vampire//3
        10, //Heal//4
        10, //Radar//5
        10, //Dash//6
        1, //Shadow Clone//7
        1, //Time//8
        1, //Miner//9
        2, //Crit//10
        2, //Block//11
        1, //Return//12
        0, //Fury//10
        0, //Amaterasu//11
        0  //Far Away//12
    };
    public static final int[] ActivableAbility={2,4,6,7,8,9,12};
    public static int BulletSize=4,BulletType=0;
    public static int Quality=3;
    public static int Hat=0,Face=0;
    public static int Map=0;
    
    public static void load(Context context) {
        SharedPreferences pref = context.getSharedPreferences("OMGSettings", Context.MODE_PRIVATE);
        if(pref!=null) {
            TeamCount=pref.getInt("TeamCount", TeamCount);
            PlayerCount=pref.getInt("PlayerCount", PlayerCount);
            PlayerTeam=pref.getInt("PlayerTeam", PlayerTeam);
            Level=pref.getInt("Level", Level);
            PlayerEXP=pref.getInt("PlayerEXP", PlayerEXP);
            selAA=pref.getInt("selAA", selAA);
            String str=pref.getString("Ability", null);
            if(str!=null) {//1|10|
                int n,n1=0,i=0;
                while((n=str.indexOf("|",n1))!=-1) {
                    Ability[i++]=Integer.parseInt(str.substring(n1, n));
                    n1=n+1;
                }
            }
            BulletSize=pref.getInt("BulletSize", BulletSize);
            BulletType=pref.getInt("BulletType", BulletType);
            Quality=pref.getInt("Quality", Quality);
            Hat=pref.getInt("Hat", Hat);
            Face=pref.getInt("Face", Face);
            Map=pref.getInt("Map", Map);
        }
    }
    
    public static void save(Context context) {
        Editor e=context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        if(e!=null) {
            e.putInt("TeamCount", TeamCount);
            e.putInt("PlayerCount", PlayerCount);
            e.putInt("PlayerTeam", PlayerTeam);
            e.putInt("Level", Level);
            e.putInt("PlayerEXP", PlayerEXP);
            e.putInt("selAA", selAA);
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<Ability.length;i++) {
                sb.append(Ability[i]);
                sb.append("|");
            }
            e.putString("Ability", sb.toString());
            e.putInt("BulletSize", BulletSize);
            e.putInt("BulletType", BulletType);
            e.putInt("Quality", Quality);
            e.putInt("Hat", Hat);
            e.putInt("Face", Face);
            e.putInt("Map", Map);
            e.commit();
        }
    }
    
//    public static void load(FileIO files) {
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new InputStreamReader(
//            files.readFile(".omgsave")));
//            TeamCount = Integer.parseInt(in.readLine());//Boolean.parseBoolean(in.readLine());
//            PlayerCount=Integer.parseInt(in.readLine());
//            PlayerTeam=Integer.parseInt(in.readLine());
//            Level=Integer.parseInt(in.readLine());
//            PlayerEXP=Integer.parseInt(in.readLine());
//        } catch (IOException e) {
//            // :( It's ok we have defaults
//        } catch (NumberFormatException e) {
//            // :/ It's ok, defaults save our day
//        } finally {
//            try {
//                if (in != null)
//                    in.close();
//            } catch (IOException e) {
//            }
//        }
//    }
//    
//    public static void save(FileIO files) {
//        BufferedWriter out = null;
//        try {
//            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".omgsave")));
//            out.write(Integer.toString(TeamCount));
//            out.write(Integer.toString(PlayerCount));
//            out.write(Integer.toString(PlayerTeam));
//            out.write(Integer.toString(Level));
//            out.write(Integer.toString(PlayerEXP));
//        } catch (IOException e) {
//        } finally {
//            try {
//                if (out != null)
//                    out.close();
//            } catch (IOException e) {
//            }
//        }
//    }

    public static void Reset() {
        TeamCount=2;
        PlayerCount=2;
        PlayerTeam=0;
        Level=100;
        PlayerEXP=0;
        Ability=new int[Ability.length];
        selAA=-1;
        BulletSize=4;
        BulletType=0;
        Quality=3;
        Hat=0;
        Face=0;
        Map=0;
    }

}