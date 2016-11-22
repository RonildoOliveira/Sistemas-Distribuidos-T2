/**
 * 
 * UNIVERSIDADE FEDERAL DO CEAR� - CAMPUS QUIXAD�
 * CI�NCIA DA COMPUTA��O - SISTEMAS DISTRIBU�DOS
 * 
 * PROF. PAULO REGO
 * 
 * DIEINISON JACK   #368339
 * RONILDO OLIVEIRA #366763
 * 
 * C�DIGOS DISPON�VEIS EM:
 * https://github.com/RonildoOliveira/Sistemas-Distribuidos-T1
 * 
 **/

package ufc.sd.calc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class CalculadoraUDPServer{
    public static void main(String args[]){
    	
    	DatagramSocket aSocket = null;
		try{
			System.out.println("Servidor Online...");
	    	aSocket = new DatagramSocket(6666);
	    	
			byte[] buffer = new byte[1000];
 			while(true){
 				
 				DatagramPacket request = 
 						new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);
  				
  				String expression = 
  						handleExpression(new String(request.getData(),0,request.getLength()));

  				DatagramPacket reply = 
  						new DatagramPacket(expression.getBytes(), expression.length(), 
        				request.getAddress(), request.getPort());
 	
    			aSocket.send(reply);
    		}
		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null)
				aSocket.close();
		}
    }

	private static String handleExpression(String expression){
		String member1 = null;
		String member2 = null;
		String[] members;
		
		if(expression.contains("+")){
			try {
				expression = expression.replace("+", "#");
				members = expression.split("#");
				member1 = members[0];
				member2 = members[1];
				
				return String.valueOf(Integer.parseInt(member1)+Integer.parseInt(member2));
			} catch (Exception e) {
				return "NaN";
			}			
		}
		
		else if(expression.contains("-")){
			try {
				expression = expression.replace("-", "#");
				members = expression.split("#");
				member1 = members[0];
				member2 = members[1];
				
				return String.valueOf(Integer.parseInt(member1)-Integer.parseInt(member2));
			} catch (Exception e) {
				return "NaN";
			}
		}
		
		else if(expression.contains("*")){
			try {
				expression = expression.replace("*", "#");
				members = expression.split("#");
				member1 = members[0];
				member2 = members[1];
				
				return String.valueOf(Integer.parseInt(member1)*Integer.parseInt(member2));
			} catch (Exception e) {
				return "NaN";
			}
		}
		
		else if(expression.contains("/")){
			try {
				expression = expression.replace("/", "#");
				members = expression.split("#");
				member1 = members[0];
				member2 = members[1];
				
				return String.valueOf(Integer.parseInt(member1)/Integer.parseInt(member2));
			} catch (Exception e) {
				return "NaN";
			}
		}
		
		else{
			return "Not a Valid Expression";
		}
	}

    
}
