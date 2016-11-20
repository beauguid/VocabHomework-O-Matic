import java.util.ArrayList;

public class Word {
	private String word;
	private String partOfSpeech;
	private ArrayList<String> definitions;
	private String sampleSentence;
	
	Word() {
		
	}

	public String getPartOfSpeech() {
		return partOfSpeech;
	}

	public void setPartOfSpeech(String partOfSpeech) {
		this.partOfSpeech = partOfSpeech;
	}

	public ArrayList<String> getDefinitions() {
		return definitions;
	}

	public void setDefinitions(ArrayList<String> definitions) {
		this.definitions = definitions;
	}

	public String getSampleSentence() {
		return sampleSentence;
	}

	public void setSampleSentence(String sampleSentence) {
		this.sampleSentence = sampleSentence;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
	
}
