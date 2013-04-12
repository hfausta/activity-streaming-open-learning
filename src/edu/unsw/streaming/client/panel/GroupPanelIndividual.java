package edu.unsw.streaming.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.unsw.streaming.bean.GroupBean;

/**
 * Container to display individual main groups, including subgroups belonging to that main group
 * @author Lianne
 *
 */
public class GroupPanelIndividual extends Composite {
	private final GroupBean group;
	//private final List<GroupBean> subGroups;

	public GroupPanelIndividual(GroupBean mainGroup, List<GroupBean> subGroups) {
		this.group = mainGroup;
		//this.subGroups = subGroups;
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStylePrimaryName("group");
		initWidget(verticalPanel);
		
		HorizontalPanel mainGroupPanel = new HorizontalPanel();
		final VerticalPanel subGroupPanel = new VerticalPanel();
		ToggleButton subToggle = new ToggleButton(new Image("images/arrowdown.gif"), new Image("images/arrow.gif"), new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (subGroupPanel.isVisible()) {
					subGroupPanel.setVisible(false);
				} else subGroupPanel.setVisible(true);
			}
		});
		Label groupName = new Label(group.getName());
		groupName.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				History.newItem("GroupChanged"+group.getId(), true);
			}
		});
		mainGroupPanel.add(subToggle);
		mainGroupPanel.add(groupName);
		verticalPanel.add(mainGroupPanel);
		
		for (final GroupBean subGroup: subGroups) {
			Label subName = new Label(subGroup.getName().split("_")[1]);
			subName.setStylePrimaryName("subgroup");
			subGroupPanel.add(subName);
			subName.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					History.newItem("GroupChanged"+subGroup.getId(), true);
				}
			});
		}
		verticalPanel.add(subGroupPanel);
	}

}
