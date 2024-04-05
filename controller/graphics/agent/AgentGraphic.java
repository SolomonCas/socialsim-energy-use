package com.socialsim.controller.graphics.agent;

import com.socialsim.controller.graphics.Graphic;
import com.socialsim.model.core.agent.Agent;

import java.util.ArrayList;
import java.util.List;

public class AgentGraphic extends Graphic {

    // VARIABLES
    public static final String AGENTS_URL_1 = "com/socialsim/view/image/Office/office_agents_1.png";
    public static final String AGENTS_URL_2 = "com/socialsim/view/image/Office/office_agents_2.png";
    public static final String AGENTS_URL_3 = "com/socialsim/view/image/Office/office_agents_3.png";
    public static final String AGENTS_URL_4 = "com/socialsim/view/image/Office/office_agents_4.png";


    public static final List<AgentGraphicLocation> directorGraphics;
    public static final List<AgentGraphicLocation> maleStrictFacultyGraphics;
    public static final List<AgentGraphicLocation> maleAppFacultyGraphics;
    public static final List<AgentGraphicLocation> femaleStrictFacultyGraphics;
    public static final List<AgentGraphicLocation> femaleAppFacultyGraphics;
    public static final List<AgentGraphicLocation> maleIntStudentGraphics;
    public static final List<AgentGraphicLocation> maleExtStudentGraphics;
    public static final List<AgentGraphicLocation> femaleIntStudentGraphics;
    public static final List<AgentGraphicLocation> femaleExtStudentGraphics;
    public static final List<AgentGraphicLocation> maleMaintenanceGraphics;
    public static final List<AgentGraphicLocation> femaleMaintenanceGraphics;
    public static final List<AgentGraphicLocation> maleGuardGraphics;
    public static final List<AgentGraphicLocation> femaleGuardGraphics;


    static {
        // Director
        directorGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            directorGraphics.add(new AgentGraphicLocation(6, i));
        for (int i = 0; i < 4; i++)
            directorGraphics.add(new AgentGraphicLocation(14, i));
        
        
        // Faculty
        maleStrictFacultyGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleStrictFacultyGraphics.add(new AgentGraphicLocation(2, i));
        for (int i = 0; i < 4; i++)
            maleStrictFacultyGraphics.add(new AgentGraphicLocation(10, i));
        
        maleAppFacultyGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleAppFacultyGraphics.add(new AgentGraphicLocation(4, i));
        for (int i = 0; i < 4; i++)
            maleAppFacultyGraphics.add(new AgentGraphicLocation(12, i));
        
        femaleStrictFacultyGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleStrictFacultyGraphics.add(new AgentGraphicLocation(3, i));
        for (int i = 0; i < 4; i++)
            femaleStrictFacultyGraphics.add(new AgentGraphicLocation(11, i));
        
        femaleAppFacultyGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleAppFacultyGraphics.add(new AgentGraphicLocation(5, i));
        for (int i = 0; i < 4; i++)
            femaleAppFacultyGraphics.add(new AgentGraphicLocation(13, i));
        
        
        // Student
        maleIntStudentGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleIntStudentGraphics.add(new AgentGraphicLocation(4, i));
        for (int i = 0; i < 4; i++)
            maleIntStudentGraphics.add(new AgentGraphicLocation(12, i));
        
        maleExtStudentGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleExtStudentGraphics.add(new AgentGraphicLocation(6, i));
        for (int i = 0; i < 4; i++)
            maleExtStudentGraphics.add(new AgentGraphicLocation(14, i));
        
        femaleIntStudentGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleIntStudentGraphics.add(new AgentGraphicLocation(5, i));
        for (int i = 0; i < 4; i++)
            femaleIntStudentGraphics.add(new AgentGraphicLocation(13, i));
        
        femaleExtStudentGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleExtStudentGraphics.add(new AgentGraphicLocation(7, i));
        for (int i = 0; i < 4; i++)
            femaleExtStudentGraphics.add(new AgentGraphicLocation(15, i));

        
        // Maintenance
        maleMaintenanceGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleMaintenanceGraphics.add(new AgentGraphicLocation(3, i));
        for (int i = 0; i < 4; i++)
            maleMaintenanceGraphics.add(new AgentGraphicLocation(10, i));

        femaleMaintenanceGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleMaintenanceGraphics.add(new AgentGraphicLocation(4, i));
        for (int i = 0; i < 4; i++)
            femaleMaintenanceGraphics.add(new AgentGraphicLocation(11, i));
        
        
        // Guard
        maleGuardGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            maleGuardGraphics.add(new AgentGraphicLocation(0, i));
        for (int i = 0; i < 4; i++)
            maleGuardGraphics.add(new AgentGraphicLocation(7, i));

        femaleGuardGraphics = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            femaleGuardGraphics.add(new AgentGraphicLocation(1, i));
        for (int i = 0; i < 4; i++)
            femaleGuardGraphics.add(new AgentGraphicLocation(8, i));
    }

    private final Agent agent;
    protected final List<AgentGraphicLocation> graphics;
    protected int graphicIndex;


    // CONSTRUCTOR(S)
    public AgentGraphic(Agent agent) {
        this.agent = agent;
        this.graphics = new ArrayList<>();

        List<AgentGraphicLocation> agentGraphics = null;



        // Director
        if (agent.getType() == Agent.Type.DIRECTOR && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = directorGraphics;
        }



        // Male Faculty
        else if (agent.getGender() == Agent.Gender.MALE && agent.getPersona() == Agent.Persona.STRICT_FACULTY) {
            agentGraphics = maleStrictFacultyGraphics;
        }
        else if (agent.getGender() == Agent.Gender.MALE && agent.getPersona() == Agent.Persona.APP_FACULTY) {
            agentGraphics = maleAppFacultyGraphics;
        }
        // Female Faculty
        else if (agent.getGender() == Agent.Gender.FEMALE && agent.getPersona() == Agent.Persona.STRICT_FACULTY) {
            agentGraphics = femaleStrictFacultyGraphics;
        }
        else if (agent.getGender() == Agent.Gender.FEMALE && agent.getPersona() == Agent.Persona.APP_FACULTY) {
            agentGraphics = femaleAppFacultyGraphics;
        }



        // Male Student
        else if (agent.getGender() == Agent.Gender.MALE && agent.getPersona() == Agent.Persona.INT_STUDENT) {
            agentGraphics = maleIntStudentGraphics;
        }
        else if (agent.getGender() == Agent.Gender.MALE && agent.getPersona() == Agent.Persona.EXT_STUDENT) {
            agentGraphics = maleExtStudentGraphics;
        }
        // Female Student
        else if (agent.getGender() == Agent.Gender.FEMALE && agent.getPersona() == Agent.Persona.INT_STUDENT) {
            agentGraphics = femaleIntStudentGraphics;
        }
        else if (agent.getGender() == Agent.Gender.FEMALE && agent.getPersona() == Agent.Persona.EXT_STUDENT) {
            agentGraphics = femaleExtStudentGraphics;
        }



        // Male Maintenance
        else if (agent.getType() == Agent.Type.MAINTENANCE && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleMaintenanceGraphics;
        }
        // Female Maintenance
        else if (agent.getType() == Agent.Type.MAINTENANCE && agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleMaintenanceGraphics;
        }



        // Male Guard
        else if (agent.getType() == Agent.Type.GUARD && agent.getGender() == Agent.Gender.MALE) {
            agentGraphics = maleGuardGraphics;
        }
        // Female Guard
        else if (agent.getType() == Agent.Type.GUARD && agent.getGender() == Agent.Gender.FEMALE) {
            agentGraphics = femaleGuardGraphics;
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
