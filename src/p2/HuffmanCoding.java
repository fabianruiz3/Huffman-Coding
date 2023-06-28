package p2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import p2.DataStructures.List.List;
import p2.DataStructures.Map.HashTableSC;
import p2.DataStructures.Map.Map;
import p2.DataStructures.SortedList.SortedLinkedList;
import p2.DataStructures.SortedList.SortedList;
import p2.DataStructures.Tree.BTNode;
import p2.Utils.BinaryTreePrinter;

/**
 * The Huffman Encoding Algorithm
 * 
 * This is a data compression algorithm designed 
 * by David A. Huffman and published in 1952
 * 
 * What it does is it takes a string and by constructing 
 * a special binary tree with the frequencies of each character.
 * 
 * This tree generates special prefix codes that make the size 
 * of each string encoded a lot smaller, thus saving space.
 * 
 * @author Fabian Ruiz - fabianruiz3
 */
public class HuffmanCoding {

	/** 
	 * The main method of the program, which calls the HuffmanEncodedResult method to perform Huffman encoding and decoding.
	 * 
	 * @param args an array of command-line arguments, which are not used in this program.
	 */
	public static void main(String[] args) {
		
		HuffmanEncodedResult();
	}

	/* This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		/* You can create other test input files and add them to the inputData Folder */
		String data = load_data("input4.txt");

