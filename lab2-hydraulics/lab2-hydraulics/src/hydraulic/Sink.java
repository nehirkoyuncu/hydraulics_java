package hydraulic;

public class Sink extends Element {

	
	public Sink(String name) {
		super(name);
	}
	
	@Override
	public void connect(Element elem) {
	}
	
	@Override
	public Element getOutput() {
		return null;
	}

	@Override
	public Element[] getOutputs(){
		return new Element[]{};
	}

	@Override
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		
		if (enableMaxFlowCheck && inFlow > maxFlow) {
			observer.notifyFlowError(this.getClass().getSimpleName(), name, inFlow, maxFlow);
		}

		observer.notifyFlow(this.getClass().getSimpleName(), name, inFlow, SimulationObserver.NO_FLOW);

		return new double[]{SimulationObserver.NO_FLOW};
	}

	@Override
	public void recursiveSimulate(double flow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		simulate(flow, observer, enableMaxFlowCheck);
	}
}