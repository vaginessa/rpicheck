/**
 * Copyright (C) 2017  RasPi Check Contributors
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
/**
 *
 */
package de.eidottermihi.rpicheck.ssh.impl;

import net.schmizz.sshj.transport.verification.HostKeyVerifier;

import java.security.PublicKey;

/**
 * Always returns true.
 *
 * @author Michael
 */
public class NoHostKeyVerifierImplementation implements HostKeyVerifier {

    /*
     * (non-Javadoc)
     *
     * @see
     * net.schmizz.sshj.transport.verification.HostKeyVerifier#verify(java.lang
     * .String, int, java.security.PublicKey)
     */
    public boolean verify(String hostname, int port, PublicKey key) {
        return true;
    }

}
