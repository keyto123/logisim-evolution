package edu.single.mips;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Direction;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;

import edu.single.funcoes.LsdFunctions;

public class HazardUnit extends InstanceFactory {

	public static final int STALLFINDEX = 0;
	public static final int STALLDINDEX = 1;
	public static final int INSTRUCTIONINDEX = 2;
	public static final int RSDINDEX = 3;
	public static final int RTDINDEX = 4;
	public static final int FLUSHEINDEX = 5;
	public static final int RSEINDEX = 6;
	public static final int RTEINDEX = 7;
	public static final int FORWARDAEINDEX = 8;
	public static final int FORWARDBEINDEX = 9;
	public static final int WRITEREGWINDEX = 10;
	public static final int MEMTOREGEINDEX = 11;
	public static final int REGWRITEMINDEX = 12;
	public static final int REGWRITEWINDEX = 13;
	
	public HazardUnit() {
		super("Hazard Unit");
		
		Bounds bounds = Bounds.create(-1200, -30, 2400, 60);
		this.setOffsetBounds(bounds);
		
		int distances[] = LsdFunctions.getDistanceFromMiddle(bounds);
		
		Port ps[] = new Port[] {
			new Port(-distances[0] + 30, -distances[1], Port.OUTPUT, 1), // StallF
			new Port(-600, -distances[1], Port.OUTPUT, 1), // StallD
			new Port(-480, -distances[1], Port.INPUT, 32), // Instruction
			new Port(30, -distances[1], Port.INPUT, 5), // RsD
			new Port(60, -distances[1], Port.INPUT, 5), // RtD
			new Port(110, -distances[1], Port.OUTPUT, 1), // FlushE
			new Port(160, -distances[1], Port.INPUT, 5), // RsE
			new Port(190, -distances[1], Port.INPUT, 5), // RtE
			new Port(280, -distances[1], Port.OUTPUT, 2), // ForwardAE
			new Port(350, -distances[1], Port.OUTPUT, 2), // ForwardBE
			new Port(distances[0] - 300, -distances[1], Port.INPUT, 5), // WriteRegW
			
			// Extras
//			new Port(0, 0, Port.OUTPUT, 0), // RegWriteM
//			new Port(0, 0, Port.OUTPUT, 0), // RegWriteW
//			new Port(0, 0, Port.OUTPUT, 0), // MemToRegE
			
		};
		
		this.setPorts(ps);
	}

	@Override
	public void paintInstance(InstancePainter painter) {
		painter.drawBounds();
		
		painter.drawPort(STALLFINDEX, "StallF", Direction.NORTH);
		painter.drawPort(STALLDINDEX, "StallD", Direction.NORTH);
		painter.drawPort(INSTRUCTIONINDEX, "Instruction", Direction.NORTH);
		painter.drawPort(RSDINDEX, "RsD", Direction.NORTH);
		painter.drawPort(RTDINDEX, "RtD", Direction.NORTH);
		painter.drawPort(FLUSHEINDEX, "FlushE", Direction.NORTH);
		painter.drawPort(RSEINDEX, "RsE", Direction.NORTH);
		painter.drawPort(RTEINDEX, "RtE", Direction.NORTH);
		painter.drawPort(FORWARDAEINDEX, "ForwardA", Direction.NORTH);
		painter.drawPort(FORWARDBEINDEX, "ForwardB", Direction.NORTH);
		painter.drawPort(WRITEREGWINDEX, "WriteRegW", Direction.NORTH);
		
		
		LsdFunctions.setTitle(painter, this.getName(), false);
	}

	@Override
	public void propagate(InstanceState state) {
		
		state.setPort(STALLFINDEX, Value.createKnown(BitWidth.create(1), 0), 0);
		state.setPort(STALLDINDEX, Value.createKnown(BitWidth.create(1), 0), 0);
		state.setPort(FLUSHEINDEX, Value.createKnown(BitWidth.create(1), 0), 0);
	}
	
}
