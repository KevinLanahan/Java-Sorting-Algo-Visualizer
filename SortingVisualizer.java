import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Graphics;
import java.util.Random;

public class SortingVisualizer {
    private static int[] array;
    private static JPanel graph;
    private static JSlider slider;
    private static String sortingAlgorithm = "Insertion Sort";
    private static int currentIndex = -1;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(600, 600);
        frame.setTitle("Algorithm Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(99, 99, 99));
        frame.setResizable(true);

        JPanel panel1 = new JPanel();
        JLabel title = new JLabel("Visualization of Sorting Algorithms");
        title.setFont(new Font("Comic Sans", Font.PLAIN, 24));
        panel1.add(title);

        frame.add(panel1, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton button1 = new JButton("Insertion Sort");
        JButton button2 = new JButton("Bubble Sort");
        JButton button3 = new JButton("Selection Sort");
        JButton button4 = new JButton("Quick Sort");
        JButton button5 = new JButton("Merge Sort");
        JButton startButton = new JButton("Start");
        JButton resetButton = new JButton("Reset");

        initializeArray(3);

        button1.addActionListener(e -> sortingAlgorithm = "Insertion Sort");
        button2.addActionListener(e -> sortingAlgorithm = "Bubble Sort");
        button3.addActionListener(e -> sortingAlgorithm = "Selection Sort");
        button4.addActionListener(e -> sortingAlgorithm = "Quick Sort");
        button5.addActionListener(e -> sortingAlgorithm = "Merge Sort");

        startButton.addActionListener(e -> {
            currentIndex = -1; 
            new Thread(() -> {
                switch (sortingAlgorithm) {
                    case "Insertion Sort":
                        insertionSort();
                        break;
                    case "Bubble Sort":
                        bubbleSort();
                        break;
                    case "Selection Sort":
                        selectionSort();
                        break;
                    case "Quick Sort":
                        quickSort(0, array.length - 1);
                        break;
                    case "Merge Sort":
                        mergeSort(0, array.length - 1);
                        break;
                }
            }).start();
        });

        resetButton.addActionListener(e -> {
            initializeArray(slider.getValue());
            graph.repaint();
        });

        buttonPanel.add(button1);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(button2);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(button3);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(button4);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(button5);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonPanel.add(resetButton);

        frame.add(buttonPanel, BorderLayout.WEST);

        graph = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawArray(g);
            }
        };
        graph.setBackground(Color.WHITE);
        frame.add(graph, BorderLayout.CENTER);

        slider = new JSlider(3, 20, 3);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPreferredSize(new Dimension(400, 50));

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int value = source.getValue();
                initializeArray(value);
                graph.repaint();
            }
        });
        graph.add(slider);

        frame.setVisible(true);
    }

    private static void initializeArray(int size) {
        array = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(100) + 1; 
        }
    }

    private static void drawArray(Graphics g) {
        int width = graph.getWidth() / array.length;
        for (int i = 0; i < array.length; i++) {
            int height = array[i] * 3; 
            if (i == currentIndex) {
                g.setColor(Color.RED); 
            } else {
                g.setColor(Color.BLUE); 
            }
            g.fillRect(i * width, graph.getHeight() - height, width - 2, height);
        }
    }

    private static void insertionSort() {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            currentIndex = i; 

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
                graph.repaint();
                pause();
            }
            array[j + 1] = key;
            graph.repaint();
            pause();
        }
        currentIndex = -1;
    }

    private static void bubbleSort() {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                currentIndex = j;
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
                graph.repaint();
                pause();
            }
        }
        currentIndex = -1; 
    }

    private static void selectionSort() {
        for (int i = 0; i < array.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < array.length; j++) {
                currentIndex = j; 
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
            graph.repaint();
            pause();
        }
        currentIndex = -1; 
    }

    private static void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
            graph.repaint();
            pause();
        }
    }

    private static int partition(int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            currentIndex = j; 
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                graph.repaint();
                pause();
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        graph.repaint();
        return i + 1;
    }

    private static void mergeSort(int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;
            mergeSort(left, middle);
            mergeSort(middle + 1, right);
            merge(left, middle, right);
            graph.repaint();
            pause();
        }
    }

    private static void merge(int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++)
            L[i] = array[left + i];
        for (int j = 0; j < n2; j++)
            R[j] = array[middle + 1 + j];

        int i = 0, j = 0;
        int k = left;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
            graph.repaint();
            pause();
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
            graph.repaint();
            pause();
        }

        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
            graph.repaint();
            pause();
        }
    }

    private static void pause() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
