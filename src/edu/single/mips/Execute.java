package edu.single.mips;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.cburch.logisim.instance.StdAttr;
import com.cburch.logisim.tools.Strings;

import edu.single.funcoes.LsdFunctions;

public class Execute extends InstanceFactory {
	
	public static final byte INPUTA = 0;
	public static final byte INPUTB = 1;
	public static final byte OPCODE = 2;
	public static final byte SHIFTAMOUNT = 3;
	public static final byte OUTPUT = 4;
	
	public Execute() {
		super("EX", Strings.getter("Execute"));
		
		Bounds bounds = Bounds.create(-50, -50, 100, 100);
		this.setOffsetBounds(bounds);
		
		this.setAttributes(new Attribute[] { StdAttr.WIDTH }, new Object[] { BitWidth.create(32) });
		
		int size[] = LsdFunctions.getDistanceFromMiddle(bounds);
		
		Port ps[] = new Port[] {
			new Port(-size[0], -20, Port.INPUT, StdAttr.WIDTH), 	// InputA
			new Port(-size[0], 20, Port.INPUT, StdAttr.WIDTH), 	// InputB
			new Port(-20, size[1], Port.INPUT, 3), // code
			new Port(0, size[1], Port.INPUT, 5), // Shift Amount
			new Port(size[0], 0, Port.OUTPUT, StdAttr.WIDTH), 	// Output
		};
		
		ps[INPUTA].setToolTip(Strings.getter("Left operand as InputA operation InputB"));
		ps[INPUTB].setToolTip(Strings.getter("Right operand as InputA operation InputB"));
		ps[OPCODE].setToolTip(Strings.getter("Code for operation to be done"));
		ps[SHIFTAMOUNT].setToolTip(Strings.getter("Shift Amount for shift operations"));
		ps[OUTPUT].setToolTip(Strings.getter("Output for the operation"));
		
		this.setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int inputA = state.getPortValue(INPUTA).toIntValue();
		int inputB = state.getPortValue(INPUTB).toIntValue();
		int code = state.getPortValue(OPCODE).toIntValue();
		int shiftAmount = state.getPortValue(SHIFTAMOUNT).toIntValue();
		
		int output = 0;
		
		switch(code) {
		case 0x0:
			output = inputA + inputB;
			break;
			
		case 0x1:
			output = inputA - inputB;
			break;
			
		case 0x2:
			output = inputA * inputB;
			break;
		case 0x3:
			output = inputA / inputB;
			break;
		}
		
		Value Output = Value.createKnown(state.getAttributeSet().getValue(StdAttr.WIDTH), output);
		state.setPort(OUTPUT, Output, 32);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		
		painter.drawPort(INPUTA, "Input A", Direction.EAST);
		painter.drawPort(INPUTB, "Input B", Direction.EAST);
		painter.drawPort(OPCODE, "Op", Direction.SOUTH);
		painter.drawPort(SHIFTAMOUNT, "SA", Direction.SOUTH);
		painter.drawPort(OUTPUT, "Output", Direction.WEST);
		
		LsdFunctions.setTitle(painter, this.getName());
	}
	
	/*
	@Override
	public void paintIcon(InstancePainter painter) {
		
	}
	*/
}
