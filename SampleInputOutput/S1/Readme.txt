The simulation made the following assumptions:

-Issuing a page fault and subsequently blocking a process takes no time (this explains why all four processes are able to issue a page fault "simultaneously" at t=0)
-Execution of a swapped page cannot occur immediately after the page arrives in main memory (e.g. page 1 of process1 arrived in main memory at t=6 and executed at t=6, then a page fault was issued for page 2 at t=7.

NOTE: No frame replacement was necessary in this simple example. Therefore, do difference in the output for LRU and clock.