//written by Andre Betz 2007 in Java 1.4.2
//http://www.andrebetz.de

import java.io.*;

public class NeumannRules {
	public static class NeumannStates {
		public static int U 	= 1;	// ground state U
		// the transition or sensitised states (in 8 substates)
		public static int S 	= 2;	// newly sensitised
		public static int S0 	= 3;	// sensitised, having received no input for one cycle
		public static int S00 	= 4;	// sensitised, having received no input for two cycles
		public static int S01 	= 5;	// sensitised, having received no input for one cycle and then an input for one cycle
		public static int S000 	= 6;	// sensitised, having received no input for three cycle
		public static int S1 	= 7;	// sensitised, having received an input for one cycle
		public static int S10 	= 8;	// sensitised, having received an input for one cycle and then no input for one cycle
		public static int S11 	= 9;	// sensitised, having received input for two cycles
		// the confluent states (in 4 states of excitation)
		public static int C00 	= 10;	// quiescent (and will also be quiescent next cycle)
		public static int C01 	= 11;	// excited (but will be quiescent next cycle)
		public static int C10 	= 12;	// next-excited (now quiescent, but will be excited next cycle)
		public static int C11 	= 13;	// excited next-excited (currently excited and will be excited next cycle)
		// the ordinary transmission states (in 4 directions, excited or quiescent)
		public static int OER  = 14;	// Right-directed (excited)
		public static int OEU  = 15;	// Upper-directed (excited)
		public static int OEL  = 16;	// Left-directed (excited)
		public static int OED  = 17;	// Down-directed (excited)
		public static int OQR  = 18;	// Right-directed (quiescent)
		public static int OQU  = 19;	// Upper-directed (quiescent)
		public static int OQL  = 20;	// Left-directed (quiescent)
		public static int OQD 	= 21;	// Down-directed (quiescent)
		// the special transmission states (in 4 directions, excited or quiescent)
		public static int SER 	= 22;	// Right-directed (excited)
		public static int SEU 	= 23;	// Upper-directed (excited)
		public static int SEL 	= 24;	// Left-directed (excited)
		public static int SED 	= 25;	// Down-directed (excited)
		public static int SQR 	= 26;	// Right-directed (quiescent)
		public static int SQU 	= 27;	// Upper-directed (quiescent)
		public static int SQL 	= 28;	// Left-directed (quiescent)
		public static int SQD 	= 29;	// Down-directed (quiescent)
		public static String GetStateName(int state){
			if(state==U){
				return "U";
			}else if(state==S){
				return "S";
			}else if(state==S0){
				return "S0";
			}else if(state==S1){
				return "S1";
			}else if(state==S00){
				return "S00";
			}else if(state==S01){
				return "S01";
			}else if(state==S10){
				return "S10";
			}else if(state==S11){
				return "S11";
			}else if(state==S000){
				return "S000";
			}else if(state==C00){
				return "C00";
			}else if(state==C01){
				return "C01";
			}else if(state==C10){
				return "C10";
			}else if(state==C11){
				return "C11";
			}else if(state==OER){
				return "OER";
			}else if(state==OEU){
				return "OEU";
			}else if(state==OEL){
				return "OEL";
			}else if(state==OED){
				return "OED";
			}else if(state==OQR){
				return "OQR";
			}else if(state==OQU){
				return "OQU";
			}else if(state==OQL){
				return "OQL";
			}else if(state==OQD){
				return "OQD";
			}else if(state==SER){
				return "SER";
			}else if(state==SEU){
				return "SEU";
			}else if(state==SEL){
				return "SEL";
			}else if(state==SED){
				return "SED";
			}else if(state==SQR){
				return "SQR";
			}else if(state==SQU){
				return "SQU";
			}else if(state==SQL){
				return "SQL";
			}else if(state==SQD){
				return "SQD";
			}else if(state==0){
				return "z";
			}
			return "nn";
		}
	}
	public static int Transition(int Right,int Upper,int Left,int Down,int Current){
		if(Current==NeumannStates.U){
			if(	Right==NeumannStates.U &&
				Upper==NeumannStates.U &&
				Left ==NeumannStates.U &&
				Down ==NeumannStates.U){
					return NeumannStates.U;
			}else if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.S;
			}else{
				return NeumannStates.U;
			}
		}else if(Current==NeumannStates.S){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.S1;
			}else{
				return NeumannStates.S0;
			}
		}else if(Current==NeumannStates.S0){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.S01;
			}else{
				return NeumannStates.S00;
			}
		}else if(Current==NeumannStates.S1){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.S11;
			}else{
				return NeumannStates.S10;
			}
		}else if(Current==NeumannStates.S00){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.OQL;
			}else{
				return NeumannStates.S000;
			}
		}else if(Current==NeumannStates.S01){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.SQR;
			}else{
				return NeumannStates.OQD;
			}
		}else if(Current==NeumannStates.S10){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.SQL;
			}else{
				return NeumannStates.SQU;
			}
		}else if(Current==NeumannStates.S11){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.C00;
			}else{
				return NeumannStates.SQD;
			}
		}else if(Current==NeumannStates.S000){
			if(IsExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.OQU;
			}else{
				return NeumannStates.OQR;
			}
		}else if(IsOrdinaryState(Current)){
			if(IsSpecialExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.U;
			}else if(IsFlankedByExcitedConfluent(Right,Upper,Left,Down,Current) ||
					 IsPointedByExcitedOrdinaryTransmission(Right,Upper,Left,Down,Current)){
				return Excite(Current);
			}else{
				return UnExcite(Current);				
			}
		}else if(IsSpecialState(Current)){	
			if(IsOrdinaryExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.U;
			}else if(IsFlankedByExcitedConfluent(Right,Upper,Left,Down,Current) ||
					IsPointedByExcitedSpecialTransmission(Right,Upper,Left,Down,Current)){
				return Excite(Current);
			}else{
				return UnExcite(Current);
			}
		}else if(IsConfluentState(Current)){
			if(IsSpecialExcitedTransmission(Right,Upper,Left,Down)){
				return NeumannStates.U;
			}else if(IsPointedByOrdinaryExcited(Right,Upper,Left,Down)){
				return ExciteConfluent(Current);
			}else{
				return DecayConfluent(Current);
			}
		}
		return 0;
	}
	private static boolean IsExcitedTransmission(int Right,int Upper,int Left,int Down){
		if( Right==NeumannStates.SEL || Right==NeumannStates.OEL ||
			Upper==NeumannStates.SED || Upper==NeumannStates.OED ||
			Left ==NeumannStates.SER || Left ==NeumannStates.OER ||
			Down ==NeumannStates.SEU || Down ==NeumannStates.OEU){
			return true;
		}
		return false;
	}
	private static boolean IsSpecialExcitedTransmission(int Right,int Upper,int Left,int Down){
		if(	Right==NeumannStates.SEL || Upper==NeumannStates.SED ||
			Left ==NeumannStates.SER ||	Down ==NeumannStates.SEU){
			return true;
		}
		return false;
	}
	private static boolean IsOrdinaryExcitedTransmission(int Right,int Upper,int Left,int Down){
		if(	Right==NeumannStates.OEL || Upper==NeumannStates.OED ||
			Left ==NeumannStates.OER ||	Down ==NeumannStates.OEU){
			return true;
		}
		return false;
	}
	private static boolean IsPointedByExcitedOrdinaryTransmission(int Right,int Upper,int Left,int Down,int Current){
		if(Right==NeumannStates.OEL && !IsRightState(Current)){
			return true;
		}else if(Upper==NeumannStates.OED && !IsUpperState(Current)){
			return true;
		}else if(Left==NeumannStates.OER && !IsLeftState(Current)){
			return true;
		}else if(Down==NeumannStates.OEU && !IsDownState(Current)){
			return true;
		}
		return false;
	}
	private static boolean IsPointedByExcitedSpecialTransmission(int Right,int Upper,int Left,int Down,int Current){
		if(Right==NeumannStates.SEL && !IsRightState(Current)){
			return true;
		}else if(Upper==NeumannStates.SED && !IsUpperState(Current)){
			return true;
		}else if(Left==NeumannStates.SER && !IsLeftState(Current)){
			return true;
		}else if(Down==NeumannStates.SEU && !IsDownState(Current)){
			return true;
		}
		return false;
	}
	private static boolean IsFlankedByExcitedConfluent(int Right,int Upper,int Left,int Down,int Current){ 
	    if(!IsRightState(Current) && (Right==NeumannStates.C10 || Right==NeumannStates.C11)){
    		return true;
	    }else if(!IsUpperState(Current) && (Upper==NeumannStates.C10 || Upper==NeumannStates.C11)){
    		return true;
	    }else if(!IsLeftState(Current) && (Left==NeumannStates.C10 || Left==NeumannStates.C11)){
    		return true;
	    }else if(!IsDownState(Current) && (Down==NeumannStates.C10 || Down==NeumannStates.C11)){
    		return true;
	    }	    	
	    return false;
	}
	private static boolean IsPointedByOrdinaryExcited(int Right,int Upper,int Left,int Down){
		if((Right== NeumannStates.OEL || Upper == NeumannStates.OED ||
			Left == NeumannStates.OER || Down  == NeumannStates.OEU)&&
		  !(Right== NeumannStates.OQL || Upper == NeumannStates.OQD ||
			Left == NeumannStates.OQR || Down  == NeumannStates.OQU)){
			return true;
		}
		return false;
	}
	private static boolean IsOrdinaryState(int state){
		if( state==NeumannStates.OER || state==NeumannStates.OEU ||
			state==NeumannStates.OEL || state==NeumannStates.OED ||
			state==NeumannStates.OQR || state==NeumannStates.OQU ||
			state==NeumannStates.OQL || state==NeumannStates.OQD){
			return true;
		}
		return false;
	}
	private static int ExciteConfluent(int state){
		if(state==NeumannStates.C00 || state==NeumannStates.C10){
			return NeumannStates.C01;
		}else{
			return NeumannStates.C11;			
		}
	}
	private static int DecayConfluent(int state){
		if(state==NeumannStates.C00 || state==NeumannStates.C10){
			return NeumannStates.C00;
		}else{
			return NeumannStates.C10;			
		}
	}
	private static boolean IsSpecialState(int state){
		if( state==NeumannStates.SER || state==NeumannStates.SEU ||
			state==NeumannStates.SEL || state==NeumannStates.SED ||
			state==NeumannStates.SQR || state==NeumannStates.SQU ||
			state==NeumannStates.SQL || state==NeumannStates.SQD){
			return true;
		}
		return false;
	}
	private static boolean IsRightState(int state){
		if( state==NeumannStates.SER || state==NeumannStates.OER ||
			state==NeumannStates.SQR || state==NeumannStates.OQR){
			return true;
		}
		return false;
	}
	private static boolean IsUpperState(int state){
		if( state==NeumannStates.SEU || state==NeumannStates.OEU ||
			state==NeumannStates.SQU || state==NeumannStates.OQU){
			return true;
		}
		return false;
	}
	private static boolean IsLeftState(int state){
		if( state==NeumannStates.SEL || state==NeumannStates.OEL ||
			state==NeumannStates.SQL || state==NeumannStates.OQL){
			return true;
		}
		return false;
	}
	private static boolean IsDownState(int state){
		if( state==NeumannStates.SED || state==NeumannStates.OED ||
			state==NeumannStates.SQD || state==NeumannStates.OQD){
			return true;
		}
		return false;
	}
	private static boolean IsConfluentState(int state){
		if( state==NeumannStates.C00 || state==NeumannStates.C01 ||
			state==NeumannStates.C10 || state==NeumannStates.C11){
			return true;
		}
		return false;
	}
	private static int UnExcite(int state){
		if( state==NeumannStates.OER){
			return NeumannStates.OQR;
		}else if( state==NeumannStates.OEU){
			return NeumannStates.OQU;
		}else if( state==NeumannStates.OEL){
			return NeumannStates.OQL;
		}else if( state==NeumannStates.OED){
			return NeumannStates.OQD;
		}else if( state==NeumannStates.SER){
			return NeumannStates.SQR;
		}else if( state==NeumannStates.SEU){
			return NeumannStates.SQU;
		}else if( state==NeumannStates.SEL){
			return NeumannStates.SQL;
		}else if( state==NeumannStates.SED){
			return NeumannStates.SQD;
		}
		return state;
	}
	private static int Excite(int state){
		if( state==NeumannStates.OQR){
			return NeumannStates.OER;
		}else if( state==NeumannStates.OQU){
			return NeumannStates.OEU;
		}else if( state==NeumannStates.OQL){
			return NeumannStates.OEL;
		}else if( state==NeumannStates.OQD){
			return NeumannStates.OED;
		}else if( state==NeumannStates.SQR){
			return NeumannStates.SER;
		}else if( state==NeumannStates.SQU){
			return NeumannStates.SEU;
		}else if( state==NeumannStates.SQL){
			return NeumannStates.SEL;
		}else if( state==NeumannStates.SQD){
			return NeumannStates.SED;
		}
		return state;
	}
	public static String GetTransitionString(int Right,int Upper,int Left,int Down,int Current,int ret){
		return  "r " + NeumannStates.GetStateName(Right)+" "+
				"u " + NeumannStates.GetStateName(Upper)+" "+
				"l " + NeumannStates.GetStateName(Left)+" "+
				"d " + NeumannStates.GetStateName(Down)+" "+
				"a " + NeumannStates.GetStateName(Current)+" "+
				"n " + NeumannStates.GetStateName(ret);
	}
	public static boolean test(){
		String fileName = "transitions.txt";
		boolean testOK = true;
		File file = new File(fileName);
    	if(file.exists()){
    		InputStreamReader isr = null;
    		try{
    			isr = new InputStreamReader(new FileInputStream(fileName));
    			BufferedReader br = new BufferedReader(isr);
    			String line;
    			while((line = br.readLine())!=null){
    				String[] tokens = line.split(" ");
    				if(tokens.length==16){
    					int[] testbytes = new int[tokens.length/2];
    					for (int i=0; i<testbytes.length; i++){
    						String r = tokens[i*2+1];
    						testbytes[i] = Integer.parseInt(r); 
    					}
						int ret = Transition(testbytes[2],testbytes[3],testbytes[4],testbytes[5],testbytes[6]);
						if(ret!=testbytes[7]&&testbytes[7]!=-1){
							testOK = false;
							System.out.println("x 0 y 0 "+GetTransitionString(testbytes[2],testbytes[3],testbytes[4],testbytes[5],testbytes[6],testbytes[7]));
						}else{
							System.out.print(".");
						}
    				}
    			}
    			br.close();
    		} catch (Exception e) {
    			testOK = false;
    		}
    		
    	}
    	return testOK;
	}
	public static void main(String[] args) {
		test();			
	}
}