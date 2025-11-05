//FIFO Page Replacement Algorithm

import java.util.*;

public class FIFO_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of frames
        System.out.print("Enter number of frames: ");
        int framesCount = sc.nextInt();

        // Input number of pages
        System.out.print("Enter number of pages: ");
        int pagesCount = sc.nextInt();

        int[] pages = new int[pagesCount];

        // Input reference string
        System.out.println("Enter the reference string:");
        for (int i = 0; i < pagesCount; i++) {
            pages[i] = sc.nextInt();
        }

        Queue<Integer> frames = new LinkedList<>();
        int pageFaults = 0, pageHits = 0;

        System.out.println("\nPage Replacement Process (FIFO):");
        for (int currentPage : pages) {
            if (frames.contains(currentPage)) {
                // Page Hit
                pageHits++;
                System.out.println("Page " + currentPage + " => HIT   | Frames: " + frames);
            } else {
                // Page Fault
                pageFaults++;
                if (frames.size() == framesCount) {
                    int removed = frames.poll(); // remove oldest page
                    System.out.println("Page " + removed + " removed.");
                }
                frames.add(currentPage);
                System.out.println("Page " + currentPage + " => FAULT | Frames: " + frames);
            }
        }

        System.out.println("\nTotal Page Hits   = " + pageHits);
        System.out.println("Total Page Faults = " + pageFaults);
    }
}
/*Enter number of frames: 3
Enter number of pages: 9
Enter the reference string:
1 2 3 4 1 2 5 1 2*/


//LRU PAGE REPLACEMENT ALGORITHM

import java.util.*;

class LRU_PageReplacement {
    int[] p, fr, fs;
    int n, m, frsize = 3, pf = 0;
    Scanner src = new Scanner(System.in);

    void read() {
        System.out.println("Enter number of pages in reference string:");
        n = src.nextInt();
        p = new int[n];

        System.out.println("Enter elements of page reference string:");
        for (int i = 0; i < n; i++) {
            p[i] = src.nextInt();
        }

        System.out.println("Enter frame size:");
        m = src.nextInt();
        fr = new int[m];
        fs = new int[m];
    }

    void display() {
        for (int i = 0; i < m; i++) {
            if (fr[i] == -1)
                System.out.print("[ ] ");
            else
                System.out.print("[" + fr[i] + "] ");
        }
        System.out.println();
    }

    void lru() {
        Arrays.fill(fr, -1);

        for (int j = 0; j < n; j++) {
            int current = p[j];
            boolean hit = false;

            // Check if page is already in frame (HIT)
            for (int i = 0; i < m; i++) {
                if (fr[i] == current) {
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                // Check for empty frame
                boolean placed = false;
                for (int i = 0; i < m; i++) {
                    if (fr[i] == -1) {
                        fr[i] = current;
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    Arrays.fill(fs, 0);
                    // Mark pages used recently
                    for (int k = j - 1, count = 1; count <= frsize - 1 && k >= 0; count++, k--) {
                        for (int i = 0; i < m; i++) {
                            if (fr[i] == p[k])
                                fs[i] = 1;
                        }
                    }

                    int replaceIndex = -1;
                    for (int i = 0; i < m; i++) {
                        if (fs[i] == 0) {
                            replaceIndex = i;
                            break;
                        }
                    }
                    fr[replaceIndex] = current;
                }

                pf++;
            }

            System.out.print("Page " + current + " => ");
            display();
        }
        System.out.println("\nTotal Page Faults: " + pf);
    }

    public static void main(String[] args) {
        LRU_PageReplacement algo = new LRU_PageReplacement();
        algo.read();
        algo.lru();
    }
}
/*Enter number of frames: 3
Enter number of pages: 9
Enter the reference string:
1 2 3 4 1 2 5 1 2 */

//Optimal Page Replacement Algorithm

import java.util.*;

public class Optimal_PageReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input number of frames
        System.out.print("Enter number of frames: ");
        int frames = sc.nextInt();

        // Input number of pages
        System.out.print("Enter number of pages: ");
        int n = sc.nextInt();

        int[] pages = new int[n];
        System.out.println("Enter the page reference string:");
        for (int i = 0; i < n; i++) {
            pages[i] = sc.nextInt();
        }

        List<Integer> memory = new ArrayList<>();
        int pageFaults = 0;

        for (int i = 0; i < n; i++) {
            int currentPage = pages[i];

            // Page hit
            if (memory.contains(currentPage)) {
                System.out.println("Step " + (i + 1) + " -> " + memory);
                continue;
            }

            // Page fault
            if (memory.size() < frames) {
                memory.add(currentPage);
            } else {
                // Find page to replace (farthest future use)
                int farthestIndex = -1;
                int pageToReplace = -1;

                for (int page : memory) {
                    int nextUse = Integer.MAX_VALUE;
                    for (int j = i + 1; j < n; j++) {
                        if (pages[j] == page) {
                            nextUse = j;
                            break;
                        }
                    }
                    if (nextUse == Integer.MAX_VALUE) {
                        pageToReplace = page;
                        break;
                    }
                    if (nextUse > farthestIndex) {
                        farthestIndex = nextUse;
                        pageToReplace = page;
                    }
                }

                memory.remove(Integer.valueOf(pageToReplace));
                memory.add(currentPage);
            }

            pageFaults++;
            System.out.println("Step " + (i + 1) + " -> " + memory);
        }

        System.out.println("Total Page Faults: " + pageFaults);
    }
}
/*Enter number of frames: 3
Enter number of pages: 9
Enter the reference string:
1 2 3 4 1 2 5 1 2 */
