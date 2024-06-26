package com.socialsim.controller.graphics.agent;

import com.socialsim.controller.graphics.Graphic;
import com.socialsim.model.core.agent.Agent;

import java.util.ArrayList;
import java.util.List;

public class AgentGraphic extends Graphic {

    // VARIABLES
    public static final String AGENTS_SPRITE_SHEET_URL = "com/socialsim/view/spritesheets/Agents Sprite Sheet.png";
    public static final String AGENTS_URL_1 = "com/socialsim/view/image/Office/office_agents_1.png";
    public static final String AGENTS_URL_2 = "com/socialsim/view/image/Office/office_agents_2.png";
    public static final String AGENTS_URL_3 = "com/socialsim/view/image/Office/office_agents_3.png";
    public static final String AGENTS_URL_4 = "com/socialsim/view/image/Office/office_agents_4.png";



    public static final List<AgentGraphicLocation> femaleStudentGraphics;
    public static final List<AgentGraphicLocation> maleStudentGraphics;
    public static final List<AgentGraphicLocation> femaleFacultyGraphics;
    public static final List<AgentGraphicLocation> maleFacultyGraphics;
    public static final List<AgentGraphicLocation> femaleDirectorGraphics;
    public static final List<AgentGraphicLocation> maleDirectorGraphics;
    public static final List<AgentGraphicLocation> femaleGuardGraphics;
    public static final List<AgentGraphicLocation> maleGuardGraphics;
    public static final List<AgentGraphicLocation> femaleMaintenanceGraphics;
    public static final List<AgentGraphicLocation> maleMaintenanceGraphics;


    static {
        /*** Student ***/
        femaleStudentGraphics = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                femaleStudentGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

        maleStudentGraphics = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            for (int j = 0; j <= 3; j++) {
                maleStudentGraphics.add(new AgentGraphicLocation(i, j));
            }
        }


        /*** Faculty ***/
        femaleFacultyGraphics = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 4; j <= 7; j++) {
                femaleFacultyGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

        maleFacultyGraphics = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            for (int j = 4; j <= 7; j++) {
                maleFacultyGraphics.add(new AgentGraphicLocation(i, j));
            }
        }


        /*** Director ***/
        femaleDirectorGraphics = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 8; j <= 11; j++) {
                femaleDirectorGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

        maleDirectorGraphics = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            for (int j = 8; j <= 11; j++) {
                maleDirectorGraphics.add(new AgentGraphicLocation(i, j));
            }
        }


        /*** Guard ***/
        femaleGuardGraphics = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 12; j <= 15; j++) {
                femaleGuardGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

        maleGuardGraphics = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            for (int j = 12; j <= 15; j++) {
                maleGuardGraphics.add(new AgentGraphicLocation(i, j));
            }
        }
        

        /*** Maintenance ***/
        femaleMaintenanceGraphics = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            for (int j = 16; j <= 19; j++) {
                femaleMaintenanceGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

        maleMaintenanceGraphics = new ArrayList<>();
        for (int i = 4; i <= 6; i++) {
            for (int j = 16; j <= 19; j++) {
                maleMaintenanceGraphics.add(new AgentGraphicLocation(i, j));
            }
        }

    }

    private final Agent agent;
    protected final List<AgentGraphicLocation> graphics;
    protected int graphicIndex;


    // CONSTRUCTOR(S)
    public AgentGraphic(Agent agent) {
        this.agent = agent;
        this.graphics = new ArrayList<>();

        List<AgentGraphicLocation> agentGraphics = null;


        /*** Student ***/
        if (agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleStudentGraphics;
        }
        else if (agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleStudentGraphics;
        }

        // Director
        else if (agent.getType() == Agent.Type.DIRECTOR && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleDirectorGraphics;
        }


        /*** Faculty ***/
        else if (agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleFacultyGraphics;
        }
        else if (agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleFacultyGraphics;
        }


        /*** Guard ***/
        else if (agent.getType() == Agent.Type.GUARD && agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleGuardGraphics;
        }
        else if (agent.getType() == Agent.Type.GUARD && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleGuardGraphics;
        }


        /*** Maintenance ***/
        else if (agent.getType() == Agent.Type.MAINTENANCE && agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleMaintenanceGraphics;
        }
        else if (agent.getType() == Agent.Type.MAINTENANCE && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleMaintenanceGraphics;
        }




        for (AgentGraphicLocation agentGraphicLocations : agentGraphics) {
            AgentGraphicLocation newAgentGraphicLocation = new AgentGraphicLocation(agentGraphicLocations.getGraphicRow(), agentGraphicLocations.getGraphicColumn());

            newAgentGraphicLocation.setGraphicWidth(1);
            newAgentGraphicLocation.setGraphicHeight(1);
            this.graphics.add(newAgentGraphicLocation);
        }

        this.graphicIndex = 2;
    }




    // METHOD
    public void change() {
        Agent agent = this.agent;

        double agentHeading = agent.getAgentMovement().getHeading();
        double agentHeadingDegrees = Math.toDegrees(agentHeading);

        if (agentHeadingDegrees >= 315 && agentHeadingDegrees < 360 || agentHeadingDegrees >= 0 && agentHeadingDegrees < 45) {
            if (this.agent.getAgentMovement().isInteracting()) {
                this.graphicIndex = 5;
            }
            else {
                this.graphicIndex = 1;
            }
        }
        else if (agentHeadingDegrees >= 45 && agentHeadingDegrees < 135) {
            if (this.agent.getAgentMovement().isInteracting()) {
                this.graphicIndex = 4;
            }
            else {
                this.graphicIndex = 0;
            }
        }
        else if (agentHeadingDegrees >= 135 && agentHeadingDegrees < 225) {
            if (this.agent.getAgentMovement().isInteracting()) {
                this.graphicIndex = 7;
            }
            else {
                this.graphicIndex = 3;
            }
        }
        else if (agentHeadingDegrees >= 225 && agentHeadingDegrees < 315) {
            if (this.agent.getAgentMovement().isInteracting()) {
                this.graphicIndex = 6;
            }
            else {
                this.graphicIndex = 2;
            }
        }
    }




    // GETTERS
    public Agent getAgent() {
        return agent;
    }
    public AgentGraphicLocation getGraphicLocation() {
        return this.graphics.get(this.graphicIndex);
    }



}
