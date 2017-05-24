/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networksp1;

import java.io.*;

/**
 *
 * @author Kanat
 */
public class FMTEntry implements Serializable{
    public String filename;
    public String filetype;
    public long filesize;
    public long lastmodified;
    public String ip;
    public int port;
    
    public FMTEntry(String filename, String filetype, long filesize, long lastmodified, int port){
        this.filename = filename;
        this.filetype = filetype;
        this.filesize = filesize;
        this.lastmodified = lastmodified;
        this.port = port;
    }
    
    @Override
    public String toString(){
        return "(" + filename + ", " + filetype + ", " + filesize + ", " + lastmodified + ", " + ip + ", " + port + ")";
    }
}