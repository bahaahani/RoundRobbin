import java.util.*;
//FAKE NOT WORKING!!!!
public class RoundRobin {
    public static void main(String[] args) {
        int quantum;
        ArrayList<Integer> processID = new ArrayList<>();
        ArrayList<Integer> arrivalTime = new ArrayList<>();
        ArrayList<Integer> burstTime = new ArrayList<>();

        quantum = getInput(processID, arrivalTime, burstTime);

        sortByArrivalTime(processID, arrivalTime, burstTime);

        int[] start = new int[arrivalTime.size()];
        int[] finish = new int[arrivalTime.size()];
        Queue<Integer> ganttChartProcessList = new LinkedList<>();
        Queue<Integer> timeLine = new LinkedList<>();
        ArrayList<Integer> readyQueue = new ArrayList<>();

        executeProcesses(processID, arrivalTime, burstTime, quantum, start, finish, ganttChartProcessList, timeLine, readyQueue);

        printResults(processID, arrivalTime, burstTime, start, finish, ganttChartProcessList, timeLine);
    }

    public static int getInput(ArrayList<Integer> processID, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime) {
        Scanner kb = new Scanner(System.in);
        int quantum;
        System.out.println("Enter quantum number:");
        quantum = kb.nextInt();
        System.out.println("Enter process ID, arrival time and burst time; enter 0 0 0 to stop.");
        while (true) {
            int enteredProcessID = kb.nextInt();
            int enteredArrivalTime = kb.nextInt();
            int enteredBurstTime = kb.nextInt();
            if (enteredProcessID == 0 && enteredArrivalTime == 0 && enteredBurstTime == 0) {
                break;
            }
            processID.add(enteredProcessID);
            arrivalTime.add(enteredArrivalTime);
            burstTime.add(enteredBurstTime);
        }
        return quantum;
    }

    public static void sortByArrivalTime(ArrayList<Integer> processID, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime) {
        int n = arrivalTime.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arrivalTime.get(j) > arrivalTime.get(j + 1)) {
                    swap(j, j + 1, arrivalTime);
                    swap(j, j + 1, burstTime);
                    swap(j, j + 1, processID);
                }
            }
        }
    }

    public static void swap(int i, int j, ArrayList<Integer> list) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    public static void executeProcesses(ArrayList<Integer> processID, ArrayList<Integer> arrivalTime, ArrayList<Integer> burstTime, int quantum, int[] start, int[] finish, Queue<Integer> ganttChartProcessList, Queue<Integer> timeLine, ArrayList<Integer> readyQueue) {
        int currentRunningProcess;
        int index;
        int time = arrivalTime.get(0);
        timeLine.add(time);
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
                } else if (burstTime.get(index) < quantum && burstTime.get(index) != 0) {
                    ganttChartProcessList.add(processID.get(index));
                    time += burstTime.get(index);
                    timeLine.add(time);
                    burstTime.set(index, 0);
                    finish[index] = time;
                }

                if (burstTime.get(index) != 0) {
                    for (int i = index+1; i<arrivalTime.size(); i++) {
                        if (arrivalTime.get(i) <= time)
                            if (!readyQueue.contains(processID.get(i)))
                                readyQueue.add(processID.get(i));
                    }
                    readyQueue.add(currentRunningProcess);
                } else {
                    for (int i = index+1; i<arrivalTime.size(); i++) {
                        if (arrivalTime.get(i) <= time)
                            if (!readyQueue.contains(processID.get(i)))
                                readyQueue.add(processID.get(i));
                    }
                    if (index == processID.size()-1 && readyQueue.isEmpty()) {
                        readyQueue.add(processID.get(0));
                    }
                }
            } else if (arrivalTime.get(index) > time) {
                time++;
                if (readyQueue.isEmpty() || (readyQueue.size() == 1 && readyQueue.contains(currentRunningProcess))) {
                    readyQueue.add(processID.get(index));
                }
            }
        }
    }

    public static void printResults(ArrayList<Integer> processID, ArrayList<Integer> arrivalTimeCopy, ArrayList<Integer> burstTimeCopy, int[] start, int[] finish, Queue<Integer> ganttChartProcessList, Queue<Integer> timeLine) {

        System.out.println("Round Robin (RR) scheduling algorithm");
        System.out.println("Gantt chart: ");
        while (!timeLine.isEmpty()) {
            System.out.print(timeLine.remove() + " | p" + ganttChartProcessList.remove() + " | ");
            if (ganttChartProcessList.isEmpty()) {
                System.out.print(timeLine.remove());
            }
        }

        System.out.println();
        System.out.println();
        int waitingTime, turnAroundTime, responseTime;
        double waitingTimeTotal = 0, turnAroundTimeTotal = 0, responseTimeTotal = 0;
        int n = processID.size();
        ArrayList<Integer> processIDCopy = new ArrayList<>(processID);
        for (int i = 0; i<n; i++) {
            responseTime = start[i] - arrivalTimeCopy.get(i);
            responseTimeTotal += responseTime;
            turnAroundTime = finish[i] - arrivalTimeCopy.get(i);
            turnAroundTimeTotal += turnAroundTime;
            waitingTime = turnAroundTime - burstTimeCopy.get(i);
            waitingTimeTotal += waitingTime;
            System.out.println("P" + processIDCopy.get(i) + ":");
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