package GUIChat;

import java.io.*;

import omoke.Map;




public class ChatObject implements Serializable  {
   
   private int code;
   private String message;
   private String chatname;
   private int play;
   private String ip;
   Map map;
   
   public static final int CONNECT_SERVER = 1000;
   public static final int DISCONNECT_SERVER = -1000;
   public static final int CONFIRM_NAME=1001;
   public static final int WHISPHER_MESSAGE = 1002;
   
   public static final int NORMAL_MESSAGE = 10;
   public static final int MOUSE_EVENT_XY =-10;
   
   
   public static final short STONE_BLACK = 1;
   public static final short STONE_WHITE = -1;
   
   public ChatObject(int code,String chatname){
	   this.code=code;
	   this.chatname=chatname;
   }
  
   public ChatObject(int code,String message,String chatname){
	   this.code=code;
	   this.message=message;
	   this.chatname=chatname;
   }
   
   public ChatObject(int code,String chatname ,int play,Map map){
	   this.code=code;
	   this.chatname=chatname;
	   this.play=play;
	   this.map=map;
   }
   
   public Map getMap(){
	   return map;
   }
   public void setMap(Map map){
	   this.map=map;
   }

   public int getCode() {
	   return code;
   }
   public void setCode(int code) {
	   this.code = code;
   }
   
   public String getMessage() {
	   return message;
   }
   public void setMessage(String message) {
	   this.message = message;
   }
   
   public String getChatname() {
	   return chatname;
   }
   public void setChatname(String chatname) {
	   this.chatname = chatname;
   }
   
   public int getPlay() {
	   return play;
   }
   public void setPlay(int play) {
	   this.play = play;
   }
   
   public String getIp() {
	   return ip;
   }
   public void setIp(String ip) {
	   this.ip = ip;
   }

   
}