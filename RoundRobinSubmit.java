/*
 *          Round Robin Scheduling Algorithm
 * Name: Bahaa Hani xxxxxxx     ID: xxxxxxxxx   Section: x
 * Name: Sayed Ahmed Khalaf     ID: 202007602   Section:2 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
 * Name: xxxxxxxxxxxxxxxxxx     ID: xxxxxxxxx   Section:x 
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.*;
public class RoundRobinSubmit {
    public static void main(String[] args){
        Scanner kb = new Scanner(System.in);
        int quantum;
        ArrayList<Integer> processID = new ArrayList<Integer>();
        ArrayList<Integer> arrivalTime = new ArrayList<Integer>();
        ArrayList<Integer> burstTime = new ArrayList<Integer>();
        
        Queue<Integer> ganttChartProcessList = new LinkedList<>();
        Queue<Integer> timeLine = new LinkedList<>();
        ArrayList<Integer> readyQueue = new ArrayList<Integer>();
        int enteredProcessID, enteredArrivalTime, enteredBurstTime;
        boolean exit = false;
        System.out.println("Enter quantum number:");
        quantum = kb.nextInt();
        System.out.println("Enter process ID, arrival time and burst time; enter 0 0 0 to stop.");
        while (!exit) {
            enteredProcessID = kb.nextInt();
            enteredArrivalTime = kb.nextInt();
            enteredBurstTime = kb.nextInt();
            if (enteredProcessID == 0 && enteredArrivalTime == 0 && enteredBurstTime == 0) {
            	exit = true;
            } else {
                processID.add(enteredProcessID);
                arrivalTime.add(enteredArrivalTime);
                burstTime.add(enteredBurstTime);
            }
        }
        
        

        int n = arrivalTime.size();
		for(int i=0; i<n - 1; i++){
			for(int j=0; j<n - i -1; j++) {
				if(arrivalTime.get(j) > arrivalTime.get(j+1)) {
					int temp1 = arrivalTime.get(j);
					int temp2 = burstTime.get(j);
					int temp3 = processID.get(j);
					// sort process Arrival Time
					arrivalTime.set(j, arrivalTime.get(j+1));
					arrivalTime.set(j+1, temp1);
					
					// sort process burst time
					burstTime.set(j, burstTime.get(j+1));
					burstTime.set(j+1, temp2);
					
					// sort process id
					processID.set(j,  processID.get(j+1));
					processID.set(j+1, temp3);
				}
				
				
			}
        }
		
		int[] start = new int[arrivalTime.size()];
        int[] finish = new int[arrivalTime.size()];
        int[] arrivalTimeCopy = new int[arrivalTime.size()];
        int[] burstTimeCopy = new int[burstTime.size()];
        int[] processIDCopy = new int[processID.size()];
        for (int i = 0; i<arrivalTime.size();i++)
        {
            arrivalTimeCopy[i] = arrivalTime.get(i);
            burstTimeCopy[i] = burstTime.get(i);
            processIDCopy[i] = processID.get(i);
        }
        int time = 0;
        int currentRunningProcess = 0;
        int index = 0;
        
        timeLine.add(arrivalTime.get(0));
        readyQueue.add(processID.get(0));
        start[0] = arrivalTime.get(0);
        while (!readyQueue.isEmpty()) {
            currentRunningProcess = readyQueue.remove(0);
            index = processID.indexOf(currentRunningProcess);
            if (arrivalTime.get(index) <= time) {
                if (!ganttChartProcessList.contains(processID.get(index)))
                    start[index] = time;
                if (burstTime.get(index) >= quantum) {
                    ganttChartProcessList.add(processID.get(index));
                    burstTime.set(index, burstTime.get(index) - quantum);
                    time += quantum;
                    timeLine.add(time);
                    if (burstTime.get(index) == 0)
                         finish[index] = time;
                }else if (burstTime.get(index) < quantum && burstTime.get(index) != 0) {
                	ganttChartProcessList.add(processID.get(index));
                    time += burstTime.get(index);
                    timeLine.add(time);
                    burstTime.set(index, 0);
                    finish[index] = time;
                }


                if (burstTime.get(index) != 0)
                {
                    for (int i = index+1; i<arrivalTime.size();i++)
                    {
                        if (arrivalTime.get(i)<=time)
                            if (!readyQueue.contains(processID.get(i)))
                            {
                                readyQueue.add(processID.get(i));
                            }

                    }
                    readyQueue.add(currentRunningProcess);
                }else { 
                	for (int i = index+1; i<arrivalTime.size();i++)
                    {
                        if (arrivalTime.get(i)<=time)
                            if (!readyQueue.contains(processID.get(i)))
                            {
                                readyQueue.add(processID.get(i));
                            }

                    }
                	
                	if (processID.indexOf(index) == processID.size()-1 && readyQueue.isEmpty()) 
                		readyQueue.add(processID.get(0));
                	
                }
            }else if (arrivalTime.get(index) > time){
                	time++;
                	if (readyQueue.isEmpty())
                		readyQueue.add(processID.get(index));
            }

        }

        System.out.println("Round Robin (RR) scheduling algorithm");
        System.out.println("Gantt chart: ");
        while (!timeLine.isEmpty())
        {

            System.out.print(timeLine.remove()+ " | p"+ ganttChartProcessList.remove()+" | ");
            if (ganttChartProcessList.isEmpty())
                System.out.print(timeLine.remove());
        }

        System.out.println();
        System.out.println();
        int waitingTime, turnAroundTime, responseTime;
        double waitingTimeTotal = 0, turnAroundTimeTotal = 0, responseTimeTotal = 0;
        
        for (int i = 0; i<arrivalTime.size(); i++) {
            responseTime = start[i] - arrivalTimeCopy[i];
            responseTimeTotal += responseTime;
            turnAroundTime = finish[i] - arrivalTimeCopy[i];
            turnAroundTimeTotal += turnAroundTime;
            waitingTime = turnAroundTime - burstTimeCopy[i];
            waitingTimeTotal += waitingTime;
            System.out.println("P" + processIDCopy[i] + ":");
            System.out.println("--------------------");
            System.out.println("Turnaround Time = " + turnAroundTime);
            System.out.println("Response Time = " + responseTime);
            System.out.println("Waiting Time = " + waitingTime);
            System.out.println("---------------------");
            System.out.println();

        }
        System.out.println();
        System.out.println("Average Time for all processes:");
        System.out.println("Turnaround time average: " + turnAroundTimeTotal/n);
        System.out.println("Response time average: " + responseTimeTotal/n);
        System.out.println("Waiting time average: " + waitingTimeTotal/n);


    }
}
