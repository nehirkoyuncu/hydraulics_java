package hydraulic;


public abstract class Element {
	
	protected String name;
	protected Element output; 
	protected Element[] outputs;
	protected double maxFlow = Double.POSITIVE_INFINITY; 
	protected Element input; 
	
	public Element(String name) {
		this.name = name;
	}

	
	public String getName() {
		return this.name;
	}
	
	
	public void connect(Element elem) {
		connect(elem, 0);
	}
	
	
	public void connect(Element elem, int index){
		this.output = elem;
		if (elem != null) {
			elem.input = this;
		}
	}
	
	
	public Element getOutput(){
		if (outputs != null && outputs.length > 0) {
			return outputs[0];
		}
		return output;
	}

	
	public Element[] getOutputs(){
		if (outputs != null) {
			return outputs;
		}
		if (output != null) {
			return new Element[]{output};
		}
		return new Element[]{};
	}
	
	
	public void setMaxFlow(double maxFlow) {
		this.maxFlow = maxFlow;
	}

	
	public double[] simulate(double inFlow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		
		if (enableMaxFlowCheck && inFlow > maxFlow) {
			observer.notifyFlowError(this.getClass().getSimpleName(), name, inFlow, maxFlow);
		}
		
		double outFlow = inFlow;
		
		if (output != null) {
			output.simulate(outFlow, observer, enableMaxFlowCheck);
		}
		
		observer.notifyFlow(this.getClass().getSimpleName(), name, inFlow, outFlow);

		return new double[]{outFlow};
	}
	
	
	public void recursiveSimulate(double flow, SimulationObserver observer, boolean enableMaxFlowCheck) {
		double[] outFlows = simulate(flow, observer, enableMaxFlowCheck);
		
		Element[] outs = getOutputs();
		for (int i = 0; i < outs.length; i++) {
			if (outs[i] != null && i < outFlows.length) {
				outs[i].recursiveSimulate(outFlows[i], observer, enableMaxFlowCheck);
			}
		}
	}

	protected static String pad(String current, String down){
		int n = current.length();
		final String fmt = "\n%"+n+"s";
		return current + down.replace("\n", fmt.formatted("") );
	}

	@Override
	public String toString(){
		String res = "[%s]%s ".formatted(getName(), this.getClass().getSimpleName());
		Element[] out = getOutputs();
		if( out != null && out.length > 0){
			StringBuilder buffer = new StringBuilder();
			for(int i=0; i<out.length; ++i) {
				if(i>0) buffer.append("\n");
				if (out[i] == null) buffer.append("+-> *");
				else buffer.append(pad("+-> ", out[i].toString()));
			}
			res = pad(res,buffer.toString());
		}
		return res;
	}


}