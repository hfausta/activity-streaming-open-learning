package edu.unsw.streaming.client.event;

import de.novanic.eventservice.client.event.Event;
import edu.unsw.streaming.bean.MaterialBean;

@SuppressWarnings("serial")
public class NewMaterialEvent implements Event {
	
	private MaterialBean material;
	
	public NewMaterialEvent() {}
	
	//Push Material Event
	public NewMaterialEvent(MaterialBean material) {
		this.material = material;
	}

	public void setMaterial(MaterialBean material) {
		this.material = material;
	}
	
	public MaterialBean getMaterial() {
		return material;
	}

}
