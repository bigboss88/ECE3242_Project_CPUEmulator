import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class CPU1616 extends Abstract_CPU{
	private ArrayList<Short> registers;
	private int PC=0;
	private ArrayList<String> instructions;
	private ArrayList<Short> data_mem;
	
	public CPU1616() {
		//Initializing all registers to 0
		Short[] init_regvals = new Short[16];
		Arrays.fill(init_regvals, 0);
		registers = (ArrayList<Short>) Arrays.asList(init_regvals);
		
		//Same with memory
		Short[] init_memvals = new Short[4096];
		Arrays.fill(init_memvals, 0);
		data_mem = (ArrayList<Short>) Arrays.asList(init_memvals);
	}
	@Override
	public void load_instructions(File input) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(input));
			String ins = reader.readLine();
			if(ins.length() != 4) {
				System.out.println("Error CPU instruction word must have 4 hex characters");
				System.exit(0);
			}
			instructions.add(ins);
			while(ins!=null) {
				ins = reader.readLine();
				if(ins.length() != 4) {
					System.out.println("Error CPU instruction word must have 4 hex characters");
					System.exit(0);
				}
				instructions.add(ins);
			}
		}
		catch (Exception e) {
			System.out.print(e.getStackTrace());
		}
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		while(PC< instructions.size()) {
			String ins = instructions.get(PC);
			char opcode = ins.charAt(0);
			String args = ins.substring(1, 3);
			switch(opcode) {
			case '0':
				mov1(args);
			case '1':
				mov2(args);
			case '2':
				mov3(args);
			case '3':
				mov4(args);
			case '4':
				add(args);
			case '5':
				subt(args);
			case '6':
				jz(args);
			case '7':
				halt();
			case '8':
				inc(args);
			case '9':
				dec(args);
			case 'A':
				mul(args);
			case 'B':
				div(args);
			case 'C':
				mov5(args);
			case 'F':
				halt();
			default:
				System.out.println("Error no instruction found for OPCODE: "+opcode);
				System.exit(0);
			}
			PC++;
		}
	}

	@Override
	public void mov1(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		int imm = Integer.parseInt(args.substring(1, 2),16);
		imm = 0xFF &imm;
		if(imm > 4096) {imm = 4096;} //Only have 4 KB of memory
		registers.set(r1, data_mem.get(imm)); // // RF[rn] <= mem[direct]		
	}

	@Override
	public void mov2(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		int imm = Integer.parseInt(args.substring(1, 2),16);
		imm = 0xFF &imm;
		if(imm > 4096) {imm = 4096;} //Only have 4 KB of memory
		data_mem.set(imm, registers.get(r1)); // mem[direct] <= RF[rn]
		
	}

	@Override
	public void mov3(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r2); //Anding to make sure MSByte is 0
		data_mem.set(registers.get(r1), registers.get(r2));  // mem[RF[rn]] <= RF[rm]
		
	}

	@Override
	public void mov4(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		int imm = Integer.parseInt(args.substring(1, 2),16);
		imm = 0xFF &imm;
		registers.set(r1, (short)imm);	
	}

	@Override
	public void mov5(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xFF & r2); //Anding to make sure MSByte is 0
		registers.set(registers.get(r2), data_mem.get(r1));// RF[rm] <= mem[RF[rn]]
		
	}

	@Override
	public void add(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r2); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)+registers.get(r2))&0xFFFF)); //RF[rn] <= RF[rn]+RF[rm]
		
	}

	@Override
	public void subt(String args) {
		// TODO Auto-generated method stub
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r2); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)-registers.get(r2))&0xFFFF)); //RF[rn] <= RF[rn]-RF[rm]
	}

	@Override
	public void mul(String args) {
		// TODO Auto-generated method stub
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r2); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)*registers.get(r2))&0xFFFF)); //RF[rn] <= RF[rn]*RF[rm]
	}

	@Override
	public void div(String args) {
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		short r2 = Short.parseShort(args.substring(1, 1), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r2); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)/registers.get(r2))&0xFFFF)); //RF[rn] <= RF[rn]/RF[rm]
		
	}

	@Override
	public void inc(String args) {
		// TODO Auto-generated method stub
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)+1)&0xFFFF)); //RF[rn] <= RF[rn] +1
	}

	@Override
	public void dec(String args) {
		// TODO Auto-generated method stub
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		registers.set(r1, (short) ((registers.get(r1)-1)&0xFFFF)); //RF[rn] <= RF[rn] +1
	}

	@Override
	public void jz(String args) {
		// TODO Auto-generated method stub
		short r1 = Short.parseShort(args.substring(0, 0), 16);// I need to store a byte in a short to make sure it's unsigned;
		r1 = (short) (0xF & r1); //Anding to make sure MSByte is 0
		
		if(registers.get(r1) == 0) {
			int jump_location = Integer.parseInt(args.substring(1, 2),16);
			if(jump_location > 4096) {jump_location = 4096;}
			PC = jump_location;
		}
	}

	@Override
	public void halt() {
		// TODO Auto-generated method stub
		PC = registers.size()+1;
	}

	@Override
	public void readm(String args) {
		int imm = Integer.parseInt(args.substring(1, 2),16);
		imm = 0xFF &imm;
		if(imm > 4096) {imm = 4096;} //Only have 4 KB of memory
		System.out.println(data_mem.get(imm));
		
	}

}
