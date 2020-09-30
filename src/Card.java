public class Card
{
	private char frontSide;
	private char backSide;
	private boolean hidden;
	
	public Card(char frontSide, char backSide)
	{
		this.frontSide = frontSide;
		this.backSide = backSide;
		hidden = true;
	}
	
	public char getFrontSide()
	{
		return frontSide;
	}
	
	public void setFrontSide(char frontSide)
	{
		this.frontSide = frontSide;
	}
	
	public char getBackSide()
	{
		return backSide;
	}
	
	public void setBackSide(char backSide)
	{
		this.backSide = backSide;
	}
	
	public void changeCardStatus()
	{
		if(hidden)
			hidden = false;
		else
			hidden = true;
	}

	public void showCard()
	{
		if(hidden)
			System.out.print(backSide + " ");
		else
			System.out.print(frontSide + " ");
	}
}