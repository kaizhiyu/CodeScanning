package com.structurizr.view;

/**
 * A wrapper for automatic layout configuration.
 */
public final class AutomaticLayout {

    private RankDirection rankDirection;
    private int rankSeparation;
    private int nodeSeparation;
    private int edgeSeparation;
    private boolean vertices;

    AutomaticLayout() {
    }

    AutomaticLayout(RankDirection rankDirection, int rankSeparation, int nodeSeparation, int edgeSeparation, boolean vertices) {
        setRankDirection(rankDirection);
        setRankSeparation(rankSeparation);
        setNodeSeparation(nodeSeparation);
        setEdgeSeparation(edgeSeparation);
        setVertices(vertices);
    }

    /**
     * Gets the rank direction.
     *
     * @return  a RankDirection enum
     */
    public RankDirection getRankDirection() {
        return rankDirection;
    }

    void setRankDirection(RankDirection rankDirection) {
        if (rankDirection == null) {
            throw new IllegalArgumentException("A rank direction must be specified.");
        }

        this.rankDirection = rankDirection;
    }

    /**
     * Gets the rank separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getRankSeparation() {
        return rankSeparation;
    }

    void setRankSeparation(int rankSeparation) {
        if (rankSeparation < 0) {
            throw new IllegalArgumentException("The rank separation must be a positive integer.");
        }

        this.rankSeparation = rankSeparation;
    }

    /**
     * Gets the node separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getNodeSeparation() {
        return nodeSeparation;
    }

    void setNodeSeparation(int nodeSeparation) {
        if (nodeSeparation < 0) {
            throw new IllegalArgumentException("The node separation must be a positive integer.");
        }

        this.nodeSeparation = nodeSeparation;
    }

    /**
     * Gets the edge separation (in pixels).
     *
     * @return      a positive integer
     */
    public int getEdgeSeparation() {
        return edgeSeparation;
    }

    void setEdgeSeparation(int edgeSeparation) {
        if (edgeSeparation < 0) {
            throw new IllegalArgumentException("The edge separation must be a positive integer.");
        }

        this.edgeSeparation = edgeSeparation;
    }

    /**
     * Gets whether the automatic layout algorithm should create vertices.
     *
     * @return      a boolean
     */
    public boolean isVertices() {
        return vertices;
    }

    void setVertices(boolean vertices) {
        this.vertices = vertices;
    }

    public enum RankDirection {
        TopBottom,
        BottomTop,
        LeftRight,
        RightLeft
    }

}