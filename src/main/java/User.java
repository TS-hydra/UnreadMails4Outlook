import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

public class User {
	static public Mp3Player player;
	static {
		System.out.println("static init mp3player");
		player = new Mp3Player("contra.mp3");
	}
	private String email;
	private String password;
	private int unReadNum = 0;

	private ExchangeService es;
	private ExchangeCredentials ec;
	private Folder inbox;
	ItemView view;
	SearchFilter sf;
	FindItemsResults<Item> findResults;

	User(String email, String password) throws URISyntaxException {
		this.email = email;
		this.password = password;
		//
		// connect2Email(); shuoud called by user.
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void connect2Email(/* String email,String password */) throws URISyntaxException {

		es = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ec = new WebCredentials(email, password);
		es.setCredentials(ec);

		// Setting the URL of the Service
		es.setUrl(new URI("https://mail.21vianet.com/EWS/Exchange.asmx"));

		// ��������
		try {
			inbox = Folder.bind(es, WellKnownFolderName.Inbox);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("pls check your email and password!");
		}

		// ׼������δ���ʼ���ȡ
		// ����ѯ��������
		view = new ItemView(10);

		// ����δ���ʼ�
		sf = new SearchFilter.IsEqualTo(EmailMessageSchema.IsRead, false);

	}

	public void outputUnReadNum() {
		/// unread email numbers
		//boolean hasUnreadMails=false;

		// Binding to an Existing Folder���ռ���
		try {
			// ��ѯ���ؽ��
			findResults = es.findItems(inbox.getId(), sf, view);
			// get all unread numbers.
			unReadNum = findResults.getTotalCount();
			// System.out.println("" + unReadNum);
			/*
			 * for (Item item : findResults.getItems()) { EmailMessage message =
			 * EmailMessage.bind(es, item.getId()); // ���δ���ʼ���Ϣ
			 * System.out.print(message.getSender());
			 * System.out.println("  Subject:" + message.getSubject()); //
			 * unReadNum++;
			 * 
			 * // MessageBody mb=message.getBody(); //
			 * System.out.println("body-->" + mb.toString());
			 * 
			 * // ���ʼ���Ϊ�Ѷ� // message.setIsRead(true); // ���µ��������� //
			 * message.update(ConflictResolutionMode.AlwaysOverwrite); }
			 */
			System.out.printf("%30s : %d��δ���ʼ�����\n", email, unReadNum);
			// warning
			if (unReadNum != 0) {
				player.play();
				//TODO �����̵߳��������� ���̣߳��������һֱ�����������޷���ɣ����Կ����߳�ȥ������������ʱ��ȥ��
				//=true;
				//JOptionPane.showMessageDialog(null, "You hava " + unReadNum + " unread email!", "Unread email",
				//		JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "pls check your email and password!"+e.toString(), "check again",
							JOptionPane.WARNING_MESSAGE);
			System.out.println("pls check your email and password!");
		}
		//return hasUnreadMails;

	}

	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + "]";
	}

}
