package ufc.sd.calc;

import java.net.Socket;
import java.util.Scanner;

import ufc.sd.calc.Calculadora.Reply;
import ufc.sd.calc.Calculadora.Request;

public class CalculadoraCliente {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Socket s ;
		Request.Builder mensagem  = null;
		Reply resposta ;
		Scanner ler;
		try{
			
				
			ler = new Scanner(System.in);
			s = new Socket("localhost", 6695);
			mensagem = Request.newBuilder();
			while(true){
			
			System.out.println("Digite o primeiro numero");
			String n1 = ler.next();
			mensagem.setN1(Float.parseFloat(n1));
			
			System.out.println("Digite o segundo numero");
			String n2 = ler.next();
			mensagem.setN2(Float.parseFloat(n2));
			
			System.out.println("Digite a operação : (0 | +) (1 | -) (2 | *) (3 | /) ");
			String op = ler.next();
			switch (op) {
			case "0":
				mensagem.setOpValue(Integer.parseInt(op));
				break;
			case "1":
				mensagem.setOpValue(Integer.parseInt(op));
				break;
			case "2":
				mensagem.setOpValue(Integer.parseInt(op));
				break;
			case "3":
				mensagem.setOpValue(Integer.parseInt(op));
				break;
			default:
				break;
			}
		   mensagem.build().writeDelimitedTo(s.getOutputStream());
		   resposta = Reply.parseDelimitedFrom(s.getInputStream());

		   System.out.println("Resposta: "+resposta.getRes());
		}
		}catch(Exception e){}
		
		
		
		
	}
}
