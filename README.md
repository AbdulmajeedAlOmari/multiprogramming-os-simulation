# MULTIPROGRAMMING OS SIMULATION
BATCH PROCESSING AND MEMORY ALLOCATION

### HARDWARE
The computer hardware is assumed to have:
1. A RAM of size 192MB, where 32MB is used to store the OS.
2. A single core CPU that executes one instruction each unit of time.
3. An I/O device for input and output operations.
4. An internal clock that allows to measure time in milliseconds.

### OPERATING SYSTEM
The operating system is the multiprogramming OS. We would be interested in only two features in this simulation: The Job and CPU scheduling.
1. Job Scheduling: The system implements multiprogramming batch processing.
2. A long term scheduler selects jobs in sequence and allocates them the needed memory until the 90% of the memory is full. A job is loaded only if there is enough memory to satisfy its first memory requirement.
3. Each required memory block should be contiguous but a process may have more than one block of memory in different locations of the memory
4. The short term scheduler will allocate processes to the CPU following Shortest Job First (SJF) sequence. Each process will remain in the CPU until the end of its CPU burst then it will perform an I/O burst for the requested period of time before becoming ready again.
5. A process will have several CPU-burst / IO burst sequences at described in the example below. In each CPU burst a process may require additional memory or decide to free part of its memory.
6. If a process requires additional memory and there is not enough memory to satisfy its request, it should be put in Waiting state until there is enough memory for it.
7. Any process that is put in WAITING state for I/O or memory allocation will be put at the end of the ready queue after the end of its waiting.
8. If all processes are in Waiting state, only if all waiting for memory allocation, this is a deadlock. The system should declare a deadlock and select the largest waiting process to be killed in order to get some free memory for the other processes.
9. At any moment the processes will have one of the states, READY, WAITING, RUNNING, TERMINATED, KILLED.
10. When the job queue is empty and all processes are killed or terminated, the system should write a file containing statistics about all processes and their final status TERMINATED or KILLED.
11. Every 200 milliseconds, the long term scheduler will wake-up, check the memory and load more jobs until the 90% of the memory is full.

### PROGRAM SPECIFICATIONS
Each job in the jobs queue is defined as a sequence of several CPU-burst / IO burst as follows:

| Job description | Explanation                                           |
| --------------- | ----------------------------------------------------- |
| Name            | Process name                                          |
| 15 11           | CPU-burst of 15ms – Memory required 11 MB             |
| 3               | I/O burst of 3ms                                      |
| 5 4             | CPU-burst of 5ms – Additional memory required 4 MB    |
| 6               | I/O burst of 6ms                                      |
| 3 -6            | CPU-burst of 3ms – Free 6 MB of the allocated memory  |
| 1               | I/O burst of 1ms                                      |
| 3 0             | CPU-burst of 3ms – No change in memory                |
| -1              | Job terminates after the last CPU-burst               |
