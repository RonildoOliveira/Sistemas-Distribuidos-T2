package ufc.sd.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import ufc.sd.chat.Chat.Reply;

public class ChatTCPCliente {
	
	private static Socket cliente;
	private static Reply.Builder msg = null;
	private static Reply recebida = null;
	public static void main(String[] args) {
	
		try{
			msg = Reply.newBuilder();
			cliente = new Socket("localhost", 6000);
			
			//A criação da Thread e necessária para que o cliente possa enviar e receber mensagens ao mesmo tempo
			//O método run é sobrescrito
			
			
			//leitura de mensagens
			
			new Thread(){
				
				public void run(){
				
					try{
						
											
					while(true){
						
						recebida = Reply.parseDelimitedFrom(cliente.getInputStream());
						System.out.println("Mensagem do servidor - "+ recebida.getMessagem());
						
						
					}
					
					} catch(IOException e){
					
						System.out.println("Impossível ler a mensagem do servidor");
						e.printStackTrace();
						}
					}
					
			}.start();
			
		
			
			String mensagemTerminal = "";
			Scanner ler= new Scanner(System.in);
			
			while(true){
				
				//mensagemTerminal = leitorTerminal.readLine();
				mensagemTerminal = ler.nextLine();
				
				if(mensagemTerminal == null || mensagemTerminal.length() == 0)
					continue;
					
				
				//escritor.println(mensagemTerminal);
				msg.setMessagem(mensagemTerminal);
				msg.build().writeDelimitedTo(cliente.getOutputStream());   // Envia para Management
				
				if(mensagemTerminal.equalsIgnoreCase("::Sair"))
						System.exit(0);
					
				
				
			}
			
		} catch(UnknownHostException e){
			System.out.println("O endereçado passado é invalido");
			e.printStackTrace();
		
		} catch(IOException e){
		
			System.out.println("O servidor pode estar fora do ar");
			e.printStackTrace();
		}
		
	}

}
