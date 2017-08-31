package de.rcblum.stream.deck;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import purejavahidapi.HidDevice;
import purejavahidapi.HidDeviceInfo;
import purejavahidapi.InputReportListener;
import purejavahidapi.PureJavaHidApi;

/**
 * 
 * 
 * MIT License
 * 
 * Copyright (c) 2017 Roland von Werden
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author Roland von Werden
 * @version 0.1
 *
 */
public class HidDevices {
	
	public static final int VENDOR_ID = 4057;
	public static final int PRODUCT_ID = 96;

	private static List<HidDeviceInfo> STREAM_DECK_INFOS = null;

	private static List<HidDevice> STREAM_DECK_DEVICES = null;

	private static List<StreamDeck> STREAM_DECKS = null;
	
	
	public static HidDeviceInfo getStreamDeckInfo() {
		if (STREAM_DECK_INFOS == null) {
			STREAM_DECK_INFOS = new ArrayList<>(5);
			System.out.println("scanning");
			List<HidDeviceInfo> devList = PureJavaHidApi.enumerateDevices();
			for (HidDeviceInfo info : devList) {
				System.out.println("Vendor-ID: " + info.getVendorId() + ", Product-ID: " + info.getProductId());
				if (info.getVendorId() == VENDOR_ID && info.getProductId() == PRODUCT_ID) {
					STREAM_DECK_INFOS.add(info);
				}
			}
		}
		return STREAM_DECK_INFOS.size() > 0 ? STREAM_DECK_INFOS.get(0) : null;
	}
	
	public static HidDevice getStreamDeckDevice() {
		if (STREAM_DECK_DEVICES == null || STREAM_DECK_DEVICES.size() == 0) {
			HidDeviceInfo info = getStreamDeckInfo();
			STREAM_DECK_DEVICES = new ArrayList<>(STREAM_DECK_INFOS.size());
			if (info != null) {
				try {
					System.out.println();
					System.out.println("Connected Stream Decks:");
					for (HidDeviceInfo hidDeviceinfo : STREAM_DECK_INFOS) {
						System.out.println("  Manufacurer: " + hidDeviceinfo.getManufacturerString());
						System.out.println("  Product:     " + hidDeviceinfo.getProductString());
						System.out.println("  Device-Id:   " + hidDeviceinfo.getDeviceId());
						System.out.println("  Serial-No:   " + hidDeviceinfo.getSerialNumberString());
						System.out.println("  Path:        " + hidDeviceinfo.getPath());
						System.out.println();
						STREAM_DECK_DEVICES.add(PureJavaHidApi.openDevice(hidDeviceinfo));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return STREAM_DECK_DEVICES.size() > 0 ? STREAM_DECK_DEVICES.get(0) : null;
	}
	
	public static StreamDeck getStreamDeck() {
		if (STREAM_DECKS == null || STREAM_DECKS.size() == 0) {
			HidDevice dev = getStreamDeckDevice();
			STREAM_DECKS = new ArrayList<>(STREAM_DECK_DEVICES.size());
			if (dev != null) {
				for (HidDevice hidDevice : STREAM_DECK_DEVICES) {
					STREAM_DECKS.add(new StreamDeck(hidDevice, 99));
				}
			}
		}
		return STREAM_DECKS.size() > 0 ? STREAM_DECKS.get(0) : null;
	}
	
	public static int getStreamDeckSize() {
		return STREAM_DECKS != null ? STREAM_DECKS.size() : 0;
	}
	
	public static StreamDeck getStreamDeck(int id) {
		if (STREAM_DECKS == null || id < 0 || id >= getStreamDeckSize())
			return null;
		return STREAM_DECKS.get(id);
	}
	
	public static String bytesToHex(byte[] in) {
	    final StringBuilder builder = new StringBuilder();
	    builder.append("{");
	    for(byte b : in) {
	        builder.append(String.format(" 0x%02x,", b));
	    }
	    builder.append("}");
	    return builder.toString();
	}
	
	
}
 