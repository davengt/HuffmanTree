//Daven Giftian Tejalaksana
//Sunday, 30 May 2021
//CSE 143
//Instructor: Stuart Reges
//TA: Andrew Cheng
//Assignment #8
//This program creates a specialized tree node class for HuffmanTree.java.

public class HuffmanNode implements Comparable<HuffmanNode> {
   public int charIntValue; //character integer value stored at each binary tree node
   public int frequency; //frequency value stored at each binary tree node
   public HuffmanNode zero; //zero branch of the tree node (reference to zero subtree)
   public HuffmanNode one; //one branch of the tree node (reference to one subtree)
   
   //post: constructs a leaf node with given charIntValue and frequency
   public HuffmanNode(int charIntValue, int frequency) {
      this(charIntValue, frequency, null, null);
   }
   
   //post: constructs a branch node with given charIntValue, frequency, zero subtree, and one subtree.
   public HuffmanNode(int charIntValue, int frequency, HuffmanNode zero, HuffmanNode one) {
      this.charIntValue = charIntValue;
      this.frequency = frequency;
      this.zero = zero;
      this.one = one;
   }
   
   //post: a method part of the implemented Comparable<HuffmanNode> interface.
      //It uses frequency of the subtree to determine its ordering relative to other subtrees,
         //with lower frequencies considered "less" than higher frequencies.
      //If two frequencies are equal, the nodes should be considered equal.
   public int compareTo(HuffmanNode other) {
      return this.frequency - other.frequency;
   }
}