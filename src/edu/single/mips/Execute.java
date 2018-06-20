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

import edu.single.funcoes.LalaFunctions;

public class Execute extends InstanceFactory {
	public Execute() {
		super("EX", Strings.getter("Execute"));
		
		Bounds bounds = Bounds.create(-50, -50, 100, 100);
		this.setOffsetBounds(bounds);
		
		this.setAttributes(new Attribute[] { StdAttr.WIDTH }, new Object[] { BitWidth.create(32) });
		
		int size[] = LalaFunctions.getDistanceFromMiddle(bounds);
		
		Port ps[] = new Port[] {
				new Port(-size[0], -20, Port.INPUT, StdAttr.WIDTH), 	// InputA
				new Port(-size[0], 20, Port.INPUT, StdAttr.WIDTH), 	// InputB
				new Port(-20, size[1], Port.INPUT, 3), // code
				new Port(size[0], 0, Port.OUTPUT, StdAttr.WIDTH), 	// Output
		};
		
		ps[2].setToolTip(Strings.getter("Code for operation to be done"));
		
		this.setPorts(ps);
	}

	@Override
	public void propagate(InstanceState state) {
		int inputA = state.getPortValue(0).toIntValue();
		int inputB = state.getPortValue(1).toIntValue();
		int code = state.getPortValue(2).toIntValue();
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
		
		Value Output = Value.createKnown(state.getPortValue(0).getBitWidth(), output);
		state.setPort(3, Output, 32);
	}
	
	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		
		painter.drawPort(0, "Input A", Direction.EAST);
		painter.drawPort(1, "Input B", Direction.EAST);
		painter.drawPort(2, "Code", Direction.SOUTH);
		painter.drawPort(3, "Output", Direction.WEST);
		
		LalaFunctions.setTitle(painter, this.getName());
	}
	
	/*
	@Override
	public void paintIcon(InstancePainter painter) {
		
	}
	*/
}
