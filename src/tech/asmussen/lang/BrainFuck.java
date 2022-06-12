package tech.asmussen.lang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * BrainFuck is an esoteric programming language and is not designed to create real software.
 *
 * @author Bastian A. W. Asmussen (BastianA)
 * @version 1.0.0
 * @see #MEMORY_SIZE
 * @see #pointer
 * @see #brackets
 * @see #readFile(String)
 * @see #interpretCode(String)
 * @see #reset()
 */
public class BrainFuck {
	
	/**
	 * How many cells are allocated to the memory of the program.
	 *
	 * @see #interpretCode(String)
	 * @since 1.0.0
	 */
	private static final int MEMORY_SIZE = 30_000;
	
	/**
	 * The memory of the program.
	 *
	 * @see #interpretCode(String)
	 * @see #MEMORY_SIZE
	 * @since 1.0.0
	 */
	private final byte[] memory = new byte[MEMORY_SIZE];
	
	/**
	 * The current position in the memory.
	 *
	 * @see #interpretCode(String)
	 * @see #MEMORY_SIZE
	 * @since 1.0.0
	 */
	private int pointer = 0;
	
	/**
	 * How many brackets are open in the current code.
	 *
	 * @see #interpretCode(String)
	 * @since 1.0.0
	 */
	private int brackets = 0;
	
	public static void main(String[] args) {
		
		if (args.length == 0 || "--help".equalsIgnoreCase(args[0]) || "-h".equalsIgnoreCase(args[0])) {
			
			System.out.println("Usage: java -jar BrainFuck.jar [instruction]");
			System.out.println("\t| --file path/to/file.bf: Reads the code from a file.");
			System.out.println("\t| --code: Reads the code from the console.");
			System.out.println("\t| --help: Shows this help message.");
			
			return;
		}
		
		BrainFuck brainFuck = new BrainFuck();
		
		if ("--file".equalsIgnoreCase(args[0]) || "-f".equalsIgnoreCase(args[0])) {
			
			if (args.length == 1)
				
				throw new RuntimeException("Usage: java -jar BrainFuck.jar --file path/to/file.bf");
			
			try {
				
				System.out.println(brainFuck.interpretCode(brainFuck.readFile(args[1])));
				
			} catch (IOException e) {
				
				throw new RuntimeException(e);
			}
			
		} else if ("--code".equalsIgnoreCase(args[0]) || "-c".equalsIgnoreCase(args[0])) {
			
			System.out.print("Enter the code: ");
			System.out.println(brainFuck.interpretCode(new Scanner(System.in).nextLine()));
			
		} else {
			
			throw new RuntimeException("Usage: java -jar BrainFuck.jar --help");
		}
	}
	
	/**
	 * Read code from a file and return it as a string.
	 * A given file must end with .bf, otherwise an exception is thrown.
	 *
	 * @param fileName The name of the file to read.
	 * @return The values of the file.
	 * @throws IOException If the file could not be read.
	 * @since 1.0.0
	 */
	public String readFile(String fileName) throws IOException {
		
		if (!fileName.endsWith(".bf"))
			
			throw new IOException("File must be a BrainFuck file!");
		
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		
		StringBuilder code = new StringBuilder();
		
		String line;
		
		while ((line = reader.readLine()) != null)
			
			code.append(line);
		
		reader.close();
		
		return code.toString();
	}
	
	/**
	 * Execute BrainFuck code and return the result.
	 *
	 * @param code The code to execute.
	 * @return The result of the code.
	 * @throws InputMismatchException If invalid input was given throw this exception.
	 * @see #pointer
	 * @see #memory
	 * @see #brackets
	 * @see #MEMORY_SIZE
	 * @since 1.0.0
	 */
	public String interpretCode(String code) throws InputMismatchException {
		
		StringBuilder result = new StringBuilder();
		
		for (int i = 0; i < code.length(); i++) {
			
			switch (code.charAt(i)) {
				
				case '>' -> {
					
					if (pointer == MEMORY_SIZE - 1)
						
						pointer = 0;
					
					else
						
						pointer++;
				}
				
				case '<' -> {
					
					if (pointer == 0)
						
						pointer = MEMORY_SIZE - 1;
					
					else
						
						pointer--;
				}
				
				case '+' -> memory[pointer]++;
				case '-' -> memory[pointer]--;
				case '[' -> {
					
					if (memory[pointer] == 0) {
						
						i++;
						
						while (brackets > 0 || code.charAt(i) != ']') {
							
							if (code.charAt(i) == '[')
								
								brackets++;
							
							else if (code.charAt(i) == ']')
								
								brackets--;
							
							i++;
						}
					}
				}
				
				case ']' -> {
					
					if (memory[pointer] != 0) {
						
						i--;
						
						while (brackets > 0 || code.charAt(i) != '[') {
							
							if (code.charAt(i) == ']')
								
								brackets++;
							
							else if (code.charAt(i) == '[')
								
								brackets--;
							
							i--;
						}
						
						i--;
					}
				}
				
				case '.' -> result.append((char) memory[pointer]);
				case ',' -> memory[pointer] = (byte) new Scanner(System.in).next().charAt(0);
			}
		}
		
		return result.toString();
	}
	
	/**
	 * Reset the memory of the program.
	 *
	 * @see #MEMORY_SIZE
	 * @see #memory
	 * @see #pointer
	 * @see #brackets
	 * @since 1.0.0
	 */
	public void reset() {
		
		pointer = 0;
		brackets = 0;
		
		for (int i = 0; i < MEMORY_SIZE; i++)
			
			memory[i] = 0;
	}
}
