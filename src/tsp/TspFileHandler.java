package tsp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * class TspFileHandler
 *
 * Reads TSP instances from files and decode it into graph notation.
 * Writes reports after a algorithm's execution.
 * 
 * @author david
 */
public class TspFileHandler {

    private String tspFileName;
    private String reportFileName;
    private PrintWriter pw;
    private BufferedReader br;

    public TspFileHandler() {
        br = null;
        pw = null;
    }

    public TspFileHandler(String _problem) {
        this();
        tspFileName = "src/problems/" + _problem;
        reportFileName = "src/reports/" + _problem + ".txt";
    }

    public TspFileHandler(String _problem, String _report) {
        this(_problem);
        reportFileName = "src/reports/" + _report + ".txt";
    }

    public double[][] read() throws Exception {
        br = new BufferedReader(new FileReader(tspFileName));

        String sCurrentLine, token;
        double graph[][], nodes[][];
        int dimension;
        double dx, dy;
        int i, j;

        graph = null;
        dimension = 0;

        // Decoding file header
        while ((sCurrentLine = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(sCurrentLine, " ");

            // finding it's dimension
            token = st.nextToken();
            if (token.equals("DIMENSION:")) {
                token = st.nextToken();
                dimension = Integer.parseInt(token);
                graph = new double[dimension][dimension];

            } else if (sCurrentLine.equals("NODE_COORD_SECTION")
                    || sCurrentLine.equals("EDGE_WEIGHT_SECTION")
                    || sCurrentLine.equals("DISPLAY_DATA_SECTION")) {
                break; // end of file header
            }
        }

        if (graph == null) {
            throw new IOException("Invalid or unknown file format");
        }

        // Reading edge values under "EDGE_WEIGHT_SECTION" standard
        if (sCurrentLine.equals("EDGE_WEIGHT_SECTION")) {
            
            for (i = 0; i < graph.length; i++) {
                sCurrentLine = br.readLine();
                StringTokenizer st = new StringTokenizer(sCurrentLine, " ");

                for (j = i + 1; j < graph.length; j++) {
                    token = st.nextToken();
                    graph[i][j] = graph[j][i] = Double.parseDouble(token);
                }
            }
        } else {
            // sCurrentLine == NODE_COORD_SECTION
            // Calculate Pitagoras' distance between each node.
            i = 0;
            nodes = new double[dimension][2];

            while (i < dimension && (sCurrentLine = br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(sCurrentLine, " ");
                st.nextToken(); // ignoring node index
                nodes[i][0] = Double.parseDouble(st.nextToken());
                nodes[i][1] = Double.parseDouble(st.nextToken());
                i++;
            }

            for (i = 0; i < graph.length; i++) {
                for (j = i +1; j < graph.length; j++) {
                    dx = Math.pow(nodes[i][0] - nodes[j][0], 2);
                    dy = Math.pow(nodes[i][1] - nodes[j][1], 2);

                    graph[i][j] = graph[j][i] = Math.sqrt(dx + dy);
                }
            }
        }

        br.close();
        br = null;

        return graph;
    }

    public void append(String _line) throws IOException {
        if (pw == null) {
            pw = new PrintWriter(new FileWriter(reportFileName));
        }
        
        pw.println(_line);
        pw.flush();
    }

    public void commit() {
        pw.close();
        pw = null;
    }
}
