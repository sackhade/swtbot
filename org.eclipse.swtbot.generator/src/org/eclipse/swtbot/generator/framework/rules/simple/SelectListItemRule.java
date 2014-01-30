/*******************************************************************************
 * Copyright (c) 2014 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Mickael Istria (Red Hat Inc.) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.framework.rules.simple;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class SelectListItemRule extends GenerationSimpleRule {

	private int index;
	boolean useIndices = false;
	private String[] selectedItems;
	private int[] selectedIndices;

	@Override
	public boolean appliesTo(Event event) {
		return event.widget instanceof org.eclipse.swt.widgets.List
				&& event.type == SWT.Selection;
	}

	@Override
	public void initializeForEvent(Event event) {
		org.eclipse.swt.widgets.List list = (org.eclipse.swt.widgets.List)event.widget;
		this.index = WidgetUtils.getIndex(list);
		this.selectedIndices = list.getSelectionIndices();
		this.selectedItems = list.getSelection();
		for (String selectedItem : list.getSelection()) {
			int nbOccurrences = 0;
			for (String item : list.getItems()) {
				if (item.equals(selectedItem)) {
					nbOccurrences++;
				}
			}
			if (nbOccurrences > 1) {
				this.useIndices = true;
			}
		}
	}


	@Override
	public List<String> getActions() {
		List<String> actions = new ArrayList<String>();
		StringBuilder code = new StringBuilder();

		code.append("bot.list("); //$NON-NLS-1$
		if (this.index != 0) {
			code.append(this.index);
		}
		code.append(")"); //$NON-NLS-1$

		code.append(".select(");  //$NON-NLS-1$
		if (this.useIndices) {
			for (int i = 0; i < this.selectedIndices.length; i++) {
				code.append(this.selectedIndices[i]);
				if (i < this.selectedIndices.length - 1) {
					code.append(", ");  //$NON-NLS-1$
				}
			}
		} else {
			for (int i = 0; i < this.selectedItems.length; i++) {
				code.append('"');
				code.append(this.selectedItems[i]);
				code.append('"');
				if (i < this.selectedIndices.length - 1) {
					code.append(", "); //$NON-NLS-1$
				}
			}
		}
		code.append(')');

		actions.add(code.toString());
		return actions;
	}

	@Override
	public List<String> getImports() {
		// TODO Auto-generated method stub
		return null;
	}

}