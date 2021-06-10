//Daven Giftian Tejalaksana
//Sunday, 30 May 2021
//CSE 143
//Instructor: Stuart Reges
//TA: Andrew Cheng
//Assignment #8
//This program uses a coding scheme called Huffman coding.
//It compresses text files using a coding scheme based on the frequency of characters,
   //using the Huffman tree, and also for decoding binary files into text files.
//It is also able to write the Huffman Tree to an output file (in standard format), vice versa.

import java.util.*;
import java.io.*;

public class HuffmanTree {
   private HuffmanNode overallRoot; //reference to the HuffmanTree (binary tree)
   
   //Post: constructs your initial Huffman tree using the given array of frequencies 
      //where count[i] is the number of occurences of the character with integer value i.
   public HuffmanTree(int[] count) {
      Queue<HuffmanNode> characterOrganizer = new PriorityQueue<>();
      for (int i = 0; i < count.length; i++) {
         if (count[i] > 0) {
            characterOrganizer.add(new HuffmanNode(i, count[i]));
         }
      }
      characterOrganizer.add(new HuffmanNode(count.length, 1));//pseudo-eof character      
   
      overallRoot = createHuffmanTree(characterOrganizer);//initialize Huffman Tree (overallRoot)
   }
   
   //Post: private helper method which helps sorts and creates the Huffman tree
      // from a queue of HuffmanNodes.
   private HuffmanNode createHuffmanTree(Queue<HuffmanNode> characterOrganizer) {
      while (characterOrganizer.size() != 1) {
         HuffmanNode firstValue = characterOrganizer.remove();
         HuffmanNode secondValue = characterOrganizer.remove();
         int totalFrequency = firstValue.frequency + secondValue.frequency;
         characterOrganizer.add(new HuffmanNode(-1, totalFrequency, firstValue, secondValue));
      }
      return characterOrganizer.remove();
   }
   
   //post: reconstructs the tree from a file, assuming that Scanner input contains a tree
      //stored in standard format.
   public HuffmanTree(Scanner input) {
      overallRoot = new HuffmanNode(-1,-1); //initializing overallRoot (Huffman Tree)
      while (input.hasNextLine()) {
         int charIntValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = nodePlacement(overallRoot, charIntValue, code); //builds up huffman tree
      }
   }
   
   //post: private helper method of HuffmanTree(Scanner input) constructor which reconstructs
      //the tree using information stored in file (in standard format).
   private HuffmanNode nodePlacement(HuffmanNode root, int charIntValue, String code) {
      if (code.length() == 0) { //reaches the end of the tree (base case)
         root = new HuffmanNode(charIntValue, -1);
      } else {
         if (code.charAt(0) == '0') { //go to zero branch
            if (root.zero == null) { //check if there is a node 
               root.zero = new HuffmanNode(-1,-1);
            }
            root.zero = nodePlacement(root.zero, charIntValue, code.substring(1));     
         } else { //go to one branch
            if (root.one == null) { //check if there is a node 
               root.one = new HuffmanNode(-1,-1);
            }
            root.one = nodePlacement(root.one, charIntValue, code.substring(1));
         }
      }
      return root;
   }
   
   //Post: writes your Huffman tree to the given output stream in standard format.
   public void write(PrintStream output) {
      write(overallRoot, output, "");
   }
   
   //Post: private helper method which writes the tree line by line to given output stream.
      //Generates code that will be written to the output stream 
         //alongside its integer value (integer value printed before code).
   public void write(HuffmanNode root, PrintStream output, String code) {
      if (root != null) {
         if (root.zero == null && root.one == null) {
            output.println(root.charIntValue);
            output.println(code);
         } else {
            write(root.zero, output, code + "0");
            write(root.one, output, code + "1");
         }
      }
   }
   
   //post: read individual bits from the input stream and should write corresponding
      //characters to the output.
      //Should stop reading when it encounters a character with value equal to eof parameter,
         //which is a pseudo-eof character (not written to the PrintStream).
      //Assume that the input stream contains a legal encoding of characters for this tree's
         //Huffman code.
   public void decode(BitInputStream input, PrintStream output, int eof) {
      boolean notEof = true;
      while (notEof) {
         HuffmanNode root = overallRoot;
         while (root.zero != null && root.one != null) { //finds a leaf node
            int code = input.readBit();
            if (code == 0) {
               root = root.zero;
            } else {
               root = root.one;
            }
         }
         if (root.charIntValue == eof) {
            notEof = false;
         } else {
            output.write(root.charIntValue);
         }
      }
   }
}