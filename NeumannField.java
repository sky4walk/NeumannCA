//written by Andre Betz 2007 in Java 1.4.2
//http://www.andrebetz.de

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NeumannField{
	int m_actXSize;
	int m_actYSize;
	byte[][] m_FieldActual = null;
	byte[][] m_FieldNew = null;
	NeumannUniversalConstructor nuc = null;
	
	public NeumannField(int xSize,int ySize){
		m_actXSize = xSize;
		m_actYSize = ySize;
		m_FieldActual = new byte[m_actXSize+2][m_actYSize+2]; 
		m_FieldNew = new byte[m_actXSize+2][m_actYSize+2];
		nuc = new NeumannUniversalConstructor();
		Init();
		nuc.CopyAt(m_FieldActual,10,10);
	}
	public void Init(){
		for(int y=0;y<m_actYSize+2;y++){
			for(int x=0;x<m_actXSize+2;x++){
				m_FieldActual[x][y] = (byte)NeumannRules.NeumannStates.U;
			}			
		}
	}
	public int getM_actXSize() {
		return m_actXSize;
	}
	public int getM_actYSize() {
		return m_actYSize;
	}
	public int GetCell(int x,int y){
		if(x<m_actXSize&&y<m_actYSize){
			return m_FieldActual[x+1][y+1];
		}
		return -1;
	}
	public void SetCell(int x,int y,int st){
		if(x<m_actXSize&&y<m_actYSize){
			m_FieldActual[x+1][y+1] = (byte)st;
		}
	}
	public void Calc(){
		for(int y=1;y<m_actYSize+1;y++){
			for(int x=1;x<m_actXSize+1;x++){
				int Right = m_FieldActual[x+1][y];
				int Upper = m_FieldActual[x][y-1];
				int Left  = m_FieldActual[x-1][y];
				int Down  = m_FieldActual[x][y+1];
				int Current = m_FieldActual[x][y];
				
				int ret = NeumannRules.Transition(Right,Upper,Left,Down,Current);
				m_FieldNew[x][y] = (byte)ret;
			}			
		}
		for(int y=1;y<m_actYSize+1;y++){
			for(int x=1;x<m_actXSize+1;x++){
				m_FieldActual[x][y] = m_FieldNew[x][y];
			}			
		}
	}
	private String FormatLen(int a,int size){
		String frst = Integer.toString(a);
		if(frst.length()<size){
			String buf = "";
			int miss = size-frst.length();
			for(int i=0;i<miss;i++){
				buf += " ";
			}
			frst = buf + frst;
		}
		return frst;
	}
	public void SaveField(int Nr){
		try{
			File f = new File("Log_"+Nr+".txt");
			FileWriter out = new FileWriter(f);
			for(int y=1;y<m_actYSize+1;y++){
				for(int x=1;x<m_actXSize+1;x++){
					int b = m_FieldActual[x][y];
					String s = FormatLen(b,2);
					out.write(s+" ");
				}
				out.write("\r\n");
			}
			out.close();
		}
		catch(IOException e) {
			System.out.println("Dateifehler");
		}
	}
}