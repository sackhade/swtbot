/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
package org.eclipse.swt.examples.dnd;

/**
 * Transfer type to transfer SWT ImageData objects.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class ImageTransfer extends ByteArrayTransfer {

	private static ImageTransfer	_instance	= new ImageTransfer();
	private static final int		TYPEID		= Transfer.registerType(ImageTransfer.TYPENAME);
	private static final String		TYPENAME	= "imagedata";

	public static ImageTransfer getInstance() {
		return ImageTransfer._instance;
	}

	public void javaToNative(Object object, TransferData transferData) {
		if (!checkImage(object) || !isSupportedType(transferData))
			DND.error(DND.ERROR_INVALID_DATA);
		ImageData imdata = (ImageData) object;
		try {
			// write data to a byte array and then ask super to convert to pMedium
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream writeOut = new DataOutputStream(out);
			ImageLoader loader = new ImageLoader();
			loader.data = new ImageData[] { imdata };
			loader.save(writeOut, SWT.IMAGE_BMP);
			writeOut.close();
			byte[] buffer = out.toByteArray();
			super.javaToNative(buffer, transferData);
			out.close();
		} catch (IOException e) {
		}
	}

	public Object nativeToJava(TransferData transferData) {
		if (!isSupportedType(transferData))
			return null;

		byte[] buffer = (byte[]) super.nativeToJava(transferData);
		if (buffer == null)
			return null;

		ImageData imdata;
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
			DataInputStream readIn = new DataInputStream(in);
			imdata = new ImageData(readIn);
			readIn.close();
		} catch (IOException ex) {
			return null;
		}
		return imdata;
	}

	protected int[] getTypeIds() {
		return new int[] { ImageTransfer.TYPEID };
	}

	protected String[] getTypeNames() {
		return new String[] { ImageTransfer.TYPENAME };
	}

	protected boolean validate(Object object) {
		return checkImage(object);
	}

	boolean checkImage(Object object) {
		return object != null && object instanceof ImageData;
	}
}
