package hydraulic;


import java.util.Arrays;


public class HSystem {
	
	private static final int MAX_ELEMENTS = 100; // R1 Hint
	private Element[] elements = new Element[MAX_ELEMENTS];
	private int count = 0;


	public void addElement(Element elem){
		if (count < MAX_ELEMENTS) {
			elements[count++] = elem;
		}
	}

	
	public int size() {
		return count;
    }

	
	public Element[] getElements(){
		return Arrays.copyOf(elements, count);
	}


	public void simulate(SimulationObserver observer){
		simulate(observer, false);
	}



	public boolean deleteElement(String name) {
		int indexToRemove = -1;
		Element elemToRemove = null;
		
		for (int i = 0; i < count; i++) {
			if (elements[i].getName().equals(name)) {
				indexToRemove = i;
				elemToRemove = elements[i];
				break;
			}
		}

		if (elemToRemove == null) return false;

		if (elemToRemove instanceof Split) {
			Element[] outs = elemToRemove.getOutputs();
			int connectedOutputs = 0;
			for (Element out : outs) {
				if (out != null) connectedOutputs++;
			}
			if (connectedOutputs > 1) {
				return false;
			}
		}

		
		Element upstream = elemToRemove.input;
		Element downstream = elemToRemove.getOutputs().length > 0 ? elemToRemove.getOutputs()[0] : null;

		if (upstream != null) {
			if (upstream instanceof Split) {
				Split splitUpstream = (Split) upstream;
				Element[] upstreamOutputs = splitUpstream.getOutputs();
				for (int i = 0; i < upstreamOutputs.length; i++) {
					if (upstreamOutputs[i] == elemToRemove) {
						splitUpstream.connect(downstream, i);
						break;
					}
				}
			} else {
				upstream.connect(downstream); 
			}
		}
		
		if (downstream != null) {
			downstream.input = upstream;
		}

		if (indexToRemove != -1) {
			System.arraycopy(elements, indexToRemove + 1, elements, indexToRemove, count - indexToRemove - 1);
			elements[--count] = null; 
			return true;
		}
		
		return false;
	}


	public void simulate(SimulationObserver observer, boolean enableMaxFlowCheck) {
		for (int i = 0; i < count; i++) {
			if (elements[i] instanceof Source) {
				Source source = (Source) elements[i];
				source.recursiveSimulate(0.0, observer, enableMaxFlowCheck); 
			}
		}
	}

    public static HBuilder build() {
		return new HBuilder(new HSystem());
    }
}