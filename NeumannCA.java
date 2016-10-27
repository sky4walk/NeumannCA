//written by Andre Betz 2007 in Java 1.4.2
//http://www.andrebetz.de

import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NeumannCA{
	private JLabel label;
    private JFrame fenster = null;
    private CoordinateArea ca = null;
    private NeumannField field = null;
    private CalcThread ct = null;
    public boolean IsRun = false;
    
    public CalcThread GetCalcThread(){
    	return ct;
    }
    public NeumannCA(){
    	JFrame.setDefaultLookAndFeelDecorated(true);
        fenster = new JFrame("Neumann Universal Constructor AndreBetz.de");
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenster.getContentPane().setLayout(new BoxLayout(fenster.getContentPane(),BoxLayout.PAGE_AXIS));
        JMenuBar menuBar = new JMenuBar();
        fenster.setJMenuBar(menuBar);

        JMenu function = new JMenu("Funktionen");
        menuBar.add(function);
        
		JMenuItem item = new JMenuItem("Start");
		item.addActionListener(new MenuEventCatcher(0,this));
		function.add(item);
		item = new JMenuItem("Stop");
		item.addActionListener(new MenuEventCatcher(1,this));
		function.add(item);
		item = new JMenuItem("Reset");
		item.addActionListener(new MenuEventCatcher(2,this));
		function.add(item);
		item = new JMenuItem("Step");
		item.addActionListener(new MenuEventCatcher(3,this));
		function.add(item);

        field = new NeumannField(300,300);
        ca = new CoordinateArea(this,field);
        ct = new CalcThread(this);
        ct.setPriority(1);
        fenster.getContentPane().add(ca);
        
        label = new JLabel();
        fenster.getContentPane().add(label);
        
        ca.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        int yDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
        int xDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;

        fenster.setLocation(0,0);
//		fenster.setSize(xDim,yDim);
        fenster.setSize(xDim,yDim);
        fenster.pack();
        fenster.setVisible(true);
    }
    public void Init(){
    	ca.RePaint();
    }
    public void Repaint(){
    	ca.RePaint();    	
    }
    public void RunCalc(){
    	field.Calc();
    }
    public class CalcThread extends Thread{
    	private boolean m_Run = false;
    	private NeumannCA m_nca = null;
    	private int rounds = 1;
    	public CalcThread(NeumannCA nca){
    		m_nca = nca;
    	}
    	public void run(){
    		if(!m_Run)
    		{
				m_Run = true;
				while(m_Run){
					for(int i=0;i<rounds;i++){
						m_nca.RunCalc();
					}
				}
    		}
    	}
    	public void Halt(){
    		m_Run = false;
    	}
    }
    public class MenuEventCatcher implements ActionListener {
    	int m_actionNr = -1;
    	int m_StepCnt = 0;
    	NeumannCA m_agm = null;
    	public MenuEventCatcher(int actionNr,NeumannCA agm){
    		m_actionNr = actionNr;
    		m_agm = agm;
    	}
        public void actionPerformed(ActionEvent e) {
        	if(m_actionNr==0){
            	m_agm.GetCalcThread().run();
        	}else if(m_actionNr==1){
            	m_agm.IsRun = false;
        	}else if(m_actionNr==2){
        		m_StepCnt = 0;
//        		m_agm.getField().SaveField(m_StepCnt++);
        	}else if(m_actionNr==3){
        		m_StepCnt++;
//        		m_agm.RunCalc();
        		m_agm.GetCalcThread().run();
//        		m_agm.getField().SaveField(m_StepCnt);
        		Repaint();
        	}
        }
    }
    public class CoordinateArea  extends JComponent implements MouseInputListener{
		private static final long serialVersionUID = 1L;
		NeumannCA agm = null;
		NeumannField m_nf = null;
        Dimension preferredSize = null;
    
        public CoordinateArea(NeumannCA controller,NeumannField nf) {
            this.agm = controller;
            this.m_nf = nf;
            addMouseListener(this);
            addMouseMotionListener(this);
            setBackground(Color.WHITE);
            setOpaque(true);
//            int yDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
//            int xDim = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
//			preferredSize = new Dimension(xDim,yDim);
			preferredSize = new Dimension(500,500);
        }
   
        public Dimension getPreferredSize() {
            return preferredSize;
        }
    
        protected void paintComponent(Graphics g) {
            if (isOpaque()) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }     
            drawPoints(g);
        }
        public void RePaint(){
        	repaint();
        }
        private void drawPoints(Graphics g){
        	for(int y=0;y<m_nf.m_actYSize;y++){
            	for(int x=0;x<m_nf.m_actYSize;x++){
            		int state = m_nf.GetCell(x,y);
            		Color c = null;
            		if(state==0){
            			c = new Color(0,0,0);
            		}else if(state==1){
            			c = new Color(250,255,255);
            		}else if(state<10){
            			c = new Color(state*25,0,0);
            		}else if(state<20){
            			c = new Color(100,(state-9)*25,100);
            		}else{
            			c = new Color(100,100,(state-19)*25);            			
            		}
            		g.setColor(c);
            		g.fillRect(x*2,y*2,2,2);
            	}        		
        	}
        }
        public void mouseClicked(MouseEvent e) { }        
        public void mouseMoved(MouseEvent e) { }
        public void mouseExited(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mousePressed(MouseEvent e) { }
        public void mouseDragged(MouseEvent e) { }
    }
	public NeumannField getField() {
		return field;
	}
	public static void main(String[] args) {
		NeumannCA nm = new NeumannCA();
		nm.Init();
	}
}