/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertEnabled;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertNotEnabled;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable.syncExec;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.utils.Traverse;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBot2Test extends AbstractControlExampleTest {

	@Test
	public void findsTextBox() throws Exception {
		UIThreadRunnable.syncExec(display, new VoidResult() {
			@Override
			public void run() {
				Shell shell2 = new Shell(display);
				shell2.setText("Hello Shell");
				shell2.setLayout(new FillLayout());
				new Label(shell2, SWT.NONE).setText("My TextBox");
				new Text(shell2, SWT.NONE).setText("Hello World");
				shell2.open();
			}
		});

		try {
			bot.shell("SWT Controls").activate();
			bot.shell("Hello Shell").activate();
			SWTBotText text = bot.textWithLabel("My TextBox");
			assertEquals("Hello World", text.getText());
			text.setText("good bye world");
			assertEquals("good bye world", text.getText());
		} finally {
			// a hacked tear down
			UIThreadRunnable.syncExec(display, new VoidResult() {
				@Override
				public void run() {
					try {
						bot.shell("Hello Shell").widget.dispose();
					} catch (WidgetNotFoundException e) {
						// do nothing
					}
				}
			});
		}
	}

	@Test
	public void isEnabled() throws Exception {
		assertEnabled(bot.radio("Left"));
		assertEnabled(bot.button("Clear"));

		assertNotEnabled(bot.radio("Up"));
		assertNotEnabled(bot.radio("Down"));
	}

	@Test
	public void getsActiveControl() throws Exception {
		bot.button("Two").setFocus();
		assertFalse(bot.button("One").isActive());
		assertTrue(bot.button("Two").isActive());

		assertSameWidget(bot.button("Two").widget, bot.getFocusedWidget());
	}

	@Test
	public void tabKeyTraversalSetsFocusOnTheNextControlAndSendsTraverseEvents() throws Exception {
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		SWTBotButton buttonOne = bot.button("One");
		buttonOne.setFocus();
		assertTrue(buttonOne.isActive());
		buttonOne.traverse(Traverse.TAB_NEXT);

		SWTBotText textInGroup = bot.textInGroup("Listeners");
		assertEventMatches(textInGroup, "Traverse [31]: TraverseEvent{Button {One} time=60232779 data=null character=" + toCharacter('\0', buttonOne.widget) + " keyCode=" + toKeyCode(0, buttonOne.widget) + " stateMask=" + toStateMask(0, buttonOne.widget) + " doit=true detail=16}");
		assertEventMatches(textInGroup, "FocusOut [16]: FocusEvent{Button {One} time=60232779 data=null}");
		assertEventMatches(textInGroup, "FocusIn [15]: FocusEvent{Button {Two} time=60232779 data=null}");
		assertTrue(bot.button("Two").isActive());
	}

	@Test
	public void findsGetsIdOfAControl() throws Exception {
		final SWTBotButton button = bot.button("Two");
		assertNull(button.getId());
		syncExec(new VoidResult() {
			@Override
			public void run() {
				button.widget.setData(SWTBotPreferences.DEFAULT_KEY, "foo");
			}
		});
		assertEquals("foo", button.getId());
	}

	public void prepareExample() throws Exception {
		bot.tabItem("Button").activate();
	}

}
