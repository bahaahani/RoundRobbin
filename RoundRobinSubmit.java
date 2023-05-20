/*
 *          Round Robin Scheduling Algorithm
 * Name: Bahaa Hani xxxxxxx     ID: xxxxxxxxx   Section: x
 * Name: Sayed Ahmed Khalaf     ID: 202007602   Section:2 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
*/

 // Required Classes
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class RoundRobinSubmit {
    public static void main(String[] args){

        // Creating A Scanner Object To Get User Input
        Scanner kb = new Scanner(System.in);

        // Declarations
        int quantum, enteredProcessID, enteredArrivalTime, enteredBurstTime;
        boolean exit = false;
        ArrayList<Integer> processID = new ArrayList<Integer>();
        ArrayList<Integer> arrivalTime = new ArrayList<Integer>();
        ArrayList<Integer> burstTime = new ArrayList<Integer>();
        ArrayList<Integer> readyQueue = new ArrayList<Integer>();

        System.out.println("\n\tRound Robin Scheduling Algorithm\t\n");
    
        // Quantum Time Input
        System.out.print("Enter the time quantum (q) = ");
        quantum = kb.nextInt();

        /*
            Process Information Input (processes identifier, arrival time, and burst time)
            Inputing 0 0 0 exits the loop, and thus stopping the input   
        */  
        System.out.println();  
        System.out.println("Enter Process ID, Arrival Time, and Burst time:\n" + "(Entering Three Consecutive 0's Stops The Input)");

        while (!exit) {
            // Reading User Input
            enteredProcessID = kb.nextInt();
            enteredArrivalTime = kb.nextInt();
            enteredBurstTime = kb.nextInt();

            // Checking End Of Input
            if (enteredProcessID == 0 && enteredArrivalTime == 0 && enteredBurstTime == 0) {
            	exit = true;
            }
            // Adding Process Information To Its Corresponding ArrayList 
            else {
                processID.add(enteredProcessID);
                arrivalTime.add(enteredArrivalTime);
                burstTime.add(enteredBurstTime);
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

					// Sort The Arrival Time ArrayList
 					arrivalTime.set(j, arrivalTime.get(j+1));
					arrivalTime.set(j+1, tempArrivalTime);
					
					// Updating The Burst Time ArrayList
					burstTime.set(j, burstTime.get(j+1));
					burstTime.set(j+1, tempBurstTime);
					
					// Updating The Process Identifier ArrayList
					processID.set(j,  processID.get(j+1));
					processID.set(j+1, tempProcessesID);
				}
				
        /*
            Declarations Necessary For Performing The Calculations
        */ 
        int time=0, currentRunningProcess=0, currentRunningIndex=0;
		int[] start = new int[arrivalTime.size()];
        int[] finish = new int[arrivalTime.size()];

        // Declaring The Copy Arrays 
        int[] arrivalTimeCopy = new int[arrivalTime.size()];
        int[] burstTimeCopy = new int[burstTime.size()];
        int[] processIDCopy = new int[processID.size()];
        /*
            Copying The Previously Sorted ArrayLists Into The Copy Arrays
            To Preserve The Original Values Entered By The User        
        */ 
        for (int i = 0; i<arrivalTime.size();i++)
        {
            arrivalTimeCopy[i] = arrivalTime.get(i);
            burstTimeCopy[i] = burstTime.get(i);
            processIDCopy[i] = processID.get(i);
        }

        /* 
            Implementing The Queue Interface Using The LinkedList Class
        */ 
        // Storing The Time Sequence Of The Processes
        Queue<Integer> timeLine = new LinkedList<>();
        // Storing The Process Sequence
        Queue<Integer> ganttChartProcessList = new LinkedList<>();

        // Adding The First Process To Arrive Into The Ready Queue 
        timeLine.add(arrivalTime.get(0));
        readyQueue.add(processID.get(0));
        start[0] = arrivalTime.get(0);

        // Enter The Loop If There Exists Some Processes In The Ready Queue
        while (!readyQueue.isEmpty()) {

            // Remove The Process To Be The Next Running From The Ready Queue And Store Its Index
            currentRunningProcess = readyQueue.remove(0);
            currentRunningIndex = processID.indexOf(currentRunningProcess);

            /*
                Checking If The Arrival Time Of The Process To Run Is Possible
                With Reference To The Timer. Else Go To The Else Block 
            */
            if (arrivalTime.get(currentRunningIndex) <= time) {

                // Adding The Process Start Time, If It Is Its First CPU Visit   
                if (!ganttChartProcessList.contains(processID.get(currentRunningIndex))) {
                    start[currentRunningIndex] = time;
                }
                
                /*
                    If The Process Burst Time is Greater Than Quantum Time, Run it for 
                    The Time Quantum While Decreasing Its Burst Time.
                */
                if (burstTime.get(currentRunningIndex) >= quantum) {
                    ganttChartProcessList.add(processID.get(currentRunningIndex));
                    burstTime.set(currentRunningIndex, burstTime.get(currentRunningIndex) - quantum);
                    time += quantum;
                    timeLine.add(time);
                    /*
                        If The  Process Burst Time is Equal To The Quantum Time, Run it for 
                        The Time Quantum While Decreasing Its Burst Time and Store Its Finish Time.
                    */
                    if (burstTime.get(currentRunningIndex) == 0) {
                        finish[currentRunningIndex] = time;
                    }     
                }
                /*
                    Else If It is less Than the Quantum Time, Run It For Its Burst Time and
                    Store The Finish Time  
                */
                else if (burstTime.get(currentRunningIndex) < quantum && burstTime.get(currentRunningIndex) != 0) {
                	ganttChartProcessList.add(processID.get(currentRunningIndex));
                    time += burstTime.get(currentRunningIndex);
                    timeLine.add(time);
                    burstTime.set(currentRunningIndex, 0);
                    finish[currentRunningIndex] = time;
                }

                /*
                    Modifying The Ready Queue After The Running Of The Process. 
                    If The Process Has Not Finished Execution, and
                    If It Did Finish Execution
                */
                if (burstTime.get(currentRunningIndex) != 0) {
                    for (int i = currentRunningIndex+1; i < numOfProcesses; i++) {
                        /*
                            If A New Process Arrived Add It To Ready, Followed By Adding 
                            The Recently Run Process 
                        */
                        if (arrivalTime.get(i)<=time)
                            if (!readyQueue.contains(processID.get(i))) {
                                readyQueue.add(processID.get(i));
                            }
                    }
                    // If No Process Arrived Just Add The Recently Run Process Into The Queue
                    readyQueue.add(currentRunningProcess);
                }
                else { 
                	for (int i = currentRunningIndex+1; i < numOfProcesses; i++) {
                        /*
                            If A New Process Arrived Add It To Ready, Followed By Adding 
                            The Recently Run Process 
                        */
                        if (arrivalTime.get(i)<=time)
                            if (!readyQueue.contains(processID.get(i))) {
                                readyQueue.add(processID.get(i));
                            }
                    }
                    /*
                        If Reached The End Of The Processes ArrayList And Ready Queue Is
                         Empty Add The First Process To Arrive Back
                    */ 
                	if (processID.indexOf(currentRunningIndex) == numOfProcesses-1 && readyQueue.isEmpty()) {
                        readyQueue.add(processID.get(0));
                    } 
                }
            }

            /*
                Else Increment The Timer And Return The Process Back To the Ready Queue
                If the Queue Is Empty
            */
            else if (arrivalTime.get(currentRunningIndex) > time) {
                	time++;
                	if (readyQueue.isEmpty()) {
                        readyQueue.add(processID.get(currentRunningIndex));
                    }		
            }
        }

        /*
            Displaying The Output 
        */
        // Displaying The Gantt Chart
        System.out.println("The Gantt Chart Associated With The Given Set Of Processes: ");
        while (!timeLine.isEmpty()) {
            System.out.print(timeLine.remove()+ " | p"+ ganttChartProcessList.remove()+" | ");
            if (ganttChartProcessList.isEmpty()) {
                System.out.print(timeLine.remove());
            } 
        }

        System.out.println("\n");

        // Variable Declarations
        int waitingTime, turnAroundTime, responseTime;
        float sumOfWaitingTime = 0.0f, sumOfTurnAroundTime = 0.0f, sumOfResponseTime = 0.0f;
        
        for (int i = 0; i<arrivalTime.size(); i++) {

            // Calculating Response Time
            responseTime = start[i] - arrivalTimeCopy[i];
            sumOfResponseTime += responseTime;

            // Calculating Turn Around Time
            turnAroundTime = finish[i] - arrivalTimeCopy[i];
            sumOfTurnAroundTime += turnAroundTime;

            // Calculating Waiting Time
            waitingTime = turnAroundTime - burstTimeCopy[i];
            sumOfWaitingTime += waitingTime;

            // Printing The Information Of Each Process
            System.out.println("P" + processIDCopy[i] + ":");
            System.out.println("--------------------");
            System.out.println("Turnaround Time = " + turnAroundTime);
            System.out.println("Response Time = " + responseTime);
            System.out.println("Waiting Time = " + waitingTime);
            System.out.println("---------------------");
            System.out.println();
        }

        System.out.println("\n");

        // Calculating The Averages And Printing Them
        float avgTurnAroundTime = sumOfTurnAroundTime / numOfProcesses;
        float avgResponseTime = sumOfResponseTime / numOfProcesses;
        float avgWaitingTime = sumOfWaitingTime / numOfProcesses;

        System.out.println("Average Time for all processes:");
        System.out.println("Average Turnaround Time = " + avgTurnAroundTime);
        System.out.println("Average Response Time = " + avgResponseTime);
        System.out.println("Average Waiting Time = " + avgWaitingTime);


    }
}
