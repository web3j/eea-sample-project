# EEA Sample Project
The project provides a simple web3j application that demonstrates the EEA features of web3j connecting to Pantheon in the Private Network.

## Functionality
This application demonstrates a number of core features of web3j and Ethereum through Pantheon nodes:

1.  Connecting to two Pantheon nodes with private transaction support;
2.  Loading the credentials associated with those nodes;
3.  Deploying a private token to the network;
4.  Transferring tokens privately between parties on the network;
5.  Viewing the state of the contract from the points of view of different nodes;


## Getting Started

To start clone the repository and import the project:

```bash
$ git clone https://github.com/web3j/eea-sample-project.git
```

## Prerequisites

To run this tutorial, you must have the following installed:

- [Docker and Docker-compose](https://docs.docker.com/compose/install/) 

- [Git command line](https://git-scm.com/)

- MacOS or Linux 
    
    !!! important 
        The Private Network Quickstart is not supported on Windows. If using Windows, run the quickstart
        inside a Linux VM such as Ubuntu. 

- [Pantheon-Quickstart](https://github.com/PegaSysEng/pantheon-quickstart) - Setup and run the private dockerized environment.
    - [Privacy](https://github.com/PegaSysEng/pantheon-quickstart/tree/4-node-example/privacy)

- [Solidity](https://solidity.readthedocs.io/en/v0.4.24/installing-solidity.html) - Solidity version between 0.4.2 < and < 0.5.

## Setup

A step by step series of  how to get an environment setup and running.
    
    
Access the folder.

```bash
$ cd eea-sample-project/
```

```bash
$ ./gradlew generateContractWrappers
```
It will generate the `HumanStandardToken`  smart contract wrapper into the project.

To execute the application.

!!! important 
        To execute `./gradlew run` you need to have all the nodes from Pantheon-Quickstart running.

```bash
$ ./gradlew run
```

Example Output
```bash
11:02:37.221 [main] INFO  org.web3j.eea.sample.Application - Alice deploying private token for {Alice, Bob}
11:02:38.961 [main] INFO  org.web3j.eea.sample.Application - Token deployed at 0x08ae6fb627eff726705d72e3705746f5353986f2 for {Alice, Bob}
11:02:38.962 [main] INFO  org.web3j.eea.sample.Application - Alice view of tokens:
11:02:42.115 [main] INFO  org.web3j.eea.sample.Application - Alice: 10
11:02:42.115 [main] INFO  org.web3j.eea.sample.Application - Bob: 0
11:02:42.115 [main] INFO  org.web3j.eea.sample.Application - Bob view of tokens:
11:02:44.443 [main] INFO  org.web3j.eea.sample.Application - Alice: 10
11:02:44.444 [main] INFO  org.web3j.eea.sample.Application - Bob: 0
11:02:44.445 [main] INFO  org.web3j.eea.sample.Application - Transferring 10 tokens from Alice to Bob
11:02:45.545 [main] INFO  org.web3j.eea.sample.Application - Alice view of tokens:
11:02:47.687 [main] INFO  org.web3j.eea.sample.Application - Alice: 0
11:02:47.687 [main] INFO  org.web3j.eea.sample.Application - Bob: 10
11:02:47.687 [main] INFO  org.web3j.eea.sample.Application - Bob view of tokens:
11:02:49.848 [main] INFO  org.web3j.eea.sample.Application - Alice: 0
11:02:49.848 [main] INFO  org.web3j.eea.sample.Application - Bob: 10
11:02:49.849 [main] INFO  org.web3j.eea.sample.Application - Transferring 1 token from Bob to Alice
11:02:50.900 [main] INFO  org.web3j.eea.sample.Application - Alice view of tokens:
11:02:53.003 [main] INFO  org.web3j.eea.sample.Application - Alice: 1
11:02:53.003 [main] INFO  org.web3j.eea.sample.Application - Bob: 9
11:02:53.003 [main] INFO  org.web3j.eea.sample.Application - Bob view of tokens:
11:02:57.098 [main] INFO  org.web3j.eea.sample.Application - Alice: 1
11:02:57.098 [main] INFO  org.web3j.eea.sample.Application - Bob: 9

BUILD SUCCESSFUL in 22s
```


## Additional Configuration (Intellij)

!!! important 
        Execute this step only if your IDE is the `Intellij`.

To mark `build` directory as `Mark Generated Sources Root` . 

Go to build > generated > source > web3j > main > java. 

```bash
build/
├──  generated/ 
    └──── source/
            └──── web3j/
                └──── main/
                        └──── java/
``` 
Right click > Mark Directory As > Mark Generated Sources Root.
