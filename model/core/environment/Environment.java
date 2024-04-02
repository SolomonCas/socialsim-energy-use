package com.socialsim.model.core.environment;

import com.socialsim.controller.Main;
import com.socialsim.model.core.agent.Agent;
import com.socialsim.model.core.environment.patchfield.*;
import com.socialsim.model.core.environment.position.Coordinates;
import com.socialsim.model.core.environment.position.MatrixPosition;
import com.socialsim.model.simulator.Simulator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Environment extends BaseObject implements Serializable {

    // VARIABLES
    private final int rows;
    private final int columns;
    private final Patch[][] patches;


    private final List<Wall> walls;
    private final List<MeetingRoom> meetingRooms;
    private final List<HumanExpRoom> humanExpRooms;
    private final List<DataCollectionRoom> dataCollectionRooms;
    private final List<ResearchCenter> researchCenters;
    private final List<FacultyRoom> facultyRooms;
    private final List<StorageRoom> storageRooms;
    private final List<Pantry> pantries;
    private final List<LearningSpace> learningSpaces;
    private final List<ControlCenter> controlCenters;
    private final List<DataCenter> dataCenters;
    private final List<SoloRoom> soloRooms;
    private final List<StaffArea> staffAreas;
    private final List<Bathroom> bathrooms;
    private final List<Reception> receptions;
    private final List<DeanRoom> deanRooms;
    
    

    private final CopyOnWriteArrayList<Agent> agents;



    private CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> IOSScales;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<Double>> IOSInteractionChances;
    private CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> interactionTypeChances;
    public static CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> defaultIOS;
    public static CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> defaultInteractionTypeChances;


    // CONSTRUCTOR(S)
    public Environment(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.patches = new Patch[rows][columns];
        initializePatches();
        

        this.walls = Collections.synchronizedList(new ArrayList<>());
        this.meetingRooms = Collections.synchronizedList(new ArrayList<>());
        this.humanExpRooms = Collections.synchronizedList(new ArrayList<>());
        this.dataCollectionRooms = Collections.synchronizedList(new ArrayList<>());
        this.researchCenters = Collections.synchronizedList(new ArrayList<>());
        this.facultyRooms = Collections.synchronizedList(new ArrayList<>());
        this.storageRooms = Collections.synchronizedList(new ArrayList<>());
        this.pantries = Collections.synchronizedList(new ArrayList<>());
        this.learningSpaces = Collections.synchronizedList(new ArrayList<>());
        this.controlCenters = Collections.synchronizedList(new ArrayList<>());
        this.dataCenters = Collections.synchronizedList(new ArrayList<>());
        this.soloRooms = Collections.synchronizedList(new ArrayList<>());
        this.staffAreas = Collections.synchronizedList(new ArrayList<>());
        this.bathrooms = Collections.synchronizedList(new ArrayList<>());
        this.receptions = Collections.synchronizedList(new ArrayList<>());
        this.deanRooms = Collections.synchronizedList(new ArrayList<>());


        this.agents = new CopyOnWriteArrayList<>();
        this.IOSInteractionChances = new CopyOnWriteArrayList<>();
    }



    // METHODS
    private void initializePatches() {
        MatrixPosition matrixPosition;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                matrixPosition = new MatrixPosition(row, column);
                patches[row][column] = new Patch(this, matrixPosition);
            }
        }
    }

    public static List<Patch> get7x7Field(Environment environment, Patch centerPatch, double heading, boolean includeCenterPatch, double fieldOfViewAngle) {
        int truncatedX = (int) (centerPatch.getPatchCenterCoordinates().getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS);
        int truncatedY = (int) (centerPatch.getPatchCenterCoordinates().getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS);

        Patch chosenPatch = null;
        List<Patch> patchesToExplore = new ArrayList<>();

        for (int rowOffset = -3; rowOffset <= 3; rowOffset++) {
            for (int columnOffset = -3; columnOffset <= 3; columnOffset++) {
                boolean xCondition;
                boolean yCondition;
                boolean isCenterPatch = rowOffset == 0 && columnOffset == 0;

                if (!includeCenterPatch) {
                    if (isCenterPatch) {
                        continue;
                    }
                }

                if (rowOffset < 0) {
                    yCondition = truncatedY + rowOffset > 0;
                }
                else if (rowOffset > 0) {
                    yCondition = truncatedY + rowOffset < 60;
                }
                else {
                    yCondition = true;
                }

                if (columnOffset < 0) {
                    xCondition = truncatedX + columnOffset > 0;
                }
                else if (columnOffset > 0) {
                    xCondition = truncatedX + columnOffset < 120;
                }
                else {
                    xCondition = true;
                }

//                if (xCondition && yCondition) {
//                    if (environment instanceof University) {
//                        chosenPatch = Main.universitySimulator.getUniversity().getPatch(truncatedY + rowOffset, truncatedX + columnOffset);
//                    }
//
//                    if ((includeCenterPatch && isCenterPatch) || Coordinates.isWithinFieldOfView(centerPatch.getPatchCenterCoordinates(), chosenPatch.getPatchCenterCoordinates(), heading, fieldOfViewAngle)) {
//                        patchesToExplore.add(chosenPatch);
//                    }
//                }
            }
        }

        return patchesToExplore;
    }

    public double convertToChanceInteraction(int x){
        double CHANCE = ((double) x - 1) / 7 + Simulator.RANDOM_NUMBER_GENERATOR.nextDouble() * 1/7;
        return CHANCE;
    }



    public void convertIOSToChances() {
        // Insert code
    }

    public static void configureDefaultIOS(){
        defaultIOS = new CopyOnWriteArrayList<>();
        for (int i = 0; i < Agent.Persona.values().length; i++){
            CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>> personaIOS = new CopyOnWriteArrayList<>();
            for (int j = 0; j < Agent.Persona.values().length; j++){
                Agent.Persona persona1 = Agent.Persona.values()[i];
                Agent.Persona persona2 = Agent.Persona.values()[j];
                switch (persona1){
                    case PROFESSIONAL_BOSS -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                        }
                    }
                    case APPROACHABLE_BOSS -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4, 5)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                        }
                    }
                    case MANAGER -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case INT_BUSINESS -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case EXT_BUSINESS -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case INT_RESEARCHER -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case EXT_RESEARCHER -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case INT_TECHNICAL -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case EXT_TECHNICAL -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3, 4, 5)));
                            case MANAGER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_BUSINESS -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_RESEARCHER -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(2, 3, 4)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case INT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case EXT_TECHNICAL -> {
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(4, 5, 6)));
                                personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            }
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                        }
                    }
                    case JANITOR -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case CLIENT -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case DRIVER -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case VISITOR -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6, 7)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6, 7)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case GUARD -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case RECEPTIONIST -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                    case SECRETARY -> {
                        switch (persona2){
                            case PROFESSIONAL_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case APPROACHABLE_BOSS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(3, 4, 5, 6)));
                            case MANAGER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case INT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case EXT_BUSINESS -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case INT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case EXT_RESEARCHER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case INT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case EXT_TECHNICAL -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1, 2, 3)));
                            case JANITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case CLIENT -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
