/**
 * 
 * UNIVERSIDADE FEDERAL DO CEARA - CAMPUS QUIXADA
 * CIENCIA DA COMPUTACAO - SISTEMAS DISTRIBUIDOS
 * 
 * PROF. PAULO REGO
 * 
 * DIEINISON JACK   #368339
 * RONILDO OLIVEIRA #366763
 * 
 * 
 **/

/** target/calculadora.pb **/

package ufc.sd.calc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import ufc.sd.calc.Calculadora.Request;
import ufc.sd.calc.Calculadora.Request.Builder;
import ufc.sd.calc.Calculadora.Request.Operacao;
import ufc.sd.exemplo.AddressBookProtos.Person;

public class CalculadoraUDPClient{

	static Request PromptForExpression(BufferedReader stdin, PrintStream stdout)
			throws IOException {

		Calculadora.Request.Builder request = Calculadora.Request.newBuilder();
		stdout.print("ID da Operação: ");
		request.setId(Integer.valueOf(stdin.readLine()));

		stdout.print("Primeiro Membro: ");
		request.setN1(Integer.valueOf(stdin.readLine()));

		stdout.print("Segundo Membro: ");
		request.setN2(Integer.valueOf(stdin.readLine()));

		return request.build();
	}

	public static void main(String args[]) throws IOException{ 

		DatagramSocket aSocket = null;

		Calculadora.Request.Builder r = Calculadora.Request.newBuilder();
		// Read the existing address book.
		try {
			r.mergeFrom(new FileInputStream(args[0]));
		} catch (FileNotFoundException e) {
			System.out.println(args[0]
					+ ": File not found.  Creating a new file.");
		}

		// Add an address.
		r.addRepeatedField(null, PromptForExpression(new BufferedReader(
				new InputStreamReader(System.in)), System.out));

		// Write the new address book back to disk.
		FileOutputStream output = new FileOutputStream(args[0]);
		r.build().writeTo(output);
		output.close();

		try {
			aSocket = new DatagramSocket();
			String msg = "32#*32";
			byte [] m = msg.getBytes();
			InetAddress aHost = InetAddress.getByName("127.0.0.1");
			int serverPort = 6666;		                                                 

			DatagramPacket request =
					new DatagramPacket(m,  msg.length(), aHost, serverPort);
			aSocket.send(request);

			byte[] buffer = new byte[1000];
			DatagramPacket reply = 
					new DatagramPacket(buffer, buffer.length);	
			aSocket.receive(reply);
			System.out.println("Reply: " + 
					new String(reply.getData(), 0, reply.getLength()));	

		}catch (SocketException e){
			System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
		}finally {
			if(aSocket != null)
				aSocket.close();
		}
	}		      	
}
