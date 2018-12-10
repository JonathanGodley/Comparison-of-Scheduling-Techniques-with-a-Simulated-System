// MemorySim.Java
// Memory Simulator class, that can contain an x amount of frames
// Centers around a linked list of frames
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  27/10/2017
import java.util.LinkedList;
import java.util.Queue;

public class MemorySim
{
// Instance Variables
private int availableFrames;
private int processes;
private int allocatedFrames; // how many frames a process can use
private LinkedList<Frame> frames = new LinkedList<Frame>();

// Constructor
public MemorySim(int avFrames, int noProcesses)
{
        availableFrames = avFrames;
        processes = noProcesses;
        allocatedFrames = availableFrames / processes;
}

// GET
// Precondition : frames is populated
// Postcondition: head of the queue is removed and string component is returned
public String dequeue()
{
        return frames.poll().checkInstruction();
}

// Precondition : frames is populated
// Postcondition: string component of the head of the queue is returned without removing
public String check()
{
        return frames.peek().checkInstruction();
}

// Precondition : N/A
// Postcondition: returns true if queue not empty, false otherwise.
public boolean hasItems()
{
        return !frames.isEmpty();
}

// Precondition : N/A
// Postcondition: returns int containing number of items in queue
public int size()
{
        return frames.size();
}


// Add an instruction to memory
// Precondition : the insturction, process no and current sim time are provided
// Postcondition: The instruction is added to memory
public boolean transferLRU(String item, int processNo, int curSimTime)
{
        String proNumString = processNo + "-";
        int count = 0;

        // count how many frames are available in the processe's allocation
        for (Frame f : frames)
        {
                if (f.checkInstruction().startsWith(proNumString))
                {
                        count++;
                }
        }

        // if there are available frames
        if (count <= (allocatedFrames-1))
        {
                Frame tmpFrame = new Frame(item, curSimTime);
                frames.add(tmpFrame);
                return true;
        }
        else
        {
                // create a dummy frame
                Frame tmpLowest = new Frame("",-1);

                for (Frame f : frames)
                {
                        // determine if our tmpFrame is the dummy frame
                        if (tmpLowest.getAccessed() == -1)
                        {
                                if (f.checkInstruction().startsWith(proNumString))
                                {
                                        // set our tmpLowest to the first real frame we find
                                        tmpLowest = f;
                                }
                        }
                        // loop through the rest of the frames in memory to find the LRU frame
                        else if (f.checkInstruction().startsWith(proNumString) && f.getAccessed() <= tmpLowest.getAccessed())
                        {
                                tmpLowest = f;
                        }
                }

                // Now we replace the frame we found with a new one
                Frame tmpFrame = new Frame(item, curSimTime);
                frames.remove(tmpLowest); // delete the frame we found
                frames.add(tmpFrame); // add our new frame
                return true;
        }

}

// check if item is present
// pre: system time and item to look for are passed
// post: boolean returned wether item is present, if present item last-accessed time is updated
public boolean contains(String item, int curSimTime)
{
        boolean found = false;
        for (Frame f : frames)
        {
                if (f.checkInstruction().equals(item))
                {
                        found = true;
                        f.access(curSimTime);
                        return found;
                }
        }
        return found;
}
}
