/*
	MyNode.java

    The Small-C cross-compiler for IBM 1401

	November-18-2014

	By Sean Papay, Matt Pleva, Luca Severini
*/

package compiler;

/**
 * Specialised node.
 */
public class MyNode extends SimpleNode
{
	private String name = "";
	private int kind = 0;
	private int col = 0;
	private int row = 0;

  	public MyNode(int id)
	{
    	super(id);
  	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public void setKind(int kind) 
	{
		this.kind = kind;
	}

	public void setCol(int col) 
	{
		this.col = col;
	}

	public void setRow(int row) 
	{
		this.row = row;
	}

	public String getName() 
	{
		return this.name;
	}

	public int getKind() 
	{
		return this.kind;
	}
	
	public int getCol() 
	{
		return this.col;
	}
	
	public int getRow() 
	{
		return this.row;
	}
}
