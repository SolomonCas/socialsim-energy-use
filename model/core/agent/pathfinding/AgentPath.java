package com.socialsim.model.core.agent.pathfinding;

import com.socialsim.model.core.environment.Patch;

import java.util.Arrays;
import java.util.Stack;

public class AgentPath extends PathfindingResult{

    private final Stack<Patch> path;

    public AgentPath(AgentPath agentPath) {
        super(agentPath.getDistance());

        this.path = (Stack<Patch>) agentPath.path.clone();
    }

    public AgentPath(double distance, Stack<Patch> path) {
        super(distance);

        this.path = path;
    }

    public Stack<Patch> getPath() {
        return path;
    }

    @Override
    public String toString(){
        return Arrays.toString(path.toArray());
    }

}