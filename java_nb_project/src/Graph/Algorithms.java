package Graph;

/**
 *
 * @author lucasdavid
 *
 * Describes algorithms that can be used over TSP instances.
 *
 */
public class Algorithms {

    /**
     * @param _m: adjacent matrix of the current graph
     *
     * @return parent[]: a list of nodes parents which represents the minimal
     * spanning tree
     */
    public int[] Prim(double[][] _m) {
        int parent[], frj[];
        double cost[], mincost;

        cost = new double[_m.length];
        frj = new int[_m.length];
        parent = new int[_m.length];

        int w, v0;
        v0 = 0;

        for (w = 1; w < _m.length; w++) {
            parent[w] = -1;
            frj[w] = 0;
            cost[w] = _m[0][w];
        }
        parent[0] = 0;

        while (true) {
            mincost = -1;
            for (w = 0; w < _m.length; w++) {
                if (parent[w] == -1 && (mincost > cost[w] || mincost == -1)) {
                    mincost = cost[v0 = w];
                }
            }

            if (mincost == -1) {
                break;
            }

            parent[v0] = frj[v0];
            for (w = 0; w < _m.length; w++) {
                if (parent[w] == -1 && cost[w] > _m[v0][w]) {
                    cost[w] = _m[v0][w];
                    frj[w] = v0;
                }
            }
        }

        return parent;
    }

    /**
     * @param _m adjacent matrix of current graph _root initial node in graph _m
     *
     * @return parent[] list containing parents of nodes in graph _m which
     * represents the shortest path tree
     */
    public int[] Dijkstra(double[][] _m, int _root) {

        double mincost, cost[] = new double[_m.length];
        int frj[], parent[], w0;

        frj = new int[_m.length];
        parent = new int[_m.length];

        for (int w = 0; w < _m.length; w++) {
            parent[w] = -1;
            cost[w] = _m[_root][w];
            frj[w] = _root;
        }

        parent[_root] = _root;
        cost[_root] = 0;
        w0 = 0;

        while (true) {
            mincost = -1;

            for (int w = 0; w < _m.length; w++) {
                if (parent[w] != -1 || cost[w] == 0) {
                    continue;
                }

                if (mincost > cost[w] || mincost == -1) {
                    mincost = cost[w0 = w];
                }
            }

            if (mincost == -1) {
                break;
            }

            parent[w0] = frj[w0];
            for (int w = 0; w < _m.length; w++) {
                if (cost[w] > cost[w0] + _m[w0][w]) {
                    cost[w] = cost[w0] + _m[w0][w];
                    frj[w] = w0;
                }
            }
        }

        return parent;
    }

    /**
     * @param _parent list containing parents of each node in graph _m _root
     * initial node in _parent tree
     *
     * @return nodeList[] list of visiting sequence over _parent tree, starting
     * from node _root
     */
    public int[] DFS(int[] _parent, int _root) {

        // visiting sequence
        int vs[] = new int[2 * _parent.length - 1];

        innerDFS(_parent, _root, vs, 0);
        return vs;
    }

    /**
     * @param 
     * _parent 
     * _root root node in current recursive interaction
     * _vs   list which represents the visiting sequence in _parent tree
     * _i    first unused position of _vs list
     *
     * @return _i current valid position in _nodeLst[]
     */
    private int innerDFS(int[] _parent, int _root, int[] _vs, int _i) {
        _vs[_i++] = _root;

        for (int i = 0; i < _parent.length; i++) {
            if (i != _root && _parent[i] == _root) {
                _i = innerDFS(_parent, i, _vs, _i);
                _vs[_i++] = _root;
            }
        }

        return _i; // return current valid position
    }

