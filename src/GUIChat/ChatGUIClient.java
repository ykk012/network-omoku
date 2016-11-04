package GUIChat;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import omoke.*;

public class ChatGUIClient extends Frame implements ActionListener  {

   public Socket s;
   
   public OutputStream os;
   public ObjectOutputStream oos;
   
   private Button btExit;
   private Button btSend;
   private Button btConnect;
   private TextArea taChatList;
   private TextField tfServerIP;
   
   private TextField tfChatName;
   private TextField tfInputMessage;
   private CardLayout card;
   private Panel pnConnect;
   private Panel pnConnectSub;
   ServerClone server_clone=null;
   
   MapSize mapsize;
   DrawBorad board;
   
   MouseEventHandler mouseEvent=null;
   
   String myName;

   
   
   
   public ChatGUIClient(){
	   setTitle("GUI 채팅 클라이언트");
	   addWindowListener(new WindowAdapter() {
		   public void windowClosing(WindowEvent e) {
			   dispose();
		   }
  });
  card = new CardLayout();
  setLayout(card);
  
  //#################GUI#########################//
  
  pnConnect = new Panel();
  pnConnect.setBackground(Color.LIGHT_GRAY);
  pnConnect.setLayout(new BorderLayout());
  
  btConnect = new Button("서버 접속");
  btConnect.addActionListener(this);/// サーバーのアクセスのListener
  
  tfServerIP = new TextField("127.0.0.1" , 15);
  tfChatName = new TextField("홍길동", 15);
  
  pnConnectSub = new Panel();
  pnConnectSub.add(new Label("접속 아이피(IP) : "));
  pnConnectSub.add(tfServerIP);
  pnConnectSub.add(new Label(" 대    화    명   :"));
  pnConnectSub.add(tfChatName);
  
  
  Label IbIChat = new Label("채팅 접속 화면", Label.CENTER);
  pnConnect.add(IbIChat, BorderLayout.NORTH);
  pnConnect.add(pnConnectSub, BorderLayout.CENTER);
  pnConnect.add(btConnect, BorderLayout.SOUTH);
  
  //////////////////////////////////////////////
  
  
  btExit = new Button("종료");
  btExit.addActionListener(this);  ///終了
  
  btSend = new Button("전송");
  btSend.addActionListener(this);  // チャット伝送のListener
  
  tfInputMessage = new TextField("", 25);
  tfInputMessage.addActionListener(this);// チャット伝送のListener
  
  Panel chat_sub = new Panel();
  chat_sub.add(tfInputMessage);
  chat_sub.add(btSend);
  chat_sub.add(btExit);
  
  
  //////////////////////////////
  
  Panel omokeAndChat = new Panel();
  omokeAndChat.setLayout(new BorderLayout());
  
  ////////////////////////////////////////
  taChatList = new TextArea();//チャット画面
  taChatList.setEditable(false);
  ////////////////////////////////////////
  
    
    mapsize= new MapSize();
    
    board =new DrawBorad(mapsize);

 
 ///////////////////////////////////////////////////////
  
  Label IbIChatTitle = new Label("채팅 프로그램", Label.CENTER);
  omokeAndChat.add(IbIChatTitle, BorderLayout.NORTH);
  omokeAndChat.add(taChatList, BorderLayout.EAST);
  omokeAndChat.add(chat_sub, BorderLayout.SOUTH);
  omokeAndChat.add(board, BorderLayout.CENTER);
 
  
  //-----------------------------///
  
  add(pnConnect, "접속창");
  add(omokeAndChat,"채팅창");
  
  
  card.show(this,  "접속창");
      setBounds(250, 250, 1100, 1300);
      setVisible(true);
      
      
   }
 //#################GUI#########################//
   //--------------サーバーにアクセスするfunction-------------//
   public void connect(String tfServerIP) {
      try {
    	  if(server_clone==null){
        	 s = new Socket(tfServerIP, 7777);
         
        	 os = s.getOutputStream();
        	 oos = new ObjectOutputStream(os);
        	 server_clone=new ServerClone(s);
         
        	 new Thread(server_clone).start();
    	  }
      }
      catch(Exception ex) {
         ex.printStackTrace();
      }
   }
   //-------------------------------------//
   
