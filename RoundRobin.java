/*           Round Robin Scheduling Algorithm
 * Name: Bahaa Hani xxxxxxx     ID: xxxxxxxxx   Section: x
 * Name: Sayed Ahmed Khalaf     ID: 202007602   Section:2 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
*/

 // Required Classes
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

public class RoundRobin {
    public static void main(String[] args){

        // Creating A Scanner Object To Get User Input
        Scanner input = new Scanner(System.in);

        // Declarations
        int timeQuantum, inputPID, inputArrivalTime, inputBurstTime;
        boolean stop = false;
        ArrayList<Integer> processID = new ArrayList<Integer>();
        ArrayList<Integer> arrivalTime = new ArrayList<Integer>();
        ArrayList<Integer> burstTime = new ArrayList<Integer>();

        System.out.println("\n\tRound Robin Scheduling Algorithm\t\n");
    
        // Quantum Time Input
        System.out.print("Enter the time quantum (q) = ");
        timeQuantum = input.nextInt();

        /*
            Process Information Input (processes identifier, arrival time, and burst time)
            Inputing 0 0 0 exits the loop, and thus stopping the input   
        */  
        System.out.println();  
        System.out.println("Enter The Process IDs, Arrival Times, and Burst times:\n" + "(Entering Three Consecutive 0's Stops The Input)");

        while (!stop) {
            // Reading User Input
            inputPID = input.nextInt();
            inputArrivalTime = input.nextInt();
            inputBurstTime = input.nextInt();

            // Checking End Of Input
            if (inputPID == 0 && inputArrivalTime == 0 && inputBurstTime == 0) {
            	stop = true;
            }
            // Adding Process Information To Its Corresponding ArrayList 
            else {
                processID.add(inputPID);
                arrivalTime.add(inputArrivalTime);
                burstTime.add(inputBurstTime);
            }
        }
        
        /*
            Sorting The Processess Based on Arrival Time (Using Bubble Sort Algorithm)
            And Updating the ArrayLists, So That Their Proper Mapping Still Holds.
        */
        int numOfProcesses = processID.size();
		for (int i = 0; i < numOfProcesses-1; i++) 
			for (int j = 0; j < numOfProcesses-i-1; j++) 
				if (arrivalTime.get(j) > arrivalTime.get(j+1)) {

                    // Storing The Process Information In Temporary Variables
					int tempArrivalTime = arrivalTime.get(j);
					int tempBurstTime = burstTime.get(j);
					int tempProcessesID = processID.get(j);

					// Swap theirr arrival times
 					arrivalTime.set(j, arrivalTime.get(j+1));
					arrivalTime.set(j+1, tempArrivalTime);
					
					// Updating The Burst Time ArrayList to the sorted order
					burstTime.set(j, burstTime.get(j+1));
					burstTime.set(j+1, tempBurstTime);
					
					// Updating The Process Identifier ArrayList to the sorted order
					processID.set(j,  processID.get(j+1));
					processID.set(j+1, tempProcessesID);
				}
				
        /*
            Declarations Necessary For Performing The Calculations
        */ 
        int timer=0, runningProcess=0, runningProcessIndex=0;
		int[] startingTime = new int[arrivalTime.size()];
        int[] finishedTime = new int[arrivalTime.size()];

        // Declaring The Copy Arrays 
        int[] arrivalTimeDup = new int[arrivalTime.size()];
        int[] burstTimeDup = new int[burstTime.size()];
        int[] processIDDup = new int[processID.size()];
        /*
            Copying The Previously Sorted ArrayLists Into The Duplicate Arrays
            To Preserve The Original Values Entered By The User        
        */ 
        for (int i = 0; i < numOfProcesses; i++)
        {
            arrivalTimeDup[i] = arrivalTime.get(i);
            burstTimeDup[i] = burstTime.get(i);
            processIDDup[i] = processID.get(i);
        }

        /* 
            Implementing The Queue Interface Using The LinkedList Class
        */ 
        // Storing The Time Sequence Of The Processes
        Queue<Integer> timeSequence = new LinkedList<>();
        // Storing The Process Sequence
        Queue<Integer> processSequence = new LinkedList<>();

        // Declaring The Ready Queue ArrayList
        ArrayList<Integer> readyQueue = new ArrayList<Integer>();

        // Adding The First Process To Arrive Into The Ready Queue 
        timeSequence.add(arrivalTime.get(0));
        readyQueue.add(processID.get(0));
        startingTime[0] = arrivalTime.get(0);

        // Enter The Loop If There Exists Some Processes In The Ready Queue
        while (readyQueue.size() > 0) {

            // Remove The Process To Be The Next Running From The Ready Queue And Store Its Index
            runningProcess = readyQueue.remove(0);
            runningProcessIndex = processID.indexOf(runningProcess);

            /*
                Checking If The Arrival Time Of The Process To Run Is Possible
                With Reference To The Timer. If It Is, Increment The Timer And 
                Return The Process Back To the Ready Queue If the Queue Is Empty
            */
            if (arrivalTime.get(runningProcessIndex) > timer) {
                timer++;
                if (readyQueue.isEmpty()) {
                    readyQueue.add(processID.get(runningProcessIndex));
                }		
            }

            /*
                If The Arrival Time Is Possible Then Enter The Else If Block
            */
            else if (arrivalTime.get(runningProcessIndex) <= timer) {

                // Adding The Process Start Time, If It Is Its First CPU Visit   
                if (!processSequence.contains(processID.get(runningProcessIndex))) {
                    startingTime[runningProcessIndex] = timer;
                }
                

                /*
                    Else If It is less Than the Quantum Time, Run It For Its Burst Time and
                    Store The Finish Time  
                */
                if (burstTime.get(runningProcessIndex) < timeQuantum && burstTime.get(runningProcessIndex) != 0) {
                	processSequence.add(processID.get(runningProcessIndex));
                    timer += burstTime.get(runningProcessIndex);
                    timeSequence.add(timer);
                    burstTime.set(runningProcessIndex, 0);
                    finishedTime[runningProcessIndex] = timer;
                }

                /*
                    If The Process Burst Time is Greater Than Quantum Time, Run it for 
                    The Time Quantum While Decreasing Its Burst Time.
                */
                else if (burstTime.get(runningProcessIndex) >= timeQuantum) {
                    processSequence.add(processID.get(runningProcessIndex));
                    burstTime.set(runningProcessIndex, burstTime.get(runningProcessIndex) - timeQuantum);
                    timer += timeQuantum;
                    timeSequence.add(timer);
                    /*
                        If The  Process Burst Time is Equal To The Quantum Time, Run it for 
                        The Time Quantum While Decreasing Its Burst Time and Store Its Finish Time.
                    */
                    if (burstTime.get(runningProcessIndex) == 0) {
                        finishedTime[runningProcessIndex] = timer;
                    }     
                }
                
                /*
                    Modifying The Ready Queue After The Running Of The Process. 
                    If The Process Has Not Finished Execution, and
                    If It Did Finish Execution
                */
                if (burstTime.get(runningProcessIndex) == 0) {
                    for (int i = runningProcessIndex+1; i < numOfProcesses; i++) {
                        /*
                            If A New Process Arrived Add It To Ready, Followed By Adding 
                            The Recently Run Process 
                        */
                        if (arrivalTime.get(i)<=timer && !readyQueue.contains(processID.get(i))) {
                            readyQueue.add(processID.get(i));
                        }
                    }
                    /*
                        If Reached The End Of The Processes ArrayList And Ready Queue Is
                         Empty Add The First Process To Arrive Back
                    */ 
                	if (processID.indexOf(runningProcessIndex) == numOfProcesses-1 && readyQueue.isEmpty()) {
                        readyQueue.add(processID.get(0));
                    } 
                }
                else { 
                	for (int i = runningProcessIndex+1; i < numOfProcesses; i++) {
                        /*
                            If A New Process Arrived Add It To Ready, Followed By Adding 
                            The Recently Run Process 
                        */
                        if (arrivalTime.get(i)<=timer && !readyQueue.contains(processID.get(i))) {
                            readyQueue.add(processID.get(i)); 
                        }      
                    }
                    // If No Process Arrived Just Add The Recently Run Process Into The Queue
                    readyQueue.add(runningProcess);
                }
            }
        }

        /*
            Displaying The Output 
        */
        // Displaying The Gantt Chart
        System.out.println("\n");
        System.out.println("The Gantt Chart Associated With The Entered Set Of Processes: ");
        while (!timeSequence.isEmpty()) {
            System.out.print(timeSequence.remove()+ " |P"+ processSequence.remove()+"| ");

            // Print The Last Element In The Time Sequence 
            if (processSequence.isEmpty()) {
                System.out.print(timeSequence.remove());
            } 
        }

        System.out.println("\n");

        // Variable Declarations For Process Timings
        int waitingTime, turnAroundTime, responseTime;
        float sumOfWaitingTime = 0.0f, sumOfTurnAroundTime = 0.0f, sumOfResponseTime = 0.0f;
        
        for (int i = 0; i < numOfProcesses; i++) {

            // Calculating The Different Times
            responseTime = startingTime[i] - arrivalTimeDup[i];
            turnAroundTime = finishedTime[i] - arrivalTimeDup[i];
            waitingTime = finishedTime[i] - arrivalTimeDup[i] - burstTimeDup[i];

            // Adding The Times to The Sum Variables
            sumOfResponseTime += responseTime;
            sumOfTurnAroundTime += turnAroundTime;
            sumOfWaitingTime += waitingTime;

            // Printing The Information Of Each Process
            System.out.println("P" + processIDDup[i] + ": ");
            System.out.println("Turnaround Time = " + turnAroundTime);
            System.out.println("Response Time = " + responseTime);
            System.out.println("Waiting Time = " + waitingTime);
            System.out.println("\n");
        }

        System.out.println();

        // Calculating The Averages And Printing Them
        float avgTurnAroundTime = sumOfTurnAroundTime / numOfProcesses;
        float avgResponseTime = sumOfResponseTime / numOfProcesses;
        float avgWaitingTime = sumOfWaitingTime / numOfProcesses;

        System.out.println("Average Turnaround Time = " + avgTurnAroundTime);
        System.out.println("Average Response Time = " + avgResponseTime);
        System.out.println("Average Waiting Time = " + avgWaitingTime);
        System.out.println("\n");
    }
}
