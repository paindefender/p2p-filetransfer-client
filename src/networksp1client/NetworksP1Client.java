package networksp1client;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import networksp1.FMTEntry;
/**
 *
 * @author Kanat Alimanov
 */

public class NetworksP1Client {
    
    public Socket clientSocket;
    public ObjectOutputStream objectWrite;
    public DataOutputStream outToServer;
    public BufferedReader inFromServer;
    public ObjectInputStream objectRead;
    public ServerSocket welcomeSocket;
    public int portNum;
    
    
    
    public NetworksP1Client(String serverHost, int serverPort) throws Exception{
        welcomeSocket = new ServerSocket(0);
        portNum = welcomeSocket.getLocalPort();
        this.clientSocket = new Socket(serverHost, serverPort);
        this.objectWrite = new ObjectOutputStream(clientSocket.getOutputStream());
        this.outToServer = new DataOutputStream(clientSocket.getOutputStream());
        this.inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.objectRead = new ObjectInputStream(clientSocket.getInputStream());
    }
    
    public void sendFMTs(ArrayList<FMTEntry> fmtEntries) throws Exception{
        outToServer.writeBytes("fmts");
        objectWrite.writeObject(fmtEntries);
    }
    
    public void endConnection() throws Exception{
        outToServer.writeBytes("deld");
        System.out.println("endconnection called");
    }
    
    public ArrayList<FMTEntry> createFMTEntryList(String folderPath){
        File[] files = new File(folderPath).listFiles();
        ArrayList<FMTEntry> fmtEntries = new ArrayList<>();
        for (File file : files){
            if (file.isFile()){
                String fileName = file.getName();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i > 0) {
                    extension = fileName.substring(i+1);
                }
                FMTEntry fmte = new FMTEntry(fileName, extension, file.length(), file.lastModified(), portNum);
                fmtEntries.add(fmte);
            }
        }
        return fmtEntries;
    }
    
    public ArrayList<FMTEntry> getFMTList(String keyword) throws Exception{
        outToServer.writeBytes("gfmt");
        
        ArrayList<FMTEntry> fmtEntries = new ArrayList<>();
        ArrayList<FMTEntry> finalEntries = new ArrayList<>();
        
        fmtEntries = (ArrayList<FMTEntry>) objectRead.readObject();
        
        for (FMTEntry i: fmtEntries){
            if (i.filename.toLowerCase().contains(keyword.toLowerCase())){
                finalEntries.add(i);
            }
            //System.out.println(i);
        }
        
        return finalEntries;
    }
}