		/* If input string is not empty we can encode the text using our algorithm */
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);	
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else 
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 * 
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/**
			 * We create a new reader that accepts UTF-8 encoding and 
			 * extract the input string from the file, and we return it
			 */
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/**
			 * If input file is empty just return an 
			 * empty string, if not just extract the data
			 */
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) 
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return line;
	}

	/**
	 * Computes the symbol frequency distribution of each character in the input string.
	 * 
	 * @param inputString the string to analyze for symbol frequency distribution
	 * @return A map containing the symbol frequency distribution of each character in the input string
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		/* TODO Compute Symbol Frequency Distribution of each character inside input string */
		
		// Initialize the symbol frequency distribution map
		Map<String, Integer> symbolFD = new HashTableSC<String, Integer>();
		
		// Iterate through each character in the input string
		for(int i = 0; i < inputString.length(); i++) {
			
			// If the character is already in the symbol frequency distribution map, increase its frequency by 1
			if(symbolFD.containsKey(inputString.substring(i,i+1)))
				symbolFD.put(inputString.substring(i,i+1), symbolFD.get(inputString.substring(i,i+1))+1);
			
			// Otherwise, add the character to the map with a frequency of 1
			else
				symbolFD.put(inputString.substring(i,i+1), 1);
				
		}
		
		// Return the symbol frequency distribution map
		return symbolFD; 
	}

	/**
	 * Constructs a Huffman tree from a frequency distribution map.
	 * The keys in the map are the symbols to be encoded and the values
	 * are their respective frequencies. The constructed tree can be
	 * used to generate Huffman codes for each symbol.
	 * 
	 * @param fD a frequency distribution map
	 * @return The root node of the Huffman tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {

		/* TODO Construct Huffman Tree */
		
		// Initialize the root node of the tree
		BTNode<Integer, String> rootNode = null;
		
		// Create a sorted linked list of nodes from the map
		SortedList<BTNode<Integer, String>> sL = new SortedLinkedList<BTNode<Integer,String>>(); 
		for(String key: fD.getKeys()) {
			sL.add(new BTNode<Integer, String>(fD.get(key),key));
		}
		
		// If the sorted list has size one, return its only node
		if(sL.size() == 1)
			return sL.get(0);
		
		// Construct the Huffman tree by repeatedly combining the two lowest frequency nodes
		while(sL.size() > 1) {
			
			// Remove the two nodes with the lowest frequency
			BTNode<Integer, String> leftChild = sL.removeIndex(0);
			BTNode<Integer, String> rightChild = sL.removeIndex(0);
			
			 // Combine the two nodes to create a new parent node
			rootNode = new BTNode<Integer, String>(leftChild.getKey() + rightChild.getKey(), leftChild.getValue() + rightChild.getValue());
			
			
			// Assign the left and right children of the parent node
			if(leftChild.getKey().equals(rightChild.getKey()) && leftChild.getValue().compareTo(rightChild.getValue()) > 0) {
				// If the two nodes have the same frequency and the left node is lexicographically greater than the right node,
	            // assign the right node as the left child and the left node as the right child
				rootNode.setLeftChild(rightChild);
				rootNode.setRightChild(leftChild);
			}
			else {
				rootNode.setLeftChild(leftChild);
				rootNode.setRightChild(rightChild);
			}
			
			// Set the parent node of the children
			leftChild.setParent(rootNode);
			rightChild.setParent(rootNode);
			
			// Add the parent node back to the sorted list
			sL.add(rootNode);
		}
		 
		/* Use this method to see full Huffman Tree built with the generated root node */
		 BinaryTreePrinter.print(rootNode); 
		
		// Return the root node of the Huffman tree
		return rootNode;
	}

	/**
	 * Constructs a map with the codification of all nodes in the tree rooted at the given node,
	 * utilizing the Huffman Coding algorithm. The keys of the map are symbols to be encoded,
	 * and the values are the corresponding codes that represent those symbols.
	 * 
	 * @param huffmanRoot the root node of the tree
	 * @return A map with the codification of all nodes in the given tree
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		/* TODO Construct Prefix Codes */
		
		// If the root is a leaf, return a map with only one element with the value "0"
		if(huffmanRoot.getLeftChild() == null && huffmanRoot.getRightChild() == null) {
			HashTableSC<String, String> prefixCodes = new HashTableSC<String, String>();
			prefixCodes.put(huffmanRoot.getValue(), "0");
			return prefixCodes;
		}
		
		// Call the auxiliary recursive method to generate a map with the codification of all nodes in the given tree
		return recHuffmanCode(huffmanRoot, new HashTableSC<String, String>(), "");
	}

	/**
	 * Encodes a given input string using the provided encoding map. The keys of the map are the characters in the input string,
	 * and the values are the corresponding codes that represent those characters.
	 * 
	 * @param encodingMap a map containing the encoded version of each character in the input string
	 * @param inputString the string to encode 
	 * @return The encoded input string
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		/* TODO Encode String */
		
		// Initialize the encoded string
		String res = "";
		
		// Iterate through each character in the input string
		for(int i = 0; i < inputString.length(); i++) {
			
			// Retrieve the codification of the current character from the encoding map and append it to the encoded string.
			res += encodingMap.get(inputString.substring(i, i+1));
		}
		
		// Return encoded input string
		return res;
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string, 
	 * and the output string, and prints the results to the screen (per specifications).
	 * 
	 * Output Includes: symbol, frequency and code. 
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 * 
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 * 
		 * Here we have to get the bytes the same way we got the bytes 
		 * for the original one but we divide it by 8, because 
		 * 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes. 
		 * 
		 * This is because we want to calculate how many bytes we saved by 
		 * counting how many bits we generated with the encoding 
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes 
		 * will give us how much space we "chopped off".
		 * 
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%) 
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console 
		 * with a more visual pleasing version of both our 
		 * Hash Tables in decreasing order by frequency.
		 * 
		 * Notice that when the output is shown, the characters 
		 * with the highest frequency have the lowest amount of bits.
		 * 
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/**
		 * To print the table in decreasing order by frequency, 
		 * we do the same thing we did when we built the tree.
		 * 
		 * We add each key with it's frequency in a node into a SortedList, 
		 * this way we get the frequencies in ascending order
		 */
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order, 
		 * we just traverse the list backwards and start printing 
		 * the nodes key (character) and value (frequency) and find 
		 * the same key in our prefix code "Lookup Table" we made 
		 * earlier on in huffman_code(). 
		 * 
		 * That way we get the table in decreasing order by frequency
		 */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	
	/**
	 * Constructs a map with the codification of all nodes in the tree rooted at the given node.
	 * This is an auxiliary recursive method for the huffmanCode method.
	 * 
	 * @param huffmanRoot the root node of the tree
	 * @param prefixCodes an empty map that will contain the codification of all nodes in the tree that has the given root
	 * @param code an empty string that will contain the codification of the current node in the tree
	 * @return A map with the codification of all nodes in the given tree
	 */
	private static Map<String, String> recHuffmanCode(BTNode<Integer,String> huffmanRoot, Map<String, String> prefixCodes, String code) {
		/* TODO Construct Prefix Codes */
		
		// If the current node is null, backtrack to the parent node
		if(huffmanRoot == null)
			return prefixCodes;
		
		// If the current node is a leaf, add it to the codification map and backtrack to the parent node
		if(huffmanRoot.getLeftChild() == null && huffmanRoot.getRightChild() == null) {
			prefixCodes.put(huffmanRoot.getValue(), code);
			return prefixCodes;
		}
		
		// Traverse left subtree
		recHuffmanCode(huffmanRoot.getLeftChild(), prefixCodes, code + "0");
		
		// Traverse right subtree
		recHuffmanCode(huffmanRoot.getRightChild(), prefixCodes, code + "1");
		
		// Return the codification map
		return prefixCodes; 
	}

	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 * 
	 * Used for output Purposes
	 * 
	 * @param output Encoded String
	 * @param lookupTable a map containing the prefix codes and their corresponding symbols.
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/**
		 * Loop through output until a prefix code is found on map and 
		 * adding the symbol that the code that represents it to result 
		 */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { // Found it!
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;    
	}
}