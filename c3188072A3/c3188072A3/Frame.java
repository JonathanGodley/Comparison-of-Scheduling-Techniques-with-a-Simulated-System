// Frame.java
// Just a minor data structure to make handling frames easier
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  27/10/2017
public class Frame
{
  // instance variables
  private String instruction;
  private int lastAccessed;

  // constructor
  public Frame(String ins, int time)
  {
    instruction = ins;
    lastAccessed = time;
  }

  // change the "last accessed" int to the current system time
  // Precondition: int passed
  // Postcondition: lastAccessed updated
  public void access(int time)
  {
    lastAccessed = time;
  }

  // return lastAccessed
  // PreC: this instance is initialised with valid data
  // postC: lastAccessed is returned
  public int getAccessed()
  {
    return lastAccessed;
  }

  // return instruction
  // PreC: this instance is initialised with valid data
  // postC: instruction is returned
  public String checkInstruction()
  {
    return instruction;
  }
}
