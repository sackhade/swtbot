/*******************************************************************************
 * Copyright (c) 2010, 2017 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.matchers;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Widget;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class MessageMatcherTest {

	@Test
	public void doesNotMatchObjectsWithNoGetMessageMethod() throws Exception {
		Matcher<Widget> matcher = withMessage("Some Text");
		assertFalse(matcher.matches(new Object()));
	}

	@Test
	public void doesNotMatchObjectsWithNullGetMessage() throws Exception {
		Matcher<Widget> matcher = withMessage("Some Text");
		assertFalse(matcher.matches(new ObjectWithGetMessage(null)));
	}

	@Test
	public void doesNotMatchMessage() throws Exception {
		Matcher<Widget> matcher = withMessage("Some Text");
		assertFalse(matcher.matches(new ObjectWithGetMessage("Some Other Text")));
	}

	@Test
	public void matchMessage() throws Exception {
		Matcher<Widget> matcher = withMessage("Some Text");
		assertTrue(matcher.matches(new ObjectWithGetMessage("Some Text")));
	}


	@Test
	public void getsToString() throws Exception {
		Matcher<Widget> matcher = withMessage("Some Text");
		assertEquals("with message 'Some Text'", matcher.toString());
	}

}