   //---------------サーバーにチャットを送るfunction-------//
   public void sendMessage(String message, String chatName) {
	      try {
	    	 
	         if(message != null) {
	            oos.writeObject(new ChatObject(ChatObject.NORMAL_MESSAGE, message, chatName));
	            oos.flush();
	         }
	         
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
   }
 //---------------user名を確認するため、サーバーに送るfunction-------//
   public void confirm_chatName(String chatName) {
	      try {
	    	  oos.writeObject(new ChatObject(ChatObject.CONFIRM_NAME,chatName));
	    	  oos.flush();
	         
	      }
	      catch(Exception e) {
	         e.printStackTrace();
	      }
   }

   
   //-------------------最初、五目ゲーム画面に入ること-----------------------//
  
   public void get_into_room(ChatObject chatObject){
	   
	   board.importMap(chatObject.getMap());
	   if(chatObject.getPlay()==1&&myName.equals(chatObject.getChatname())){
		  
		   mouseEvent=new MouseEventHandler(mapsize,board);
		   mouseEvent.setObjectOutputStream(oos);
		   mouseEvent.setChatObject(chatObject);
		   board.addMouseListener(mouseEvent);
	   }else  if(chatObject.getPlay()==2&&myName.equals(chatObject.getChatname())){
		   
		   mouseEvent=new MouseEventHandler(mapsize,board);
		   mouseEvent.setObjectOutputStream(oos);
	   }
	   board.repaint();
	   card.show(this, "채팅창");
   taChatList.append("["+chatObject.getChatname() +"] 님이  입장 하였습니다.\n");
   }
   //------------------------------------------------------------//
  
   //-----------------sub class-----------------------//
   //---------------サーバーからデータを受けて処理-------///
   public class ServerClone implements Runnable {
      private InputStream is;
      private ObjectInputStream ois;
    
      private Socket s2;
      private ChatObject chatObject;
      
      public ServerClone(Socket s) throws Exception {
         this.s2 = s;
         is = s.getInputStream();
         ois = new ObjectInputStream(is);
       
      }
      
      public void run() {
         try {
        	 
            while(true) {
            	chatObject = (ChatObject)ois.readObject();
            		  
            	switch(chatObject.getCode()){
            	case ChatObject.CONNECT_SERVER: //サーバーにアクセス
            		get_into_room(chatObject);
            		break;
            	case ChatObject.MOUSE_EVENT_XY : //碁盤のデータ
            		board.importMap(chatObject.getMap());
        			board.repaint();
            		if(chatObject.getPlay()<=2 && chatObject.getChatname().equals(myName)){ //playerなら
            			mouseEvent.setChatObject(chatObject);
            			board.addMouseListener(mouseEvent);
            		
            		}
            		break;
            	case ChatObject.NORMAL_MESSAGE ://チャット
            		taChatList.append("["+chatObject.getChatname() +"] "+chatObject.getMessage() + "\n");
            		break;
            	 
            	}
            			   
            }
           
         } catch (Exception ex) {
            ex.printStackTrace();
         } finally {
            try {
               ois.close();
               s.close();
            } catch (Exception e2) {}
         }
      }
   }
   //-------------------내부 class끝----------------------------------//
   
 //------------------입력한데이터AND서버로 보낼데이터 $데이터처리컨트롤$ ------------//
   public void actionPerformed(ActionEvent e) {
      try {
         Object obj = e.getSource();
         String ip=tfServerIP.getText();
         if (obj == btConnect) {  // サーバーアクセスボタン
	
        	 connect(ip);// サーバーアクセス
  
        	 myName=tfChatName.getText();//自分の名前を保管 

        	 confirm_chatName(myName);//user名を確認するため、サーバーに送る
        	
         }else if ( obj == btExit ) { //終了ボタン
        	 System.exit(0);
         } else if ( obj == btSend || obj == tfInputMessage ) { //チャット
	 
        	 String msg = tfInputMessage.getText();
        	 if( msg.length() != 0 ) {//보낸 데이터가 있을때
        		 sendMessage(msg, tfChatName.getText());
        	 }
        	 tfInputMessage.setText("");
        	 tfInputMessage.requestFocus();
         }
      } catch ( Exception ex ) {
    	  ex.printStackTrace();
    	  tfInputMessage.setText("");
    	  taChatList.append("채팅 전송이 되지 않았습니다.\n");
      }
   }
   //-------------------------------------------------------//
   
   public static void main(String args[]) throws IOException {
      ChatGUIClient cc = new ChatGUIClient();
   }
   
   
   
   
}