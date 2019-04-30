import java.io.File;

public interface CPU_Interface {
	public void load_instructions(File input);
	public void execute();
	
	//Data Manipulation
	public void mov1(String args); //0
	public void mov2(String args); //1
	public void mov3(String args); //2
	public void mov4(String args); //3
	public void mov5(String args); //C
	
	//ALU
	public void add(String args); //4
	public void subt(String args); //5
	public void mul(String args); //A 
	public void div(String args); //B
	public void inc(String args); //8
	public void dec(String args); //9
	
	//Controls
	public void jz(String args); //6
	public void halt(); //F
	
	//Output
	public void readm(String args); //7
}
