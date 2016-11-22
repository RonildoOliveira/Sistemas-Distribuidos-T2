package ufc.sd.calc;

import java.net.ServerSocket;
import java.net.Socket;

import ufc.sd.calc.Calculadora.Reply;
import ufc.sd.calc.Calculadora.Request;

public class CalculadoraServidor {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket servidor ;// Cria o socket servidor;
		Socket s;// cria o socket cliente
		Request.Builder mensagem;  // Constroi a mensagem que será enviada
		Reply.Builder resposta;   // Constroi a mensagem de resposta
		try{
			
			mensagem = Request.newBuilder(); // Icializa  a mensagem como mensagem de requisição  request
			resposta = Reply.newBuilder();    // incializa a mensagem como mensagem de resposta reply
			servidor = new ServerSocket(6695);   // coloca o servidor para ouvir a na porta 
			s = servidor.accept();               // aceita uma conexao
			Request m = null ;                
			while(true){
			 m = mensagem.build().parseDelimitedFrom(s.getInputStream());    //  m recebe o que a mensagem recebida pelo cliente ;
			
			switch (m.getOpValue()) {         // pega o campo da operação escolhido pelo cliente
			case 0:
				resposta.setRes(m.getN1()+m.getN2());  //  faz o campo resposta.Res recebe o valor da operacao com N1 e N2
				break;
			case 1:
				resposta.setRes(m.getN1()-m.getN2());
				break;
			case 2:
				resposta.setRes(m.getN1()*m.getN2());
				break;
			case 3:
				resposta.setRes(m.getN1()/m.getN2());
				break;
			default:
				break;
			}
			
			resposta.build().writeDelimitedTo(s.getOutputStream());  // Envia a resposta pelo socket s;
		}
			
			
		}catch(Exception e){}
		
		
		
		
		
		
	}
}
