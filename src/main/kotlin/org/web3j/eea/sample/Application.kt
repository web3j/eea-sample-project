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
import org.web3j.protocol.eea.Eea
import org.web3j.protocol.http.HttpService
import org.web3j.tx.EeaTransactionManager
import org.web3j.tx.gas.EeaGasProvider
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

class Application {

    val nodeAlice = Eea.build(HttpService("http:localhost:20000"))
    val nodeBob = Eea.build(HttpService("http:localhost:20002"))

    val enclaveKeyAlice = "A1aVtMxLCUHmBVHXoZzzBgPbW/wj5axDpW9X8l91SGo="
    val enclaveKeyBob = "Ko2bVqD+nNlNYL5EE7y3IdOnviftjiizpjRt+HTuFBs="

    val alice = Credentials
        .create("8f2a55949038a9610f50fb23b5883af3b4ecb3c3bb792cbcefbd1542c692be63")
    val bob = Credentials
        .create("c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3")

    val tmAlice = EeaTransactionManager(
        nodeAlice, alice,
        2018, enclaveKeyAlice, listOf(enclaveKeyBob)
    )

    val tmBob = EeaTransactionManager(
        nodeBob, bob,
        2018, enclaveKeyBob, listOf(enclaveKeyAlice)
    )

    val tokenAlice: HumanStandardToken
    val tokenBob: HumanStandardToken

    init {
        logger.info { "Alice deploying private token for {Alice, Bob}" }
        tokenAlice = HumanStandardToken.deploy(
            nodeAlice, tmAlice, EeaGasProvider(BigInteger.valueOf(5000)),
            BigInteger.TEN, "eea_token",
            BigInteger.TEN, "EEATKN"
        ).send()
        logger.info { "Token deployed at ${tokenAlice.contractAddress} for {Alice, Bob}" }
        tokenBob = HumanStandardToken.load(
            tokenAlice.contractAddress,
            nodeBob, tmBob, EeaGasProvider(BigInteger.valueOf(5000))
        )
    }

    fun printTokenBalanceViewsToTerminal() {
        logger.info { "Alice view of tokens:" }
        val aliceAlice = tokenAlice.balanceOf(alice.address).send()
        val aliceBob = tokenAlice.balanceOf(bob.address).send()
        logger.info { "Alice: $aliceAlice" }
        logger.info { "Bob: $aliceBob" }
        logger.info { "Bob view of tokens:" }
        val bobAlice = tokenBob.balanceOf(alice.address).send()
        val bobBob = tokenBob.balanceOf(bob.address).send()
        logger.info { "Alice: $bobAlice" }
        logger.info { "Bob: $bobBob" }
    }
}

fun main() {

    val app = Application()

    app.printTokenBalanceViewsToTerminal()

    logger.info { "Transferring 10 tokens from Alice to Bob" }
    app.tokenAlice.transfer(app.bob.address, BigInteger.TEN).send()

    app.printTokenBalanceViewsToTerminal()

    logger.info { "Transferring 1 token from Bob to Alice" }
    app.tokenBob.transfer(app.alice.address, BigInteger.ONE).send()

    app.printTokenBalanceViewsToTerminal()
}
