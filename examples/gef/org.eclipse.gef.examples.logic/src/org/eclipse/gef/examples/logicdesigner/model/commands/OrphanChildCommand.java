/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;

public class OrphanChildCommand
	extends Command
{

private Point oldLocation;
private LogicDiagram diagram;
private LogicSubpart child;
private int index;

public OrphanChildCommand () {
	super(LogicMessages.OrphanChildCommand_Label);
}

public void execute() {
	List children = diagram.getChildren();
	index = children.indexOf(child);
	oldLocation = child.getLocation();
	diagram.removeChild(child);
}

public void redo() {
	diagram.removeChild(child);
}

public void setChild(LogicSubpart child) {
	this.child = child;
}

public void setParent(LogicDiagram parent) { 
	diagram = parent;
}

public void undo() {
	child.setLocation(oldLocation);
	diagram.addChild(child, index);
}

}
