/*
 * Copyright 2019 WEB3 LABS LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.eea.sample

import mu.KotlinLogging
import org.web3j.crypto.Credentials
import org.web3j.humanstandardtoken.HumanStandardToken
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.eea.Eea
import org.web3j.protocol.eea.tx.EeaTransactionManager
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

/**
 * A simple web3j application that demonstrates the EEA features of web3j:
 *
 * <ol>
 *     <li>Connecting to a Pantheon the Ethereum network</li>
 *     <li>Loading an Ethereum wallet file</li>
 *     <li>Deploying a private token to the network</li>
 *     <li>Transferring tokens privately between parties on the network</li>
 *     <li>Viewing events from the point of view of different parties on the network</li>
 * </ol>
 *
 * <p>To run this demo, you will need to provide:
 *
 * <ol>
 *     <li>Pantheon client (or node) endpoint. The simplest thing to do is
 *     <a href="https://infura.io/register.html">Use the pantheon/quickstart private dockerized environment</a></li>
 *     <li>A wallet file. This can be generated using the web3j
 *     <a href="https://docs.web3j.io/command_line.html">command line tools</a></li>
 *     <li>Some Ether. This can be requested from the
 *     <a href="https://www.rinkeby.io/#faucet">Rinkeby Faucet</a></li>
 * </ol>
 *
 * <p>For further background information, refer to the project README.
 */

private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) {

    val eea = Eea.build(HttpService("http:localhost:22000"))
    val eea1 = Eea.build(HttpService("http:localhost:22001"))

    val credentials = Credentials
        .create("8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63")
    val credentials1 = Credentials
        .create("8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63")

    val eeatm = EeaTransactionManager(eea, credentials, "", listOf())

    val hst = HumanStandardToken.deploy(
        eea, eeatm, DefaultGasProvider(),
        BigInteger.ZERO, "eea_token",
        BigInteger.ZERO, "EEATKN").send()

    hst.transferEventFlowable(
        DefaultBlockParameterName.LATEST,
        DefaultBlockParameterName.LATEST)
        .subscribe { transfer ->
            logger.debug("Transfer from {} to {} of value {}",
                transfer._from, transfer._to, transfer._value)
        }

    val hst1 = HumanStandardToken.load(
        hst.contractAddress, eea1, credentials1, DefaultGasProvider())

    hst1.transferEventFlowable(
        DefaultBlockParameterName.LATEST,
        DefaultBlockParameterName.LATEST)
        .subscribe { transfer ->
            logger.debug("Transfer from {} to {} of value {}",
                transfer._from, transfer._to, transfer._value)
        }

    hst.transfer(credentials1.address, BigInteger.ZERO)
}
