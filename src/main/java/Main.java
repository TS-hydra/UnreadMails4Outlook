import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.List;

public class Main{
	static MyJFrame f;
	static Thread queryUnReadMailNum;
	static boolean QUIT=false;
	static List<User> users;
	static RefreshUserListener refreshUserListener=new RefreshUserListener() {
		//imp adduserlistener in order to read the latest data and refresh UI 
		@Override
		public void refresh() {
			users = Tool.readProperties();
			f.setList(users);

		}

		@Override
		public void begin() {
			QUIT=false;
		}

		@Override
		public void stop() {
			QUIT=true;
		}
	};
	
	public static void main(String[] args) {

		//TFFrame frame=new TFFrame(); 
		f=new MyJFrame();
		f.addWindowListener(new MyWin());
		f.setRefreshUserListener(refreshUserListener);
		users = Tool.readProperties();
		for(User u:users){
			//we should connect to email server first
			try {
				u.connect2Email();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		f.setList(users);
		//Tool.writeProperties("jhd147350", "aaaaaaaaa");
		
		queryUnReadMailNum=new Thread(){
			@Override
			public void run() {
				super.run();
				boolean PLAY=false;
				while(!QUIT){
					
					System.out.println("last refresh:"+LocalTime.now());
					f.lastRefresh.setText("last refresh:"+LocalTime.now());
					for (User temp : users)
					{
						temp.outputUnReadNum();
						//System.out.println(PLAY);
					}
					if(PLAY){
					//	User.player.play();
					}
					try {
						//Thread.currentThread();
						//sleep 30s
						Thread.sleep(30l*1000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		queryUnReadMailNum.start();
		
	}

	//��Ϊ�ӿ�WindowLinstener�е����з����������� WindowAdapterʵ����,.  
	//���Ҹ��������е����з���,��ô����ֻ�ܼ̳� WindowAdapter �������ǵķ�������  
	static class MyWin extends WindowAdapter{  
	      
	    @Override  
	    public void windowClosing(WindowEvent e) {  
	        //System.out.println("Window closing"+e.toString());  
	        System.out.println("�ҹ���");  
	        System.exit(0);  
	    }  
	    @Override  
	    public void windowActivated(WindowEvent e) {  
	        //ÿ�λ�ý��� �ͻᴥ��  
	        System.out.println("�һ���");    
	        //super.windowActivated(e);  
	    }  
	    @Override  
	    public void windowOpened(WindowEvent e) {  
	        System.out.println("�ҿ���");  
	        //super.windowOpened(e);  
	    }  
	      
	}
	
}
