import java.io.*;
import java.net.*;

class client {
    public static void main(String[] args) throws IOException{

        ServerSocket s = new ServerSocket("127.0.0.1",1201);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String msgin="" , msgout="";
        while(!msgin.equals("end")){
            msgout = br.readLine();
            dout.writeUTF(msgout);
            msgin = din.readUTF();
            System.out.println(msgin); //printing server message
            dout.flush();
        }
        s.close();
    }
}
