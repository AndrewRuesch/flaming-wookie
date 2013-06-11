import java.util.Scanner;
public class HuffmanLibraryTester {
	public static void main(String[] args) throws CloneNotSupportedException, Exception{
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter input file name: ");
		String input_file = input.next();
		System.out.print("Please enter desired output filename: ");
		String output_file = input.next();
		
		//invoke Huffman_coder to produce a coded output file
		HuffmanLibrary.Huffman_coder(input_file, output_file);
		
		System.out.println("Please check your pwd for the coded output file.");
	}
}
		