    /**
     * @param _m adjacent matrix of current graph
     *
     * @return cost of a minimal circuit candidate
     */
    public double NearestNeighbor(double[][] _m) {
        double cost = 0;
        int current;

        int nodes[] = new int[_m.length];
        int nodesLength = _m.length - 1;

        for (int i = 0; i < nodes.length - 1; i++) {
            nodes[i] = i + 1;
        }
        current = nodes[nodes.length - 1] = 0;

        while (nodesLength > 0) {
            int j = 0;
            int nearest = nodes[0];
            for (int i = 1; i < nodesLength; i++) {
                if (_m[current][nearest] > _m[current][nodes[i]]) {
                    nearest = nodes[i];
                    j = i;
                }
            }

            cost += _m[current][nearest];
            nodes[j] = nodes[--nodesLength];
            current = nearest;
        }

        cost += _m[current][0];
        return cost;
    }

    /**
     * @param _m adjacent matrix of current graph
     *
     * @return cost of a minimal circuit candidate
     */
    public double TwiceAround(double[][] _m) {

        int _mst[] = Prim(_m);
        int visitedNodes[] = DFS(_mst, 0);
        int lstLim = 2 * _m.length - 1;

        int i = 0;
        while (i < lstLim - 1) {
            int j = i + 1;

            while (j < lstLim - 1) {
                if (visitedNodes[j] != visitedNodes[i]) {
                    j++;
                } else {
                    for (int k = j; k < lstLim - 1; k++) {
                        visitedNodes[k] = visitedNodes[k + 1];
                    }
                    lstLim--;
                }
            }
            i++;
        }

        double cost = 0;
        for (i = 1; i < lstLim; i++) {
            cost += _m[ visitedNodes[i - 1]][ visitedNodes[i]];
        }

        return cost;
    }

    /**
     * @param _m adjacent matrix of current graph
     *
     * @return cost of a minimal circuit candidate
     */
    public double TwiceAroundWDijkstra(double[][] _m) {

        int _mst[] = Dijkstra(_m, 0);
        int visitedNodes[] = DFS(_mst, 0);
        int lstLim = 2 * _m.length - 1;

        double cost = 0;

        int i = 0;
        while (i < visitedNodes.length - 1) {
            int j = i + 1;

            while (j < lstLim - 1) {
                if (visitedNodes[j] != visitedNodes[i]) {
                    j++;
                } else {
                    for (int k = j; k < lstLim - 1; k++) {
                        visitedNodes[k] = visitedNodes[k + 1];
                    }
                    lstLim--;
                }
            }
            i++;
        }

        for (i = 1; i < lstLim; i++) {
            cost += _m[visitedNodes[i - 1]][visitedNodes[i]];
        }

        return cost;
    }

    /**
     * @param _m adjacent matrix of current graph
     *
     * @return cost of a minimal circuit candidate
     */
    public double EdgeScore(double[][] _m) {
        int edgeScore[][] = new int[_m.length][_m.length];
        boolean reacheds[] = new boolean[_m.length];
        int insertedNodes = 1;
        int current = 0;

        for (int i = 0; i < _m.length; i++) {
            for (int j = 0; j < _m.length; j++) {
                edgeScore[i][j] = 0;
            }
        }

        for (int k = 0; k < _m.length; k++) {
            int spt[] = Dijkstra(_m, k);

            for (int i = 0; i < k; i++) {
                edgeScore[i][spt[i]]++;
            }
            // ignores node k
            for (int i = k + 1; i < _m.length; i++) {
                edgeScore[i][spt[i]]++;
            }
        }

        for (int i = 0; i < _m.length; i++) {
            reacheds[i] = false;
        }

        int reached;
        double cost = 0;
        while (insertedNodes < _m.length) {
            reacheds[reached = current] = true;

            for (int i = 0; i < _m.length; i++) {
                if (reacheds[i] == true) {
                    continue;
                }
                if (edgeScore[current][i] > edgeScore[current][reached]) {
                    reached = i;
                } else if (edgeScore[current][i] == edgeScore[current][reached]
                        && _m[current][i] <= _m[current][reached]) {
                    reached = i;
                }
            }

            insertedNodes++;
            reacheds[reached] = true;
            cost += _m[current][reached];
            current = reached;
        }

        cost += _m[current][0];
        return cost;
    }
}
