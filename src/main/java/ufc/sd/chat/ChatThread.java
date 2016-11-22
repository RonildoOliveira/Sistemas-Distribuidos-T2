package ufc.sd.chat;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ufc.sd.chat.Chat.Reply;

public class ChatThread extends Thread {
	private Socket cliente;
	private String nomeCliente;
	private Reply.Builder resposta = Reply.newBuilder();
	private Reply.Builder decisao = Reply.newBuilder();
	private Reply Recebida = null;
	private Scanner ler = null;
	//private BufferedReader leitor;
	//private PrintWriter escritor;
	
	private static final Map<String, ChatThread> clientes = new HashMap<String, ChatThread>();

	public ChatThread(Socket cliente){
		this.cliente = cliente;
		start();
	}
	
	//Método run: Conversa entre os clientes(Envio e Recepção)
	//getinputstream = Conexão que envia dados, em tempo real, através de pacotes
	//BufferedReader = Ler o inputstream e transforma em Stream
	
	public void run(){
		
		try{
			String receber = null; 
			
			//leitor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
			//escritor =  new PrintWriter(cliente.getOutputStream(), true);
		
			//escritor.println();
			String inicio  = "escreva seu nome";
			
			resposta.setMessagem(inicio);
			resposta.build().writeDelimitedTo(cliente.getOutputStream());
			
			Recebida = Reply.parseDelimitedFrom(cliente.getInputStream());
			receber = Recebida.getMessagem();
			
			//this.nomeCliente = receber.substring(4, receber.length());;
			this.nomeCliente = receber;
			clientes.put(this.nomeCliente, this);
			//escritor.println("olá " + this.nomeCliente);
			resposta.setMessagem("Ola:"+this.nomeCliente);
			resposta.build().writeDelimitedTo(cliente.getOutputStream());
			
			while(true){
				
				//receber = leitor.readLine();
				
				Recebida = Reply.parseDelimitedFrom(cliente.getInputStream());
				receber  = Recebida.getMessagem();
			
				//Sair do programa
				
				if(receber.equalsIgnoreCase("::Sair")){
					this.cliente.close();
				//FUNCIONA DE BOAS
					
				}else if(receber.startsWith("::msg")){
					
		
					String nomeDestinario  = receber.substring(5, receber.length()); // Tira os : e o msg
					
					//escritor.println(this.nomeCliente + ": " + receber);
					
					resposta.setMessagem(this.nomeCliente +" voce disse : " + receber);
					receber = "Mensagem enviada com sucesso! ";
					resposta.build().writeDelimitedTo(cliente.getOutputStream());
					
					System.out.println("Enviando para " + nomeDestinario);
					ChatThread destinario = clientes.get(nomeDestinario);
					
					if(destinario == null){
						resposta.setMessagem("O cliente informado não existe");
						resposta.build().writeDelimitedTo(cliente.getOutputStream());
						//escritor.println("O cliente informado não existe");
						
						//Funcionando 
					
					}else{
						//escritor.println("Digite uma A para " + destinario.getNomeCliente());
						resposta.setMessagem("Digite uma mensagem para " + destinario.getNomeCliente());
						resposta.build().writeDelimitedTo(cliente.getOutputStream());
						
						
						Recebida = Reply.parseDelimitedFrom(cliente.getInputStream());
						String praEnviar  = "Usuario"+this.nomeCliente + resposta.getMessagem();
						
		
						//resposta.setA(praEnviar);
						//resposta.build().writeDelimitedTo(cliente.getOutputStream());
						String nomao = this.nomeCliente + " disse: ";
						decisao.setMessagem(nomao + Recebida.getMessagem());
						decisao.build().writeDelimitedTo(destinario.getEscritor().getOutputStream());
						
						//destinario.getEscritor().resposta.build().writeDelimitedTo();
					    //destinario.getEscritor().("Usuário "+this.nomeCliente + " :" + praEnviar);
						 //escritor.println("Digite uma mensagem para " + destinario.getNomeCliente());
						 //destinario.getEscritor().println("Usuário "+this.nomeCliente + " :" + leitor.readLine());
						
					}
				}
				

				
		
				resposta.setMessagem("Servidor disse: "+ receber);
				
				resposta.build().writeDelimitedTo(cliente.getOutputStream());
				
			}
			
			} catch (IOException e){
				
				System.err.println("O cliente fechou conexão");
				e.printStackTrace();
			}
			
		}
		
	
	public Socket getEscritor(){
			return cliente;
	}
	
	public String getNomeCliente(){
		
		return nomeCliente;
		
	} 
		
//		public BufferedReader getLeitor(){
//		
//		return leitor;
//		}
	
}
