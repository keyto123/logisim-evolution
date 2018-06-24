package edu.single.mips;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.tools.Strings;

import edu.single.funcoes.LsdFunctions;

public class InstructionSplitter extends InstanceFactory {

	public static final byte INSTRUCTIONINDEX = 0;
	public static final byte OPCODEINDEX = 1;
	public static final byte FUNCTINDEX = 2;
	public static final byte REG1INDEX = 3;
	public static final byte REG2INDEX = 4;
	public static final byte REG3INDEX = 5;
	public static final byte SHIFTAMTINDEX = 6;
	public static final byte IMMEDIATEINDEX = 7;
	
	public InstructionSplitter() {
		super("InstSplit");
		
		Bounds bounds = Bounds.create(-60, -80, 120, 160);
		this.setOffsetBounds(bounds);
		
		int sizeX[] = {bounds.getX(), -bounds.getX()};
		
		Port ps[] = new Port[] {
			new Port(sizeX[0], 0, Port.INPUT, 32), // instruction
			new Port(sizeX[1], -60, Port.OUTPUT, 6), // 31:26 opcode
			new Port(sizeX[1], -40, Port.OUTPUT, 6), // 5:0 funct
			new Port(sizeX[1], -20, Port.OUTPUT, 5), // 25:21 RA
			new Port(sizeX[1], 0, Port.OUTPUT, 5), // 20:16 RB
			new Port(sizeX[1], 20, Port.OUTPUT, 5), // 15:11 RC
			new Port(sizeX[1], 40, Port.OUTPUT, 5), // 10:6 Shift
			new Port(sizeX[1], 60, Port.OUTPUT, 16), // 15:0 Immediate
		};
		
		ps[INSTRUCTIONINDEX].setToolTip(Strings.getter("Instruction input 31:0"));
		ps[OPCODEINDEX].setToolTip(Strings.getter("opcode for control unit 31:26"));
		ps[REG1INDEX].setToolTip(Strings.getter("Register bits 25:21"));
		ps[REG2INDEX].setToolTip(Strings.getter("Register bits 20:16"));
		ps[REG3INDEX].setToolTip(Strings.getter("Register bits 16:11"));
		ps[SHIFTAMTINDEX].setToolTip(Strings.getter("Shift amount for ALU"));
		ps[FUNCTINDEX].setToolTip(Strings.getter("funct bits for control unit"));
		ps[IMMEDIATEINDEX].setToolTip(Strings.getter("Immediate value for I instructions"));
		
		this.setPorts(ps);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		painter.drawPort(INSTRUCTIONINDEX, "Instruction", Direction.EAST);
		painter.drawPort(OPCODEINDEX, "opcode", Direction.WEST);
		painter.drawPort(REG1INDEX, "Rs", Direction.WEST);
		painter.drawPort(REG2INDEX, "Rt", Direction.WEST);
		painter.drawPort(REG3INDEX, "Rd", Direction.WEST);
		painter.drawPort(SHIFTAMTINDEX, "shift", Direction.WEST);
		painter.drawPort(FUNCTINDEX, "funct", Direction.WEST);
		painter.drawPort(IMMEDIATEINDEX, "immediate", Direction.WEST);
		
		LsdFunctions.setTitle(painter, this.getName());
	}

	@Override
	public void propagate(InstanceState state) {
		int instruction = state.getPortValue(INSTRUCTIONINDEX).toIntValue();
		
		byte regBits = 5;
		byte shiftBits = 5;
		byte opcodeBits = 6;
		byte functBits = 6;
		byte immediateBits = 16;
		
		int opcode = LsdFunctions.getBits(instruction, 26, opcodeBits);
		int reg1 = LsdFunctions.getBits(instruction, 20, regBits);
		int reg2 = LsdFunctions.getBits(instruction, 16, regBits);
		int reg3 = LsdFunctions.getBits(instruction, 11, regBits);
		int shift = LsdFunctions.getBits(instruction, 6, shiftBits);
		int funct = LsdFunctions.getBits(instruction, 0, functBits);
		int immediate = LsdFunctions.getBits(instruction, 0, immediateBits);
		
		int delay = 0;
		
		state.setPort(OPCODEINDEX, Value.createKnown(BitWidth.create(opcodeBits), opcode), delay);
		state.setPort(REG1INDEX, Value.createKnown(BitWidth.create(regBits), reg1), delay);
		state.setPort(REG2INDEX, Value.createKnown(BitWidth.create(regBits), reg2), delay);
		state.setPort(REG3INDEX, Value.createKnown(BitWidth.create(regBits), reg3), delay);
		state.setPort(SHIFTAMTINDEX, Value.createKnown(BitWidth.create(shiftBits), shift), delay);
		state.setPort(FUNCTINDEX, Value.createKnown(BitWidth.create(functBits), funct), delay);
		state.setPort(IMMEDIATEINDEX, Value.createKnown(BitWidth.create(immediateBits), immediate), delay);
	}

}
