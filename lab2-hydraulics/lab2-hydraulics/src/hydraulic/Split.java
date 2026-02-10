package hydraulic;

public class Split extends Element {
	
	protected static final int NUM_OUTPUTS = 2;

	public Split(String name) {
		super(name);
		this.outputs = new Element[NUM_OUTPUTS];
	}
	
	@Override
	public void connect(Element elem, int index) {
		if (index >= 0 && index < NUM_OUTPUTS) {
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
	public Element getOutput(){
		return null;
	}

	@Override
	public Element[] getOutputs(){
		return this.outputs;
	}

	@Override
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		
		if (enableMaxFlowCheck && inFlow > maxFlow) {
			observer.notifyFlowError(this.getClass().getSimpleName(), name, inFlow, maxFlow);
		}
		
		double outFlow = inFlow / 2.0;
		double[] outFlows = new double[NUM_OUTPUTS];
		outFlows[0] = outFlow;
		outFlows[1] = outFlow;
		
		observer.notifyFlow(this.getClass().getSimpleName(), name, inFlow, outFlows);
		
		return outFlows;
	}
}
