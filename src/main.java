import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Properties;;

public class main {

	public static void main(String[] args) {
		
		//***********************Begin Looping Through Vocab********************
		//Powershell command to get vocab list from txt file
		String command = "powershell.exe Get-Content -Path C:\\Users\\Beau\\Desktop\\vocabList.txt";
		//StringBuffer strLine = new StringBuffer();
		String strLine = null;
		try{
			Process procPowerShell = Runtime.getRuntime().exec(command);
			procPowerShell.getOutputStream().close();
			//initializes the string variable which stores the contents of the file
			
			System.out.println("Standard Output:");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(procPowerShell.getInputStream()));
			String line;
			while ((line = stdout.readLine()) != null) {
				//System.out.println(line);
				strLine += "\n" + line;
				//strLine.append(line);
			}
			//closes the connection for the buffered reader
			stdout.close();
			//**************This is for error handling************************
//			System.out.println("Standard Error:");
//			BufferedReader stderr = new BufferedReader(new InputStreamReader(procPowerShell.getErrorStream()));
//			while ((strLine = stderr.readLine()) != null) {
//				System.out.println(strLine);
//				}
//			stderr.close();
			//System.out.println(strLine);
		}
		catch(Exception e) {
			System.out.println("Powershell Exception:  " + e);
		}
		//*********************processing for the line variable************************
		String[] arrWeeks = strLine.split("List");
		for(int i = 1; i < arrWeeks.length; i++){
			int intWeekNum = i;
			String strWeekNum = String.valueOf(intWeekNum);
			System.out.println("Week " + strWeekNum);
			String strWeeklyList = arrWeeks[i];
			String[] arrWeeklyWords = strWeeklyList.split("\n");
			for(int j = 1; j < arrWeeklyWords.length; j++) {
				//String wordStuff = arrWeeklyWords[j].substring(beginIndex)
				String word = arrWeeklyWords[j];
				word = word.substring(word.indexOf(".") + 1).trim();
				arrWeeklyWords[j] = word;
				String strPartOfSpeech = "";
			    String strDefinition = "";
				
				//****************API Portion**************************
				
			    //gotta get the API key from my config.properties file so
				//I can leave it off of the web when source controlling
			    String apiKey = "";
			    Properties prop = new Properties();
			    InputStream input = null;
			    try
			    {
			    	input = new FileInputStream("C:\\Users\\Beau\\workspace\\VocabHomework-O-Matic\\src\\config.properties");
			    	
			    	// load a properties file
			    	prop.load(input);
			    	apiKey = prop.getProperty("apiKey");
			    }
			    catch (IOException ex) 
			    {
					ex.printStackTrace();
				}
			    finally 
			    {
					if (input != null) 
					{
						try 
						{
							input.close();
						} 
						catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
			    }
			    
			    //System.out.println(arg0);
				String uri =
					    "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" + word + "?key=" + apiKey;
				try {
					URL url = new URL(uri);
					HttpURLConnection connection =
					    (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setRequestProperty("Accept", "application/xml");
					BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String webStuff = null;
					String result = "";
					//result += url.getProtocol();
					while ((webStuff = br.readLine()) != null) {
		                //System.out.println(webStuff);
		                result += webStuff;
		            }
					InputStream streamResult = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
					StreamSource xmlSource = new StreamSource(streamResult);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				    org.w3c.dom.Document doc = dBuilder.parse(xmlSource.getInputStream());
				    NodeList nodeList = doc.getElementsByTagName("*");
				    
				    for (int k = 0; k < nodeList.getLength(); k++) {
				        Node node = nodeList.item(k);
				        if (node.getNodeType() == Node.ELEMENT_NODE) {
				        	//System.out.println(node.getNodeName());
				            if(node.getNodeName() == "fl"){
				            	strPartOfSpeech = node.getTextContent();
				            }
				            if(node.getNodeName() == "dt"){
				            	strDefinition = node.getTextContent();
				            }
				            
				        }
				    }
					
				}
				catch (Exception e){
					System.out.println();
				}
				
				System.out.println(arrWeeklyWords[j]);
				System.out.println("Part of speech:  " + strPartOfSpeech);
				System.out.println("Definition:  " + strDefinition);
				System.out.println("Sample sentence:  " + "\n");
			}
			
		}
		//****************End Looping through Vocab**************************
		
		

	}
//	public static Document loadXMLFromString(String xml) throws Exception
//	{
//	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//	    DocumentBuilder builder = factory.newDocumentBuilder();
//	    InputSource is = new InputSource(new StringReader(xml));
//	    return (Document) builder.parse(is);
//	}

}
