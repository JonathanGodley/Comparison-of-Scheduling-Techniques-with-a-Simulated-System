// Process.Java
// Process Class to hold instructions that make up the pages the process requires
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  26/10/2017
import java.util.LinkedList;
import java.util.Queue;

public class Process implements Comparable<Process>
{
  // Instance Variables
  private int processID;
  private int arrivalTime;
  private int finishTime;     // turnaround is finish - arrival
  private int waitingTime = 0;
  private int execSize;
  private int readyTime = 0;


  // waiting time is calcualted by the use of
  private int stoppedWorking;     // when we pause to work on somthing else
  private int startedWorking;     // when we resume this process,
  // when we initialise the process, we set the stopped working time to the
//  arrival time, so that when we actually start working on the process,
//  we record the gap with waitingTime += (startedWorking-stoppedWorking)
//  and continue to increment the waiting time every time a process is
//  stopped (time stamp is recorded) and resumed (gap added to waitingTime)
  private Queue<Integer> pages = new LinkedList<Integer>();
  //record of page fault occurances
  private Queue<Integer> pageFaults = new LinkedList<Integer>();

  // Constructor
  public Process(){
    arrivalTime = 0;
    execSize = 0;
    stoppedWorking = arrivalTime;
  }

  // GET
  // Precondition : This process object has been correctly initialsed
  // Postcondition: unique ID integer returned
  public int getID()
  {
          return processID;
  }

  // return ready time
  // pre: this object is initialised
  // post: readyTime returned
  public int getReadyTime()
  {
    return readyTime;
  }

  // get faults
  // pre: this object is initialised
  // post: number of faults returned
  public int getFaultsCount()
  {
    return pageFaults.size();
  }

  // get faults
  // pre: this object is initialised and has experianced atleast 1 fault
  // post: fault timestamps returned as formatted string
  public String getFaults()
  {

    String tmpOutput = "{" + pageFaults.poll();

    while(!pageFaults.isEmpty())
    {
      tmpOutput = tmpOutput + ", "  + pageFaults.poll();
    }

    tmpOutput = tmpOutput + "}";
    return tmpOutput;
  }

  // Precondition : This process object has been correctly initialsed
// Postcondition: execSize integer returned
public int getExecSize()
{
        return execSize;
}

  // Precondition : This process object has been correctly initialsed
// Postcondition: arrivalTime integer returned
public int getArrivalTime()
{
        return arrivalTime;
}

// Precondition : This process object has been initialised and processing has finished
// Postcondition: the turnaround time is calculated and returned
public int getTurnaroundTime()
{
        // turnaround is finish - arrival
        return (finishTime - arrivalTime);
}

// Precondition : process needs to be finished processing (aka finishtime is populated)
// Postcondition: finishtime integer returned
public int getFinishTime()
{
        return finishTime;
}

// Precondition : This process object has been correctly initialsed
// Postcondition: waiting time integer returned
public int getWaitingTime()
{
        return waitingTime;
}

  // Precondition : queue is populated
  // Postcondition: head of the queue is removed and returned
  public Integer dequeue()
  {
          return pages.poll();
  }

  // Precondition : queue is populated
  // Postcondition: head of the queue is returned without removing
  public Integer check()
  {
          return pages.peek();
  }

  // Precondition : N/A
  // Postcondition: returns true if queue not empty, false otherwise.
  public boolean hasItems()
  {
          return !pages.isEmpty();
  }

  // Precondition : N/A
  // Postcondition: returns int containing number of items in queue
  public int size()
  {
          return pages.size();
  }

  // SET
  // Set the process ID
  // Precondition : an int is passed to the process
  // Postcondition: process ID set
  public void setProcessID(int input)
  {
    processID = input;
  }

  // set ready time
  // pre: integer passed
  // post: readyTime updated to current simulation time
  public void setReadyTime(int simTime)
  {
    readyTime = simTime;
  }

  // Add an item to the queue
  // Precondition : a int is passed to the queue
  // Postcondition: added item to queue
  public boolean enqueue(Integer item)
  {
          pages.add(item);
          execSize++;
          return true;
  }

  // Precondition : valid timestamp is passed
// Postcondition: stoppedWorking updated, execSizeLeft remainder calculated
public void pause(int timestamp)
{
        stoppedWorking = timestamp;

}

// Precondition : valid timestamp is passed
// Postcondition: startedWorking updated, waitingTime calculated and updated
public void resume(int timestamp)
{
        startedWorking = timestamp;
        // update waiting time
        waitingTime += (startedWorking-stoppedWorking);
}

// Precondition : valid timestamp is passed
// Postcondition: stoppedWorking & finishTime updated, execSizeLeft set to 0
public void finish(int timestamp)
{
        finishTime = timestamp;
        stoppedWorking = timestamp;
}

// new fault
// pre: timestamp page fault occured is passed
// post: pagefault timestamp is recorded
public void newFault(int simTime)
{
  pageFaults.add(simTime);
}

  /**
     Preconditions  -- pro is a valid Process object
     Postconditions -- objects compared, their relation returned
   */
  public int compareTo(Process pro)
  {
          if(size()==pro.size()) // if identical size
          {

                  // process with the lower process ID takes precedence.
                  int left = getID();
                  int right = pro.getID();

                  if(left>right)
                  {
                          return 1; // higher ID
                  }
                  else
                  {
                          return -1; // lower ID
                  }
          }
          else if(size()>pro.size())
          {
                  return 1; // greater execution time
          }
          else
          {
                  return -1; // lesser execution time
          }
  }
}
