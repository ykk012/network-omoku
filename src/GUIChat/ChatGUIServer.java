package GUIChat;

import java.net.*;
import java.sql.SQLException;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import DB.*;
import omoke.Map;
import omoke.MapSize;

public class ChatGUIServer extends Frame implements ActionListener {

   private ServerSocket ss;
   private ArrayList<ClientClone> clientClones;
   
   private Button btExit; // 서버 종류 버튼;
   private TextArea taChat;
   omokeDB db;
   MapSize mapsize;
   Map map;
   
   public ChatGUIServer() throws Exception {
      ss = new ServerSocket(7777);
      db = new omokeDB();
      mapsize=new MapSize();
      map = new Map(mapsize);
      
      this.setTitle("GUI 채팅 서버");
      
   addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
         dispose();
      }
   });
   
     clientClones = new ArrayList<ClientClone>();
     btExit = new Button("서버 종료");
     btExit.addActionListener(this);
     taChat = new TextArea();
     taChat.setEditable(false);
     this.add(taChat, BorderLayout.CENTER);
     this.add(btExit, BorderLayout.SOUTH);
     
     this.setBounds(250, 250, 200, 200);
     this.setVisible(true);
     
     waitClient();
   }
   
   public void waitClient() throws Exception {
      Socket s;
      ClientClone clientClone;
      
      while(true) {
         s = ss.accept(); // client를 기다림.
         
         taChat.append(s.getInetAddress().getHostAddress() + "에서 접속함\n");
         
         clientClone = new ClientClone(s);
         new Thread(clientClone).start();
         
         clientClones.add(clientClone);
      }
   }
   //-----------------各ユーザーに処理したデータを送るfunction---------------------//
   public void sendObjectToAll(ChatObject chatObject) throws Exception {
	 
	   switch(chatObject.getCode()){
	   case ChatObject.CONFIRM_NAME://アクセスする時の信号
		   if(db.selectIsName(chatObject.getChatname())==null){//user名を確認してuser名がなかったらDBに登録
			   if(db.selectCheckPlayer()<2){//prayerが2人がなかったらゲームできるprayerになる
				   if(db.selectNameByPlayer(1)==null){//play番号1がなかったら黒石
					   db.insert( chatObject.getChatname(),1);
					   chatObject.setPlay(1);
				   
				   }else if(db.selectNameByPlayer(2)==null){//play番号2がなかったら白石
					   db.insert( chatObject.getChatname(),2);
					   chatObject.setPlay(2);
				   }
			   }else{//prayerが全員いると観点者になる
				   db.insert(chatObject.getChatname(),100);
			   }
			   chatObject.setMap(map);
			   chatObject.setCode(ChatObject.CONNECT_SERVER);
		   }
		   break;
	   case ChatObject.MOUSE_EVENT_XY ://mouseでクリックする時の信号 
		   if(db.selectPlayerByName(chatObject.getChatname())==1){
			   chatObject.setChatname(db.selectNameByPlayer(2));
			   chatObject.setPlay(2);
		   }else if(db.selectPlayerByName(chatObject.getChatname())==2){
			   chatObject.setChatname(db.selectNameByPlayer(1));
			   chatObject.setPlay(1);
		   }
		   map=chatObject.getMap();
		   break;
	   case ChatObject.NORMAL_MESSAGE ://チャットする時の信号 
		   break;
	   }
	   
 
      Iterator<ClientClone> it = clientClones.iterator();
      
     
      while(it.hasNext()) {
         ((ClientClone)it.next()).sendObject(chatObject);
      }
   }
   //------------------------------------------------------------------------------------------------//
   public class ClientClone implements Runnable {
      
      private Socket s;
      private InputStream is;
      private ObjectInputStream ois;
      private OutputStream os;
      private ObjectOutputStream oos;
      private ChatObject chatObject;
      
      public ClientClone(Socket s) throws Exception {
         this.s = s;
         
         is = s.getInputStream();
         ois = new ObjectInputStream(is);
         
         os = s.getOutputStream();
         oos = new ObjectOutputStream(os);
      }
      public void sendObject(ChatObject chatObject) throws Exception {
         
    	  oos.writeObject(chatObject);
      }
      
      public void run() {
         
         try {
            
            while(true) {
               chatObject = (ChatObject)ois.readObject();
               sendObjectToAll(chatObject);//各ユーザーにデータを送る
            }
         } 
         catch (Exception ex) {
            ex.printStackTrace();
         }
         finally {
            try {
               ois.close();
               oos.close();
               s.close();
            }
            catch(Exception ex2) {}
         }
      }
   }
   public void actionPerformed(ActionEvent e) {
	   try {
		db.deleteCleanRoom();//終了ボタンをクリックする時、終了したgame roomのDBを削除
	} catch (ClassNotFoundException | SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	   System.exit(0);
      
   }
   
   public static void main(String args []) throws Exception {
      
      ChatGUIServer cs = new ChatGUIServer();
   }
}