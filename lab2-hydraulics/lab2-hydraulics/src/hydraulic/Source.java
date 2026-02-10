package hydraulic;

public class Source extends Element {

	private double flow;
	public Source(String name) {
		super(name);
	}
	public void setFlow(double flow){
		this.flow = flow;
	}

	@Override
	public void setMaxFlow(double maxFlow) {
	}

	@Override
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		double outFlow = flow;

		observer.notifyFlow(this.getClass().getSimpleName(), name, SimulationObserver.NO_FLOW, outFlow);
		
		return new double[]{outFlow};
	}

	@Override
	public void recursiveSimulate(double flow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		double[] outFlows = simulate(flow, observer, enableMaxFlowCheck);
		
		Element[] outs = getOutputs();
		if (outs.length > 0 && outs[0] != null) {
			outs[0].recursiveSimulate(outFlows[0], observer, enableMaxFlowCheck);
		}
	}
}