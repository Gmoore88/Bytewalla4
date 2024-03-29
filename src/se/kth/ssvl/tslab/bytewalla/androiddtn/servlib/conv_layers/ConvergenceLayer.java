/*
 *	  This file is part of the Bytewalla Project
 *    More information can be found at "http://www.tslab.ssvl.kth.se/csd/projects/092106/".
 *    
 *    Copyright 2009 Telecommunication Systems Laboratory (TSLab), Royal Institute of Technology, Sweden.
 *    
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *    
 */

package se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.conv_layers;

import java.util.Iterator;

import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.bundling.Bundle;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.bundling.BundleDaemon;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.bundling.event.CLAParamsSetEvent;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.contacts.AttributeNameVector;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.contacts.AttributeVector;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.contacts.Contact;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.contacts.Interface;
import se.kth.ssvl.tslab.bytewalla.androiddtn.servlib.contacts.Link;
import android.util.Log;

/**
 * The abstract interface for a convergence layer
 * 
 * @author Mar�a Jos� Peroza Marval (mjpm@kth.se).
 */

public abstract class ConvergenceLayer {

	/**
	 * TAG for Android Logging mechanism
	 */
	final String TAG = "ConvergenceLayer";

	/**
	 * Set CL-specific options.
	 */
	public boolean set_cla_parameters(AttributeVector params) {

		Log.d(TAG, "set cla parameters");
		BundleDaemon Daemon = BundleDaemon.getInstance();
		Daemon.post(new CLAParamsSetEvent(this, ""));
		return true;
	}

	/**
	 * Set default interface options.
	 */
	public boolean set_interface_defaults(int argc) {
		return true;
		// Not implemented
	}

	/**
	 * Set default link options.
	 */
	public boolean set_link_defaults(int argc) {
		return true;
		// Not implemented
	}

	/**
	 * Bring up a new interface.
	 */
	public boolean interface_up(Interface iface) {
		return true;
		// Not implemented
	}

	/**
	 * Bring down the interface.
	 */
	public boolean interface_down(Interface iface) {
		return true;
		// Not implemented yet
	}

	/**
	 * Dump out CL specific interface information.
	 */
	public void dump_interface(Interface iface, StringBuffer buf) {
	}

	/**
	 * Create any CL-specific components of the Link.
	 */
	public abstract boolean init_link(Link link);

	/**
	 * Parse the information of the nexthop (IPadress and port)
	 */
	public abstract boolean parse_nexthop(Link link, LinkParams params);

	/**
	 * Delete any CL-specific components of the link (requests pertaining to
	 * this link must be ignored gracefully by the CL in the future).
	 */
	public void delete_link(Link link) {
	}

	/**
	 * Dump out CL specific link information.
	 */
	public void dump_link(Link link, StringBuffer buf) {
	}

	/**
	 * Post-initialization, parse any CL-specific options for the link.
	 */
	public boolean reconfigure_link(Link link, int argc, String argv) {
		return true;
	}

	public void reconfigure_link(Link link, AttributeVector params) {
	}

	/**
	 * "Open a new contact for the given link. The implementation will create a
	 * new Contact object (or find one that already exists), establish any CL
	 * specific connections, then post a ContactUpEvent when the contact is
	 * successfully initiated" [DTN2].
	 */
	public abstract boolean open_contact(Contact contact);

	/**
	 * "Close the open contact.
	 * 
	 * Mainly used to clean the state that is associated with this contact. This
	 * is called by the link->close() function.
	 * 
	 * Note that this function should NOT post a ContactDownEvent, as this
	 * function is only called to clean up the contact state after it has been
	 * closed (i.e. after the ContactDownEvent has been generated by some other
	 * part of the system)"[DTN2].
	 */
	public abstract boolean close_contact(Contact contact);

	/**
	 * "Notify the convergence layer that a bundle was queued on the link.
	 * 
	 * In some cases (e.g. TCP) this just kicks the other thread to notice that
	 * there are bundles to send out. In others (e.g. UDP) there is no
	 * per-contact thread, so this callback is used to send the bundle" [DTN2].
	 */
	public abstract void bundle_queued(Link link, Bundle bundle);

	/**
	 * Try to cancel transmission of a given bundle on the link.
	 */
	public void cancel_bundle(Link link, Bundle bundle) {
	}

	/**
	 * Report if the given endpoint is reachable via the given interface.
	 */
	public void is_eid_reachable(String query_id, Interface iface,
			String endpoint) {
	}

	/**
	 * Report the values of the given link attributes.
	 */
	public void query_link_attributes(String query_id, Link link,
			AttributeNameVector attributes) {
	}

	/**
	 * Report the values of the given interface attributes.
	 */
	public void query_iface_attributes(String query_id, Interface iface,
			AttributeNameVector attributes) {
	}

	/**
	 * Report the values of the convergence layer parameters.
	 */
	public void query_cla_parameters(String query_id,
			AttributeNameVector parameters) {
	}

	/**
	 * Perform any necessary shutdown procedures.
	 */
	public void shutdown() {
	}

	/**
	 * Boot-time initialization and registration of statically defined
	 * convergence layers.
	 */
	public static void init_clayers() {

		add_clayer(new TCPConvergenceLayer("tcp"));
	}

	public static void add_clayer(ConvergenceLayer cl) {

		CLVector.getInstance().add(cl);

	}

	/**
	 * Find the appropriate convergence layer for the given string.
	 */

	public static ConvergenceLayer find_clayer(String proto) {

		Iterator<ConvergenceLayer> iter = CLVector.getInstance().iterator();

		while (iter.hasNext()) {
			ConvergenceLayer element = iter.next();
			if (proto.compareTo(element.name()) == 0) {
				return element;
			}
		}

		return null;
	}

	/**
	 * Shutdown all registerd convergence layers.
	 */
	public static void shutdown_clayers() {

		Iterator<ConvergenceLayer> iter = CLVector.getInstance().iterator();

		while (iter.hasNext()) {
			iter.next().shutdown();
		}

	}

	/**
	 * Accessor for the convergence layer name.
	 */
	public String name() {
		return name_;
	}

	// Test case

	public String Testname(String name) {

		name_ = name;
		return name_;
	}

	public abstract void set_local_port(short port);

	/**
	 * Magic number used for DTN convergence layers
	 */
	public static int MAGIC = 0x64746e21; // 'DTN!'

	/**
	 * The unique name of this convergence layer.
	 */
	protected String name_;

}
