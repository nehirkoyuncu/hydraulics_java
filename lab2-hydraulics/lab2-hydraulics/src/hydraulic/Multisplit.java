package hydraulic;

import java.util.Arrays;


public class Multisplit extends Split {

	private final int numOutput;
	private double[] proportions;

	public Multisplit(String name, int numOutput) {
		super(name);
		this.numOutput = numOutput;
		this.outputs = new Element[numOutput];
		this.proportions = new double[numOutput];
		if (numOutput > 0) {
			Arrays.fill(this.proportions, 1.0 / numOutput);
		}
	}
	
	@Override
	public void connect(Element elem, int index) {
		if (index >= 0 && index < numOutput) {
			if (this.outputs[index] != null) {
				this.outputs[index].input = null;
			}
			this.outputs[index] = elem;
			if (elem != null) {
				elem.input = this;
			}
		}
	}

	@Override
	public Element[] getOutputs(){
		return this.outputs;
	}
	
	public void setProportions(double... proportions) {
		this.proportions = proportions;
	}
	
	@Override
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {

		if (enableMaxFlowCheck && inFlow > maxFlow) {
			observer.notifyFlowError(this.getClass().getSimpleName(), name, inFlow, maxFlow);
		}
		
		double[] outFlows = new double[numOutput];
		for (int i = 0; i < numOutput; i++) {
			outFlows[i] = inFlow * proportions[i];
		}
		
		observer.notifyFlow(this.getClass().getSimpleName(), name, inFlow, outFlows);
		
		return outFlows;
	}
}