package com.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;

public class BlockChain 
{
    public static ArrayList<Block> blockChain = new ArrayList<>();
    public static int difficulty = 5;
    public static void main( String[] args )
    {
        blockChain.add(new Block("This is the first Block!!", "0"));
        System.out.println("Trying to Mine block 1... ");
        blockChain.get(0).mineBlock(difficulty);

        blockChain.add(new Block("This is the Second Block!!",blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to Mine block 2... ");
        blockChain.get(1).mineBlock(difficulty);

		blockChain.add(new Block("This is the Third Block!!",blockChain.get(blockChain.size()-1).hash));
        System.out.println("Trying to Mine block 3... ");
        blockChain.get(2).mineBlock(difficulty);

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockChainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockChain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockChainJson);
    }

    public static Boolean isChainValid(){
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i=1; i < blockChain.size();i++){
            currentBlock = blockChain.get(i);
		    previousBlock = blockChain.get(i-1);

            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");			
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}
        }
        return true;
    }

    public class Wallet {
	    public PrivateKey privateKey;
	    public PublicKey publicKey;
        
        public Wallet(){
            generateKeyPair();	
        }

        public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
	        	KeyPair keyPair = keyGen.generateKeyPair();
	        	// Set the public and private keys from the keyPair
	        	privateKey = keyPair.getPrivate();
	        	publicKey = keyPair.getPublic();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
    }
}
