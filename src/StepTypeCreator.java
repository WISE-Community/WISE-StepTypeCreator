import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * How to use this file
 * 
 * To create the StepTypeCreator.class file, run the command below from the command line
 * javac StepTypeCreator.java
 * 
 * To create the jar, make sure the StepTypeCreator.class file, manifest.txt file, 
 * and the template folder are all in the same directory. Then run the command below
 * jar cvfm StepTypeCreator.jar manifest.txt StepTypeCreator.class template/*template* template/*Template* template/setup.js
 * 
 * To run the jar, run the command below. Ideally the StepTypeCreator.jar
 * should be run in the the node directory (vlewrapper/WebContent/vle/node) 
 * so that the new step folder will automatically be placed in the node folder 
 * and the setupNodes.js file will be automatically updated.
 * java -jar StepTypeCreator.jar <new step type name without brackets> <new step type extension without brackets>
 * 
 * @author geoffreykwan
 *
 */
public class StepTypeCreator {
	
	/**
	 * Copies all of the template files, renames them, and modifies the content
	 * within the files so that it reflects the name of the new step type.
	 * @param args the first element should contain the name of the new step type
	 * the second element should contain the file extension for the new step type
	 */
	public static void main(String[] args) {
		if(args.length == 0 || args.length == 1) {
			//the user did not provide the step type name as a command line argument
			System.out.println("#Error: you must provide two arguments, the name of your new step type and the file extension of your new step type e.g.");
			System.out.println("java -jar StepTypeCreator.jar chocoTaco ct");
		} else {
			//get the step type name
			String newStepTypeName = args[0];
			String newStepTypeExtension = args[1];
			
			/*
			 * make sure the new step type name and extension only contains
			 * letters and numbers
			 */
			Pattern pattern = Pattern.compile("[a-zA-Z_0-9]*");
			Matcher nameMatcher = pattern.matcher(newStepTypeName);
			boolean isStepTypeNameValid = nameMatcher.matches();
			Matcher extensionMatcher = pattern.matcher(newStepTypeExtension);
			boolean isStepTypeExtensionvalid = extensionMatcher.matches();
			
			if(!isStepTypeNameValid) {
				//the step type name the user entered is invalid
				System.out.println("#Error: invalid step type name, only letters and numbers are allowed");
			} else if(!isStepTypeExtensionvalid) {
				//the step type extension the user entered is invalid
				System.out.println("#Error: invalid step type extension, only letters and numbers are allowed");
			} else {
				//the step type name the user entered is valid

				//get the step type name with the first character lower case e.g. quiz
				String newStepTypeNameFirstLowerCase = newStepTypeName.substring(0, 1).toLowerCase() + newStepTypeName.substring(1);
				
				//get the step type name with the first character upper case e.g. Quiz
				String newStepTypeNameFirstUpperCase = newStepTypeName.substring(0, 1).toUpperCase() + newStepTypeName.substring(1);
				
				try{
					//create the folder with the name of the new step type e.g. quiz
					File newStepTypeFolder = new File(newStepTypeNameFirstLowerCase);
					
					boolean createFolder = false;
					
					if(newStepTypeFolder.exists()) {
						//the folder already exists so we will ask the user if they want to overwrite the contents
						
						//get the System.in reader
						BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
						
						//display a warning message and prompt
						System.out.println("Warning: the folder " + newStepTypeNameFirstLowerCase + " already exists, if you continue, the contents will be overwritten.");
						System.out.print("Are you sure you want to continue? (yes, no) ");
						
						//get the answer from the user
						String answer = systemInReader.readLine();
						
						if(answer != null && answer.equals("yes")) {
							//the user answered yes
							createFolder = true;
						}
					} else {
						//the folder does not already exist so we can create it
						createFolder = true;
					}
					
					if(createFolder) {
						//create the folder and continue to create the new step type files
						newStepTypeFolder.mkdir();
						System.out.println("#Creating folder:");
						System.out.println(newStepTypeFolder);
						
						//get the source of this file
						CodeSource src = StepTypeCreator.class.getProtectionDomain().getCodeSource();

						if (src != null) {
							//get the location of this jar
							URL jar = src.getLocation();
							
							//obtain a handle on the jar
							ZipInputStream zipInputStream = new ZipInputStream(jar.openStream());
							
							ZipEntry zipEntry;
							StringBuffer fileContentStringBuffer;
							
							System.out.println("#Creating Files:");
							
							//loop through all the files in the jar file
							while((zipEntry = zipInputStream.getNextEntry()) != null) {
								/*
								 * only look for files that are in the template folder within the jar.
								 * we will be copying and modifying all of the files in the template
								 * folder within the jar.
								 */
								if(!zipEntry.isDirectory() && zipEntry.getName().startsWith("template/")) {
									
									//get one of the template files
									String zipEntryName = zipEntry.getName();

									//get the name of the file
									String newFileName = zipEntryName.substring(zipEntryName.lastIndexOf("/") + 1);
									
									//replace the file extension for templateTemplate.te with the file extension for the new step type
									newFileName = newFileName.replaceAll("\\.te", "." + newStepTypeExtension);
									
									/*
									 * generate the file name for the new step type file by 
									 * replacing 'template' with the new step type name 'quiz'
									 * e.g.
									 * template.html will turn into quiz.html
									 */
									String newStepTypeFileName = newFileName.replaceFirst("template", newStepTypeNameFirstLowerCase);
									
									if(!newFileName.contains("templateTemplate")) {
										/*
										 * generate the file name for the new step type file by 
										 * replacing 'Template' with the new step type name 'Quiz'
										 * e.g.
										 * TemplateNode.js will turn into QuizNode.js
										 */
										newStepTypeFileName = newStepTypeFileName.replaceFirst("Template", newStepTypeNameFirstUpperCase);	
									}
									
									//create the new file in the new folder
									File newFile = new File(newStepTypeFolder, newStepTypeFileName);
									
									//create a string buffer to hold the content of the file we are copying
									fileContentStringBuffer = new StringBuffer();
									
									int n;
									byte[] buf = new byte[1024];
									char[] charBuf = new char[1024];
									
									//copy the contents of the template file into our new step type file
									while ((n = zipInputStream.read(buf, 0, 1024)) > -1) {
										charBuf = (new String(buf)).toCharArray();
										fileContentStringBuffer.append(charBuf, 0, n);
									}
									
									//get the contents from the template file
									String fileContents = fileContentStringBuffer.toString();
									
									//replace the .te file extension with the new step type file extension
									fileContents = fileContents.replaceAll("nodeTemplateFilePath:'node/template/templateTemplate.te'", "nodeTemplateFilePath:'node/template/templateTemplate." + newStepTypeExtension + "'");
									fileContents = fileContents.replaceAll("nodeExtension:'te'", "nodeExtension:'" + newStepTypeExtension + "'");
									
									//replace all instances of 'template' with the new step type name e.g. 'quiz'
									fileContents = fileContents.replaceAll("template", newStepTypeNameFirstLowerCase);
									
									//replace all instances of 'Template' with the new step type name e.g. 'Quiz'
									fileContents = fileContents.replaceAll("Template", newStepTypeNameFirstUpperCase);
									
									/*
									 * there are several variables in the template files that contain the word 'template'
									 * or 'Template' and should not have their names changed so we need to change them back
									 */
									
									//templateTemplate
									fileContents = fileContents.replaceAll(newStepTypeNameFirstLowerCase + newStepTypeNameFirstUpperCase, newStepTypeNameFirstLowerCase + "Template");
									
									//nodeTemplateParams
									fileContents = fileContents.replaceAll("node" + newStepTypeNameFirstUpperCase + "Params", "nodeTemplateParams");
									
									//addNodeTemplateParams
									fileContents = fileContents.replaceAll("addNode" + newStepTypeNameFirstUpperCase + "Params", "addNodeTemplateParams");
									
									//nodeTemplateFilePath
									fileContents = fileContents.replaceAll("node" + newStepTypeNameFirstUpperCase + "FilePath", "nodeTemplateFilePath");
									
									//getHTMLContentTemplate
									fileContents = fileContents.replaceAll("getHTMLContent" + newStepTypeNameFirstUpperCase, "getHTMLContentTemplate");
									
									//getHTMLContentTemplate
									fileContents = fileContents.replaceAll("excelExportString" + newStepTypeNameFirstUpperCase, "excelExportStringTemplate");

									//obtain a handle on the new step type file so we can write to it
									BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(newFile));
									
									//write the modified contents to the new step type file
									bufferedWriter.write(fileContents);
									
									//close the file
									bufferedWriter.close();
									
									//output the path of the new file we have created
									System.out.println(newStepTypeFolder.getName() + File.separator + newStepTypeFileName);
								}
							}
						}
						
						//see if the setupNodes.js file is in the current directory
						File setupNodesFile = new File("setupNodes.js");
						if(setupNodesFile.exists()) {
							//the setupNodes.js file is in the current directory so we will update it
							
							//string buffer to hold the content of the setupNodes.js file
							StringBuffer setupNodesStringBuffer = new StringBuffer();
							BufferedReader bufferedReader = new BufferedReader(new FileReader(setupNodesFile));

					        char[] buf = new char[1024];
					        int r = 0;

					        //read the contents of the setupNodes.js file
					        while ((r = bufferedReader.read(buf)) != -1) {
					        	setupNodesStringBuffer.append(buf, 0, r);
					        }
					        
					        String setupNodesContent = setupNodesStringBuffer.toString();
					        
					        //check if the setupNodes.js already contains an entry with our new step type name
					        boolean alreadyContainsNodeName = setupNodesContent.contains(newStepTypeNameFirstUpperCase);
					        
					        //find the last closing bracket which should be the closing bracket for the SetupNodes.setupFiles array
					        int indexOfClosingArrayBracket = setupNodesContent.indexOf("]");
					        String substring = setupNodesContent.substring(0, indexOfClosingArrayBracket);
					        
					        //find the last closing curly brace within the setupFiles array
					        int indexOfClosingCurlyBrace = substring.lastIndexOf("}");
					        
					        /*
					         * insert the new entry into the end of the SetupNodes.setupFiles array, 
					         * the text we will be inserting will look like this below
					         * 
					         * 	,
					         * 	{
					         *		nodeName:"YourNewNode",
					         *		nodeSetupPath:"vle/node/yourNew/setup.js"
					         *	}
					         */
					        String updatedSetupNodesContent = setupNodesContent.substring(0, indexOfClosingCurlyBrace + 1) + 
					        	",\n\t{\n\t\tnodeName:\"" + newStepTypeNameFirstUpperCase + "Node\",\n\t\tnodeSetupPath:\"vle/node/" + newStepTypeNameFirstLowerCase + "/setup.js\"\n\t}" + 
					        	setupNodesContent.substring(indexOfClosingCurlyBrace + 1);
					        
					        //close the file reader
					        bufferedReader.close();
					        
					        //obtain a handle on the setupNodes.js file so we can write to it
					        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(setupNodesFile));
					        
					        //write the updated content into the setupNodes.js file
					        bufferedWriter.write(updatedSetupNodesContent);
					        
					        //close the writer
					        bufferedWriter.close();
					        
					        //tell the user that we have updated the setupNodes.js file
					        System.out.println("#Adding new step to setupNodes.js file");
					        
					        if(alreadyContainsNodeName) {
					        	//display the message that there are duplicate entries with the same nodeName in the setupNodes.js file
					        	System.out.println("#Warning, setupNodes.js contains duplicate entries with the nodeName " + newStepTypeNameFirstUpperCase + ". Please resolve the duplicate entry manually.");
					        }
						}
						System.out.println("#Step creation complete");
					} else {
						System.out.println("#Step creation aborted");
					}
				} catch(IOException e) {
					System.err.println(e.getMessage());
				}
			}
		}
	}
}
