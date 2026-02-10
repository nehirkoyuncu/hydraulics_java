package hydraulic;

import java.util.ArrayList;
import java.util.List;


public class HBuilder {

	private final HSystem system;
	private Element lastElement;
	private Split currentSplit;
	private int currentSplitOutputIndex = 0;
	private List<Element> splitAncestors = new ArrayList<>();


	public HBuilder(HSystem system) {
		this.system = system;
	}

    
    public HBuilder addSource(String name) {
        Source src = new Source(name);
        system.addElement(src);
        lastElement = src;
        return this;
    }

    
    public HSystem complete() {
        return system;
    }

    private void link(Element newElement) {
        if (lastElement != null) {
			if (lastElement instanceof Split) {
				((Split)lastElement).connect(newElement, currentSplitOutputIndex);
			} else {
				lastElement.connect(newElement);
			}
        }
        system.addElement(newElement);
        lastElement = newElement;
    }

    public HBuilder linkToTap(String name) {
        Tap tap = new Tap(name);
        link(tap);
        return this;
    }


    public HBuilder linkToSink(String name) {
        Sink sink = new Sink(name);
        link(sink);
        return this;
    }

    public HBuilder linkToSplit(String name) {
        Split split = new Split(name);
        link(split);
        return this;
    }

    public HBuilder linkToMultisplit(String name, int numOutput) {
        Multisplit multisplit = new Multisplit(name, numOutput);
        link(multisplit);
        return this;
    }

    
    public HBuilder withOutputs() {
        if (lastElement instanceof Split) {
            currentSplit = (Split) lastElement;
            splitAncestors.add(currentSplit.input);
            currentSplitOutputIndex = 0;
            lastElement = currentSplit;
        }
        return this;     
    }

    public HBuilder then() {
        if (currentSplit != null) {
            currentSplitOutputIndex++;
            lastElement = currentSplit;
        }
        return this;
    }

    public HBuilder done() {
        if (currentSplit != null && !splitAncestors.isEmpty()) {
            lastElement = splitAncestors.remove(splitAncestors.size() - 1);
            currentSplit = (lastElement instanceof Split) ? (Split) lastElement : null;
            currentSplitOutputIndex = 0;
        }
        return this;
    }

    
    public HBuilder withFlow(double flow) {
        if (lastElement instanceof Source) {
            ((Source) lastElement).setFlow(flow);
        }
        return this;
    }

    
    public HBuilder open() {
        if (lastElement instanceof Tap) {
            ((Tap) lastElement).setOpen(true);
        }
        return this;
    }

    
    public HBuilder closed() {
        if (lastElement instanceof Tap) {
            ((Tap) lastElement).setOpen(false);
        }
        return this;
    }

    
    public HBuilder withPropotions(double[] props) {
        if (lastElement instanceof Multisplit) {
            ((Multisplit) lastElement).setProportions(props);
        }
        return this;
    }

    
    public HBuilder maxFlow(double max) {
        if (lastElement != null) {
            lastElement.setMaxFlow(max);
        }
        return this;
    }
}