package omoke;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import GUIChat.ChatObject;
public class MouseEventHandler extends MouseAdapter {
	Map map;
	MapSize mapsize;
	public int x;
	public int y;
	public int isPlayer;
	ObjectOutputStream oos;
	DrawBorad board;
	ChatObject chatObject=null;
	public MouseEventHandler(MapSize mapsize,DrawBorad board)  {
		// TODO Auto-generated constructor stub
		
		this.mapsize=mapsize;
		this.board=board;
		System.out.println("MouseEventHandler");
	}
	public void setMapdata(Map map){
		this.map=map;
	}
	public void setPlayer(int play){
		isPlayer=play;
	}
	public int getPlayer(){
		return isPlayer;
	}
	public void setObjectOutputStream(ObjectOutputStream oos){
		this.oos=oos;
	}
	public void setChatObject(ChatObject chatObject){
		this.chatObject=chatObject;
	}
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//super.mousePressed(arg0);//
			
		
		
			 x=(int)Math.round(arg0.getX()/(double)mapsize.getCell())-1;
		     y=(int)Math.round(arg0.getY()/(double)mapsize.getCell())-1; 
		    
		    if(x<0||x>19||y<0||y>19)
		    	return;
		    
		    try {
		    	if(chatObject!=null){
		    		map=chatObject.getMap();
		    		if(map.getXY(y, x)==map.getBlack()||map.getXY(y, x)==map.getWhite())
				    	return;
			    	map.setMap(y, x);
			    	board.importMap(map);
			    	board.repaint();
			
			    	chatObject.setCode(ChatObject.MOUSE_EVENT_XY);
			    	chatObject.setMap(map);
			    
			    	oos.writeObject(chatObject);
			    	oos.flush();
		    	}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    board.removeMouseListener(this);
		    
		
	}
	    /*if(map.winCheck(x, y))
	    	if(map.getCheck()==true)
	    		main.showPopUp("¹éµ¹ ½Â¸®!");
	    	else
	    		main.showPopUp("Èæµ¹ ½Â¸®!");
		*/
	
}
