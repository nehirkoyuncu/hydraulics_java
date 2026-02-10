package hydraulic;

public class Tap extends Element {

	private boolean open = true; 

	public Tap(String name) {
		super(name);
	}

	public void setOpen(boolean open){
		this.open = open;
	}

	@Override
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		
		if (enableMaxFlowCheck && inFlow > maxFlow) {
			observer.notifyFlowError(this.getClass().getSimpleName(), name, inFlow, maxFlow);
		}
		
		double outFlow = open ? inFlow : 0.0;
		
		observer.notifyFlow(this.getClass().getSimpleName(), name, inFlow, outFlow);
		
		return new double[]{outFlow};
	}
}