// c3188072A3.java
// Comparing LRU and Clock Page replacement Alogithms
//
// Programmer:  Jonathan Godley - c3188072
// Course: Comp2240
// Last modified:  27/10/2017
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class c3188072A3
{
public static void main (String[] args)
{
        try
        {
                c3188072A3 obj = new c3188072A3 ();
                obj.run (args);
        }
        catch (Exception e)
        {
                e.printStackTrace ();     //so we can actually see when stuff goes wrong
        }
}

public void run (String[] args) throws Exception
{
        // Variables
        double avgPRRt = 0, avgPRRw = 0;
        int simTime = 0;
        int finTime = 0;
        String tmpInput;
        Boolean ioInterrupted = false;
        int tempAQ; // tempAssignedQuantums - how many quantums we're working for

        // Class Init
        // ready queue contains all our processes ready for processing
        ProcessQueue readyQueue = new ProcessQueue();
        // finishedQueue holds our processed processes, for later retrieval and
        // statistical analysis
        ProcessQueue finishedQueue = new ProcessQueue();
        // our Memory Class which we're initialising with 30 frames availableFrames
        MemorySim mainMemory;
        // queue to hold our processes waiting for IO
        ProcessQueue ioQueue = new ProcessQueue();

        // Pointers
        Process currentProcess;

        // read input files, populate queue with processes
        readyQueue = readFiles(args);
        mainMemory = new MemorySim(30, readyQueue.size());

        // run dispatcher
        currentProcess = runDispatcher(readyQueue);

        tempAQ = 3;

        // check if process finishes in less than alloted time
        if (currentProcess.size() < tempAQ)
        {
                tempAQ = currentProcess.size();
        }
        else if (!readyQueue.hasItems() && !ioQueue.hasItems())
        {
                tempAQ = currentProcess.size();
        }

        // set finish time
        finTime = simTime + tempAQ;

        // resume process and output data
        currentProcess.resume(simTime);

        // loop until done
        while(finTime != -1)
        {
                // do work, increment time and pause process
                // work through the assigned time quantums
                while (tempAQ != 0)
                {
                        // peek at our instruction
                        // check if instruction is present in main memory
                        tmpInput = currentProcess.getID()+"-"+currentProcess.check();
                        if (mainMemory.contains(tmpInput, simTime))
                        {

                                // if present, pull instruction, increment time by 1
                                currentProcess.dequeue();
                                simTime++;

                                // check the ioQueue for items that are ready
                                while (ioQueue.hasItems() && ioQueue.check().getReadyTime() <= simTime)
                                {
                                        readyQueue.enqueue(ioQueue.dequeue());
                                }

                                // we've done 1 unit of work, and incremented the system time
                                tempAQ--;
                        }
                        else
                        {
                                // if not present, throw page error, pass to IO requests, add process back to queue
                                currentProcess.newFault(simTime);
                                tempAQ = 0; // no more work to be done, interrupted
                                currentProcess.setReadyTime(simTime + 6); // time process can resume work
                                mainMemory.transferLRU(tmpInput, currentProcess.getID(), simTime); // add our instruction to memory
                                ioInterrupted = true;
                                //ioQueue.enqueue(currentProcess);
                        }
                }

                currentProcess.pause(simTime);

                // first we check if our current process is finished, or is interrupted.
                if (currentProcess.size()==0) // finished
                {
                        currentProcess.finish(simTime);
                        finishedQueue.enqueue(currentProcess);
                }
                else if (currentProcess.size()!=0) // interrupted
                {
                        if (ioInterrupted == false)
                        {
                                readyQueue.enqueue(currentProcess);
                        }
                        else if (ioInterrupted == true)
                        {
                                ioInterrupted = false;
                                // put our paused process in the queue
                                ioQueue.enqueue(currentProcess);

                        }

                }

                // check the ready queue for items
                if (readyQueue.hasItems())
                {
                        // run dispatcher & increment time
                        currentProcess = runDispatcher(readyQueue);

                        // work out how many quantums to allow the process to work for
                        tempAQ = 3;

                        // does the process need that many?
                        if (currentProcess.size() < tempAQ)
                        {
                                tempAQ = currentProcess.size();
                        }
                        else if (!readyQueue.hasItems() && !ioQueue.hasItems())
                        {
                                tempAQ = currentProcess.size();
                        }

                        // set finish time, resume process & output data
                        finTime = simTime + tempAQ;
                        currentProcess.resume(simTime);
                }
                else if (!readyQueue.hasItems() && ioQueue.hasItems())
                {
                        // if no items in ready queue AND there are items in the arrival queue
                        // we increment our sim time to the arrival time of the next process
                        //simTime = ioQueue.check().getReadyTime();
                        while (!readyQueue.hasItems())
                        {
                                simTime++;

                                // check for items that are ready to move
                                while (ioQueue.hasItems() && ioQueue.check().getReadyTime() <= simTime)
                                {
                                        readyQueue.enqueue(ioQueue.dequeue());
                                }

                        }
                        // run dispatcher & increment time
                        currentProcess = runDispatcher(readyQueue);

                        // work out how many quantums to allow the process to work for
                        tempAQ = 3;

                        // does it need that many?
                        if (currentProcess.size() < tempAQ)
                        {
                                tempAQ = currentProcess.size();
                        }
                        else if (!readyQueue.hasItems() && !ioQueue.hasItems())
                        {
                                tempAQ = currentProcess.size();
                        }

                        // set finish time, resume process and output data
                        finTime = simTime + tempAQ;
                        currentProcess.resume(simTime);
                }
                // if nothing left in arrival or ready queue, finished looping
                else if(!readyQueue.hasItems() && !ioQueue.hasItems()) {finTime = -1;}
        }

        // reorder finished processes
        sortFinQ(finishedQueue);

        // get our averages
        avgPRRw /= finishedQueue.size();
        avgPRRt /= finishedQueue.size();

        // OUTPUT:
        System.out.println("LRU - Fixed:");
        System.out.println("PID\tTurnaround Time\t# Faults\tFault Times");
        // loop through our queue and pull data from each entry
        while(finishedQueue.hasItems())
        {
                currentProcess = finishedQueue.dequeue();
                System.out.println(currentProcess.getID()+"\t"+currentProcess.getTurnaroundTime()+"\t\t"+currentProcess.getFaultsCount()+"\t\t"+currentProcess.getFaults());

        }
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("Clock - Fixed:");
        System.out.println("PID\tTurnaround Time\t# Faults\tFault Times");
        System.out.println("Not Attempted");

        // exit properly
        System.exit ( 0 );
}

// Precondition : arguments are passed, arguments include text file names
// Postcondition: queue containing processes is created from text files,
public ProcessQueue readFiles(String[] args)
{
        // init our queue
        ProcessQueue arrivalQueue = new ProcessQueue();
        // read from file,
        Scanner inputStream = null;
        // int to tell us which file from our arguments we're accessing
        int i;

        // while there are still files we haven't read
        for (i = 0; i < args.length; i++)
        {
                String file = args[i];

                // try/catch to prevent file not found exceptions
                try
                {
                        // opens file specified by commandline arguments
                        inputStream = new Scanner (new File (file));
                }
                catch (FileNotFoundException e)
                {
                        System.out.println ("Error opening the file " + file);
                        System.exit (0);
                }
                // loop through text file line by line,
                // set a variable to contain our nextLine for easier manipulation
                String line = inputStream.nextLine();
                if (line.contains("begin")) // check if file is formatted correctly
                {
                        // init our temp process
                        Process tempItem = new Process();

                        // set our process id to the integer in the process filename e.g. Process1.txt becomes 1
                        tempItem.setProcessID(Integer.parseInt(args[i].replaceAll("Process", "").replaceAll(".txt", "")));

                        line = inputStream.nextLine(); // step over BEGIN
                        // stop when hit EOF marker or run out of lines
                        while (!line.contains("end") && inputStream.hasNextLine())
                        {
                                if (!line.trim().isEmpty()) // skip empty lines
                                {
                                        // add the required page number to the processes inbuilt queue
                                        tempItem.enqueue(Integer.parseInt(line));
                                }
                                line = inputStream.nextLine();
                        }
                        // no more lines, so enqueue process
                        arrivalQueue.enqueue(tempItem);
                }
                else
                {
                        // files must start with "begin"
                        System.out.println ("ERROR: Specified file is incorrectly formatted");
                        System.exit (0);
                }
                inputStream.close(); // finished with our file

        }
        return arrivalQueue;
}

// Dispatcher - Round Robin
// NOTE: directly lifted from A1's Dispatcher.java
// Precondition : a populated ProcessQueue is passed, with processes that
//    have arrival times equal to or prior to the current simulation time
// Postcondition: the next process to run is removed from the readyQueue and
//    is returned for processing.
public Process runDispatcher(ProcessQueue rdyQ)
{
        // since we're only passing a unified queue, all we have to do is remove
        //    the head of the queue, since they're not ordered by priority
        return rdyQ.dequeue();
}

// Precondition : A populated queue is passed
// Postcondition: Queue is sorted and returned
public void sortFinQ(ProcessQueue finQ)
{
        // pull the items out of the queue, and then sort them, and then requeue them.
        // create and populate array from our queue
        Process[] processArray = new Process[finQ.size()];
        int x = 0;
        while (finQ.hasItems())
        {
                processArray[x] = finQ.dequeue();
                x++;
        }

        // Insertion Sort
        int j;
        Process sortProcess;
        int i = 0;

        // insertion sort using .compareTo
        for (j = 1; j < processArray.length; j++)
        {
                sortProcess = processArray[ j ];
                for(i = j - 1; (i >= 0) &&
                    (processArray[i].compareTo(sortProcess) == 1); i--)
                {
                        processArray[ i+1 ] = processArray[ i ];
                }
                processArray[ i+1 ] = sortProcess;
        }


        // reinsert back into queue
        int n = processArray.length;
        for (i = 0; i < n; i++)
        {
                finQ.enqueue(processArray[i]);
        }

}

}
