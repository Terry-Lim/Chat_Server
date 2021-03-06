import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
// 깃허브 두번쨰 수정
public class Server {
	
	private static final int PORT = 4900;
	private static DataInputStream dis;
	private static DataOutputStream dos;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;
	
	
	public static void main(String[] args) {
		RoomManager rm = new RoomManager();
		try (ServerSocket server = new ServerSocket(PORT);) {
			while (true) {	
				System.out.println("접속 시도");
				Socket client = server.accept();
				dis = new DataInputStream(
						client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				PrintWriter pw = new PrintWriter(new BufferedOutputStream(client.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				Thread t1 = new Thread(new Runnable() {
					
					@Override
					public void run() {
						int fromClient = 0;
						try {
							ChatProtocol cp = new ChatProtocol(dos, dis, pw, br, rm);
							while (true) {
								fromClient = dis.readInt();
								cp.process(fromClient);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				t1.start();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
}

