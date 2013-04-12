package edu.unsw.streaming.client.panel;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

import edu.unsw.streaming.bean.GroupBean;
import edu.unsw.streaming.client.StreamED;
import edu.unsw.streaming.service.GroupServiceAsync;

/**
 * Container to display all groups
 * @author Lianne
 *
 */
public class GroupPanel extends Composite {
	private final GroupServiceAsync groupService = StreamED.groupService;

	private VerticalPanel verticalPanel;

	public GroupPanel() {

		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);

		groupService.viewGroups(StreamED.user.getId(),
				new AsyncCallback<List<GroupBean>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Error encountered from getting grouplist");
					}

					@Override
					public void onSuccess(List<GroupBean> result) {
						Label title = new Label("Groups");
						title.setStylePrimaryName("h1");
						verticalPanel.add(title);
						final List<GroupBean> allGroups = result;
						groupService.getMainGroups(allGroups,
								new AsyncCallback<List<GroupBean>>() {

									@Override
									public void onFailure(Throwable caught) {
										Window.alert("Error encountered from getting main groups");
									}

									@Override
									public void onSuccess(List<GroupBean> result) {
										Label all = new Label("All Groups");
										all.addClickHandler(new ClickHandler() {

											@Override
											public void onClick(ClickEvent event) {
												History.newItem("LoginSuccess");
											}
											
										});
										verticalPanel.add(all);
										for (final GroupBean mainGroup : result) {
											groupService.getSubGroups(
															allGroups,
															mainGroup,
															new AsyncCallback<List<GroupBean>>() {

																@Override
																public void onFailure(
																		Throwable caught) {
																	Window.alert("Error encountered from getting subgroups");
																}

																@Override
																public void onSuccess(
																		List<GroupBean> result) {
																	verticalPanel
																			.add(new GroupPanelIndividual(
																					mainGroup,
																					result));
																}

															});

										}
									}

								});
					}

				});

	}

}
