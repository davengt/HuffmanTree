// This is a starter file that includes the read9/write9 methods described in
// the bonus assignment writeup.

//Daven Giftian Tejalaksana
//Tuesday, 1 June 2021
//CSE 143
//Instructor: Stuart Reges
//TA: Andrew Cheng
//Assignment #8(bonus)
//This program uses Huffman coding to compress, encode, and decode text files.
//HuffmanTree2 is able to eliminate the code file, 
   //so it only needs binary file to get the desired output file.

import java.util.*;
import java.io.*;

public class HuffmanTree2 {
   private HuffmanNode overallRoot; //reference to the HuffmanTree (binary tree)
   
   //Post: constructs your initial Huffman tree using the given array of frequencies 
      //where count[i] is the number of occurences of the character with integer value i.
   public HuffmanTree2(int[] count) {
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
   public HuffmanTree2(Scanner input) {
      overallRoot = new HuffmanNode(-1,-1); //initializing overallRoot (Huffman Tree)
      while (input.hasNextLine()) {
         int charIntValue = Integer.parseInt(input.nextLine());
         String code = input.nextLine();
         overallRoot = nodePlacement(overallRoot, charIntValue, code); //builds up huffman tree
      }
   }
   
   //post: private helper method of HuffmanTree(scanner input) constructor which reconstructs
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
   
   //post: contructs a huffman tree from the given input stream.
      //Assumes that the standard bit representation has been used for the tree.
   public HuffmanTree2(BitInputStream input) {
      overallRoot = readInput(input);
   }
   
   //post: private helper method of HuffmanTree2(BitInputStream input) constructor.
   private HuffmanNode readInput(BitInputStream input) {
      HuffmanNode root = null;
      if (input.readBit() == 1) {
         root = new HuffmanNode(read9(input), -1);
      } else {
         root = new HuffmanNode(-1,-1);
         root.zero = readInput(input);
         root.one = readInput(input);
      }
      return root;
   }
   
   //post: assigns codes for each character of the tree.
      //Assumes that array has null values before method is called.
      //Fills in a string for each character in the tree indicating its code.
   public void assign(String[] codes) {
      assign(overallRoot, codes, "");
   }
   
   //post: private helper method of assign(codes) which assigns a code (string) for
      //each character of the tree.
   private void assign(HuffmanNode root, String[] codes, String code) {
      if (root.zero == null && root.one == null) {
         codes[root.charIntValue] = code;
      } else {
         assign(root.zero, codes, code + "0");
         assign(root.one, codes, code + "1");
      }
   }
   
   //post: writes the current tree to the output stream using the standard bit representation.
   public void writeHeader(BitOutputStream output) {
      writeHeader(overallRoot, output);
   }
   
   //post: private helper method of writeHeader(output) which performs the task
      //of writing current tree to output stream (in standard bit representation).
   private void writeHeader(HuffmanNode root, BitOutputStream output) {
      if (root.zero == null && root.one == null) {
         output.writeBit(1);
         write9(output, root.charIntValue);
      } else {
         output.writeBit(0);
         writeHeader(root.zero, output);
         writeHeader(root.one, output);
      }
   }
   
   //Post: writes your tree to the given output stream in standard format.
   public void write(PrintStream output) {
      write(overallRoot, output, "");
   }
   
   //Post: private helper method which writes the tree line by line to given output stream.
      //Generates code (example: "1101") that will be written to the output stream 
      //alongside its integer value.
   public void write(HuffmanNode root, PrintStream output, String code) {
      if (root.one == null && root.zero == null) {
         output.println(root.charIntValue);
         output.println(code);
      } else {
         write(root.zero, output, code + "0");
         write(root.one, output, code + "1");
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

    // pre : an integer n has been encoded using write9 or its equivalent
    // post: reads 9 bits to reconstruct the original integer
   private int read9(BitInputStream input) {
      int multiplier = 1;
      int sum = 0;
      for (int i = 0; i < 9; i++) {
         sum += multiplier * input.readBit();
         multiplier = multiplier * 2;
      }
      return sum;
   }

    // pre : 0 <= n < 512
    // post: writes a 9-bit representation of n to the given output stream
   private void write9(BitOutputStream output, int n) {
      for (int i = 0; i < 9; i++) {
         output.writeBit(n % 2);
         n = n / 2;
      }
   }
}