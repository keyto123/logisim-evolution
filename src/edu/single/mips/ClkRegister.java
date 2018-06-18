package edu.single.mips;

import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.std.wiring.Clock;

import edu.single.funcoes.ProjectUtils;

public class ClkRegister extends Clock {

	private int oldClockState = 0;
	
	@Override
	public void propagate(InstanceState state) {
		super.propagate(state);
		int clockState = state.getPortValue(0).toIntValue();
		if (clockState != oldClockState) {
			oldClockState = clockState;
			if (clockState == 0) {
				DataFlow_Frame f = ProjectUtils.getDataFlowFrame(state.getProject());
				if (f != null) {
					f.getFlow().updateTable();
				}
			}
		}
	}
}
