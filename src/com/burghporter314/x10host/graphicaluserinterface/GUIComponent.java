package com.burghporter314.x10host.graphicaluserinterface;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.burghporter314.x10host.indigenoustweets.Language;
import com.burghporter314.x10host.indigenoustweets.IndigenousUser;

public class GUIComponent extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JList<String> list;
	
	private JLabel label;
	private JButton actionButton;
	
	private JScrollPane pane;
	private static List<String> selectedValuesList;
	
	private Object[] items;
	private boolean listenersSet = false;
	
	public GUIComponent() {
		
		this.setTitle("Indigenous Web Crawler");
		this.setSize(300,400);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
	}
	
	public void setListComponents(Object[] items, String message) {
		
		if(this.label != null) {
			this.remove(this.label);
			this.remove(this.actionButton);
			this.remove(this.pane);
		}
		
		DefaultListModel<String> listModel = new DefaultListModel<>();
		this.items = items;
		
		if(items[0] instanceof Language) {
			for(Language language: (Language[])items) {
				listModel.addElement(language.getLanguage());
			}
			
		} else {
			for(Object user:  items) {
				listModel.addElement(((IndigenousUser) user).getUserUrl().replace("http://twitter.com/",""));
			}
		}
		
		list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		this.label = new JLabel(message);
		this.label.setAlignmentX(Component.RIGHT_ALIGNMENT);
		
		this.pane = new JScrollPane(list);
		this.actionButton = new JButton("Submit");
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.ipady = 0;
		
		this.add(label, constraints);
		
		constraints.gridwidth = 3;
		constraints.weightx = 1;
		constraints.ipady = 150;
		constraints.fill = GridBagConstraints.BOTH;

		constraints.gridx = 0;
		constraints.gridy = 1;
		
		this.add(pane, constraints);
		
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.ipady = 0;
		
		this.add(actionButton, constraints);
		
		this.pack();
		
		this.setSize(300,400);
		this.setVisible(true);
		
		setComponentListeners();			
	}
	
	private void setComponentListeners() {
		
		list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
            	
                if (!e.getValueIsAdjusting()) {
                    GUIComponent.selectedValuesList = list.getSelectedValuesList();
                    System.out.println(selectedValuesList);
                }
            }
        });
		
		actionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(selectedValuesList);
				setListComponents(getListFromResults(), "Select Users to Open");
			}
		});
		
		listenersSet = true;
	}
	
	private Object[] getListFromResults() {
		
		ArrayList<IndigenousUser> userList = new ArrayList<IndigenousUser>();
		
		for(Object language: this.items) {
			if(this.selectedValuesList.contains(((Language) language).getLanguage())) {
				IndigenousUser[] temp = ((Language) language).getCorrespondingUsers();
				for(int i = 0; i < temp.length; i++) {
					userList.add(temp[i]);
				}
			}
		}
		
		return userList.toArray();
	}
	
	
}