//                            case DRIVER -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case VISITOR -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case GUARD -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case RECEPTIONIST -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                            case SECRETARY -> personaIOS.add(new CopyOnWriteArrayList<>(List.of(1)));
                        }
                    }
                }
            }
            defaultIOS.add(personaIOS);
        }
    }

    public static void configureDefaultInteractionTypeChances() {
        // Insert code
    }

    public void copyDefaultToIOS() {
        this.IOSScales = new CopyOnWriteArrayList<>();
        for(int i = 0; i < defaultIOS.size(); i++){
            this.IOSScales.add(new CopyOnWriteArrayList<>());
            for(int j = 0; j < defaultIOS.get(i).size(); j++){
                this.IOSScales.get(i).add(new CopyOnWriteArrayList<>());
                for (int k = 0; k < defaultIOS.get(i).get(j).size(); k++){
                    this.IOSScales.get(i).get(j).add(defaultIOS.get(i).get(j).get(k));
                }
            }
        }
    }

    public void copyDefaultToInteractionTypeChances(){
        this.interactionTypeChances = new CopyOnWriteArrayList<>();
        for(int i = 0; i < defaultInteractionTypeChances.size(); i++){
            this.interactionTypeChances.add(new CopyOnWriteArrayList<>());
            for(int j = 0; j < defaultInteractionTypeChances.get(i).size(); j++){
                this.interactionTypeChances.get(i).add(new CopyOnWriteArrayList<>());
                for (int k = 0; k < defaultInteractionTypeChances.get(i).get(j).size(); k++){
                    this.interactionTypeChances.get(i).get(j).add(defaultInteractionTypeChances.get(i).get(j).get(k));
                }
            }
        }
    }

    public int numBathroomsFree(){
        // Insert code
        return 0;
    }



    // GETTERS
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Patch getPatch(Coordinates coordinates) {
        return getPatch((int) (coordinates.getY() / Patch.PATCH_SIZE_IN_SQUARE_METERS), (int) (coordinates.getX() / Patch.PATCH_SIZE_IN_SQUARE_METERS));
    }

    public Patch getPatch(MatrixPosition matrixPosition) {
        return getPatch(matrixPosition.getRow(), matrixPosition.getColumn());
    }

    public Patch getPatch(int row, int column) {
        return patches[row][column];
    }

    public Patch[][] getPatches() {
        return this.patches;
    }
    public List<MeetingRoom> getMeetingRooms() {
        return meetingRooms;
    }
    public List<HumanExpRoom> getHumanExpRooms() {
        return humanExpRooms;
    }
    public List<DataCollectionRoom> getDataCollectionRooms() {
        return dataCollectionRooms;
    }
    public List<ResearchCenter> getResearchCenters() {
        return researchCenters;
    }
    public List<FacultyRoom> getFacultyRooms() {
        return facultyRooms;
    }
    public List<StorageRoom> getStorageRooms(){
        return storageRooms;
    }
    public List<Pantry> getPantries() {
        return pantries;
    }
    public List<LearningSpace> getLearningSpaces() {
        return learningSpaces;
    }
    public List<ControlCenter> getControlCenters() {
        return controlCenters;
    }
    public List<DataCenter> getDataCenters() {
        return dataCenters;
    }
    public List<SoloRoom> getSoloRooms() {
        return soloRooms;
    }
    public List<StaffArea> getStaffRooms() {
        return staffAreas;
    }
    public List<Bathroom> getBathrooms() {
        return bathrooms;
    }
    public List<Reception> getReceptions() {
        return receptions;
    }
    public List<DeanRoom> getDeanRooms() {
        return deanRooms;
    }
    public List<Wall> getWalls() {
        return walls;
    }


    public CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> getIOSScales(){
        return this.IOSScales;
    }

    public CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> getInteractionTypeChances(){
        return this.interactionTypeChances;
    }


    // SETTERS
    public void setIOSScales(CopyOnWriteArrayList<CopyOnWriteArrayList<CopyOnWriteArrayList<Integer>>> IOSScales){
        this.IOSScales = IOSScales;
    }






    public static class Factory extends BaseObject.ObjectFactory {
        public static Environment create(int rows, int columns) {
            return new Environment(rows, columns) {
            };
        }
    }
}