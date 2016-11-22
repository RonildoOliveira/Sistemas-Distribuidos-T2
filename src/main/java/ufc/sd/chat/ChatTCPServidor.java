package ufc.sd.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatTCPServidor{
	
	private List<Socket> clientesOnline;
	private List<PrintStream> clientes;
	private int porta;
	
	//construtor
	public ChatTCPServidor(int porta){
		this.porta = porta;
		this.clientes = new ArrayList<PrintStream>();
		this.clientesOnline = new ArrayList<Socket>();
	}
	
	public void executa(){
		try {
			//Abre socket servidor
			ServerSocket serv = new ServerSocket(this.porta);
			System.out.println("Servidor rodando na porta "+ serv.getLocalPort());
			
			while(true){
				//Para conectar mais de um cliente
				//precisamos criar threads.
				Socket client = serv.accept();
				//System.out.println("Nova conexao com cliente "+ 
				//			client.getInetAddress().getHostName());
				this.clientesOnline.add(client);
				for(Socket c: clientesOnline){
					if(c != client){
						System.out.println("Cliente "+ client.getInetAddress().getHostName());
					}
				}
				// adiciona saida do cliente à lista
			    PrintStream ps = new PrintStream(client.getOutputStream());
			    this.clientes.add(ps);
				
				//Cria uma thread do servidor para tratar a conexão
				Tratamento tratamento = new Tratamento(client.getInputStream(), this);
				
				Thread t = new Thread(tratamento);
				t.start();//inicia essa thread
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void broadcastMsg(String msg){
		//envia msg pra todos conectados
		for(PrintStream cliente: this.clientes){
			cliente.println(msg);
		}
	}
	
	public class Tratamento implements Runnable { 
		   private InputStream cliente;
		   private ChatTCPServidor servidor;
		 
		   public Tratamento(InputStream cliente, ChatTCPServidor servidor) {
		     this.cliente = cliente;
		     this.servidor = servidor;
		   }
		 
		   public void run() {
		     // quando chegar uma msg, distribui pra todos
		     Scanner s = new Scanner(this.cliente);
		     while (s.hasNextLine()) {
		       servidor.broadcastMsg(s.nextLine());
		     }
		     s.close();
		   }
	}
	
	public static void main(String[] args) {
		ChatTCPServidor s = new ChatTCPServidor(6666);
		s.executa();
	}
}
